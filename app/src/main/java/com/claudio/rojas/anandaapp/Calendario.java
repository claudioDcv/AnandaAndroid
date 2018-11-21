package com.claudio.rojas.anandaapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.auth0.android.jwt.JWT;
import com.claudio.rojas.anandaapp.dto.AlarmaRequestDto;
import com.claudio.rojas.anandaapp.dto.AlarmaResponseDto;
import com.claudio.rojas.anandaapp.dto.AlarmaSearchRequestDto;
import com.claudio.rojas.anandaapp.dto.LoginResponseDto;
import com.claudio.rojas.anandaapp.request.AlarmaService;
import com.claudio.rojas.anandaapp.request.LoginService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Calendario extends AppCompatActivity implements View.OnClickListener {

    public static final String PREFS_NAME = "MI_TOKEN";
    AlarmaService alarmaService;
    EditText etPlannedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        Spinner spinner = (Spinner) findViewById(R.id.spin_hora);
        // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.horas_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
                spinner.setAdapter(adapter);


        Spinner spinner2 = (Spinner) findViewById(R.id.spin_tipo);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.tipo_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);


        etPlannedDate = (EditText) findViewById(R.id.text_fecha);
        etPlannedDate.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_fecha:
                showDatePickerDialog();
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                // final String selectedDate = day + " / " + (month+1) + " / " + year;

                final String selectedDate = year + "-" + (month+1) + "-" + day;
                etPlannedDate.setText(selectedDate);
            }
        });
        newFragment.show(Calendario.this.getSupportFragmentManager(), "datePicker");
    }


    public void save(View view) {
        AlarmaRequestDto a = new AlarmaRequestDto();

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String tokenData = settings.getString("silentMode", "0");
        JWT decode = new JWT(tokenData);

        Spinner spin_tipo = findViewById(R.id.spin_tipo);
        Spinner spin_hora = findViewById(R.id.spin_hora);

        String spin_tipoS = (String) spin_tipo.getSelectedItem();
        String spin_horaS = (String) spin_hora.getSelectedItem();
        Long user_id = Long.parseLong(decode.getClaim("user_id").asString());
        EditText text_descripcion = findViewById(R.id.text_descripcion);
        EditText text_fecha = findViewById(R.id.text_fecha);


        a.setTipo(spin_tipoS);
        a.setHora(spin_horaS);
        a.setUsuario(user_id);
        a.setDescripcion(text_descripcion.getText().toString());
        a.setFecha(text_fecha.getText().toString());

        if (
                !a.getDescripcion().isEmpty()
                && !a.getFecha().isEmpty()
                && !a.getHora().isEmpty()
                && a.getUsuario() > 0
                && !a.getTipo().isEmpty()
                ) {
            AlarmaService alarmaService2 = new AlarmaService();
            alarmaService2.postAlarma(a).enqueue(new Callback<AlarmaResponseDto>() {
                @Override
                public void onResponse(Call<AlarmaResponseDto> call, Response<AlarmaResponseDto> response) {
                    AlertDialog alertDialog = new AlertDialog.Builder(Calendario.this).create();
                    alertDialog.setTitle("Alerta");
                    alertDialog.setMessage("Guardado con exito");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cerrar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Spinner spin_tipo = findViewById(R.id.spin_tipo);
                                    Spinner spin_hora = findViewById(R.id.spin_hora);
                                    EditText text_descripcion = findViewById(R.id.text_descripcion);
                                    EditText text_fecha = findViewById(R.id.text_fecha);
                                    text_descripcion.setText("");
                                    text_fecha.setText("");
                                }
                            });
                    alertDialog.show();
                }

                @Override
                public void onFailure(Call<AlarmaResponseDto> call, Throwable t) {
                    AlertDialog alertDialog = new AlertDialog.Builder(Calendario.this).create();
                    alertDialog.setTitle("Alerta");
                    alertDialog.setMessage("Error al guardar, intentelo mas tarde");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cerrar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(Calendario.this).create();
            alertDialog.setTitle("Alerta");
            alertDialog.setMessage("Todos los datos son obligatorios");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cerrar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    public void verListado(View view) {
        Intent intent = new Intent(this, Listado.class);
        startActivity(intent);
    }
}
