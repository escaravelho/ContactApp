package br.com.campuscode03.contactapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import br.com.campuscode03.contactapp.adapter.ContextAdapter;
import br.com.campuscode03.contactapp.model.Contact;
import br.com.campuscode03.contactapp.network.ContactsHttpManager;
import br.com.campuscode03.contactapp.provider.ContactModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ContactsHttpManager.ReloadContacts {

    private ListView list_view;
    private FloatingActionButton add_contact_bt;

    ArrayList <Contact> list;
    ContextAdapter adapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_view = (ListView) findViewById(R.id.contact_list);
        add_contact_bt = (FloatingActionButton) findViewById(R.id.add_contact_bt);

        list = new ArrayList<>();

        adapter = new ContextAdapter(list, this);

        list_view.setAdapter(adapter);

        add_contact_bt.setOnClickListener(this);
    }

    protected void onResume(){
        super.onResume();

        ContactsHttpManager receive = new ContactsHttpManager();
        receive.receiveContacts(this);

    }

    public void refreshList(){
        Cursor cursor = getContentResolver().query(ContactModel.CONTENT_URI, null, null, null, null);
        list.clear();

        if(cursor != null){
            while (cursor.moveToNext()){

                list.add(new Contact(cursor.getInt(cursor.getColumnIndex(ContactModel._ID)),cursor.getString(cursor.getColumnIndex(ContactModel.NAME)),
                        cursor.getString(cursor.getColumnIndex(ContactModel.PHONE))));
            }

            cursor.close();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(this, CreateContactActivity.class));
    }

    @Override
    public void onContactsSync(List<Contact> listContacts) {

        getContentResolver().delete(ContactModel.CONTENT_URI, null, null);

        for (Contact contact:listContacts) {
            ContentValues values = new ContentValues();
            values.put(ContactModel.NAME, contact.getName());
            values.put(ContactModel.PHONE, contact.getPhone());
            getContentResolver().insert(ContactModel.CONTENT_URI, values);
        }
        refreshList();
    }
}