package br.com.campuscode03.contactapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import br.com.campuscode03.contactapp.R;
import br.com.campuscode03.contactapp.provider.ContactModel;


public class ContactAdapter extends CursorAdapter {

    TextView text1;
    TextView text2;

    public interface ReloadDataSetChanged {
        void reloadDataSet();
    }

    ReloadDataSetChanged callback;

    public ContactAdapter(Context context, Cursor c, int flags, ReloadDataSetChanged callback) {
        super(context, c, flags);
        this.callback = callback;
    }

    public ContactAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.contacts_item, null);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView text1;
        TextView text2;
        Button del_contact_bt;

        text1 = view.findViewById(R.id.name_contact);
        text2 = view.findViewById(R.id.phone_contact);

        text1.setText(cursor.getString(cursor.getColumnIndex(ContactModel.NAME)));
        text2.setText(cursor.getString(cursor.getColumnIndex(ContactModel.PHONE)));
        final String id = cursor.getString(cursor.getColumnIndex(ContactModel._ID));

        del_contact_bt = (Button) view.findViewById(R.id.delete_contact_bt);

        del_contact_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.getContentResolver().delete(Uri.withAppendedPath(ContactModel.CONTENT_URI, id), null, null);
                callback.reloadDataSet();
            }
        });
    }
}
