package dk.izbrannick.glutter.sheetstest;

/**
 * Created by luther on 03/11/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kstanoev on 1/14/2015.
 */
public class MyContactsAdapter extends ArrayAdapter<MyContact> {

    Context context;
    private ArrayList<MyContact> myContacts;

    public MyContactsAdapter(Context context, int textViewResourceId, ArrayList<MyContact> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.myContacts = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.my_contact, null);
        }
        MyContact o = myContacts.get(position);
        if (o != null) {
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView numberPrimary = (TextView) v.findViewById(R.id.numberPrimary);
            TextView groupsParticipatingIn = (TextView) v.findViewById(R.id.groupsParticipatingIn);
            TextView email = (TextView) v.findViewById(R.id.email);

            name.setText(o.getName());
            numberPrimary.setText(o.getNumberPrimary());
            groupsParticipatingIn.setText( o.getGroups().toString()  ); //String.valueOf(o.getGroups())
            email.setText("getNumberOther()!");
        }
        return v;
    }
}
