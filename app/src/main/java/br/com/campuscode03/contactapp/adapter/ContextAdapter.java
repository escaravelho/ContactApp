package br.com.campuscode03.contactapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.campuscode03.contactapp.R;
import br.com.campuscode03.contactapp.model.Contact;


public class ContextAdapter extends BaseAdapter{

    ArrayList<Contact> list;
    LayoutInflater inflater;

    public ContextAdapter(ArrayList <Contact> list, Context context){

        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View viewHolder = view;
        TextView text1;
        TextView text2;

        if(viewHolder == null){
            viewHolder = inflater.inflate(R.layout.contacts_item, null);
        }

        text1 = (TextView) viewHolder.findViewById(R.id.name_contact);
        text2 = (TextView) viewHolder.findViewById(R.id.phone_contact);

        text1.setText(list.get(i).getName());
        text2.setText(list.get(i).getPhone());

        return viewHolder;
    }
}