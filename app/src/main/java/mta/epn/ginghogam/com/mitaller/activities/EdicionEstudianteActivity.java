package mta.epn.ginghogam.com.mitaller.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.db.EstudianteDAO;
import mta.epn.ginghogam.com.mitaller.db.HistoriaDAO;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.db.VocabularioDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;
import mta.epn.ginghogam.com.mitaller.entidades.Vocabulario;
import mta.epn.ginghogam.com.mitaller.utilidades.RealPathUtil;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EdicionEstudianteActivity extends AppCompatActivity {

    private static final String DIRECTORIO_IMAGEN = "imagenesEstudiantes/";


    final static int RESULTADO_EDITAR = 1;
    final static int RESULTADO_GALERIA= 2;
    final static int RESULTADO_FOTO= 3;

    private File fileImagen;

    private String pathCamara, pathGaleria;

    private EditText nombre, apellido, edad, perfil;
    private RadioButton masculino, femenino;
    private ImageView foto, galeria;



    private EstudianteDAO estudianteDAO;
    private Tutor tutor;
    private Estudiante estudiante;

    private String generoSelected="";
    boolean checked;



    public static void startE(Context context, Tutor tutor){
        Intent intent = new Intent(context, EdicionEstudianteActivity.class);
        intent.putExtra("tutor", tutor);
        context.startActivity(intent);
    }

    public static void startE(Context context, Estudiante estudiante, Tutor tutor){
        Intent intent = new Intent(context, EdicionEstudianteActivity.class);
        intent.putExtra(EdicionEstudianteActivity.class.getSimpleName(), estudiante);
        intent.putExtra("tutor", tutor);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_estudiante);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");

        validaPermiso();

        nombre = (EditText) findViewById(R.id.nombre_estudiante);
        apellido = (EditText) findViewById(R.id.apellido_estudiante);
        edad = (EditText) findViewById(R.id.edad_estudiante);
        perfil = (EditText) findViewById(R.id.perfil_estudiante);
        foto = (ImageView) findViewById(R.id.img_estudiante);
        masculino = (RadioButton) findViewById(R.id.rbMasculino);
        femenino = (RadioButton) findViewById(R.id.rbFemenino);


        estudiante = getIntent().getParcelableExtra(EdicionEstudianteActivity.class.getSimpleName());

        if(estudiante != null){
            //Toast.makeText(this,"id t: "+estudiante.getGeneroEstudiante(), Toast.LENGTH_LONG).show();
            nombre.setText(estudiante.getNombreEstudiate());
            apellido.setText(estudiante.getApellidoEstudiante());
            edad.setText(Integer.toString(estudiante.getEdadEstudiante()));
            perfil.setText(estudiante.getPerfilEstudiante());



            generoSelected = estudiante.getGeneroEstudiante();
            checked = true;

            if(generoSelected.trim().equals("Masculino")){
                masculino.setChecked(checked);
                masculino.setText(generoSelected);
            }
            if(generoSelected.trim().equals("Femenino")){
                femenino.setChecked(checked);
                femenino.setText(generoSelected);
            }




            File file = new File(estudiante.getFotoEstudiante());
            if (!file.exists()) {
                Toast.makeText(this, "no existe" + estudiante.getFotoEstudiante().toString(), Toast.LENGTH_LONG).show();
                foto.setImageResource(R.drawable.no_foto);
            } else {
                foto.setImageBitmap(BitmapFactory.decodeFile(estudiante.getFotoEstudiante().toString()));
            }

        }


        estudianteDAO = new EstudianteDAO(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edicion_estudiante, menu);
        if(estudiante==null){
            getSupportActionBar().setCustomView(R.layout.menu_estudiante_titulo_nuevo);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else{
            getSupportActionBar().setCustomView(R.layout.menu_estudiante_titulo_editar);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.accion_guardar_estudiante) {
            guardarEstudiante();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void guardarEstudiante() {

        if(estudiante != null){
            if(fileImagen != null && Integer.parseInt(String.valueOf(edad.getText()))>=1 && Integer.parseInt(String.valueOf(edad.getText()))<=101){

                estudiante.setNombreEstudiate(nombre.getText().toString());
                estudiante.setApellidoEstudiante(apellido.getText().toString());
                estudiante.setGeneroEstudiante(generoSelected);
                estudiante.setPerfilEstudiante(perfil.getText().toString());
                estudiante.setEdadEstudiante(Integer.parseInt(edad.getText().toString()));
                estudiante.setFotoEstudiante(fileImagen.getPath().toString());
                estudianteDAO.update(estudiante);
                Toast.makeText(this,"El estudiante ha sido modificado", Toast.LENGTH_SHORT).show();
                finish();

            }
            if (pathGaleria != null && Integer.parseInt(String.valueOf(edad.getText()))>=1 && Integer.parseInt(String.valueOf(edad.getText()))<=101){
                estudiante.setNombreEstudiate(nombre.getText().toString());
                estudiante.setApellidoEstudiante(apellido.getText().toString());
                estudiante.setGeneroEstudiante(generoSelected);
                estudiante.setPerfilEstudiante(perfil.getText().toString());
                estudiante.setEdadEstudiante(Integer.parseInt(edad.getText().toString()));
                estudiante.setFotoEstudiante(RealPathUtil.getRealPath(getApplicationContext(), Uri.parse(pathGaleria)));
                estudianteDAO.update(estudiante);
                Toast.makeText(this,"El estudiante ha sido modificado", Toast.LENGTH_SHORT).show();                finish();

            }
            if (pathGaleria == null && fileImagen == null && Integer.parseInt(String.valueOf(edad.getText()))>=1 && Integer.parseInt(String.valueOf(edad.getText()))<=101){
                estudiante.setNombreEstudiate(nombre.getText().toString());
                estudiante.setApellidoEstudiante(apellido.getText().toString());
                estudiante.setGeneroEstudiante(generoSelected);
                estudiante.setPerfilEstudiante(perfil.getText().toString());
                estudiante.setEdadEstudiante(Integer.parseInt(edad.getText().toString()));
                estudiante.setFotoEstudiante(estudiante.getFotoEstudiante());
                estudianteDAO.update(estudiante);
                Toast.makeText(this,"El estudiante ha sido modificado", Toast.LENGTH_SHORT).show();                finish();
            }
            if (pathGaleria == null && Integer.parseInt(String.valueOf(edad.getText()))>=1 && Integer.parseInt(String.valueOf(edad.getText()))<=101){
                estudiante.setNombreEstudiate(nombre.getText().toString());
                estudiante.setApellidoEstudiante(apellido.getText().toString());
                estudiante.setGeneroEstudiante(generoSelected);
                estudiante.setPerfilEstudiante(perfil.getText().toString());
                estudiante.setEdadEstudiante(Integer.parseInt(edad.getText().toString()));
                estudiante.setFotoEstudiante(estudiante.getFotoEstudiante());
                estudianteDAO.update(estudiante);
                Toast.makeText(this,"El estudiante ha sido modificado", Toast.LENGTH_SHORT).show();                finish();
            }
            if (fileImagen == null && Integer.parseInt(String.valueOf(edad.getText()))>=1 && Integer.parseInt(String.valueOf(edad.getText()))<=101){
                estudiante.setNombreEstudiate(nombre.getText().toString());
                estudiante.setApellidoEstudiante(apellido.getText().toString());
                estudiante.setGeneroEstudiante(generoSelected);
                estudiante.setPerfilEstudiante(perfil.getText().toString());
                estudiante.setEdadEstudiante(Integer.parseInt(edad.getText().toString()));
                estudiante.setFotoEstudiante(estudiante.getFotoEstudiante());
                estudianteDAO.update(estudiante);
                Toast.makeText(this,"El estudiante ha sido modificado", Toast.LENGTH_SHORT).show();                finish();
            }


        }else {

            if (fileImagen != null && Integer.parseInt(String.valueOf(edad.getText()))>=1 && Integer.parseInt(String.valueOf(edad.getText()))<=101) {
                estudiante = new Estudiante();
                estudiante.setNombreEstudiate(nombre.getText().toString());
                estudiante.setApellidoEstudiante(apellido.getText().toString());
                estudiante.setGeneroEstudiante(generoSelected);
                estudiante.setPerfilEstudiante(perfil.getText().toString());
                estudiante.setEdadEstudiante(Integer.parseInt(edad.getText().toString()));
                estudiante.setFotoEstudiante(fileImagen.getPath().toString());
                estudiante.setIdTutor(tutor.getIdTutor());
               // Toast.makeText(this, "genero: " + generoSelected, Toast.LENGTH_LONG).show();
                estudianteDAO.create(estudiante);
                Toast.makeText(this,"Estudiante registrado", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (pathGaleria != null && Integer.parseInt(String.valueOf(edad.getText()))>=1 && Integer.parseInt(String.valueOf(edad.getText()))<=101) {
                estudiante = new Estudiante();
                estudiante.setNombreEstudiate(nombre.getText().toString());
                estudiante.setApellidoEstudiante(apellido.getText().toString());
                estudiante.setGeneroEstudiante(generoSelected);
                estudiante.setPerfilEstudiante(perfil.getText().toString());
                estudiante.setEdadEstudiante(Integer.parseInt(edad.getText().toString()));
                estudiante.setFotoEstudiante(RealPathUtil.getRealPath(getApplicationContext(), Uri.parse(pathGaleria)));
                estudiante.setIdTutor(tutor.getIdTutor());
                estudianteDAO.create(estudiante);
                Toast.makeText(this,"Estudiante registrado", Toast.LENGTH_SHORT).show();
                finish();
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {

        switch (requestCode) {
            case RESULTADO_FOTO:
                if (resultCode == Activity.RESULT_OK ) {

//                    Bitmap bitmap = BitmapFactory.decodeFile(fileImagen.getPath());
//                    foto.setImageBitmap(Bitmap.createScaledBitmap(bitmap,500,500, true));
                    foto.setImageBitmap(BitmapFactory.decodeFile(fileImagen.getPath()));

                    break;
                } else
                    Toast.makeText(this, "Error en captura", Toast.LENGTH_LONG).show();
                break;
            case RESULTADO_EDITAR:
                break;
            case RESULTADO_GALERIA:
                if (resultCode == Activity.RESULT_OK) {
                    pathGaleria = data.getDataString();
                    ponerFoto(foto, pathGaleria);
                } else
                    Toast.makeText(this, "Foto no cargada", Toast.LENGTH_LONG).show();
                break;
        }

    }
    private boolean validaPermiso() {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }
        return false;
    }

    public void galeria(View view) {
        Intent intent;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }else{
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,""), RESULTADO_GALERIA);
    }

    public void tomarFoto(View view) {
        File miFile = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN);
        String nombre="";
        boolean isCreada=miFile.exists();
        if(isCreada==false){
            isCreada=miFile.mkdirs();
        }
        if(isCreada==true) {
            Long consecutivo = System.currentTimeMillis() / 1000;
            nombre = consecutivo.toString() + ".jpg";
        }
        pathCamara=Environment.getExternalStorageDirectory()+
                miFile.separator+DIRECTORIO_IMAGEN+miFile.separator+nombre;
        fileImagen=new File(pathCamara);
        Intent intent=null;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            String authorities=getApplicationContext().getPackageName()+".provider";
            Uri imageUri= FileProvider.getUriForFile(this,authorities,fileImagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        }else{
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(fileImagen));

        }
        startActivityForResult(intent,RESULTADO_FOTO);
    }

    protected void ponerFoto(ImageView imageView, String uri) {


        Bitmap bitmap = BitmapFactory.decodeFile(RealPathUtil.getRealPath(getApplicationContext(), Uri.parse(uri)));
        if (uri != null && !uri.equals("null")) {
            try{
                imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,1024,1024, true));
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"No encontrado", Toast.LENGTH_LONG).show();
            }

        } else{
            imageView.setImageBitmap(null);
        }
    }

    public void generoSelected(View view){
        checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.rbMasculino:
                if (checked)
                    generoSelected = "Masculino";
                break;
            case R.id.rbFemenino:
                if (checked)
                    generoSelected = "Femenino";
                break;


        }

    }

    public void llamarmenu(View view){
        Intent intent = new Intent(getApplicationContext(), MenuInicialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }
}
