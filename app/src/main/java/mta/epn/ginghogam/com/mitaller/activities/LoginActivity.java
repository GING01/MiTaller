package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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
    private TutorDAO tutorDAO;
    private static  final String preference="mitaller.iniciosesion";
    public Integer id;
    private String nombre, apellido, ci, user, password;

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
        if (obtenerEstado(tutor)){
            Intent i = new Intent(LoginActivity.this,MenuInicialActivity.class);
            i.putExtra("tutor",tutor);
            startActivity(i);
            finish();
        }

    }
    public void registrarTutor(View view) {

        Intent intent = new Intent(LoginActivity.this, RegistroTutorActivity.class);
        intent.putExtra("editar", false);
        startActivity(intent);

    }

    public void validar(View view) {
        if(validarUsuario()){
            Intent intent = new Intent(LoginActivity.this,MenuInicialActivity.class);

            guardarEstado();
            intent.putExtra("tutor", tutor);
            startActivity(intent);
            finish();

        }
        else
            Toast.makeText(LoginActivity.this, "No se pudo abrir", Toast.LENGTH_SHORT).show();

    }

    private void guardarEstado() {

        SharedPreferences preferences =  getSharedPreferences(preference, MODE_PRIVATE);


        preferences.edit().putInt("ID", Integer.valueOf(id)).commit();
        preferences.edit().putString("nombre", String.valueOf(nombre)).commit();
        preferences.edit().putString("apellido", String.valueOf(apellido)).commit();
        preferences.edit().putString("usuario", String.valueOf(user)).commit();
        preferences.edit().putString("contraseña", String.valueOf(password)).commit();
        preferences.edit().putString("ci", String.valueOf(ci)).commit();



        if(tutor==null){
            Toast.makeText(LoginActivity.this, "PAto", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(LoginActivity.this,"datos"+ id, Toast.LENGTH_LONG).show();

        }

    }
    private boolean obtenerEstado(Tutor tutor) {

        SharedPreferences preferences = getSharedPreferences(preference, MODE_PRIVATE);
        Integer restoredText = preferences.getInt("ID", 0);


        if (restoredText != 0) {
            return true;
        }
        else
            return false;

    }

    private boolean validarUsuario() {
        tutorDAO =new TutorDAO(this);
        tutor = new Tutor();
        String usr= "'"+usuario.getText().toString().trim()+"'";
        String pass = "'"+contraseña.getText().toString().trim()+"'";
        Cursor cursor = tutorDAO.retrieve(usr , pass);
        try {
            cursor.moveToFirst();
            id = cursor.getInt(0);
            nombre = cursor.getString(1);
            apellido = cursor.getString(2);
            ci = cursor.getString(3);
            user = cursor.getString(4);
            password = cursor.getString(5);

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

