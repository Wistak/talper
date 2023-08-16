package com.example.talper;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Perfil extends AppCompatActivity {

    ArrayList<ProspectosItem> prospectosList;
    ListView prospectosListView;
    Context context = this;
    private DatabaseReference db;
    LinearProgressIndicator progress;
    Uri file;
    MaterialButton selectFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        FirebaseApp.initializeApp(this);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialoglayout = inflater.inflate(R.layout.nuevo_prospecto_dialog, null);
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

        Button nuevoBtn = findViewById(R.id.nuevoBtn);
        nuevoBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                db = FirebaseDatabase.getInstance().getReference("prospectos");

                if (dialoglayout.getParent() != null) {
                    ((ViewGroup) dialoglayout.getParent()).removeView(dialoglayout);
                    }

                    builder.setView(dialoglayout).setCancelable(false).setTitle("Datos de Prospecto");

                    MaterialButton selectFile = dialoglayout.findViewById(R.id.uploadFileButton);
                    selectFile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            activityResultLauncher.launch(intent);
                        }
                    });

                    builder.setPositiveButton("Guardar", (DialogInterface.OnClickListener) (dialog, which) -> {

                        EditText NuevoNombreET = dialoglayout.findViewById(R.id.NuevoNombreText);
                        EditText NuevoApePatET  = dialoglayout.findViewById(R.id.NuevoApePatText);
                        EditText NuevoApeMatET  = dialoglayout.findViewById(R.id.NuevoApeMatText);
                        EditText NuevoCalleET  = dialoglayout.findViewById(R.id.NuevoCalleText);
                        EditText NuevoNumeroET  = dialoglayout.findViewById(R.id.NuevoNumeroText);
                        EditText NuevoColoniaET  = dialoglayout.findViewById(R.id.NuevoColoniaText);
                        EditText NuevoCodigoPostalET  = dialoglayout.findViewById(R.id.NuevoCodigoPostalText);
                        EditText NuevoTelefonoET  = dialoglayout.findViewById(R.id.NuevoTelefonoText);
                        EditText  NuevoRfcET  = dialoglayout.findViewById(R.id.NuevoRfcText);

                        String nombre = NuevoNombreET.getText().toString();
                        String apePat = NuevoApePatET.getText().toString();
                        String apeMat = NuevoApeMatET.getText().toString();
                        String calle = NuevoCalleET.getText().toString();
                        String numero = NuevoNumeroET.getText().toString();
                        String colonia = NuevoColoniaET.getText().toString();
                        String codigoPostal = NuevoCodigoPostalET.getText().toString();
                        String telefono = NuevoTelefonoET.getText().toString();
                        String rfc = NuevoRfcET.getText().toString();
                        String id = "prospecto" + new Date().getTime();

                        try {

                            if (file != null) {
                                StorageReference reference = storageRef.child("files/" + id);
                                reference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(Perfil.this, "Archivo subido satisfactoriamente!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            db.child(id).setValue(new ProspectosItem(nombre, apePat, apeMat, calle, numero, colonia, codigoPostal, telefono, rfc, "No aprovado"));
                            loadDatainListview();
                        } catch (Exception e) {
                            Log.e("ELQUERYYY", id + "Hubo pedos con la bd" + e.getMessage());
                        }
                    });

                    builder.setNegativeButton("Cancelar", (DialogInterface.OnClickListener) (dialog, which) -> {
                        dialog.cancel();

                    });

                    builder.show();
                }

        });

    }


    
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                file = result.getData().getData();
            }
        }
    });



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