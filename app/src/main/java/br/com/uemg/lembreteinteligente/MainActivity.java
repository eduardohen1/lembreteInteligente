package br.com.uemg.lembreteinteligente;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String CANAL_ID = "canal_lembretes";
    private static final int CODIGO_PERMISSAO = 100;
    private static final int ID_NOTIFICACAO = 1;

    private EditText edtLembrete;
    private Button btnNotificar;
    private TextView txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //configurar o parse entre XML e Java
        edtLembrete = findViewById(R.id.edtLembrete);
        btnNotificar = findViewById(R.id.btnNotificar);
        txtStatus = findViewById(R.id.txtStatus);

        criarCanalNotificacao();

        btnNotificar.setOnClickListener(
                v -> {
                    //o trim() remove os espaços em branco do início e do fim da string
                    String mensagem = edtLembrete.getText().toString().trim();
                    if(mensagem.isEmpty()){
                        edtLembrete.setError("Digite um lembrete");
                        edtLembrete.requestFocus();
                        return; //mato a execução do metodo
                    }
                    //verifico se a permissão de notificação foi concedida
                    if(Build.VERSION.SDK_INT >= 33 &&
                        checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                                != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(
                                new String[]{
                                        android.Manifest.permission.POST_NOTIFICATIONS
                                }, CODIGO_PERMISSAO
                        );
                        return; //mato a execução do metodo
                    }
                    enviarNotificacao(mensagem);
                }
        );
    }

    private void criarCanalNotificacao(){
        //letra maiúscula O é a versão 26 do Android (Android 8.0)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel canal =
                    new NotificationChannel(CANAL_ID, "Lembretes da aplicação",
                            android.app.NotificationManager.IMPORTANCE_DEFAULT);
            canal.setDescription("Avisos criados pelo Lembrete Inteligente");
            NotificationManager gerenciador = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            gerenciador.createNotificationChannel(canal);
        }
    }

    private void enviarNotificacao(String mensagem){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );
        Notification.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder = new Notification.Builder(this, CANAL_ID);
        } else {
            builder = new Notification.Builder(this);
            builder.setPriority(Notification.PRIORITY_DEFAULT);
        }
        Notification notification = builder
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Meu Lembrete")
                .setContentText(mensagem)
                .setStyle(
                        new Notification.BigTextStyle().bigText(mensagem)
                )
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        NotificationManager gerenciador = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        gerenciador.notify(ID_NOTIFICACAO, notification);
        txtStatus.setText("Notificação enviada: " + mensagem);
        Toast.makeText(this, "Confira as notificações!", Toast.LENGTH_SHORT).show();

    }

}