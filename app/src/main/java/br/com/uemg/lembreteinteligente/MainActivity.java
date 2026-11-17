package br.com.uemg.lembreteinteligente;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
                    

                }
        );


    }
}