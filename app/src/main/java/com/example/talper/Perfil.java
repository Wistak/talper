package com.example.talper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class Perfil extends AppCompatActivity {

    ArrayList<ProspectosItem> prospectosList;
    ListView prospectosListView;

    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ListView listView = (ListView) findViewById(R.id.prospectosListView);


        prospectosList = new ArrayList<>();
        prospectosListView = findViewById(R.id.prospectosListView);

        loadDatainListview();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Log.e("UAUA", selectedItem);
            }
        });

    }
    private void loadDatainListview() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("prospectos").orderByChild("idprospecto").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                prospectosList.clear();

                if (snapshot.exists()) {
                    Log.e("JUAJUA", "EXISTEEE ");
                    Log.e("VALORES", String.valueOf(snapshot));

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        ProspectosItem prospectosItem = dataSnapshot.getValue(ProspectosItem.class);
                        prospectosList.add(prospectosItem);

                    }

                    ProspectosAdapter adapter = new ProspectosAdapter(Perfil.this, prospectosList);
                    prospectosListView.setAdapter(adapter);

                } else {
                    Log.e("JUAJUA", "No Existe");
                    // if the snapshot is empty we are displaying a toast message.
                    Toast.makeText(Perfil.this, "No se encontró nada en la BD", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}