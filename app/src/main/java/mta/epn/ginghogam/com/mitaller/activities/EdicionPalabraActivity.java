package mta.epn.ginghogam.com.mitaller.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.db.HistoriaDAO;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.db.VocabularioDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Vocabulario;
import mta.epn.ginghogam.com.mitaller.utilidades.RealPathUtil;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;






public class EdicionPalabraActivity extends AppCompatActivity implements View.OnClickListener {

    //AUDIO
    private int RECORD_AUDIO_REQUEST_CODE =123 ;

    private Chronometer chronometer;
    private ImageView imageViewRecord, imageViewPlay, imageViewStop;
    private SeekBar seekBar;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private String fileName = null;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;
    //
    private static final String DIRECTORIO_IMAGEN = "imagenesPalabras/";


    final static int RESULTADO_EDITAR = 1;
    final static int RESULTADO_GALERIA= 2;
    final static int RESULTADO_FOTO= 3;

    private File fileImagen;

    private String pathCamara, pathGaleria;

    private EditText palabra;
    private RadioButton alimento, limpieza, peligroso, herramienta;

    private ImageView imgPalabra, galeria;

    private Vocabulario vocabulario;
    private Taller taller;
    private VocabularioDAO vocabularioDAO;

    private String tipoPalabra="";
    boolean checked;

    public static void startP(Context context, Taller taller){
        Intent intent = new Intent(context, EdicionPalabraActivity.class);
        intent.putExtra("taller", taller);
        context.startActivity(intent);
    }

