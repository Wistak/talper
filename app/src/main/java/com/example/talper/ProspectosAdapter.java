package com.example.talper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProspectosAdapter extends ArrayAdapter<ProspectosItem> {

    private Activity Perfil = (Perfil) getContext();
    public ProspectosAdapter(@NonNull Context context, ArrayList<ProspectosItem> dataModalArrayList) {
        super(context, 0, dataModalArrayList);
    }
    AlertDialog.Builder builder = new AlertDialog.Builder(Perfil);

    ArrayList<ProspectosItem> prospectosList;

    ListView prospectosListView;

    LayoutInflater inflater = LayoutInflater.from(Perfil);

    private String id_record = "";
    private DatabaseReference db;

    View dialoglayout = inflater.inflate(R.layout.prospecto_dialog, null);
    // initializing the textview and  from our dialog box.
    EditText nombreET = dialoglayout.findViewById(R.id.NombreText);
    EditText apePatET  = dialoglayout.findViewById(R.id.apePatText);
    EditText apeMatET  = dialoglayout.findViewById(R.id.apeMatText);
    EditText calleET  = dialoglayout.findViewById(R.id.CalleText);
    EditText numeroET  = dialoglayout.findViewById(R.id.NumeroText);
    EditText coloniaET  = dialoglayout.findViewById(R.id.ColoniaText);
    EditText codigoPostalET  = dialoglayout.findViewById(R.id.CodigoPostalText);
    EditText telefonoET  = dialoglayout.findViewById(R.id.TelefonoText);
    EditText  rfcET  = dialoglayout.findViewById(R.id.RfcText);


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        prospectosList = new ArrayList<>();
        View listView = inflater.inflate(R.layout.activity_perfil, parent, false);
        prospectosListView = (ListView) listView.findViewById(R.id.prospectosListView);

        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.perfil_item, parent, false);
        }

        ProspectosItem dataModal = getItem(position);

        TextView nombreText = listitemView.findViewById(R.id.perfilNombreText);

        nombreText.setText(dataModal.getNombre().toString());

        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialoglayout.getParent() != null) {
                    ((ViewGroup) dialoglayout.getParent()).removeView(dialoglayout);
                }

                db = FirebaseDatabase.getInstance().getReference("prospectos");
                db.orderByChild("nombre").equalTo(dataModal.getNombre()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            for(DataSnapshot data: snapshot.getChildren()) {

                                id_record = data.getKey();
                                String nombreData = data.child("nombre").getValue() == null ? "" : data.child("nombre").getValue().toString();
                                nombreET.setText(nombreData);
                                String apePatData = data.child("apePat").getValue() == null ? "" : data.child("apePat").getValue().toString();
                                apePatET.setText(apePatData);
                                String apeMatData = data.child("apeMat").getValue() == null ? "" : data.child("apeMat").getValue().toString();
                                apeMatET.setText(apeMatData);
                                String calleData = data.child("calle").getValue() == null ? "" : data.child("calle").getValue().toString();
                                calleET.setText(calleData);
                                String numeroData = data.child("numero").getValue() == null ? "" : data.child("numero").getValue().toString();
                                numeroET.setText(numeroData);
                                String coloniaData = data.child("colonia").getValue() == null ? "" : data.child("colonia").getValue().toString();
                                coloniaET.setText(coloniaData);
                                String codigoPostalData = data.child("codigoPostal").getValue() == null ? "" : data.child("codigoPostal").getValue().toString();
                                codigoPostalET.setText(codigoPostalData);
                                String telefonoData = data.child("telefono").getValue() == null ? "" : data.child("telefono").getValue().toString();
                                telefonoET.setText(telefonoData);
                                String rfcData = data.child("rfc").getValue() == null ? "" : data.child("rfc").getValue().toString();
                                rfcET.setText(rfcData);

                            }
                        } else {
                            Log.e("Error", "No se encontro el registro" + String.valueOf(snapshot));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                builder.setView(dialoglayout).setCancelable(false).setTitle("Datos de Prospecto");

                builder.setNeutralButton("Eliminar", (DialogInterface.OnClickListener) (dialog, wich) -> {
                    try {
                    db.child(id_record).removeValue();
                        Context context = parent.getContext();
                        context.startActivity(new Intent(context, Perfil.class));

                    } catch (Exception e) {
                        Log.e("Error", id_record + "  Error con la bd " + e.getMessage());
                    }
                });

                builder.setPositiveButton("Guardar", (DialogInterface.OnClickListener) (dialog, which) -> {

                    String nombre = nombreET.getText().toString();
                    String apePat = apePatET.getText().toString();
                    String apeMat = apeMatET.getText().toString();
                    String calle = calleET.getText().toString();
                    String numero = numeroET.getText().toString();
                    String colonia = coloniaET.getText().toString();
                    String codigoPostal = codigoPostalET.getText().toString();
                    String telefono = telefonoET.getText().toString();
                    String rfc = rfcET.getText().toString();

                    try {
                        Map<String, Object> updates = new HashMap<String,Object>();

                        updates.put("nombre", nombre);
                        updates.put("apePat", apePat);
                        updates.put("apeMat", apeMat);
                        updates.put("calle", calle);
                        updates.put("numero", numero);
                        updates.put("colonia", colonia);
                        updates.put("codigoPostal", codigoPostal);
                        updates.put("telefono", telefono);
                        updates.put("rfc", rfc);
                        updates.put("estatus", "No aprovado");

                        db.child(id_record).updateChildren(updates);

                    } catch (Exception e) {
                        Log.e("Error", "Error con la bd" + e.getMessage());
                    }
                });


                builder.setNegativeButton("Cancelar", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();

                });
                builder.show();
                //Toast.makeText(getContext(), "Item clicked is : " + dataModal.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });

        return listitemView;
    }

    public void loadDatainListview() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("prospectos").orderByChild("nombre").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (prospectosList != null) {
                    prospectosList.clear();
                }


                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ProspectosItem prospectosItem = dataSnapshot.getValue(ProspectosItem.class);
                        if (prospectosList != null) {
                            prospectosList.add(prospectosItem);
                        }
                    }

                    ProspectosAdapter adapter = new ProspectosAdapter(Perfil, prospectosList);
                    prospectosListView.setAdapter(adapter);

                } else {
                    Log.e("Error", "No Existe");
                    Toast.makeText(Perfil, "No se encontró nada en la BD", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
