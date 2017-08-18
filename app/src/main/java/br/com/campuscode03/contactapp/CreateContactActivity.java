package br.com.campuscode03.contactapp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import br.com.campuscode03.contactapp.network.ContactsHttpManager;
import br.com.campuscode03.contactapp.provider.ContactModel;

public class CreateContactActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edit_name;
    private EditText edit_phone;
    private Button bt_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        edit_name = (EditText) findViewById(R.id.name);
        edit_phone = (EditText) findViewById(R.id.phone);
        bt_save = (Button) findViewById(R.id.bt_contact_save);

        bt_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        boolean bool = true;

        String inputName = edit_name.getText().toString();
        String inputPhone = edit_phone.getText().toString();

        if(TextUtils.isEmpty(inputName)){
            edit_name.setError("Invalid name!");
            edit_name.setAnimation(animation);
            bool = false;
        }

        if(TextUtils.isEmpty(inputPhone)){
            edit_phone.setError("Invalid number!");
            edit_phone.setAnimation(animation);
            bool = false;
        }

        if(bool) {
            ContentValues values = new ContentValues();

            values.put(ContactModel.NAME, inputName);
            values.put(ContactModel.PHONE, inputPhone);

            Uri result = getContentResolver().insert(ContactModel.CONTENT_URI, values);

            ContactsHttpManager sendContacts = new ContactsHttpManager();
            sendContacts.sendContacts(inputName, inputPhone);
            finish();
        }
    }
}
