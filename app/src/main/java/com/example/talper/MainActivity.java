package com.example.talper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText usuarioText;
    private EditText passwordText;
    private EditText puestoText;
    private Button loginButton;

    ArrayList<UsersItem> usersItemArrayList;

    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioText = findViewById(R.id.usuarioText);
        passwordText = findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.loginButton);

        db = FirebaseDatabase.getInstance().getReference("usuarios");

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String id = "user" + new Date().getTime();
                String usuario = usuarioText.getText().toString();
                String contrasena = passwordText.getText().toString();

                db.orderByChild("userName").equalTo(usuario).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            for(DataSnapshot data: snapshot.getChildren()) {

                                String userPassword = data.child("userPassword").getValue().toString();

                                if (contrasena.equals(userPassword)) {
                                    Log.e("JUAJUA", "Logueado " + userPassword);
                                    int duration = Toast.LENGTH_SHORT;
                                    CharSequence mensaje = "Bienvenido" + usuario;
                                    Intent i = new Intent(getApplicationContext(), Perfil.class);
                                    startActivity(i);
                                } else {
                                    Log.e("JUAJUA", "Nel homs contraseña incorrecta" + " " + contrasena.getClass().getName() + " " + userPassword.getClass().getName());
                                }
                            }
                        } else {
                            Log.e("JUAJUA", "No existe" + String.valueOf(snapshot));
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

               //db.child("usuarios").child(id).setValue(new UsersItem(id, usuario, contrasena, puesto));

            }

        });

    }
}