    public static void startP(Context context, Vocabulario vocabulario, Taller taller){
        Intent intent = new Intent(context, EdicionPalabraActivity.class);
        intent.putExtra(EdicionPalabraActivity.class.getSimpleName(), vocabulario);
        intent.putExtra("taller", taller);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_palabra);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            getPermissionToRecordAudio();
        }

        Bundle extras = getIntent().getExtras();
        taller = extras.getParcelable("taller");
        validaPermiso();

        palabra = (EditText) findViewById(R.id.palabra);
        imgPalabra = (ImageView) findViewById(R.id.img_palabra);
        alimento = (RadioButton) findViewById(R.id.rbAlimento);
        limpieza = (RadioButton) findViewById(R.id.rbLimpieza);
        peligroso = (RadioButton) findViewById(R.id.rbPeligro);

        chronometer = (Chronometer) findViewById(R.id.chronometerTimer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        imageViewRecord = (ImageView) findViewById(R.id.imageViewRecord);
        imageViewStop = (ImageView) findViewById(R.id.imageViewStop);
        imageViewPlay = (ImageView) findViewById(R.id.imageViewPlay);

        seekBar = (SeekBar) findViewById(R.id.seekBar);

        imageViewRecord.setOnClickListener(this);
        imageViewStop.setOnClickListener(this);
        imageViewPlay.setOnClickListener(this);

        vocabulario = getIntent().getParcelableExtra(EdicionPalabraActivity.class.getSimpleName());
        if(alimento.isChecked()){
            tipoPalabra = "Alimento";
        }

        if(vocabulario != null){
            palabra.setText(vocabulario.getPalabra());
            fileName = vocabulario.getSonidoPalabra();


            tipoPalabra = vocabulario.getTipoPalabra();
            checked = true;

            if(tipoPalabra.trim().equals("Alimento")){
                alimento.setChecked(checked);
                alimento.setText(tipoPalabra);
            }
            if(tipoPalabra.trim().equals("Limpieza")){
                limpieza.setChecked(checked);
                limpieza.setText(tipoPalabra);
            }
            if(tipoPalabra.trim().equals("Peligro")){
                peligroso.setChecked(checked);
                peligroso.setText(tipoPalabra);
            }
            if(tipoPalabra.trim().equals("Herramienta")){
                peligroso.setChecked(checked);
                peligroso.setText(tipoPalabra);
            }

            File file = new File(vocabulario.getImagenPalabra());
            if (!file.exists()) {
                imgPalabra.setImageResource(R.drawable.no_foto);
            } else {
                imgPalabra.setImageBitmap(BitmapFactory.decodeFile(vocabulario.getImagenPalabra().toString()));
            }

        }


        vocabularioDAO = new VocabularioDAO(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {

        switch (requestCode) {
            case RESULTADO_FOTO:
                if (resultCode == Activity.RESULT_OK ) {

                    Bitmap bitmap = BitmapFactory.decodeFile(fileImagen.getPath());
                    imgPalabra.setImageBitmap(Bitmap.createScaledBitmap(bitmap,1024,1024, true));

                    break;
                } else
                    Toast.makeText(this, "Error en captura", Toast.LENGTH_LONG).show();
                break;
            case RESULTADO_EDITAR:
                break;
            case RESULTADO_GALERIA:
                if (resultCode == Activity.RESULT_OK) {
                    pathGaleria = data.getDataString();
                    ponerFoto(imgPalabra, pathGaleria);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToRecordAudio() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RECORD_AUDIO_REQUEST_CODE);

        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edicion_palabra, menu);
        if(vocabulario==null){
            getSupportActionBar().setCustomView(R.layout.menu_palabra_titulo_nuevo);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else{
            getSupportActionBar().setCustomView(R.layout.menu_palabra_titulo_editar);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.accion_guardar_palabra) {
            guardarPalabra();
            stopRecording();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            if(mRecorder != null) {
                mRecorder.stop();
            }
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    private void guardarPalabra() {
        if(vocabulario != null){
            if(fileImagen != null){
                vocabulario.setPalabra(palabra.getText().toString());
                vocabulario.setSonidoPalabra(fileName);
                vocabulario.setTipoPalabra(tipoPalabra);
                vocabulario.setImagenPalabra(fileImagen.getPath().toString());
                vocabularioDAO.update(vocabulario);
                Toast.makeText(this,"La palabra ha sido modificada", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (pathGaleria != null){
                vocabulario.setPalabra(palabra.getText().toString());
                vocabulario.setSonidoPalabra(fileName);
                vocabulario.setTipoPalabra(tipoPalabra);
                vocabulario.setImagenPalabra(RealPathUtil.getRealPath(getApplicationContext(), Uri.parse(pathGaleria)));
                vocabularioDAO.update(vocabulario);
                Toast.makeText(this,"La palabra ha sido modificada", Toast.LENGTH_SHORT).show();                finish();
            }
            if (pathGaleria == null && fileImagen == null){
                vocabulario.setPalabra(palabra.getText().toString());
                vocabulario.setSonidoPalabra(fileName);
                vocabulario.setTipoPalabra(tipoPalabra);
                vocabulario.setImagenPalabra(vocabulario.getImagenPalabra());
                vocabularioDAO.update(vocabulario);
                Toast.makeText(this,"La palabra ha sido modificada", Toast.LENGTH_SHORT).show();                finish();
            }
            if (pathGaleria == null){
                vocabulario.setPalabra(palabra.getText().toString());
                vocabulario.setSonidoPalabra(fileName);
                vocabulario.setTipoPalabra(tipoPalabra);
                vocabulario.setImagenPalabra(vocabulario.getImagenPalabra());
                vocabularioDAO.update(vocabulario);
                Toast.makeText(this,"La palabra ha sido modificada", Toast.LENGTH_SHORT).show();                finish();
            }
            if (fileImagen == null){
                vocabulario.setPalabra(palabra.getText().toString());
                vocabulario.setSonidoPalabra(fileName);
                vocabulario.setTipoPalabra(tipoPalabra);
                vocabulario.setImagenPalabra(vocabulario.getImagenPalabra());
                vocabularioDAO.update(vocabulario);
                Toast.makeText(this,"La palabra ha sido modificada", Toast.LENGTH_SHORT).show();                finish();
            }


        }else{

            if(fileImagen != null && !palabra.getText().toString().isEmpty()){
                vocabulario = new Vocabulario();
                vocabulario.setPalabra(palabra.getText().toString());
                vocabulario.setSonidoPalabra(fileName);
                vocabulario.setTipoPalabra(tipoPalabra);

                vocabulario.setImagenPalabra(fileImagen.getPath().toString());
                vocabulario.setIdTaller(taller.getIdTaller());
                vocabularioDAO.create(vocabulario);
                Toast.makeText(this,"Palabra registrada", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (pathGaleria != null && !palabra.getText().toString().isEmpty()){
                vocabulario = new Vocabulario();
                vocabulario.setPalabra(palabra.getText().toString());
                vocabulario.setSonidoPalabra(fileName);
                vocabulario.setTipoPalabra(tipoPalabra);
                vocabulario.setImagenPalabra(RealPathUtil.getRealPath(getApplicationContext(),Uri.parse(pathGaleria)));
                vocabulario.setIdTaller(taller.getIdTaller());
                vocabularioDAO.create(vocabulario);
                Toast.makeText(this,"Palabra registrada", Toast.LENGTH_SHORT).show();
                finish();
            }
       /*     if(pathGaleria == null && fileImagen == null && !nombreHistoria.getText().toString().isEmpty() && !descripcionHistoria.getText().toString().isEmpty()){
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
            }*/

        }
    }

    public void radioClicked(View view){
        checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.rbAlimento:
                if (checked)
                    tipoPalabra = "Ingrediente";
                break;
            case R.id.rbLimpieza:
                if (checked)
                    tipoPalabra = "Limpieza";
                break;
            case R.id.rbPeligro:
                if (checked)
                    tipoPalabra = "Peligro";
                break;
            case  R.id.rbHerramienta:
                if (checked)
                    tipoPalabra = "Herramienta";
                break;

        }

    }

    @Override
    public void onClick(View view) {
        if( view == imageViewRecord ){
            imageViewRecord.setVisibility(View.VISIBLE);
            chronometer.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.GONE);
            imageViewPlay.setVisibility(View.GONE);
            imageViewStop.setVisibility(View.VISIBLE);
            startRecording();
        }else if( view == imageViewStop ){
            imageViewRecord.setEnabled(true);
            imageViewRecord.setVisibility(View.VISIBLE);
            chronometer.setVisibility(View.GONE);
            imageViewStop.setVisibility(View.GONE);
            seekBar.setVisibility(View.VISIBLE);
            imageViewPlay.setVisibility(View.VISIBLE);
            stopRecording();
        }else if( view == imageViewPlay ){
            if( !isPlaying && fileName != null ){
                isPlaying = true;
                startPlaying();
            }else{
                isPlaying = false;
                stopPlaying();
            }
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
//fileName is global string. it contains the Uri to the recently recorded audio.
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        //making the imageview pause button
        imageViewPlay.setImageResource(R.drawable.ic_pause);

        seekBar.setProgress(lastProgress);
        mPlayer.seekTo(lastProgress);
        seekBar.setMax(mPlayer.getDuration());
        seekUpdation();
        chronometer.start();

        /** once the audio is complete, timer is stopped here**/
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageViewPlay.setImageResource(R.drawable.ic_play);
                isPlaying = false;
                chronometer.stop();
            }
        });

        /** moving the track as per the seekBar's position**/
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( mPlayer!=null && fromUser ){
                    //here the track's progress is being changed as per the progress bar
                    mPlayer.seekTo(progress);
                    //timer is being updated as per the progress of the seekbar
                    chronometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void stopRecording() {

        try{
            mRecorder.stop();
            mRecorder.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mRecorder = null;
        //starting the chronometer
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        //showing the play button
    }

    private void startRecording() {
        //we use the MediaRecorder class to record
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        /**In the lines below, we create a directory VoiceRecorderSimplifiedCoding/Audios in the phone storage
         * and the audios are being stored in the Audios folder **/
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "Audios");
        if (!file.exists()) {
            file.mkdirs();
        }

        fileName =  root.getAbsolutePath() + "Audios/" +
                String.valueOf(System.currentTimeMillis() + ".mp3");
        Log.d("filename",fileName);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            imageViewRecord.setEnabled(false);
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastProgress = 0;
        seekBar.setProgress(0);
        stopPlaying();
        //starting the chronometer
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    private void seekUpdation() {
        if(mPlayer != null){
            int mCurrentPosition = mPlayer.getCurrentPosition() ;
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }

    private void stopPlaying() {
        try{
            mPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mPlayer = null;
        //showing the play button
        imageViewPlay.setImageResource(R.drawable.ic_play);
        chronometer.stop();
    }

    public void llamarmenu(View view){
        Intent intent = new Intent(getApplicationContext(), MenuInicialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mRecorder != null) {
            mRecorder.stop();
        }

    }
}
