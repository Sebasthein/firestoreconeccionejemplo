package com.sebas.firestoreconeccionejemplo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText mEditTextTitulo;
    EditText mEditTextContenido;
    Button mbutttonCrearDatos;
    TextView mTextViewDatos;

    FirebaseFirestore mFirestore ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirestore = FirebaseFirestore.getInstance();

        mEditTextTitulo = findViewById(R.id.editTextTitulo);
        mEditTextContenido = findViewById(R.id.editTextContenido);
        mTextViewDatos =findViewById(R.id.textViewDatos);
        mbutttonCrearDatos = findViewById(R.id.btnCrearDatos);

        mbutttonCrearDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                crearDatos();

            }
        });

        obtenerDatos();

    }

    private void obtenerDatos () {

        mFirestore.collection("Articulos").document("3").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    String titulo = documentSnapshot.getString("titulo");
                    String contenido = documentSnapshot.getString("contenido");
                    Long fecha = documentSnapshot.getLong("fecha");

                    mTextViewDatos.setText("Titulo: " + titulo + "\n" + "Contenido: " + contenido + "\n" + "Fecha: " + fecha);

                }
            }
        });


        /*
        mFirestore.collection("Articulos").document("3").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String titulo = documentSnapshot.getString("titulo");
                    String contenido = documentSnapshot.getString("contenido");
                    Long fecha = documentSnapshot.getLong("fecha");

                    mTextViewDatos.setText("Titulo: " + titulo + "\n" + "Contenido: " + contenido + "\n" + "Fecha: " + fecha);
                }
            }
        });*/
    }

    private void crearDatos() {

        String titulo = mEditTextTitulo.getText().toString();
        String contenido = mEditTextContenido.getText().toString();

        Map<String, Object> map = new HashMap<>();
        map.put("titulo", titulo);
        map.put("contenido", contenido);
        map.put("fecha", new Date().getTime());



       // mFirestore.collection( "Articulos").document().set(map);
        mFirestore.collection("Articulos").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Toast.makeText(MainActivity.this, "El artivulo se creo Correctamente", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "El articulo no se pudo crear", Toast.LENGTH_SHORT).show();
            }
        });
    }
}