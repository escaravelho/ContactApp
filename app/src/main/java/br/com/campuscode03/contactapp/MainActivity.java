package br.com.campuscode03.contactapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import java.util.List;

import br.com.campuscode03.contactapp.adapter.ContactAdapter;
import br.com.campuscode03.contactapp.model.Contact;
import br.com.campuscode03.contactapp.network.ContactsHttpManager;
import br.com.campuscode03.contactapp.provider.ContactModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ContactsHttpManager.ReloadContacts, SwipeRefreshLayout.OnRefreshListener, ContactAdapter.ReloadDataSetChanged {

    private ListView list_view;
    private FloatingActionButton add_contact_bt;

    ContactAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    ContactsHttpManager receive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_view = (ListView) findViewById(R.id.contact_list);
        add_contact_bt = (FloatingActionButton) findViewById(R.id.add_contact_bt);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swype_refresh);

        refreshLayout.setOnRefreshListener(this);

        add_contact_bt.setOnClickListener(this);

        receive = new ContactsHttpManager();
        receive.receiveContacts(this);

    }

    protected void onResume(){
        super.onResume();
        refreshList();

    }

    public void refreshList(){
        Cursor cursor = getContentResolver().query(ContactModel.CONTENT_URI, null, null, null, null);

        adapter = new ContactAdapter(this, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER, this);
        list_view.setAdapter(adapter);
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
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        receive.receiveContacts(this);
    }

    @Override
    public void reloadDataSet() {
        refreshList();
    }
}