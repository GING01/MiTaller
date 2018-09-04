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
    public String id;

    private Tutor tutor = new Tutor();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        usuario =findViewById(R.id.txtUsuario);
        contraseña=findViewById(R.id.txtcontraseña);
        if (obtenerEstado()){
            Intent i = new Intent(LoginActivity.this,MenuInicialActivity.class);
            //i.putExtra("tutor",tutor);
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
        preferences.edit().putInt("ID", tutor.getIdTutor()).commit();
        preferences.edit().putString("nombre", String.valueOf(tutor.getNombreTutor())).commit();
        preferences.edit().putString("usuario", String.valueOf(tutor.getUsuarioTutor())).commit();
        preferences.edit().putString("contraseña", String.valueOf(tutor.getContraseñaTutor())).commit();
        //SharedPreferences.Editor prefsEditor = preferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(tutor);
//        preferences.edit().putString("tutor", json);
//        preferences.edit().commit();
//        //preferences.edit().

        Toast.makeText(LoginActivity.this, tutor.getIdTutor(), Toast.LENGTH_LONG).show();

    }
    private boolean obtenerEstado() {

        SharedPreferences preferences = getSharedPreferences(preference, MODE_PRIVATE);
        Integer restoredText = preferences.getInt("ID", 0);

//        Gson gson = new Gson();
//        String json = preferences.getString("tutor", "");
//        Tutor obj = gson.fromJson(json, Tutor.class);

        if (restoredText != 0) {
            return true;
        }
        else
            return false;

    }

    private boolean validarUsuario() {
        tutorDAO =new TutorDAO(this);
        //tutor = new Tutor();
        String usr= "'"+usuario.getText().toString().trim()+"'";
        String pass = "'"+contraseña.getText().toString().trim()+"'";
        Cursor cursor = tutorDAO.retrieve(usr , pass);
        try {
            cursor.moveToFirst();
            tutor.setIdTutor(cursor.getInt(0));
            tutor.setNombreTutor(cursor.getString(1));
            tutor.setApellidoTutor(cursor.getString(2));
            tutor.setCiTutor(cursor.getString(3));
            tutor.setUsuarioTutor(cursor.getString(4));
            tutor.setContraseñaTutor(cursor.getString(5));
           // id = cursor.getString(0).trim();

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

