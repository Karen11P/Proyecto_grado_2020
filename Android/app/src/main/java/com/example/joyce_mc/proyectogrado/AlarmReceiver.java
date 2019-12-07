package com.example.joyce_mc.proyectogrado;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AlarmReceiver extends android.content.BroadcastReceiver {
    private Bundle bundle;
    private String usuario;
    private String[] lista_ids;
    private boolean[] lista_estados;
    private DatabaseReference db_switch;

    @Override
    public void onReceive(android.content.Context context, android.content.Intent intent) {
        bundle = intent.getExtras();
        usuario = bundle.getString("usuario");
        lista_ids = bundle.getStringArray("lista_ids");
        lista_estados = bundle.getBooleanArray("lista_estados");
        db_switch = FirebaseDatabase.getInstance().getReference().child(usuario);
        for (int i = 0; i < lista_ids.length; i++)
        {
            db_switch.child("switch").child(lista_ids[i]).child("Estado").setValue(lista_estados[i]);
        }
        Toast.makeText(context, "Alarma", Toast.LENGTH_LONG).show();
    }
}
