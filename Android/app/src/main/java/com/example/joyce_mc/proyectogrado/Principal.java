package com.example.joyce_mc.proyectogrado;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Principal extends AppCompatActivity {
    private DatabaseReference db_switch;
    private LinearLayout elementos;
    private Bundle bundle;
    private String usuario;
    private int switchId = 0,switchId2;
    private String switchNombre;
    private boolean switchEstado,switchEstado2;
    private String lista_switch;
    private String[] lista_ids;
    private boolean[] lista_eliminar;
    private ValueEventListener vSwitch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bundle = getIntent().getExtras();
        usuario = bundle.getString("usuario");
        db_switch = FirebaseDatabase.getInstance().getReference().child(usuario);
        elementos = (LinearLayout)findViewById(R.id.elementos);

        db_switch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_switch = dataSnapshot.child("lista_switch").getValue().toString();
                if (lista_switch != Integer.toString(0))
                {
                    lista_ids = lista_switch.split(",");
                    for (int i = 0; i < lista_ids.length; i++) {
                        switchId = Integer.parseInt(lista_ids[i]);
                        switchNombre = dataSnapshot.child("switch").child(Integer.toString(switchId)).child("Nombre").getValue().toString();
                        switchEstado = Boolean.valueOf(dataSnapshot.child("switch").child(Integer.toString(switchId)).child("Estado").getValue().toString());
                        Ingreso_Switch();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialoglayout = inflater.inflate(R.layout.pop_up,null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(Principal.this);
                final AlertDialog alertDialog = builder.create();

                final EditText etNombre = (EditText)dialoglayout.findViewById(R.id.etNombre);
                final EditText etID = (EditText)dialoglayout.findViewById(R.id.etId);
                final Button buttonAgregar = (Button)dialoglayout.findViewById(R.id.buttonAgregar);
                db_switch.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        lista_switch = dataSnapshot.child("lista_switch").getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        finish();
                    }
                });

                buttonAgregar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (etNombre.getText().toString().isEmpty() || etID.getText().toString().isEmpty())
                        {
                            Toast.makeText(getApplicationContext(),"Ingrese los datos",Toast.LENGTH_LONG);
                        }
                        else
                        {
                            switchNombre = etNombre.getText().toString();
                            switchId = Integer.parseInt(etID.getText().toString());
                            switchEstado = false;
                            if (lista_switch == Integer.toString(0)) {
                                lista_switch = Integer.toString(switchId);
                            } else {
                                lista_switch = lista_switch + "," + switchId;
                            }
                            Ingreso_Switch();
                            alertDialog.cancel();
                        }
                    }
                });
                alertDialog.setView(dialoglayout);
                alertDialog.show();
            }
        });

        FloatingActionButton elim = (FloatingActionButton)findViewById(R.id.elim);
        elim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater2 = getLayoutInflater();
                final View dialoglayout2 = inflater2.inflate(R.layout.pop_up_del_switch,null);
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(Principal.this);
                final AlertDialog alertDialog2 = builder2.create();
                final LinearLayout elementos2 = (LinearLayout)dialoglayout2.findViewById(R.id.elementos2);

                db_switch.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        lista_switch = dataSnapshot.child("lista_switch").getValue().toString();
                        if (lista_switch != Integer.toString(0))
                        {
                            lista_ids = lista_switch.split(",");
                            lista_eliminar = new boolean[lista_ids.length];
                            for (int i = 0; i < lista_ids.length; i++)
                            {
                                final CheckBox cb = new CheckBox(getApplicationContext());
                                cb.setText(dataSnapshot.child("switch").child(lista_ids[i]).child("Nombre").getValue().toString());
                                cb.setId(Integer.parseInt(lista_ids[i]));
                                cb.setTextColor(Color.BLACK);
                                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        for (int j = 0; j < lista_eliminar.length; j++)
                                        {
                                            if (lista_ids[j].equals(Integer.toString(buttonView.getId())))
                                            {
                                                lista_eliminar[j] = isChecked;
                                            }
                                        }
                                    }
                                });
                                elementos2.addView(cb);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        finish();
                    }
                });

                final Button buttonEliminar = (Button)dialoglayout2.findViewById(R.id.buttonEliminar);
                buttonEliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Eliminar_Switch();
                        alertDialog2.cancel();
                    }
                });
                alertDialog2.setView(dialoglayout2);
                alertDialog2.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_alarm:
                Intent i = new Intent(this,Alarm.class);
                i.putExtra("usuario",usuario);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Ingreso_Switch ()
    {
        final Switch sw = new Switch(getApplicationContext());
        sw.setText(switchNombre);
        sw.setId(switchId);
        sw.setTextColor(Color.BLACK);
        elementos.addView(sw);
        db_switch.child("switch").child(Integer.toString(switchId)).child("Nombre").setValue(switchNombre);
        db_switch.child("switch").child(Integer.toString(switchId)).child("Estado").setValue(switchEstado);
        db_switch.child("lista_switch").setValue(lista_switch);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db_switch.child("switch").child(Integer.toString(buttonView.getId())).child("Estado").setValue(isChecked);
            }
        });
        vSwitch = db_switch.child("switch").child(Integer.toString(switchId)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                switchId2 = Integer.parseInt(dataSnapshot.getKey());
                switchEstado2 = Boolean.valueOf(dataSnapshot.child("Estado").getValue().toString());
                Switch switch_2 = (Switch) findViewById(switchId2);
                switch_2.setChecked(switchEstado2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                //finish();
            }
        });
    }

    public void Eliminar_Switch ()
    {
        String nueva_lista = "";
        for (int i = 0; i < lista_ids.length; i++) {
            if (!lista_eliminar[i]) {
                if (nueva_lista.equals("")) {
                    nueva_lista = lista_ids[i];
                } else {
                    nueva_lista = nueva_lista + "," + lista_ids[i];
                }
            } else {
                db_switch.child("switch").child(lista_ids[i]).removeEventListener(vSwitch);
                db_switch.child("switch").child(lista_ids[i]).removeValue();
                //elementos.removeView(findViewById(Integer.parseInt(lista_ids[i])));
            }
        }
        if (nueva_lista.equals(""))
        {
            elementos.removeAllViews();
            db_switch.child("lista_switch").setValue(0);
            db_switch.child("lista_alarmas").setValue(0);
            db_switch.child("alarmas").removeValue();

        }
        else
        {
            for (int i = 0; i < lista_ids.length; i++) {
                if (lista_eliminar[i]) {
                    elementos.removeViewAt(Integer.parseInt(lista_ids[i]));
                    //elementos.removeView(findViewById(Integer.parseInt(lista_ids[i])));
                }
            }
            /*elementos.removeAllViews();
            db_switch.child("lista_switch").setValue(nueva_lista);
            //elementos.removeAllViews();
            lista_switch = nueva_lista;
            db_switch.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //lista_switch = dataSnapshot.child("lista_switch").getValue().toString();
                    lista_ids = lista_switch.split(",");
                    for (int i = 0; i < lista_ids.length; i++) {
                        switchId = Integer.parseInt(lista_ids[i]);
                        switchNombre = dataSnapshot.child("switch").child(Integer.toString(switchId)).child("Nombre").getValue().toString();
                        switchEstado = Boolean.valueOf(dataSnapshot.child("switch").child(Integer.toString(switchId)).child("Estado").getValue().toString());
                        Ingreso_Switch();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    finish();
                }
            });
            //elementos.removeViewAt();*/
        }
    }
}
