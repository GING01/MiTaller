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
import android.widget.Toast;

import java.io.File;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.db.TallerDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.utilidades.RealPathUtil;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EdicionTallerActivity extends AppCompatActivity {

    private static final String DIRECTORIO_IMAGEN = "misImagenesApp/";


    final static int RESULTADO_EDITAR = 1;
    final static int RESULTADO_GALERIA= 2;
    final static int RESULTADO_FOTO= 3;

    private File fileImagen;

    private String pathCamara, pathGaleria, imagenCamara;

    private EditText nombreTaller, descripcionTaller, idTaller;
    private ImageView imgTaller, galeria;

    private Taller taller;
    private SQLiteDB sqLiteDB;
    private TallerDAO tallerDAO;


    public static void start(Context context){
        Intent intent = new Intent(context, EdicionTallerActivity.class);
        context.startActivity(intent);
    }

    public static void start(Context context, Taller taller){
        Intent intent = new Intent(context, EdicionTallerActivity.class);
        intent.putExtra(EdicionTallerActivity.class.getSimpleName(), taller);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_taller);

        setContentView(R.layout.activity_edicion_taller);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        nombreTaller = (EditText) findViewById(R.id.nombreTaller);
        descripcionTaller = (EditText) findViewById(R.id.descripcionTaller);
       // idTaller = (EditText) findViewById(R.id.idTaller);
        imgTaller = (ImageView) findViewById(R.id.img_taller);
        validaPermiso();
        taller = getIntent().getParcelableExtra(EdicionTallerActivity.class.getSimpleName());


            if(taller != null){
                nombreTaller.setText(taller.getNombreTaller());
                descripcionTaller.setText(taller.getDescripcionTaller());
                File file = new File(taller.getImagenTaller());
                if (!file.exists()) {
                    imgTaller.setImageResource(R.drawable.no_foto);
                }else {
                    Toast.makeText(this,taller.getImagenTaller(), Toast.LENGTH_LONG).show();

                    imgTaller.setImageBitmap(BitmapFactory.decodeFile(taller.getImagenTaller().toString()));
                }
            } if(savedInstanceState != null){
                pathGaleria = savedInstanceState.getString("galeria");
                imagenCamara = savedInstanceState.getString("camara");
                if(imagenCamara == null) {
                    if(taller==null){
                      imgTaller.setImageResource(R.drawable.no_foto);
                    }else {
                        imgTaller.setImageBitmap(BitmapFactory.decodeFile(taller.getImagenTaller()));
                    }
                }if(pathGaleria != null){
                imgTaller.setImageBitmap(BitmapFactory.decodeFile(RealPathUtil.getRealPath(this,Uri.parse(pathGaleria))));
            }
            if(imagenCamara != null){
                imgTaller.setImageBitmap(BitmapFactory.decodeFile(imagenCamara));
                //Toast.makeText(this,"Hola >D"+imagenCamara, Toast.LENGTH_LONG).show();
            }





        }


        sqLiteDB = new SQLiteDB(this);
        tallerDAO = new TallerDAO(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle guardarEstado) {
        super.onSaveInstanceState(guardarEstado);



            guardarEstado.putString("camara", pathCamara);

            guardarEstado.putString("galeria", imagenCamara);



    }


    private void registrarUsuarios() {
        if(taller != null){
            if(fileImagen != null){
                taller.setNombreTaller(nombreTaller.getText().toString());
                taller.setDescripcionTaller(descripcionTaller.getText().toString());
                taller.setImagenTaller(imagenCamara);
                tallerDAO.update(taller);
                finish();
            }
            if (pathGaleria != null){
                taller.setNombreTaller(nombreTaller.getText().toString());
                taller.setDescripcionTaller(descripcionTaller.getText().toString());
                taller.setImagenTaller(RealPathUtil.getRealPath(getApplicationContext(),Uri.parse(pathGaleria)));
                tallerDAO.update(taller);
                finish();
            }
            if (pathGaleria == null && fileImagen == null){
                taller.setNombreTaller(nombreTaller.getText().toString());
                taller.setDescripcionTaller(descripcionTaller.getText().toString());
                taller.setImagenTaller(taller.getImagenTaller());
                tallerDAO.update(taller);
                finish();
            }
            if (pathGaleria == null){
                taller.setNombreTaller(nombreTaller.getText().toString());
                taller.setDescripcionTaller(descripcionTaller.getText().toString());
                taller.setImagenTaller(taller.getImagenTaller());
                tallerDAO.update(taller);
                finish();
            }
            if (fileImagen == null){
                taller.setNombreTaller(nombreTaller.getText().toString());
                taller.setDescripcionTaller(descripcionTaller.getText().toString());
                taller.setImagenTaller(taller.getImagenTaller());
                tallerDAO.update(taller);
                finish();
            }


        }else{

            if(fileImagen != null && !nombreTaller.getText().toString().isEmpty() && !descripcionTaller.getText().toString().isEmpty()){
                taller = new Taller();
                taller.setNombreTaller(nombreTaller.getText().toString());
                taller.setDescripcionTaller(descripcionTaller.getText().toString());
                taller.setImagenTaller(imagenCamara);
                tallerDAO.create(taller);
                finish();
            }
            if (pathGaleria != null && !nombreTaller.getText().toString().isEmpty() && !descripcionTaller.getText().toString().isEmpty()){
                taller = new Taller();
                taller.setNombreTaller(nombreTaller.getText().toString());
                taller.setDescripcionTaller(descripcionTaller.getText().toString());
                taller.setImagenTaller(RealPathUtil.getRealPath(getApplicationContext(),Uri.parse(pathGaleria)));
                tallerDAO.create(taller);
                finish();
            }
            if(pathGaleria == null && fileImagen == null && !nombreTaller.getText().toString().isEmpty() && !descripcionTaller.getText().toString().isEmpty()){
                //Toast.makeText(getApplicationContext(),"lol",Toast.LENGTH_LONG).show();
            }
            if(pathGaleria == null && fileImagen == null && !nombreTaller.getText().toString().isEmpty() && descripcionTaller.getText().toString().isEmpty()){
                //Toast.makeText(getApplicationContext(),"hola",Toast.LENGTH_LONG).show();
            }
            if(pathGaleria == null && fileImagen == null && nombreTaller.getText().toString().isEmpty() && !descripcionTaller.getText().toString().isEmpty()){
                //Toast.makeText(getApplicationContext(),"hola mudo",Toast.LENGTH_LONG).show();
            }
            if(pathGaleria == null && fileImagen == null && nombreTaller.getText().toString().isEmpty() && descripcionTaller.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"Debes realizar alguna accion",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edicion_taller, menu);

        if(taller==null){
            getSupportActionBar().setCustomView(R.layout.menu_taller_edicion_titulo_nuevo);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else{
            getSupportActionBar().setCustomView(R.layout.menu_taller_edicion_titulo_editar);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.accion_guardar) {
            registrarUsuarios();
            return true;
        }

        if(id == R.id.vocabulario){

            if(taller == null){
                Toast.makeText(this,"Debes pimero registrar un taller", Toast.LENGTH_LONG).show();
            }else {
                Intent intent = new Intent(EdicionTallerActivity.this, VocabularioActivity.class);
                intent.putExtra("taller", taller);
                startActivity(intent);
                return true;
            }
        }

        if(id == R.id.historia){
            if(taller == null){
                Toast.makeText(this,"Debes pimero registrar un taller", Toast.LENGTH_LONG).show();
            }else {
                Intent intent = new Intent(EdicionTallerActivity.this, HistoriaActivity.class);
                intent.putExtra("taller", taller);
                startActivity(intent);
                return true;
            }
        }
        if(id == R.id.secuencia){
            if(taller == null){
                Toast.makeText(this,"Debes pimero registrar un taller", Toast.LENGTH_LONG).show();
            }else {
                Intent intent = new Intent(EdicionTallerActivity.this, SecuenciaActivity.class);
                intent.putExtra("taller", taller);
                startActivity(intent);
                return true;
            }


        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {

        switch (requestCode) {
            case RESULTADO_FOTO:
                if (resultCode == Activity.RESULT_OK ) {


                    if(fileImagen != null) {

                        imagenCamara = fileImagen.getPath();

                        Bitmap bitmap = BitmapFactory.decodeFile(imagenCamara);
                        imgTaller.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 1024, 1024, true));
                    }
                    break;
                } else
                    Toast.makeText(this, "Error en captura", Toast.LENGTH_LONG).show();
                break;
            case RESULTADO_EDITAR:
                break;
            case RESULTADO_GALERIA:
                if (resultCode == Activity.RESULT_OK) {
                    pathGaleria = data.getDataString();
                    //Toast.makeText(this,"path: "+pathGaleria,Toast.LENGTH_LONG).show();
                    ponerFoto(imgTaller, pathGaleria);
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

        //Toast.makeText(getApplicationContext(),"uri "+uri, Toast.LENGTH_LONG).show();
        Bitmap bitmap = BitmapFactory.decodeFile(RealPathUtil.getRealPath(getApplicationContext(), Uri.parse(uri)));
        if (uri != null) {
            try{
                imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,1024,1024, true));
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"No encontrado", Toast.LENGTH_LONG).show();
            }

        } else{

            imageView.setImageBitmap(null);
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
