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

            Bitmap historiaGlaceado = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_glaceado1);
            File fileHistoriaGlaceado = new File(path, "glaceado" + ".jpg");

            Bitmap historiaLimpiezaJabas = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_jabas1);
            File fileHistoriaLimpiezaJabas = new File(path, "limpieza_jabas" + ".jpg");

            Bitmap historiaLimpiezaLatas = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_latas1);
            File fileHistoriaLimpiezaLatas = new File(path, "limpieza_latas" + ".jpg");

            Bitmap historiaLimpiezaGeneral = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpiezageneral1);
            File fileHistoriaLimpiezaGeneral = new File(path, "limpieza_general" + ".jpg");

            Bitmap historiaPreparacionLatas = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_latas1);
            File fileHistoriaPreparacionLatas = new File(path, "preparacion_latas" + ".jpg");

            Bitmap historiaPreparacionMasa = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_masa1);
            File fileHistoriaPreparacionMasa= new File(path, "preparacion_masa" + ".jpg");

            Bitmap historiaPreparacionUniforme = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_uniforme1);
            File fileHistoriaPreparacionUniforme = new File(path, "preparacion_uniforme" + ".jpg");

            Bitmap historiaPreparacionMoldes = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_moldes1);
            File fileHistoriaPreparacionMoldes = new File(path, "preparacion_moldes" + ".jpg");

            Bitmap historiaProtocoloAsepcia = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_protocolo_asepcia1);
            File fileHistoriaProtocoloAsepcia = new File(path, "protocolo_asepcia" + ".jpg");



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


            //sequence empaquetado

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

            //sequence glaceado

            Bitmap galceado1 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_glaceado1);
            File fileGlaceado1 = new File(path, "glaceado1" + ".jpg");

            Bitmap galceado2 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_glaceado2);
            File fileGlaceado2 = new File(path, "glaceado2" + ".jpg");

            Bitmap galceado3 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_glaceado3);
            File fileGlaceado3 = new File(path, "glaceado3" + ".jpg");

            Bitmap galceado4 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_glaceado4);
            File fileGlaceado4 = new File(path, "glaceado4" + ".jpg");

            Bitmap galceado5 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_glaceado5);
            File fileGlaceado5 = new File(path, "glaceado5" + ".jpg");

            Bitmap galceado6 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_glaceado6);
            File fileGlaceado6 = new File(path, "glaceado6" + ".jpg");

            Bitmap galceado7 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_glaceado7);
            File fileGlaceado7 = new File(path, "glaceado7" + ".jpg");

            //sequence limpieza jabas

            Bitmap limpiezaJabas1 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_jabas1);
            File filelimpiezaJabas1 = new File(path, "limpieza_jabas1" + ".jpg");

            Bitmap limpiezaJabas2 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_jabas2);
            File filelimpiezaJabas2 = new File(path, "limpieza_jabas2" + ".jpg");

            Bitmap limpiezaJabas3 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_jabas3);
            File filelimpiezaJabas3 = new File(path, "limpieza_jabas3" + ".jpg");

            Bitmap limpiezaJabas4 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_jabas4);
            File filelimpiezaJabas4 = new File(path, "limpieza_jabas4" + ".jpg");

            Bitmap limpiezaJabas5 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_jabas5);
            File filelimpiezaJabas5 = new File(path, "limpieza_jabas5" + ".jpg");

            //sequence limpieza latas

            Bitmap limpiezaLatas1 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_latas1);
            File filelimpiezaLatas1 = new File(path, "limpieza_latas1" + ".jpg");

            Bitmap limpiezaLatas2 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_latas2);
            File filelimpiezaLatas2 = new File(path, "limpieza_latas2" + ".jpg");

            Bitmap limpiezaLatas3 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_latas3);
            File filelimpiezaLatas3 = new File(path, "limpieza_latas3" + ".jpg");

            Bitmap limpiezaLatas4 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_latas4);
            File filelimpiezaLatas4 = new File(path, "limpieza_latas4" + ".jpg");

            Bitmap limpiezaLatas5 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_latas5);
            File filelimpiezaLatas5 = new File(path, "limpieza_latas5" + ".jpg");

            Bitmap limpiezaLatas6 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_latas6);
            File filelimpiezaLatas6 = new File(path, "limpieza_latas6" + ".jpg");

            Bitmap limpiezaLatas7 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_latas7);
            File filelimpiezaLatas7 = new File(path, "limpieza_latas7" + ".jpg");

            Bitmap limpiezaLatas8 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_latas8);
            File filelimpiezaLatas8 = new File(path, "limpieza_latas8" + ".jpg");

            Bitmap limpiezaLatas9 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_latas9);
            File filelimpiezaLatas9 = new File(path, "limpieza_latas9" + ".jpg");

            Bitmap limpiezaLatas10 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_latas10);
            File filelimpiezaLatas10 = new File(path, "limpieza_latas10" + ".jpg");

            Bitmap limpiezaLatas11 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpieza_latas11);
            File filelimpiezaLatas11 = new File(path, "limpieza_latas11" + ".jpg");

            //sequence limpieza general

            Bitmap limpiezaGeneral1 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpiezageneral1);
            File filelimpiezaGeneral1 = new File(path, "limpieza_general1" + ".jpg");

            Bitmap limpiezaGeneral2 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpiezageneral2);
            File filelimpiezaGeneral2 = new File(path, "limpieza_general2" + ".jpg");

            Bitmap limpiezaGeneral3 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpiezageneral3);
            File filelimpiezaGeneral3 = new File(path, "limpieza_general3" + ".jpg");

            Bitmap limpiezaGeneral4 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpiezageneral4);
            File filelimpiezaGeneral4 = new File(path, "limpieza_general4" + ".jpg");

            Bitmap limpiezaGeneral5 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpiezageneral5);
            File filelimpiezaGeneral5 = new File(path, "limpieza_general5" + ".jpg");

            Bitmap limpiezaGeneral6 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpiezageneral6);
            File filelimpiezaGeneral6 = new File(path, "limpieza_general6" + ".jpg");

            Bitmap limpiezaGeneral7 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpiezageneral7);
            File filelimpiezaGeneral7 = new File(path, "limpieza_general7" + ".jpg");

            Bitmap limpiezaGeneral8 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpiezageneral8);
            File filelimpiezaGeneral8 = new File(path, "limpieza_general8" + ".jpg");

            Bitmap limpiezaGeneral9 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpiezageneral9);
            File filelimpiezaGeneral9 = new File(path, "limpieza_general9" + ".jpg");

            Bitmap limpiezaGeneral10 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_limpiezageneral10);
            File filelimpiezaGeneral10 = new File(path, "limpieza_general10" + ".jpg");

            //sequence preparacion latas

            Bitmap preparacionLatas1 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_latas1);
            File filePreparacionLatas1 = new File(path, "preparacion_latas1" + ".jpg");

            Bitmap preparacionLatas2 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_latas2);
            File filePreparacionLatas2 = new File(path, "preparacion_latas2" + ".jpg");

            Bitmap preparacionLatas3 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_latas3);
            File filePreparacionLatas3 = new File(path, "preparacion_latas3" + ".jpg");

            Bitmap preparacionLatas4 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_latas4);
            File filePreparacionLatas4 = new File(path, "preparacion_latas4" + ".jpg");

            Bitmap preparacionLatas5 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_latas5);
            File filePreparacionLatas5 = new File(path, "preparacion_latas5" + ".jpg");



            //sequence preparacion masa

            Bitmap preparacionMasa1 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_masa1);
            File filePreparacionMasa1 = new File(path, "preparacion_masa1" + ".jpg");

            Bitmap preparacionMasa2 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_masa2);
            File filePreparacionMasa2 = new File(path, "preparacion_masa2" + ".jpg");

            Bitmap preparacionMasa3 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_masa3);
            File filePreparacionMasa3 = new File(path, "preparacion_masa3" + ".jpg");

            Bitmap preparacionMasa4 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_masa4);
            File filePreparacionMasa4 = new File(path, "preparacion_masa4" + ".jpg");

            Bitmap preparacionMasa5 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_masa5);
            File filePreparacionMasa5 = new File(path, "preparacion_masa5" + ".jpg");

            Bitmap preparacionMasa6 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_masa6);
            File filePreparacionMasa6 = new File(path, "preparacion_masa16" + ".jpg");

            Bitmap preparacionMasa7 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_masa7);
            File filePreparacionMasa7 = new File(path, "preparacion_masa7" + ".jpg");

            Bitmap preparacionMasa8 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_masa8);
            File filePreparacionMasa8 = new File(path, "preparacion_masa8" + ".jpg");

            Bitmap preparacionMasa9 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_masa9);
            File filePreparacionMasa9 = new File(path, "preparacion_masa9" + ".jpg");

            Bitmap preparacionMasa10 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_masa10);
            File filePreparacionMasa10 = new File(path, "preparacion_masa10" + ".jpg");

            Bitmap preparacionMasa11 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_masa11);
            File filePreparacionMasa11 = new File(path, "preparacion_masa11" + ".jpg");

            Bitmap preparacionMasa12 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_masa12);
            File filePreparacionMasa12 = new File(path, "preparacion_masa12" + ".jpg");

            Bitmap preparacionMasa13 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_masa13);
            File filePreparacionMasa13 = new File(path, "preparacion_masa13" + ".jpg");

            Bitmap preparacionMasa14 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_masa14);
            File filePreparacionMasa14 = new File(path, "preparacion_masa14" + ".jpg");

            //sequence preparacion uniforme

            Bitmap preparacionUniforme1 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_uniforme1);
            File filePreparacionUniforme1 = new File(path, "preparacion_uniforme1" + ".jpg");

            Bitmap preparacionUniforme2 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_uniforme2);
            File filePreparacionUniforme2 = new File(path, "preparacion_uniforme2" + ".jpg");

            Bitmap preparacionUniforme3 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_uniforme3);
            File filePreparacionUniforme3 = new File(path, "preparacion_uniforme3" + ".jpg");

            Bitmap preparacionUniforme4 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_uniforme4);
            File filePreparacionUniforme4 = new File(path, "preparacion_uniforme4" + ".jpg");

            Bitmap preparacionUniforme5 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_uniforme5);
            File filePreparacionUniforme5 = new File(path, "preparacion_uniforme5" + ".jpg");

            Bitmap preparacionUniforme6 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_uniforme6);
            File filePreparacionUniforme6 = new File(path, "preparacion_uniforme6" + ".jpg");

            //sequence preparacion moldes

            Bitmap preparacionMolde1 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_moldes1);
            File filePreparacionMolde1 = new File(path, "preparacion_mo1de1" + ".jpg");

            Bitmap preparacionMolde2 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_moldes2);
            File filePreparacionMolde2 = new File(path, "preparacion_mo1de2" + ".jpg");

            Bitmap preparacionMolde3 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_moldes3);
            File filePreparacionMolde3 = new File(path, "preparacion_mo1de3" + ".jpg");

            Bitmap preparacionMolde4 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_moldes4);
            File filePreparacionMolde4 = new File(path, "preparacion_mo1de4" + ".jpg");

            Bitmap preparacionMolde5 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_moldes5);
            File filePreparacionMolde5 = new File(path, "preparacion_mo1de5" + ".jpg");

            Bitmap preparacionMolde6 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_moldes6);
            File filePreparacionMolde6 = new File(path, "preparacion_mo1de6" + ".jpg");

            Bitmap preparacionMolde7 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_moldes7);
            File filePreparacionMolde7 = new File(path, "preparacion_mo1de7" + ".jpg");

            Bitmap preparacionMolde8 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_moldes8);
            File filePreparacionMolde8 = new File(path, "preparacion_mo1de8" + ".jpg");

            Bitmap preparacionMolde9 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_moldes9);
            File filePreparacionMolde9 = new File(path, "preparacion_mo1de9" + ".jpg");

            Bitmap preparacionMolde10 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_preparacion_moldes10);
            File filePreparacionMolde10 = new File(path, "preparacion_mo1de10" + ".jpg");


            //sequence protocolo asepcia

            Bitmap protocoloAsepcia1 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_protocolo_asepcia1);
            File fileProtocoloAsepcia1 = new File(path, "protocolo_asepcia1" + ".jpg");

            Bitmap protocoloAsepcia2 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_protocolo_asepcia2);
            File fileProtocoloAsepcia2 = new File(path, "protocolo_asepcia2" + ".jpg");

            Bitmap protocoloAsepcia3 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_protocolo_asepcia3);
            File fileProtocoloAsepcia3 = new File(path, "protocolo_asepcia3" + ".jpg");

            Bitmap protocoloAsepcia4 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_protocolo_asepcia4);
            File fileProtocoloAsepcia4 = new File(path, "protocolo_asepcia4" + ".jpg");

            Bitmap protocoloAsepcia5 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_protocolo_asepcia5);
            File fileProtocoloAsepcia5 = new File(path, "protocolo_asepcia5" + ".jpg");

            Bitmap protocoloAsepcia6 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_protocolo_asepcia6);
            File fileProtocoloAsepcia6 = new File(path, "protocolo_asepcia6" + ".jpg");

            Bitmap protocoloAsepcia7 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_protocolo_asepcia7);
            File fileProtocoloAsepcia7 = new File(path, "protocolo_asepcia7" + ".jpg");

            Bitmap protocoloAsepcia8 = BitmapFactory.decodeResource(getResources(), R.drawable.secuencia_protocolo_asepcia8);
            File fileProtocoloAsepcia8 = new File(path, "protocolo_asepcia8" + ".jpg");





            OutputStream out = null;

            try {
                out = new FileOutputStream(filePanaderia);
                panaderia.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileHistoriaEmpaquetado);
                historiaEmpaquetado.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileHistoriaGlaceado);
                historiaGlaceado.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileHistoriaLimpiezaJabas);
                historiaLimpiezaJabas.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileHistoriaLimpiezaLatas);
                historiaLimpiezaLatas.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileHistoriaLimpiezaGeneral);
                historiaLimpiezaGeneral.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileHistoriaPreparacionLatas);
                historiaPreparacionLatas.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileHistoriaPreparacionMasa);
                historiaPreparacionMasa.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileHistoriaPreparacionUniforme);
                historiaPreparacionUniforme.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileHistoriaPreparacionMoldes);
                historiaPreparacionMoldes.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileHistoriaProtocoloAsepcia);
                historiaProtocoloAsepcia.compress(Bitmap.CompressFormat.PNG, 100, out);





                //vocabulario
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




                //sequence empaquetado

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

                //sequence glaceado

                out = new FileOutputStream(fileGlaceado1);
                galceado1.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileGlaceado2);
                galceado2.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileGlaceado3);
                galceado3.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileGlaceado4);
                galceado4.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileGlaceado5);
                galceado5.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileGlaceado6);
                galceado6.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileGlaceado7);
                galceado7.compress(Bitmap.CompressFormat.PNG, 100, out);


                //limpieza de jabas

                out = new FileOutputStream(filelimpiezaJabas1);
                limpiezaJabas1.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaJabas2);
                limpiezaJabas2.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaJabas3);
                limpiezaJabas3.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaJabas4);
                limpiezaJabas4.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaJabas5);
                limpiezaJabas5.compress(Bitmap.CompressFormat.PNG, 100, out);

                //limpieza latas

                out = new FileOutputStream(filelimpiezaLatas1);
                limpiezaLatas1.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaLatas2);
                limpiezaLatas2.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaLatas3);
                limpiezaLatas3.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaLatas4);
                limpiezaLatas4.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaLatas5);
                limpiezaLatas5.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaLatas6);
                limpiezaLatas6.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaLatas7);
                limpiezaLatas7.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaLatas8);
                limpiezaLatas8.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaLatas9);
                limpiezaLatas9.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaLatas10);
                limpiezaLatas10.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaLatas11);
                limpiezaLatas11.compress(Bitmap.CompressFormat.PNG, 100, out);

                //limpieza general

                out = new FileOutputStream(filelimpiezaGeneral1);
                limpiezaGeneral1.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaGeneral2);
                limpiezaGeneral2.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaGeneral3);
                limpiezaGeneral3.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaGeneral4);
                limpiezaGeneral4.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaGeneral5);
                limpiezaGeneral5.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaGeneral6);
                limpiezaGeneral6.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaGeneral7);
                limpiezaGeneral7.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaGeneral8);
                limpiezaGeneral8.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaGeneral9);
                limpiezaGeneral9.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filelimpiezaGeneral10);
                limpiezaGeneral10.compress(Bitmap.CompressFormat.PNG, 100, out);

                //preparacion de latas
                out = new FileOutputStream(filePreparacionLatas1);
                preparacionLatas1.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionLatas2);
                preparacionLatas2.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionLatas3);
                preparacionLatas3.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionLatas4);
                preparacionLatas4.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionLatas5);
                preparacionLatas5.compress(Bitmap.CompressFormat.PNG, 100, out);

                //preparacion masas

                out = new FileOutputStream(filePreparacionMasa1);
                preparacionMasa1.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMasa2);
                preparacionMasa2.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMasa3);
                preparacionMasa3.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMasa4);
                preparacionMasa4.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMasa5);
                preparacionMasa5.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMasa6);
                preparacionMasa6.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMasa7);
                preparacionMasa7.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMasa8);
                preparacionMasa8.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMasa9);
                preparacionMasa9.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMasa10);
                preparacionMasa10.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMasa11);
                preparacionMasa11.compress(Bitmap.CompressFormat.PNG, 100, out);


                out = new FileOutputStream(filePreparacionMasa12);
                preparacionMasa12.compress(Bitmap.CompressFormat.PNG, 100, out);


                out = new FileOutputStream(filePreparacionMasa13);
                preparacionMasa13.compress(Bitmap.CompressFormat.PNG, 100, out);


                out = new FileOutputStream(filePreparacionMasa14);
                preparacionMasa14.compress(Bitmap.CompressFormat.PNG, 100, out);


                //preparacion uniforme

                out = new FileOutputStream(filePreparacionUniforme1);
                preparacionUniforme1.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionUniforme2);
                preparacionUniforme2.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionUniforme3);
                preparacionUniforme3.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionUniforme4);
                preparacionUniforme4.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionUniforme5);
                preparacionUniforme5.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionUniforme6);
                preparacionUniforme6.compress(Bitmap.CompressFormat.PNG, 100, out);

                //preparacion moldes

                out = new FileOutputStream(filePreparacionMolde1);
                preparacionMolde1.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMolde2);
                preparacionMolde2.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMolde3);
                preparacionMolde3.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMolde4);
                preparacionMolde4.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMolde5);
                preparacionMolde5.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMolde6);
                preparacionMolde6.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMolde7);
                preparacionMolde7.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMolde8);
                preparacionMolde8.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMolde9);
                preparacionMolde9.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(filePreparacionMolde10);
                preparacionMolde10.compress(Bitmap.CompressFormat.PNG, 100, out);

                // protocolo asepcia

                out = new FileOutputStream(fileProtocoloAsepcia1);
                protocoloAsepcia1.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileProtocoloAsepcia2);
                protocoloAsepcia2.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileProtocoloAsepcia3);
                protocoloAsepcia3.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileProtocoloAsepcia4);
                protocoloAsepcia4.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileProtocoloAsepcia5);
                protocoloAsepcia5.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileProtocoloAsepcia6);
                protocoloAsepcia6.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileProtocoloAsepcia7);
                protocoloAsepcia7.compress(Bitmap.CompressFormat.PNG, 100, out);

                out = new FileOutputStream(fileProtocoloAsepcia8);
                protocoloAsepcia8.compress(Bitmap.CompressFormat.PNG, 100, out);


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




            //Historia empaquetado
            String empaquetado = fileHistoriaEmpaquetado.getPath().toString();
            historia.setNombreHistoria("Empaquetado");
            historia.setDescripcionHistoria("Una vez que se encuentran horneados los productos, es necesario empaquetarlos para su distribución al público. " +
                    "Ten en cuenta que debes usar la máquina selladora. Debes tner cuidado ya que esta se encuentra caliente");
            historia.setNumeroLaminas("5");
            historia.setDificultad("medio");
            historia.setImagenHistoria(empaquetado);
            historia.setIdHistoria(1);
            historia.setIdTaller(1);
            historiaDAO.create(historia);



            String pathEmpaquetado1 = fileEmpaquetado1.getPath().toString();
            secuencia.setImagenSecuencia(pathEmpaquetado1);
            secuencia.setOrdenImagenSecuencia(0);
            secuencia.setDescripcionImagenSecuencia("Uno");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);


            String pathEmpaquetado2 = fileEmpaquetado2.getPath().toString();
            secuencia.setImagenSecuencia(pathEmpaquetado2);
            secuencia.setOrdenImagenSecuencia(1);
            secuencia.setDescripcionImagenSecuencia("Dos");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);


            String pathEmpaquetado3 = fileEmpaquetado3.getPath().toString();
            secuencia.setImagenSecuencia(pathEmpaquetado3);
            secuencia.setOrdenImagenSecuencia(2);
            secuencia.setDescripcionImagenSecuencia("Tres");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathEmpaquetado4 = fileEmpaquetado4.getPath().toString();
            secuencia.setImagenSecuencia(pathEmpaquetado4);
            secuencia.setOrdenImagenSecuencia(2);
            secuencia.setDescripcionImagenSecuencia("Cuatro");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathEmpaquetado5 = fileEmpaquetado5.getPath().toString();
            secuencia.setImagenSecuencia(pathEmpaquetado5);
            secuencia.setOrdenImagenSecuencia(2);
            secuencia.setDescripcionImagenSecuencia("Cinco");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);



            //historia glaceado
            String glaceado = fileHistoriaGlaceado.getPath().toString();
            historia.setNombreHistoria("Glaceado");
            historia.setDescripcionHistoria("Una vez que se encuentran horneados los productos, es necesario empaquetarlos para su distribución al público. " +
                    "Ten en cuenta que debes usar la máquina selladora. Debes tner cuidado ya que esta se encuentra caliente");
            historia.setNumeroLaminas("7");
            historia.setDificultad("medio");
            historia.setImagenHistoria(glaceado);
            historia.setIdHistoria(2);
            historia.setIdTaller(1);
            historiaDAO.create(historia);

            String pathGlaceado1 = fileGlaceado1.getPath().toString();
            secuencia.setImagenSecuencia(pathGlaceado1);
            secuencia.setOrdenImagenSecuencia(1);
            secuencia.setDescripcionImagenSecuencia("Uno");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathGlaceado2 = fileGlaceado2.getPath().toString();
            secuencia.setImagenSecuencia(pathGlaceado2);
            secuencia.setOrdenImagenSecuencia(2);
            secuencia.setDescripcionImagenSecuencia("Dos");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathGlaceado3 = fileGlaceado3.getPath().toString();
            secuencia.setImagenSecuencia(pathGlaceado3);
            secuencia.setOrdenImagenSecuencia(3);
            secuencia.setDescripcionImagenSecuencia("Tres");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);


            String pathGlaceado4 = fileGlaceado4.getPath().toString();
            secuencia.setImagenSecuencia(pathGlaceado4);
            secuencia.setOrdenImagenSecuencia(4);
            secuencia.setDescripcionImagenSecuencia("Cuatro");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathGlaceado5 = fileGlaceado5.getPath().toString();
            secuencia.setImagenSecuencia(pathGlaceado5);
            secuencia.setOrdenImagenSecuencia(5);
            secuencia.setDescripcionImagenSecuencia("Cinco");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathGlaceado6 = fileGlaceado6.getPath().toString();
            secuencia.setImagenSecuencia(pathGlaceado6);
            secuencia.setOrdenImagenSecuencia(6);
            secuencia.setDescripcionImagenSecuencia("Seis");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathGlaceado7 = fileGlaceado7.getPath().toString();
            secuencia.setImagenSecuencia(pathGlaceado7);
            secuencia.setOrdenImagenSecuencia(7);
            secuencia.setDescripcionImagenSecuencia("Siete");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);



            //Historia limpieza jabas

            String limpiezaJabas = fileHistoriaLimpiezaJabas.getPath().toString();
            historia.setNombreHistoria("Limpieza de Jabas");
            historia.setDescripcionHistoria("Una vez que se encuentran horneados los productos, es necesario empaquetarlos para su distribución al público. " +
                    "Ten en cuenta que debes usar la máquina selladora. Debes tner cuidado ya que esta se encuentra caliente");
            historia.setNumeroLaminas("5");
            historia.setDificultad("medio");
            historia.setImagenHistoria(limpiezaJabas);
            historia.setIdHistoria(3);
            historia.setIdTaller(1);
            historiaDAO.create(historia);


            String pathLimpiezaJabas1 = filelimpiezaJabas1.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaJabas1);
            secuencia.setOrdenImagenSecuencia(1);
            secuencia.setDescripcionImagenSecuencia("Uno");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaJabas2 = filelimpiezaJabas2.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaJabas2);
            secuencia.setOrdenImagenSecuencia(2);
            secuencia.setDescripcionImagenSecuencia("Dos");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaJabas3 = filelimpiezaJabas3.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaJabas3);
            secuencia.setOrdenImagenSecuencia(3);
            secuencia.setDescripcionImagenSecuencia("Tres");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaJabas4 = filelimpiezaJabas4.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaJabas4);
            secuencia.setOrdenImagenSecuencia(4);
            secuencia.setDescripcionImagenSecuencia("Cuatro");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaJabas5 = filelimpiezaJabas5.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaJabas5);
            secuencia.setOrdenImagenSecuencia(5);
            secuencia.setDescripcionImagenSecuencia("Cinco");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);


            //limpieza de latas
            String limpiezaLatas = fileHistoriaLimpiezaLatas.getPath().toString();
            historia.setNombreHistoria("Limpieza de Latas");
            historia.setDescripcionHistoria("Una vez que se encuentran horneados los productos, es necesario empaquetarlos para su distribución al público. " +
                    "Ten en cuenta que debes usar la máquina selladora. Debes tner cuidado ya que esta se encuentra caliente");
            historia.setNumeroLaminas("11");
            historia.setDificultad("dificil");
            historia.setImagenHistoria(limpiezaLatas);
            historia.setIdHistoria(4);
            historia.setIdTaller(1);
            historiaDAO.create(historia);

            String pathLimpiezaLatas1 = filelimpiezaLatas1.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaLatas1);
            secuencia.setOrdenImagenSecuencia(1);
            secuencia.setDescripcionImagenSecuencia("Uno");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaLatas2 = filelimpiezaLatas2.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaLatas2);
            secuencia.setOrdenImagenSecuencia(2);
            secuencia.setDescripcionImagenSecuencia("Dos");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaLatas3 = filelimpiezaLatas3.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaLatas3);
            secuencia.setOrdenImagenSecuencia(3);
            secuencia.setDescripcionImagenSecuencia("Tres");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaLatas4 = filelimpiezaLatas4.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaLatas4);
            secuencia.setOrdenImagenSecuencia(4);
            secuencia.setDescripcionImagenSecuencia("Cuatro");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaLatas5 = filelimpiezaLatas5.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaLatas5);
            secuencia.setOrdenImagenSecuencia(5);
            secuencia.setDescripcionImagenSecuencia("Cinco");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaLatas6 = filelimpiezaLatas6.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaLatas6);
            secuencia.setOrdenImagenSecuencia(6);
            secuencia.setDescripcionImagenSecuencia("Seis");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaLatas7 = filelimpiezaLatas7.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaLatas7);
            secuencia.setOrdenImagenSecuencia(7);
            secuencia.setDescripcionImagenSecuencia("Siete");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaLatas8 = filelimpiezaLatas8.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaLatas8);
            secuencia.setOrdenImagenSecuencia(8);
            secuencia.setDescripcionImagenSecuencia("Ocho");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaLatas9 = filelimpiezaLatas9.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaLatas9);
            secuencia.setOrdenImagenSecuencia(9);
            secuencia.setDescripcionImagenSecuencia("Nueve");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaLatas10 = filelimpiezaLatas10.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaLatas10);
            secuencia.setOrdenImagenSecuencia(10);
            secuencia.setDescripcionImagenSecuencia("Diez");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaLatas11 = filelimpiezaLatas11.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaLatas11);
            secuencia.setOrdenImagenSecuencia(11);
            secuencia.setDescripcionImagenSecuencia("Once");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);


            //Historia limpieza general
            String limpiezaGeneral = fileHistoriaLimpiezaGeneral.getPath().toString();
            historia.setNombreHistoria("Limpieza General");
            historia.setDescripcionHistoria("Una vez que se encuentran horneados los productos, es necesario empaquetarlos para su distribución al público. " +
                    "Ten en cuenta que debes usar la máquina selladora. Debes tner cuidado ya que esta se encuentra caliente");
            historia.setNumeroLaminas("10");
            historia.setDificultad("dificil");
            historia.setImagenHistoria(limpiezaGeneral);
            historia.setIdHistoria(5);
            historia.setIdTaller(1);
            historiaDAO.create(historia);


            String pathLimpiezaGeneral1 = filelimpiezaGeneral1.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaGeneral1);
            secuencia.setOrdenImagenSecuencia(1);
            secuencia.setDescripcionImagenSecuencia("Uno");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaGeneral2 = filelimpiezaGeneral2.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaGeneral2);
            secuencia.setOrdenImagenSecuencia(2);
            secuencia.setDescripcionImagenSecuencia("Dos");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaGeneral3 = filelimpiezaGeneral3.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaGeneral3);
            secuencia.setOrdenImagenSecuencia(3);
            secuencia.setDescripcionImagenSecuencia("Tres");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaGeneral4 = filelimpiezaGeneral4.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaGeneral4);
            secuencia.setOrdenImagenSecuencia(4);
            secuencia.setDescripcionImagenSecuencia("Cuatro");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaGeneral5 = filelimpiezaGeneral5.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaGeneral5);
            secuencia.setOrdenImagenSecuencia(5);
            secuencia.setDescripcionImagenSecuencia("Cinco");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaGeneral6 = filelimpiezaGeneral6.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaGeneral6);
            secuencia.setOrdenImagenSecuencia(6);
            secuencia.setDescripcionImagenSecuencia("Seis");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaGeneral7 = filelimpiezaGeneral7.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaGeneral7);
            secuencia.setOrdenImagenSecuencia(7);
            secuencia.setDescripcionImagenSecuencia("Siete");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaGeneral8 = filelimpiezaGeneral8.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaGeneral8);
            secuencia.setOrdenImagenSecuencia(8);
            secuencia.setDescripcionImagenSecuencia("Ocho");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaGeneral9 = filelimpiezaGeneral9.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaGeneral9);
            secuencia.setOrdenImagenSecuencia(9);
            secuencia.setDescripcionImagenSecuencia("Nueve");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathLimpiezaGeneral10 = filelimpiezaGeneral10.getPath().toString();
            secuencia.setImagenSecuencia(pathLimpiezaGeneral10);
            secuencia.setOrdenImagenSecuencia(10);
            secuencia.setDescripcionImagenSecuencia("Diez");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);


            //Historia preparacion latas
            String preparacionLatas = fileHistoriaPreparacionLatas.getPath().toString();
            historia.setNombreHistoria("Preparación de Latas");
            historia.setDescripcionHistoria("Una vez que se encuentran horneados los productos, es necesario empaquetarlos para su distribución al público. " +
                    "Ten en cuenta que debes usar la máquina selladora. Debes tner cuidado ya que esta se encuentra caliente");
            historia.setNumeroLaminas("5");
            historia.setDificultad("facil");
            historia.setImagenHistoria(preparacionLatas);
            historia.setIdHistoria(6);
            historia.setIdTaller(1);
            historiaDAO.create(historia);


            String pathPreparacionLatas1 = filePreparacionLatas1.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionLatas1);
            secuencia.setOrdenImagenSecuencia(1);
            secuencia.setDescripcionImagenSecuencia("Uno");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionLatas2 = filePreparacionLatas2.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionLatas2);
            secuencia.setOrdenImagenSecuencia(2);
            secuencia.setDescripcionImagenSecuencia("Dos");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionLatas3 = filePreparacionLatas3.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionLatas3);
            secuencia.setOrdenImagenSecuencia(3);
            secuencia.setDescripcionImagenSecuencia("Tres");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionLatas4 = filePreparacionLatas4.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionLatas4);
            secuencia.setOrdenImagenSecuencia(4);
            secuencia.setDescripcionImagenSecuencia("Cuatro");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionLatas5 = filePreparacionLatas5.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionLatas5);
            secuencia.setOrdenImagenSecuencia(5);
            secuencia.setDescripcionImagenSecuencia("Cinco");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);



            //Historia preparacion masa
            String preparacionMasa = fileHistoriaPreparacionMasa.getPath().toString();
            historia.setNombreHistoria("Preparación de Masa");
            historia.setDescripcionHistoria("Una vez que se encuentran horneados los productos, es necesario empaquetarlos para su distribución al público. " +
                    "Ten en cuenta que debes usar la máquina selladora. Debes tner cuidado ya que esta se encuentra caliente");
            historia.setNumeroLaminas("14");
            historia.setDificultad("dificil");
            historia.setImagenHistoria(preparacionMasa);
            historia.setIdHistoria(7);
            historia.setIdTaller(1);
            historiaDAO.create(historia);

            String pathPreparacionMasa1 = filePreparacionMasa1.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMasa1);
            secuencia.setOrdenImagenSecuencia(1);
            secuencia.setDescripcionImagenSecuencia("Uno");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMasa2 = filePreparacionMasa2.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMasa2);
            secuencia.setOrdenImagenSecuencia(2);
            secuencia.setDescripcionImagenSecuencia("Dos");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMasa3 = filePreparacionMasa3.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMasa3);
            secuencia.setOrdenImagenSecuencia(3);
            secuencia.setDescripcionImagenSecuencia("Tres");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMasa4 = filePreparacionMasa4.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMasa4);
            secuencia.setOrdenImagenSecuencia(4);
            secuencia.setDescripcionImagenSecuencia("Cuatro");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMasa5 = filePreparacionMasa5.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMasa5);
            secuencia.setOrdenImagenSecuencia(5);
            secuencia.setDescripcionImagenSecuencia("Cinco");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMasa6 = filePreparacionMasa6.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMasa6);
            secuencia.setOrdenImagenSecuencia(6);
            secuencia.setDescripcionImagenSecuencia("Seis");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMasa7 = filePreparacionMasa7.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMasa7);
            secuencia.setOrdenImagenSecuencia(7);
            secuencia.setDescripcionImagenSecuencia("Siete");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMasa8 = filePreparacionMasa8.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMasa8);
            secuencia.setOrdenImagenSecuencia(8);
            secuencia.setDescripcionImagenSecuencia("Ocho");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMasa9 = filePreparacionMasa9.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMasa9);
            secuencia.setOrdenImagenSecuencia(9);
            secuencia.setDescripcionImagenSecuencia("Nueve");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMasa10 = filePreparacionMasa10.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMasa10);
            secuencia.setOrdenImagenSecuencia(10);
            secuencia.setDescripcionImagenSecuencia("Diez");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMasa11 = filePreparacionMasa11.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMasa11);
            secuencia.setOrdenImagenSecuencia(11);
            secuencia.setDescripcionImagenSecuencia("Once");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMasa12 = filePreparacionMasa12.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMasa12);
            secuencia.setOrdenImagenSecuencia(12);
            secuencia.setDescripcionImagenSecuencia("Doce");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMasa13 = filePreparacionMasa13.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMasa13);
            secuencia.setOrdenImagenSecuencia(13);
            secuencia.setDescripcionImagenSecuencia("Trece");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMasa14 = filePreparacionMasa14.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMasa14);
            secuencia.setOrdenImagenSecuencia(14);
            secuencia.setDescripcionImagenSecuencia("Catorce");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);


            //Historia preparacion unifrome
            String preparacionUniforme = fileHistoriaPreparacionUniforme.getPath().toString();
            historia.setNombreHistoria("Preparación de Uniforme");
            historia.setDescripcionHistoria("Una vez que se encuentran horneados los productos, es necesario empaquetarlos para su distribución al público. " +
                    "Ten en cuenta que debes usar la máquina selladora. Debes tner cuidado ya que esta se encuentra caliente");
            historia.setNumeroLaminas("6");
            historia.setDificultad("medio");
            historia.setImagenHistoria(preparacionUniforme);
            historia.setIdHistoria(8);
            historia.setIdTaller(1);
            historiaDAO.create(historia);

            String pathPreparacionUniforme1 = filePreparacionUniforme1.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionUniforme1);
            secuencia.setOrdenImagenSecuencia(1);
            secuencia.setDescripcionImagenSecuencia("Uno");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionUniforme2 = filePreparacionUniforme2.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionUniforme2);
            secuencia.setOrdenImagenSecuencia(2);
            secuencia.setDescripcionImagenSecuencia("Dos");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionUniforme3 = filePreparacionUniforme3.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionUniforme3);
            secuencia.setOrdenImagenSecuencia(3);
            secuencia.setDescripcionImagenSecuencia("Tres");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionUniforme4 = filePreparacionUniforme4.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionUniforme4);
            secuencia.setOrdenImagenSecuencia(4);
            secuencia.setDescripcionImagenSecuencia("Cuatro");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionUniforme5 = filePreparacionUniforme5.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionUniforme5);
            secuencia.setOrdenImagenSecuencia(5);
            secuencia.setDescripcionImagenSecuencia("Cinco");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionUniforme6 = filePreparacionUniforme6.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionUniforme6);
            secuencia.setOrdenImagenSecuencia(6);
            secuencia.setDescripcionImagenSecuencia("Seis");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            //Historia preparacion moldes
            String preparacionMolde = fileHistoriaPreparacionMoldes.getPath().toString();
            historia.setNombreHistoria("Preparación de Moldes");
            historia.setDescripcionHistoria("Una vez que se encuentran horneados los productos, es necesario empaquetarlos para su distribución al público. " +
                    "Ten en cuenta que debes usar la máquina selladora. Debes tner cuidado ya que esta se encuentra caliente");
            historia.setNumeroLaminas("10");
            historia.setDificultad("dificil");
            historia.setImagenHistoria(preparacionMolde);
            historia.setIdHistoria(9);
            historia.setIdTaller(1);
            historiaDAO.create(historia);

            String pathPreparacionMolde1 = filePreparacionMolde1.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMolde1);
            secuencia.setOrdenImagenSecuencia(1);
            secuencia.setDescripcionImagenSecuencia("Uno");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMolde2 = filePreparacionMolde2.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMolde2);
            secuencia.setOrdenImagenSecuencia(2);
            secuencia.setDescripcionImagenSecuencia("Dos");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMolde3 = filePreparacionMolde3.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMolde3);
            secuencia.setOrdenImagenSecuencia(3);
            secuencia.setDescripcionImagenSecuencia("Tres");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMolde4 = filePreparacionMolde4.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMolde4);
            secuencia.setOrdenImagenSecuencia(4);
            secuencia.setDescripcionImagenSecuencia("Cuatro");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMolde5 = filePreparacionMolde5.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMolde5);
            secuencia.setOrdenImagenSecuencia(5);
            secuencia.setDescripcionImagenSecuencia("Cinco");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMolde6 = filePreparacionMolde6.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMolde6);
            secuencia.setOrdenImagenSecuencia(6);
            secuencia.setDescripcionImagenSecuencia("Seis");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMolde7 = filePreparacionMolde7.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMolde7);
            secuencia.setOrdenImagenSecuencia(7);
            secuencia.setDescripcionImagenSecuencia("Siete");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMolde8 = filePreparacionMolde8.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMolde8);
            secuencia.setOrdenImagenSecuencia(8);
            secuencia.setDescripcionImagenSecuencia("Ocho");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMolde9 = filePreparacionMolde9.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMolde9);
            secuencia.setOrdenImagenSecuencia(9);
            secuencia.setDescripcionImagenSecuencia("Nueve");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathPreparacionMolde10 = filePreparacionMolde10.getPath().toString();
            secuencia.setImagenSecuencia(pathPreparacionMolde10);
            secuencia.setOrdenImagenSecuencia(10);
            secuencia.setDescripcionImagenSecuencia("Diez");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);


            //Historia protocolo asepcia
            String protocoloAsepcia = fileHistoriaProtocoloAsepcia.getPath().toString();
            historia.setNombreHistoria("Protocolo de Asepcia");
            historia.setDescripcionHistoria("Una vez que se encuentran horneados los productos, es necesario empaquetarlos para su distribución al público. " +
                    "Ten en cuenta que debes usar la máquina selladora. Debes tner cuidado ya que esta se encuentra caliente");
            historia.setNumeroLaminas("8");
            historia.setDificultad("media");
            historia.setImagenHistoria(protocoloAsepcia);
            historia.setIdHistoria(10);
            historia.setIdTaller(1);
            historiaDAO.create(historia);

            String pathProcoloAsepcia1 = fileProtocoloAsepcia1.getPath().toString();
            secuencia.setImagenSecuencia(pathProcoloAsepcia1);
            secuencia.setOrdenImagenSecuencia(1);
            secuencia.setDescripcionImagenSecuencia("Uno");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathProcoloAsepcia2 = fileProtocoloAsepcia2.getPath().toString();
            secuencia.setImagenSecuencia(pathProcoloAsepcia2);
            secuencia.setOrdenImagenSecuencia(2);
            secuencia.setDescripcionImagenSecuencia("Dos");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathProcoloAsepcia3 = fileProtocoloAsepcia3.getPath().toString();
            secuencia.setImagenSecuencia(pathProcoloAsepcia3);
            secuencia.setOrdenImagenSecuencia(3);
            secuencia.setDescripcionImagenSecuencia("Tres");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathProcoloAsepcia4 = fileProtocoloAsepcia4.getPath().toString();
            secuencia.setImagenSecuencia(pathProcoloAsepcia4);
            secuencia.setOrdenImagenSecuencia(4);
            secuencia.setDescripcionImagenSecuencia("Cuatro");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathProcoloAsepcia5 = fileProtocoloAsepcia5.getPath().toString();
            secuencia.setImagenSecuencia(pathProcoloAsepcia5);
            secuencia.setOrdenImagenSecuencia(5);
            secuencia.setDescripcionImagenSecuencia("Cinco");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathProcoloAsepcia6 = fileProtocoloAsepcia6.getPath().toString();
            secuencia.setImagenSecuencia(pathProcoloAsepcia6);
            secuencia.setOrdenImagenSecuencia(6);
            secuencia.setDescripcionImagenSecuencia("Seis");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathProcoloAsepcia7 = fileProtocoloAsepcia7.getPath().toString();
            secuencia.setImagenSecuencia(pathProcoloAsepcia7);
            secuencia.setOrdenImagenSecuencia(7);
            secuencia.setDescripcionImagenSecuencia("Siete");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);

            String pathProcoloAsepcia8 = fileProtocoloAsepcia8.getPath().toString();
            secuencia.setImagenSecuencia(pathProcoloAsepcia8);
            secuencia.setOrdenImagenSecuencia(8);
            secuencia.setDescripcionImagenSecuencia("Ocho");
            secuencia.setIdHistoria(historia.getIdHistoria());
            secuenciaDAO.create(secuencia);



            //Vocabulario
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

