package br.com.campuscode03.contactapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        Intent intent = new Intent();

        intent.putExtra("name", edit_name.getText().toString());
        intent.putExtra("phone", edit_phone.getText().toString());

        setResult(RESULT_OK, intent);
        finish();
    }
}
