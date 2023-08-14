package com.example.talper;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;

public class ProspectosAdapter extends ArrayAdapter<ProspectosItem> {

    // constructor for our list view adapter.
    public ProspectosAdapter(@NonNull Context context, ArrayList<ProspectosItem> dataModalArrayList) {
        super(context, 0, dataModalArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // below line is use to inflate the
        // layout for our item of list view.
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.perfil_item, parent, false);
        }
        // after inflating an item of listview item
        // we are getting data from array list inside
        // our modal class.
        ProspectosItem dataModal = getItem(position);

        // initializing our UI components of list view item.
        TextView nombreText = listitemView.findViewById(R.id.perfilNombreText);

        // after initializing our items we are
        // setting data to our view.
        // below line is use to set data to our text view.
        nombreText.setText(dataModal.getNombre().toString());


        // below line is use to add item click listener
        // for our item of list view.
        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on the item click on our list view.
                // we are displaying a toast message.
                Toast.makeText(getContext(), "Item clicked is : " + dataModal.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });
        return listitemView;
    }
}
