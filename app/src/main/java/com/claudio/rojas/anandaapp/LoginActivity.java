package com.claudio.rojas.anandaapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.auth0.android.jwt.JWT;
import com.claudio.rojas.anandaapp.dto.AlarmaResponseDto;
import com.claudio.rojas.anandaapp.dto.AlarmaSearchRequestDto;
import com.claudio.rojas.anandaapp.dto.CredentialRequestDto;
import com.claudio.rojas.anandaapp.dto.LoginResponseDto;
import com.claudio.rojas.anandaapp.request.AlarmaService;
import com.claudio.rojas.anandaapp.request.LoginService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    AlarmaService alarmaService;
    LoginService loginService;
    private String token = "";
    private Integer userId = null;
    private String CHANNEL_ID = "ananda_channel";
    private EditText username;
    private EditText password;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    public static final String PREFS_NAME = "MI_TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginService = new LoginService();
        alarmaService = new AlarmaService();

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String tokenData = settings.getString("silentMode", "");

        iniciarProcesoDeNotificaciones(tokenData);
    }

    public void handlerLogin(View view) {

        final Context ctx = this;

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        String strUsername = username.getText().toString();
        String strPassword = password.getText().toString();

        CredentialRequestDto credentialRequestDto = new CredentialRequestDto(strUsername, strPassword);

        System.out.println("LOGIN");

        loginService.onLogin(credentialRequestDto).enqueue(new Callback<LoginResponseDto>() {
            @Override
            public void onResponse(Call<LoginResponseDto> call, Response<LoginResponseDto> response) {
                JWT decode = new JWT(response.body().getToken());

                /** persiste token cuando se loguea **/
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("silentMode", response.body().getToken());
                // Commit the edits!
                editor.commit();

                System.out.println(decode.getClaim("username").asString());

                // seteando variable global para la app
                String ananda_token = response.body().getToken();

                ((LoginActivity) ctx).token = response.body().getToken();
                ((LoginActivity) ctx).userId = Integer.parseInt(decode.getClaim("user_id").asString());

                /** recupera token cuando se loguea **/
                SharedPreferences settings2 = getSharedPreferences(PREFS_NAME, 0);
                String tokenData = settings2.getString("silentMode", "");
                iniciarProcesoDeNotificaciones(tokenData);
            }

            @Override
            public void onFailure(Call<LoginResponseDto> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public void createNot(String titulo, String contenido) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(titulo)
                .setContentText(contenido)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(this, LoginActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, mBuilder.build());
    }

    public void iniciarProcesoDeNotificaciones(final String tokenData) {
        final Context ctx = this;
        final Handler h = new Handler();
        // Tiempo en que se ejecutara cada loop
        final Integer TIEMPO = 10000;

        // aqui se crea el loop que preguntara a la api
        h.postDelayed(new Runnable()
        {
            private long time = 0;

            @Override
            public void run()
            {
                // do stuff then
                // can call h again after work!
                time += TIEMPO;

                if (!tokenData.equals("")) {
                    JWT decode = new JWT(tokenData);
                    String user_id = decode.getClaim("user_id").asString();
                    System.out.println(">> LOOP NOTI EJECUTADO");

                    ((LoginActivity)ctx).getAllAlarmas(ctx, tokenData);
                }

                h.postDelayed(this, TIEMPO);
            }
        }, TIEMPO); // 1 second delay (takes millis)


        if (!tokenData.equals("")) {
            Intent intent = new Intent(this, Calendario.class);
            intent.putExtra("token", tokenData);
            startActivity(intent);
        }

    }

    public String getFechaActual() {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(new Date());
        return date;
    }

    public String getHoraActual() {
        String pattern = "HH";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(new Date());
        return date;
    }

    private void getAllAlarmas(final Context ctx, String token) {
        JWT decode = new JWT(token);
        Long userId = Long.parseLong(decode.getClaim("user_id").asString());

        if (userId != null) {

            AlarmaSearchRequestDto alarmaSearchRequestDto = new AlarmaSearchRequestDto();
            alarmaSearchRequestDto.setUsuario(userId);
            alarmaSearchRequestDto.setFecha(getFechaActual());
            alarmaSearchRequestDto.setHora(getHoraActual() + ":00:00");

            System.out.println(getHoraActual() + ":00:00");
            System.out.println(getFechaActual());

            System.out.println(alarmaSearchRequestDto.getUsuario());

            alarmaService.getAlarmas(alarmaSearchRequestDto).enqueue(new Callback<List<AlarmaResponseDto>>() {

                @Override
                public void onResponse(Call<List<AlarmaResponseDto>> call, Response<List<AlarmaResponseDto>> response) {
                    System.out.println("EN onResponse");
                    System.out.println(response.body());
                    if (response.body() != null) {
                        List<AlarmaResponseDto> alm = response.body();
                        System.out.println("EN RESULT");

                        if (alm != null) {
                            if(alm.size() > 0) {
                                if (alm.get(0) != null) {
                                    ((LoginActivity) ctx).createNot(alm.get(0).getTipo(), alm.get(0).getDescripcion());
                                    alarmaService.deleteAlarma(alm.get(0).getId()).enqueue(new Callback<Void>() {

                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {

                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {

                                        }
                                    });
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<AlarmaResponseDto>> call, Throwable t) {
                    // System.out.println(t.getMessage());
                }
            });

        }
    }
}

