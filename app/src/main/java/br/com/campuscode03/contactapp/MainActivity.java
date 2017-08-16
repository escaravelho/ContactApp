package br.com.campuscode03.contactapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import br.com.campuscode03.contactapp.adapter.ContextAdapter;
import br.com.campuscode03.contactapp.model.Contact;
import br.com.campuscode03.contactapp.provider.ContactModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView list_view;
    private FloatingActionButton add_contact_bt;

    ArrayList <Contact> list;
    ContextAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_view = (ListView) findViewById(R.id.contact_list);
        add_contact_bt = (FloatingActionButton) findViewById(R.id.add_contact_bt);

        list = new ArrayList<>();

        list.add(new Contact("Obi Wan Kenobi", "1199988556"));
        list.add(new Contact("Sandro", "1199988556"));
        list.add(new Contact("Teste", "1199988556"));

        adapter = new ContextAdapter(list, this);

        list_view.setAdapter(adapter);

        add_contact_bt.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Contact list_contacts = new Contact(data.getStringExtra("name"), data.getStringExtra("phone"));

        ContentValues values = new ContentValues();

        values.put(ContactModel.NAME, list_contacts.getName());
        values.put(ContactModel.PHONE, list_contacts.getPhone());

        Uri result = getContentResolver().insert(ContactModel.CONTENT_URI, values);

        //list.add(list_contacts);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        startActivityForResult(new Intent(this, CreateContactActivity.class), 1);
    }
}