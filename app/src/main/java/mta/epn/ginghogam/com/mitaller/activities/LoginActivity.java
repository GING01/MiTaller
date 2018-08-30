package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.db.SecuenciaDAO;
import mta.epn.ginghogam.com.mitaller.db.TutorDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;

public class LoginActivity extends AppCompatActivity {
    TextView usuario;
    TextView contraseña;
    private SQLiteDB sqLiteDB;
    private TutorDAO tutorDAO;
    private Tutor tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        usuario =findViewById(R.id.txtUsuario);
        contraseña=findViewById(R.id.txtcontraseña);

    }
    public void registrarTutor(View view) {

        Intent intent = new Intent(LoginActivity.this, RegistroTutorActivity.class);
        intent.putExtra("editar", false);
        startActivity(intent);

    }

    public void validar(View view) {
        if(validarUsuario()){
            startActivity(new Intent(LoginActivity.this,MenuInicialActivity.class));

        }
        else
            Toast.makeText(LoginActivity.this, "No se pudo abrir", Toast.LENGTH_SHORT).show();

    }

    private boolean validarUsuario() {
        tutorDAO =new TutorDAO(this);
        List<Tutor> tutorList = new ArrayList<>();
        String usr= "'"+usuario.getText().toString().trim()+"'";
        String pass = "'"+contraseña.getText().toString().trim()+"'";
        Cursor cursor = tutorDAO.retrieve(usr , pass);
        try {
            cursor.moveToFirst();
            cursor.getString(0);
            return true;
        }
        catch (Exception e){
            Toast.makeText(LoginActivity.this, "Revisar Usuario o contraseña", Toast.LENGTH_LONG).show();
            return false;

        }
    }

    public void modificarDatos(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistroTutorActivity.class);
        intent.putExtra("editar", true);
        startActivity(intent);
    }
}
