package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.db.HistoriaDAO;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.db.SecuenciaDAO;
import mta.epn.ginghogam.com.mitaller.db.TallerDAO;
import mta.epn.ginghogam.com.mitaller.db.TutorDAO;
import mta.epn.ginghogam.com.mitaller.db.VocabularioDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Secuencia;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;
import mta.epn.ginghogam.com.mitaller.entidades.Vocabulario;
import mta.epn.ginghogam.com.mitaller.utilidades.RealPathUtil;

public class LoginActivity extends AppCompatActivity {
    TextView usuario;
    TextView contraseña;
    private TutorDAO tutorDAO;
    private static  final String preference="mitaller.iniciosesion";
    public Integer id;
    private String nombre, apellido, ci, user, password;

    private Tutor tutor;
    private TallerDAO tallerDAO;
    private HistoriaDAO historiaDAO;
    private VocabularioDAO vocabularioDAO;
    private SecuenciaDAO secuenciaDAO;
    private Taller taller;
    private Historia historia;
    private Vocabulario vocabulario;
    private Secuencia secuencia;



    ArrayList<String> list;

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

        insertarRegistros();


    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Adios!",Toast.LENGTH_LONG).show();
        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
            Toast.makeText(LoginActivity.this, "Porfavor revisa los datos", Toast.LENGTH_SHORT).show();

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
            // Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();

        }else{
            // Toast.makeText(LoginActivity.this,""+ id, Toast.LENGTH_LONG).show();

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
            Toast.makeText(LoginActivity.this, "Porfavor revisa los datos", Toast.LENGTH_LONG).show();
            return false;

        }
    }

    public void modificarDatos(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistroTutorActivity.class);
        intent.putExtra("editar", true);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportActionBar().setCustomView(R.layout.titulo_login);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);

        return true;
    }


    private void insertarRegistros(){

        Bitmap buhobig = BitmapFactory.decodeResource(getResources(), R.drawable.buhobig);
        String path = Environment.getExternalStorageDirectory().toString();
        File file = new File(path, "buho" + ".jpg");
        OutputStream out = null;

        try{
            out = new FileOutputStream(file);
            buhobig.compress(Bitmap.CompressFormat.PNG,100,out);
            out.flush();
            out.close();
        }catch (Exception e){

        }

        final int[] mSongs = new int[] { R.raw.uno, R.raw.dos };
        list = new ArrayList<>();

        for (int i = 0; i < mSongs.length; i++) {
            try {
                String path2 = Environment.getExternalStorageDirectory().toString();
                File dir = new File(path2);
                if (dir.mkdirs() || dir.isDirectory()) {
                    String str_song_name = i + ".mp3";
                    CopyRAWtoSDCard(mSongs[i], path2 + File.separator + str_song_name);
                    list.add(path2 + File.separator + str_song_name);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        tallerDAO = new TallerDAO(this);
        historiaDAO = new HistoriaDAO(this);
        vocabularioDAO = new VocabularioDAO(this);
        secuenciaDAO = new SecuenciaDAO(this);

        Cursor cursor = tallerDAO.retrieve();

        if(cursor.getCount() == 0){
            taller = new Taller();
            historia = new Historia();
            vocabulario = new Vocabulario();
            secuencia = new Secuencia();

            String img = file.getPath().toString();
            taller.setNombreTaller("Panaderia");
            taller.setDescripcionTaller("En este taller inicial de panadería aprendemos los primeros conceptos y toda la teoría para elaborar panes básicos, con los ingredientes elementales que configuran un pan: harina, agua, levadura y sal.  Aprenderemos a usar diferentes tipos de harinas, sus usos y especificaciones técnicas.");
            taller.setImagenTaller(img);
            tallerDAO.create(taller);


            historia.setNombreHistoria("Historia 1");
            historia.setDescripcionHistoria("Aquí va una introducción de la historia. Se relata brevemente las actividades que se realiza.");
            historia.setNumeroLaminas("3");
            historia.setDificultad("facil");
            historia.setImagenHistoria(img);
            historia.setIdTaller(1);
            historiaDAO.create(historia);

            secuencia.setImagenSecuencia(img);
            secuencia.setOrdenImagenSecuencia(0);
            secuencia.setIdHistoria(1);
            secuenciaDAO.create(secuencia);


            secuencia.setImagenSecuencia(img);
            secuencia.setOrdenImagenSecuencia(1);
            secuencia.setIdHistoria(1);
            secuenciaDAO.create(secuencia);

            secuencia.setImagenSecuencia(img);
            secuencia.setOrdenImagenSecuencia(2);
            secuencia.setIdHistoria(1);
            secuenciaDAO.create(secuencia);



            historia.setNombreHistoria("Historia 2");
            historia.setDescripcionHistoria("Aquí va una introducción de la historia. Se relata brevemente las actividades que se realiza.");
            historia.setNumeroLaminas("6");
            historia.setDificultad("medio");
            historia.setImagenHistoria(img);
            historia.setIdTaller(1);
            historiaDAO.create(historia);


            historia.setNombreHistoria("Historia 3");
            historia.setDescripcionHistoria("Aquí va una introducción de la historia. Se relata brevemente las actividades que se realiza.");
            historia.setNumeroLaminas("9");
            historia.setDificultad("dificil");
            historia.setImagenHistoria(img);
            historia.setIdTaller(1);
            historiaDAO.create(historia);

            vocabulario.setPalabra("Harina");
            vocabulario.setTipoPalabra("Alimento");
            vocabulario.setImagenPalabra(img);
            vocabulario.setSonidoPalabra(list.get(1));
            vocabulario.setIdTaller(1);
            vocabularioDAO.create(vocabulario);


            vocabulario.setPalabra("Mantequilla");
            vocabulario.setTipoPalabra("Alimento");
            vocabulario.setImagenPalabra(img);
            vocabulario.setSonidoPalabra(list.get(0));
            vocabulario.setIdTaller(1);
            vocabularioDAO.create(vocabulario);








        }






    }
    private void CopyRAWtoSDCard(int id, String path) throws IOException {
        InputStream in = getResources().openRawResource(id);
        FileOutputStream out = new FileOutputStream(path);
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
    }

}

