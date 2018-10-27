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
    private static final String preference = "mitaller.iniciosesion";
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
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        usuario = findViewById(R.id.txtUsuario);
        contraseña = findViewById(R.id.txtcontraseña);
        if (obtenerEstado(tutor)) {
            Intent i = new Intent(LoginActivity.this, MenuInicialActivity.class);
            i.putExtra("tutor", tutor);
            startActivity(i);
            finish();
        }

        insertarRegistros();


    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Adios!", Toast.LENGTH_LONG).show();
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
        if (validarUsuario()) {
            Intent intent = new Intent(LoginActivity.this, MenuInicialActivity.class);

            guardarEstado();
            intent.putExtra("tutor", tutor);
            startActivity(intent);
            finish();

        } else
            Toast.makeText(LoginActivity.this, "Porfavor revisa los datos", Toast.LENGTH_SHORT).show();

    }

    private void guardarEstado() {

        SharedPreferences preferences = getSharedPreferences(preference, MODE_PRIVATE);


        preferences.edit().putInt("ID", Integer.valueOf(id)).commit();
        preferences.edit().putString("nombre", String.valueOf(nombre)).commit();
        preferences.edit().putString("apellido", String.valueOf(apellido)).commit();
        preferences.edit().putString("usuario", String.valueOf(user)).commit();
        preferences.edit().putString("contraseña", String.valueOf(password)).commit();
        preferences.edit().putString("ci", String.valueOf(ci)).commit();


        if (tutor == null) {
            // Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();

        } else {
            // Toast.makeText(LoginActivity.this,""+ id, Toast.LENGTH_LONG).show();

        }

    }

    private boolean obtenerEstado(Tutor tutor) {
        SharedPreferences preferences = getSharedPreferences(preference, MODE_PRIVATE);
        Integer restoredText = preferences.getInt("ID", 0);

        if (restoredText != 0) {
            return true;
        } else
            return false;
    }

    private boolean validarUsuario() {
        tutorDAO = new TutorDAO(this);
        tutor = new Tutor();
        String usr = "'" + usuario.getText().toString().trim() + "'";
        String pass = "'" + contraseña.getText().toString().trim() + "'";
        Cursor cursor = tutorDAO.retrieve(usr, pass);
        try {
            cursor.moveToFirst();
            id = cursor.getInt(0);
            nombre = cursor.getString(1);
            apellido = cursor.getString(2);
            ci = cursor.getString(3);
            user = cursor.getString(4);
            password = cursor.getString(5);

            return true;
        } catch (Exception e) {
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


    private void insertarRegistros() {



        tallerDAO = new TallerDAO(this);
        historiaDAO = new HistoriaDAO(this);
        vocabularioDAO = new VocabularioDAO(this);
        secuenciaDAO = new SecuenciaDAO(this);

        Cursor cursor = tallerDAO.retrieve();

        if (cursor.getCount() == 0) {


            String path = Environment.getExternalStorageDirectory().toString();

            Bitmap panaderia = BitmapFactory.decodeResource(getResources(), R.drawable.panaderiaport);
            File filePanaderia = new File(path, "panaderia" + ".jpg");

            Bitmap historiaEmpaquetado = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_empaquetado1);
            File fileHistoriaEmpaquetado = new File(path, "empaquetado" + ".jpg");

            Bitmap amasadora = BitmapFactory.decodeResource(getResources(), R.drawable.vocabulario_amasadora);
            File fileAmasadora = new File(path, "amasadora" + ".jpg");

            Bitmap balanza = BitmapFactory.decodeResource(getResources(), R.drawable.vocabulario_balanza);
            File fileBalanza = new File(path, "balanza" + ".jpg");

            Bitmap bodega = BitmapFactory.decodeResource(getResources(), R.drawable.vocabulario_bodega);
            File fileBodega = new File(path, "bodega" + ".jpg");

            Bitmap boleadora = BitmapFactory.decodeResource(getResources(), R.drawable.vocabulario_boleadora);
            File fileBoleadora = new File(path, "boleadora" + ".jpg");

            Bitmap camaraLeudo = BitmapFactory.decodeResource(getResources(), R.drawable.vocabulario_camara_de_leudo);
            File fileCamaraLeudo = new File(path, "camaraLeudo" + ".jpg");

            Bitmap congelador = BitmapFactory.decodeResource(getResources(), R.drawable.vocabulario_congelador);
            File fileCongelador = new File(path, "congelador" + ".jpg");

            Bitmap cortadorPan = BitmapFactory.decodeResource(getResources(), R.drawable.vocabulario_cortador_de_pan);
            File fileCortadorPan = new File(path, "cortadorPan" + ".jpg");

            Bitmap horno = BitmapFactory.decodeResource(getResources(), R.drawable.vocabulario_horno);
            File fileHorno = new File(path, "horno" + ".jpg");

            Bitmap laminadora = BitmapFactory.decodeResource(getResources(), R.drawable.vocabulario_laminadora);
            File fileLaminadora = new File(path, "laminadora" + ".jpg");

            Bitmap meson = BitmapFactory.decodeResource(getResources(), R.drawable.vocabulario_meson_de_trabajo);
            File fileMeson = new File(path, "meson" + ".jpg");

            Bitmap refrigeradora = BitmapFactory.decodeResource(getResources(), R.drawable.vocabulario_refrigeradora);
            File fileRefrigeradora = new File(path, "refrigeradora" + ".jpg");


            //sequence

            Bitmap empaquetado1 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_empaquetado1);
            File fileEmpaquetado1 = new File(path, "empaquetado1" + ".jpg");

            Bitmap empaquetado2 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_empaquetado2);
            File fileEmpaquetado2 = new File(path, "empaquetado2" + ".jpg");

            Bitmap empaquetado3 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_empaquetado3);
            File fileEmpaquetado3 = new File(path, "empaquetado3" + ".jpg");

            Bitmap empaquetado4 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_empaquetado4);
            File fileEmpaquetado4 = new File(path, "empaquetado4" + ".jpg");

            Bitmap empaquetado5 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_empaquetado5);
            File fileEmpaquetado5 = new File(path, "empaquetado5" + ".jpg");



            OutputStream out = null;

            try {
                out = new FileOutputStream(filePanaderia);
                panaderia.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileHistoriaEmpaquetado);
                historiaEmpaquetado.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileAmasadora);
                amasadora.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileBalanza);
                balanza.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileBodega);
                bodega.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileBoleadora);
                boleadora.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileCamaraLeudo);
                camaraLeudo.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileCongelador);
                congelador.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileCortadorPan);
                cortadorPan.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileHorno);
                horno.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileLaminadora);
                laminadora.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileMeson);
                meson.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileRefrigeradora);
                refrigeradora.compress(Bitmap.CompressFormat.PNG, 100, out);




                //sequence

                out = new FileOutputStream(fileEmpaquetado1);
                empaquetado1.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileEmpaquetado2);
                empaquetado2.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileEmpaquetado3);
                empaquetado3.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileEmpaquetado4);
                empaquetado4.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileEmpaquetado5);
                empaquetado5.compress(Bitmap.CompressFormat.PNG, 100, out);


                out.flush();
                out.close();
            } catch (Exception e) {

            }

            final int[] mSongs = new int[]{R.raw.amasadora, R.raw.balanza, R.raw.boleadora, R.raw.camara_leudo, R.raw.congelador,
                    R.raw.cortador_pan, R.raw.horno, R.raw.laminadora, R.raw.meson, R.raw.refrigeradora};
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



            taller = new Taller();
            historia = new Historia();
            vocabulario = new Vocabulario();
            secuencia = new Secuencia();

            String panaderiaport = filePanaderia.getPath().toString();
            taller.setNombreTaller("Panaderia");
            taller.setDescripcionTaller("En este taller de panadería aprendemos a elaborar panes básicos. Con los ingredientes elementales que configuran un pan: harina, agua, levadura y sal.  Aprenderemos a usar diferentes tipos de harinas, sus usos y especificaciones técnicas.");
            taller.setImagenTaller(panaderiaport);
            taller.setIdTaller(1);
            tallerDAO.create(taller);


            String empaquetado = fileHistoriaEmpaquetado.getPath().toString();
            historia.setNombreHistoria("Empaquetado");
            historia.setDescripcionHistoria("Una vez que se encuentran horneados los productos, es necesario empaquetarlos para su distribución al público. " +
                    "Ten en cuenta que debes usar la máquina selladora. Debes tner cuidado ya que esta se encuentra caliente");
            historia.setNumeroLaminas("5");
            historia.setDificultad("medio");
            historia.setImagenHistoria(empaquetado);
            historia.setIdTaller(1);
            historiaDAO.create(historia);



            String pathEmpaquetado1 = fileEmpaquetado1.getPath().toString();
            secuencia.setImagenSecuencia(pathEmpaquetado1);
            secuencia.setOrdenImagenSecuencia(0);
            secuencia.setDescripcionImagenSecuencia("Uno");
            secuencia.setIdHistoria(1);
            secuenciaDAO.create(secuencia);


            String pathEmpaquetado2 = fileEmpaquetado2.getPath().toString();
            secuencia.setImagenSecuencia(pathEmpaquetado2);
            secuencia.setOrdenImagenSecuencia(1);
            secuencia.setDescripcionImagenSecuencia("Dos");

            secuencia.setIdHistoria(1);
            secuenciaDAO.create(secuencia);


            String pathEmpaquetado3 = fileEmpaquetado3.getPath().toString();
            secuencia.setImagenSecuencia(pathEmpaquetado3);
            secuencia.setOrdenImagenSecuencia(2);
            secuencia.setDescripcionImagenSecuencia("Tres");

            secuencia.setIdHistoria(1);
            secuenciaDAO.create(secuencia);

            String pathEmpaquetado4 = fileEmpaquetado4.getPath().toString();
            secuencia.setImagenSecuencia(pathEmpaquetado4);
            secuencia.setOrdenImagenSecuencia(2);
            secuencia.setDescripcionImagenSecuencia("Cuatro");

            secuencia.setIdHistoria(1);
            secuenciaDAO.create(secuencia);

            String pathEmpaquetado5 = fileEmpaquetado5.getPath().toString();
            secuencia.setImagenSecuencia(pathEmpaquetado5);
            secuencia.setOrdenImagenSecuencia(2);
            secuencia.setDescripcionImagenSecuencia("Cinco");
            secuencia.setIdHistoria(1);
            secuenciaDAO.create(secuencia);


            String pathAmasadora = fileAmasadora.getPath().toString();
            vocabulario.setPalabra("Amasadora");
            vocabulario.setTipoPalabra("Herramienta");
            vocabulario.setImagenPalabra(pathAmasadora);
            vocabulario.setSonidoPalabra(list.get(0));
            vocabulario.setIdTaller(1);
            vocabularioDAO.create(vocabulario);

            String pathBalazna = fileBalanza.getPath().toString();
            vocabulario.setPalabra("Balanza");
            vocabulario.setTipoPalabra("Herramienta");
            vocabulario.setImagenPalabra(pathBalazna);
            vocabulario.setSonidoPalabra(list.get(1));
            vocabulario.setIdTaller(1);
            vocabularioDAO.create(vocabulario);


            String pathBoleadora = fileBoleadora.getPath().toString();
            vocabulario.setPalabra("Boleadora");
            vocabulario.setTipoPalabra("Herramienta");
            vocabulario.setImagenPalabra(pathBoleadora);
            vocabulario.setSonidoPalabra(list.get(2));
            vocabulario.setIdTaller(1);
            vocabularioDAO.create(vocabulario);


            String pathCamaraLeudo = fileCamaraLeudo.getPath().toString();
            vocabulario.setPalabra("Cámara de Leudo");
            vocabulario.setTipoPalabra("Herramienta");
            vocabulario.setImagenPalabra(pathCamaraLeudo);
            vocabulario.setSonidoPalabra(list.get(3));
            vocabulario.setIdTaller(1);
            vocabularioDAO.create(vocabulario);


            String pathCongelador = fileCongelador.getPath().toString();
            vocabulario.setPalabra("Congelador");
            vocabulario.setTipoPalabra("Herramienta");
            vocabulario.setImagenPalabra(pathCongelador);
            vocabulario.setSonidoPalabra(list.get(4));
            vocabulario.setIdTaller(1);
            vocabularioDAO.create(vocabulario);


            String pathCortadorPan = fileCortadorPan.getPath().toString();
            vocabulario.setPalabra("Cortador de Pan");
            vocabulario.setTipoPalabra("Herramienta");
            vocabulario.setImagenPalabra(pathCortadorPan);
            vocabulario.setSonidoPalabra(list.get(5));
            vocabulario.setIdTaller(1);
            vocabularioDAO.create(vocabulario);

            String pathHorno = fileHorno.getPath().toString();
            vocabulario.setPalabra("Horno");
            vocabulario.setTipoPalabra("Herramienta");
            vocabulario.setImagenPalabra(pathHorno);
            vocabulario.setSonidoPalabra(list.get(6));
            vocabulario.setIdTaller(1);
            vocabularioDAO.create(vocabulario);


            String pathLaminadora = fileLaminadora.getPath().toString();
            vocabulario.setPalabra("Laminadora");
            vocabulario.setTipoPalabra("Herramienta");
            vocabulario.setImagenPalabra(pathLaminadora);
            vocabulario.setSonidoPalabra(list.get(7));
            vocabulario.setIdTaller(1);
            vocabularioDAO.create(vocabulario);


            String pathMeson = fileMeson.getPath().toString();
            vocabulario.setPalabra("Meson");
            vocabulario.setTipoPalabra("Herramienta");
            vocabulario.setImagenPalabra(pathMeson);
            vocabulario.setSonidoPalabra(list.get(8));
            vocabulario.setIdTaller(1);
            vocabularioDAO.create(vocabulario);



            String pathRefrigeradora = fileRefrigeradora.getPath().toString();
            vocabulario.setPalabra("Refrigeradora");
            vocabulario.setTipoPalabra("Herramienta");
            vocabulario.setImagenPalabra(pathRefrigeradora);
            vocabulario.setSonidoPalabra(list.get(9));
            vocabulario.setIdTaller(1);
            vocabularioDAO.create(vocabulario);


        }


        cursor.close();
        vocabularioDAO.close();

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

