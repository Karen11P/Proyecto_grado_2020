package com.example.joyce_mc.proyectogrado;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Alarm extends AppCompatActivity {
    private Bundle bundle;
    private String usuario;
    private DatabaseReference db_alarm;
    private LinearLayout elementos;
    private Calendar calendar;
    private String lista_alarmas;
    private String[] lista_alarmas2;
    private String alarmas;
    private String[] alarmas2;
    private String[] alarmas3;
    private String[] lista_horas;
    private String lista_switch;
    private String[] lista_ids;
    private int switchId = 0;
    private String switchNombre;
    private boolean[] lista_estados;
    private String value_switch;
    private String editTextId;
    private int hour;
    private int min;
    private int day;
    private String editTextName;
    private String alarmId;
    private AlarmManager alarmManager;
    private Intent intent;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bundle = getIntent().getExtras();
        usuario = bundle.getString("usuario");
        db_alarm = FirebaseDatabase.getInstance().getReference().child(usuario);
        elementos = (LinearLayout)findViewById(R.id.elementos);
        calendar = Calendar.getInstance();

        db_alarm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_alarmas = dataSnapshot.child("lista_alarmas").getValue().toString();
                if (lista_alarmas != Integer.toString(0))
                {
                    lista_alarmas2 = lista_alarmas.split("/");
                    for (int i = 0; i < lista_alarmas2.length; i++)
                    {
                        alarmId = lista_alarmas2[i];
                        alarmas = dataSnapshot.child("alarmas").child(alarmId).getValue().toString();
                        alarmas2 = alarmas.split("/");
                        lista_ids = new String[alarmas2.length];
                        lista_estados = new boolean[alarmas2.length];
                        for (int k = 0; k < alarmas2.length; k++)
                        {
                            alarmas3 = alarmas2[k].split(",");
                            lista_ids[k] = alarmas3[0];
                            if (alarmas3[1].equals("false"))
                            {
                                lista_estados[k] = false;
                            }
                            else if (alarmas3[1].equals("true"))
                            {
                                lista_estados[k] = true;
                            }
                        }
                        lista_horas = lista_alarmas2[i].split(",");
                        for (int j = 0; j < lista_horas.length; j++)
                        {
                            if (j == 0)
                            {
                                hour = Integer.parseInt(lista_horas[j]);
                            }
                            else if (j == 1)
                            {
                                min = Integer.parseInt(lista_horas[j]);
                                editTextId = hour+""+min;
                            }
                            else
                            {
                                day = Integer.parseInt(lista_horas[j]);
                                editTextId = editTextId+day;
                                ProgramarAlarma();
                            }
                            editTextName = "Alarma "+hour+":"+min;
                        }
                        Ingreso_EditText();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialoglayout = inflater.inflate(R.layout.pop_up_alarm,null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(Alarm.this);
                final AlertDialog alertDialog = builder.create();

                final CheckBox cbMonday = (CheckBox)dialoglayout.findViewById(R.id.cbMonday);
                final CheckBox cbTuesday = (CheckBox)dialoglayout.findViewById(R.id.cbTuesday);
                final CheckBox cbWednesday = (CheckBox)dialoglayout.findViewById(R.id.cbWednesday);
                final CheckBox cbThursday = (CheckBox)dialoglayout.findViewById(R.id.cbThursday);
                final CheckBox cbFriday = (CheckBox)dialoglayout.findViewById(R.id.cbFriday);
                final CheckBox cbSaturday = (CheckBox)dialoglayout.findViewById(R.id.cbSaturday);
                final CheckBox cbSunday = (CheckBox)dialoglayout.findViewById(R.id.cbSunday);
                final Button buttonAceptar = (Button)dialoglayout.findViewById(R.id.buttonAceptar);

                final CheckBox days[] = {cbSunday,cbMonday,cbTuesday,cbWednesday,cbThursday,cbFriday,cbSaturday};

                int actual_hour = calendar.get(Calendar.HOUR_OF_DAY);
                int actual_minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(Alarm.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, final int hourOfDay, final int minute) {
                        buttonAceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LayoutInflater inflater2 = getLayoutInflater();
                                final View dialoglayout2 = inflater2.inflate(R.layout.pop_up_alarm_switch,null);
                                final AlertDialog.Builder builder2 = new AlertDialog.Builder(Alarm.this);
                                final AlertDialog alertDialog2 = builder2.create();

                                final Button buttonAceptar2 = (Button)dialoglayout2.findViewById(R.id.buttonAceptar2);
                                final LinearLayout elementos2 = (LinearLayout)dialoglayout2.findViewById(R.id.elementos2);

                                db_alarm.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        lista_switch = dataSnapshot.child("lista_switch").getValue().toString();
                                        if (lista_switch != Integer.toString(0))
                                        {
                                            lista_ids = lista_switch.split(",");
                                            lista_estados = new boolean[lista_ids.length];
                                            for (int i = 0; i < lista_ids.length; i++) {
                                                switchId = Integer.parseInt(lista_ids[i]);
                                                switchNombre = dataSnapshot.child("switch").child(Integer.toString(switchId)).child("Nombre").getValue().toString();
                                                final Switch sw = new Switch(getApplicationContext());
                                                sw.setText(switchNombre);
                                                sw.setId(switchId);
                                                sw.setTextColor(Color.BLACK);
                                                elementos2.addView(sw);
                                                lista_estados[i] = false;
                                                sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                        for (int j = 0; j < lista_estados.length; j++)
                                                        {
                                                            if (lista_ids[j].equals(Integer.toString(buttonView.getId())))
                                                            {
                                                                lista_estados[j] = isChecked;
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        finish();
                                    }
                                });

                                buttonAceptar2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        editTextId = hourOfDay+""+minute;
                                        editTextName = "Alarma "+hourOfDay+":"+minute;
                                        alarmId = hourOfDay+","+minute;
                                        if (lista_alarmas != Integer.toString(0))
                                        {
                                            lista_alarmas = lista_alarmas+"/"+hourOfDay+","+minute;
                                        }
                                        else
                                        {
                                            lista_alarmas = ""+hourOfDay+","+minute;
                                        }

                                        for(int i = 0; i < days.length; i++)
                                        {
                                            if (days[i].isChecked())
                                            {
                                                hour = hourOfDay;
                                                min = minute;
                                                day = i+1;

                                                editTextId = editTextId+day;
                                                lista_alarmas = lista_alarmas+","+day;
                                                alarmId = alarmId+","+day;

                                                ProgramarAlarma();

                                                alertDialog.cancel();
                                                alertDialog2.cancel();
                                            }
                                        }
                                        Ingreso_EditText();
                                    }
                                });
                                alertDialog2.setView(dialoglayout2);
                                alertDialog2.show();
                            }
                        });
                        alertDialog.setView(dialoglayout);
                        alertDialog.show();
                    }
                },actual_hour,actual_minute,true);
                timePickerDialog.setTitle("Seleccionar la hora");
                timePickerDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_actions_alarms, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_back:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void ProgramarAlarma()
    {
        calendar.set(Calendar.DAY_OF_WEEK,day);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,min);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        Calendar now = Calendar.getInstance();
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MILLISECOND,0);

        /*if (calendar.before(now))
        {
            calendar.add(Calendar.DATE,7);
        }*/

        intent = new Intent(Alarm.this, AlarmReceiver.class);
        intent.putExtra("usuario",usuario);
        intent.putExtra("lista_ids",lista_ids);
        intent.putExtra("lista_estados",lista_estados);
        pendingIntent = PendingIntent.getBroadcast(Alarm.this, Integer.parseInt(editTextId), intent,0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),7*24*60*60*1000,pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }

    public void Ingreso_EditText ()
    {
        final EditText et = new EditText(getApplicationContext());
        et.setText(editTextName);
        et.setId(Integer.parseInt(editTextId));
        et.setTextColor(Color.BLACK);
        et.setEnabled(false);
        elementos.addView(et);
        for (int i = 0; i < lista_ids.length; i++)
        {
            if (i == 0) {
                value_switch = lista_ids[i] + "," + lista_estados[i];
            } else {
                value_switch = value_switch + "/" + lista_ids[i] + "," + lista_estados[i];
            }
        }
        db_alarm.child("alarmas").child(alarmId).setValue(value_switch);
        db_alarm.child("lista_alarmas").setValue(lista_alarmas);
    }
}
