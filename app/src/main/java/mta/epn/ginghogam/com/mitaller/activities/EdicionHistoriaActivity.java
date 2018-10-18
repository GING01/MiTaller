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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.db.HistoriaDAO;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.utilidades.RealPathUtil;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EdicionHistoriaActivity extends AppCompatActivity {


    private static final String DIRECTORIO_IMAGEN = "imageneshistoria/";


    final static int RESULTADO_EDITAR = 1;
    final static int RESULTADO_GALERIA= 2;
    final static int RESULTADO_FOTO= 3;

    private File fileImagen;

    private String pathCamara, pathGaleria;

    private EditText nombreHistoria, descripcionHistoria;
    private ImageView imgHistoria, galeria;
    private TextView dificultad;
    private SeekBar seleccionDificultad;

    private Historia historia;
    private Taller taller;
    private SQLiteDB sqLiteDB;
    int seekbarvalue=1;
    private HistoriaDAO historiaDAO;

    private String dificultadSeleccionada;
    private int numeroLaminas;


    public static void startH(Context context, Taller taller){
        Intent intent = new Intent(context, EdicionHistoriaActivity.class);
        intent.putExtra("taller", taller);
        context.startActivity(intent);
    }

    public static void startH(Context context, Historia historia, Taller taller){
        Intent intent = new Intent(context, EdicionHistoriaActivity.class);
        intent.putExtra(EdicionHistoriaActivity.class.getSimpleName(), historia);
        intent.putExtra("taller", taller);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_historia);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        nombreHistoria = (EditText) findViewById(R.id.nombreHistoria);
        descripcionHistoria = (EditText) findViewById(R.id.descripcionHistoria);
        seleccionDificultad=(SeekBar)findViewById(R.id.seekBar2);
        dificultad = (TextView) findViewById(R.id.dificultadtext);
        imgHistoria = (ImageView) findViewById(R.id.img_historia);
        seleccionDificultad.setProgress(2);
        numeroLaminas=3;
        dificultadSeleccionada="facil";
        dificultad.setText("Fácil: "+3+ " láminas");


        Bundle extras = getIntent().getExtras();
        taller = extras.getParcelable("taller");
        validaPermiso();


        seleccionDificultad.setMax(8);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seleccionDificultad.setMin(1);
        }

        seleccionDificultad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress>=0 && progress <=3){
                    seekbarvalue=progress;
                    dificultad.setText("Fácil: "+(progress+1)+ " láminas");
                    dificultadSeleccionada = "facil";
                    numeroLaminas = progress+1;
                }
                if(progress>3 && progress <=6){
                    dificultad.setText("Medio: "+(progress+1)+ " láminas");
                    dificultadSeleccionada = "medio";
                    numeroLaminas = progress+1;

                }
                if(progress>6 && progress <=9){
                    dificultad.setText("Díficil: "+(progress+1)+ " láminas");
                    dificultadSeleccionada = "dificil";
                    numeroLaminas = progress+1;

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        historia = getIntent().getParcelableExtra(EdicionHistoriaActivity.class.getSimpleName());

        if(historia != null){
            //Toast.makeText(this, "Dificultad" + historia.getDificultad(), Toast.LENGTH_LONG).show();
            nombreHistoria.setText(historia.getNombreHistoria());
            descripcionHistoria.setText(historia.getDescripcionHistoria());
            seleccionDificultad.setProgress(Integer.parseInt(historia.getNumeroLaminas()));
            dificultad.setText(historia.getDificultad());

            File file = new File(historia.getImagenHistoria());
            if (!file.exists()) {
            imgHistoria.setImageResource(R.drawable.no_foto);
            } else {
                    imgHistoria.setImageBitmap(BitmapFactory.decodeFile(historia.getImagenHistoria().toString()));
            }

            dificultad.setVisibility(View.GONE);
            seleccionDificultad.setVisibility(View.GONE);
            TextView textoDificultar =findViewById(R.id.textoDificultad);
            textoDificultar.setVisibility(View.GONE);
        }
        sqLiteDB = new SQLiteDB(this);
        historiaDAO = new HistoriaDAO(this);

    }

    private void registrarHistoria() {
        if(historia != null){
            if(fileImagen != null){
                historia.setNombreHistoria(nombreHistoria.getText().toString());
                historia.setDescripcionHistoria(descripcionHistoria.getText().toString());
                historia.setImagenHistoria(fileImagen.getPath().toString());
                historiaDAO.update(historia);
                finish();
            }
            if (pathGaleria != null){
                historia.setNombreHistoria(nombreHistoria.getText().toString());
                historia.setDescripcionHistoria(descripcionHistoria.getText().toString());
                historia.setImagenHistoria(RealPathUtil.getRealPath(getApplicationContext(), Uri.parse(pathGaleria)));
                historiaDAO.update(historia);
                finish();
            }
            if (pathGaleria == null && fileImagen == null){
                historia.setNombreHistoria(nombreHistoria.getText().toString());
                historia.setDescripcionHistoria(descripcionHistoria.getText().toString());
                historia.setImagenHistoria(historia.getImagenHistoria());
                historiaDAO.update(historia);
                finish();
            }
            if (pathGaleria == null){
                historia.setNombreHistoria(nombreHistoria.getText().toString());
                historia.setDescripcionHistoria(descripcionHistoria.getText().toString());
                historia.setImagenHistoria(historia.getImagenHistoria());
                historiaDAO.update(historia);
                finish();
            }
            if (fileImagen == null){
                historia.setNombreHistoria(nombreHistoria.getText().toString());
                historia.setDescripcionHistoria(descripcionHistoria.getText().toString());
                historia.setImagenHistoria(historia.getImagenHistoria());
                historiaDAO.update(historia);
                finish();
            }


        }else{

            if(fileImagen != null && !nombreHistoria.getText().toString().isEmpty() && !descripcionHistoria.getText().toString().isEmpty()){
                historia = new Historia();
                historia.setNombreHistoria(nombreHistoria.getText().toString());
                historia.setDescripcionHistoria(descripcionHistoria.getText().toString());
                historia.setNumeroLaminas(String.valueOf(numeroLaminas));
                historia.setDificultad(dificultadSeleccionada);
                historia.setImagenHistoria(fileImagen.getPath().toString());
                historia.setIdTaller(taller.getIdTaller());
                historiaDAO.create(historia);
                finish();
            }
            if (pathGaleria != null && !nombreHistoria.getText().toString().isEmpty() && !descripcionHistoria.getText().toString().isEmpty()){
                historia = new Historia();
                historia.setNombreHistoria(nombreHistoria.getText().toString());
                historia.setDescripcionHistoria(descripcionHistoria.getText().toString());
                historia.setNumeroLaminas(String.valueOf(numeroLaminas));
                historia.setDificultad(dificultadSeleccionada);
                historia.setImagenHistoria(RealPathUtil.getRealPath(getApplicationContext(),Uri.parse(pathGaleria)));
                historia.setIdTaller(taller.getIdTaller());
                Toast.makeText(this,"id taller: "+taller.getIdTaller(),Toast.LENGTH_LONG).show();
                historiaDAO.create(historia);
                finish();
            }
            if(pathGaleria == null && fileImagen == null && !nombreHistoria.getText().toString().isEmpty() && !descripcionHistoria.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"lol",Toast.LENGTH_LONG).show();
            }
            if(pathGaleria == null && fileImagen == null && !nombreHistoria.getText().toString().isEmpty() && descripcionHistoria.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"hola",Toast.LENGTH_LONG).show();
            }
            if(pathGaleria == null && fileImagen == null && nombreHistoria.getText().toString().isEmpty() && !descripcionHistoria.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"hola mudo",Toast.LENGTH_LONG).show();
            }
            if(pathGaleria == null && fileImagen == null && nombreHistoria.getText().toString().isEmpty() && descripcionHistoria.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"Debes realizar alguna accion",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edicion_historia, menu);
        if(historia==null){
            getSupportActionBar().setCustomView(R.layout.menu_historia_titulo_nuevo);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else{
            getSupportActionBar().setCustomView(R.layout.menu_historia_titulo_editar);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.accion_guardar) {
            registrarHistoria();
            return true;
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

                    Bitmap bitmap = BitmapFactory.decodeFile(fileImagen.getPath());
                    imgHistoria.setImageBitmap(Bitmap.createScaledBitmap(bitmap,1024,1024, true));

                    break;
                } else
                    Toast.makeText(this, "Error en captura", Toast.LENGTH_LONG).show();
                break;
            case RESULTADO_EDITAR:
                break;
            case RESULTADO_GALERIA:
                if (resultCode == Activity.RESULT_OK) {
                    pathGaleria = data.getDataString();
                    ponerFoto(imgHistoria, pathGaleria);
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

    public void llamarmenu(View view){
        Intent intent = new Intent(getApplicationContext(), MenuInicialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }
}

