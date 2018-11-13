package mta.epn.ginghogam.com.mitaller.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.db.SecuenciaDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Secuencia;
import mta.epn.ginghogam.com.mitaller.utilidades.RealPathUtil;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EdicionSecuenciaActivity extends AppCompatActivity {


    private Historia historia;
    private Secuencia secuencia;
    private LinearLayout rootLayout, target;

    ImageView bt1, imagen, camara, galery;
    EditText descripcionImagenSecuencia;
    Integer numeroImg;
    private File fileImagen;
    private String pathCamara, pathGaleria, pathArrastrar;
    Bitmap bitmap;

    final static int RESULTADO_GALERIA = 2;
    final static int RESULTADO_FOTO = 3;

    private static final String DIRECTORIO_IMAGEN = "secuencias/";

    private SQLiteDB sqLiteDB;
    private SecuenciaDAO secuenciaDAO;
    private boolean editar = false;
    private ArrayList<String> imagenes = new ArrayList<String>();
    private ArrayList<String> descripcionImagenes = new ArrayList<String>();

    List<Secuencia> secuenciaList = new ArrayList<>();


    public static void startSecuen(Context context, Historia historia) {
        Intent intent = new Intent(context, EdicionSecuenciaActivity.class);
        intent.putExtra(EdicionSecuenciaActivity.class.getSimpleName(), historia);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_secuencia);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        historia = getIntent().getParcelableExtra(EdicionSecuenciaActivity.class.getSimpleName());
        validaPermiso();
        numeroImg = Integer.valueOf(historia.getNumeroLaminas());
        rootLayout = (LinearLayout) findViewById(R.id.layout_root);
        imagen = findViewById(R.id.imagenfoto);
        camara = findViewById(R.id.btncamara);
        galery = findViewById(R.id.btngalery);



        descripcionImagenSecuencia = findViewById(R.id.decripcionImagenSecuencia);
        sqLiteDB = new SQLiteDB(this);
        secuenciaDAO = new SecuenciaDAO(this);


        long params = historia.getIdHistoria();
        Cursor cursor = secuenciaDAO.retrieve(params);


        if (cursor.moveToFirst()) {


            do {
                secuencia = new Secuencia();
                secuencia.setIdSecuencia(cursor.getInt(0));
                secuencia.setImagenSecuencia(cursor.getString(1));
                secuencia.setOrdenImagenSecuencia(cursor.getInt(2));
                secuencia.setDescripcionImagenSecuencia(cursor.getString(3));
                secuencia.setIdHistoria(cursor.getInt(4));
                secuenciaList.add(secuencia);
                imagenes.add(cursor.getString(1));
                descripcionImagenes.add(cursor.getString(3));

            } while (cursor.moveToNext());
            for (int i = 0; i < secuenciaList.size(); i++) {
                bt1 = new ImageView(getApplicationContext());

                LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                parametros.gravity = Gravity.CENTER;
                parametros.setMargins(10, 10, 10, 10);

                bt1.setLayoutParams(parametros);



                File fileImagen = new File(secuenciaList.get(i).getImagenSecuencia());
                Bitmap newBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(fileImagen.getPath()), 250,
                        250, true);


                bt1.setImageBitmap(newBitmap);
                bt1.setBackgroundColor(Color.DKGRAY);
                bt1.setOnDragListener(dragListener);
                bt1.setTag((secuenciaList.get(i).getOrdenImagenSecuencia()));
                rootLayout.addView(bt1);


                final int finalI = i;
                bt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagen.setImageBitmap(BitmapFactory.decodeFile(secuenciaList.get(finalI).getImagenSecuencia()));
                        descripcionImagenSecuencia.setText(secuenciaList.get(finalI).getDescripcionImagenSecuencia());
//                        pathArrastrar= imagen


                    }
                });


            }


            editar = true;
            imagen.setOnLongClickListener(longClickListener);
        } else {
            for (int i = 0; i < numeroImg; i++) {

                bt1 = new ImageView(getApplicationContext());
                LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(180, 180);
                parametros.gravity = Gravity.CENTER;
                parametros.setMargins(10, 10, 10, 10);

                bt1.setLayoutParams(parametros);

                bt1.setImageResource(R.drawable.no_foto);
                bt1.setOnDragListener(dragListener);
                bt1.setTag(i);
                rootLayout.addView(bt1);
                imagenes.add("");
                descripcionImagenes.add("");



            }
            imagen.setOnLongClickListener(longClickListener);
        }


    }



    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, myShadowBuilder, view, 0);

            return true;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {

            int dragEvent = event.getAction();
            final View view = (View) event.getLocalState();


            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:

                    if (pathArrastrar == null && fileImagen == null) {
                        ((ImageView) v).setImageDrawable(imagen.getDrawable());
                        if(imagenes.get((Integer)v.getTag())!=""){
                            descripcionImagenes.set((Integer) v.getTag(), descripcionImagenSecuencia.getText().toString());
                        }
                        else{
                            imagenes.set((Integer) v.getTag(), "");
//                        descripcionImagenes.set((Integer) v.getTag(), "");
                            descripcionImagenes.set((Integer) v.getTag(), descripcionImagenSecuencia.getText().toString());
                        }

                    } else if (fileImagen != null) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = false;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        options.inDither = true;
                        Bitmap bitmap = BitmapFactory.decodeFile(pathArrastrar);
                        ((ImageView) v).setImageBitmap(Bitmap.createScaledBitmap(bitmap, 1024, 1024, true));
                        imagenes.set((Integer) v.getTag(), pathArrastrar);
                        descripcionImagenes.set((Integer) v.getTag(), descripcionImagenSecuencia.getText().toString());

                    } else if (pathArrastrar != null && fileImagen == null) {

                        ponerFoto((ImageView) v, pathArrastrar);
                        imagenes.set((Integer) v.getTag(), RealPathUtil.getRealPath(getApplicationContext(), Uri.parse(pathArrastrar)));
                        descripcionImagenes.set((Integer) v.getTag(), descripcionImagenSecuencia.getText().toString());


                    }


                    break;
            }

            return true;
        }
    };


    private boolean validaPermiso() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if ((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
        }
        return false;
    }

    public void galeria(View view) {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, ""), RESULTADO_GALERIA);
        fileImagen = null;
    }

    public void tomarFoto(View view) {
        File miFile = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN);
        String nombre = "";
        boolean isCreada = miFile.exists();
        if (isCreada == false) {
            isCreada = miFile.mkdirs();
        }
        if (isCreada == true) {
            Long consecutivo = System.currentTimeMillis() / 1000;
            nombre = consecutivo.toString() + ".jpg";
        }
        pathCamara = Environment.getExternalStorageDirectory() +
                miFile.separator + DIRECTORIO_IMAGEN + miFile.separator + nombre;
        fileImagen = new File(pathCamara);
        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authorities = getApplicationContext().getPackageName() + ".provider";
            Uri imageUri = FileProvider.getUriForFile(this, authorities, fileImagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));

        }
        startActivityForResult(intent, RESULTADO_FOTO);
    }

    protected void ponerFoto(ImageView imageView, String uri) {

        Bitmap bitmap = BitmapFactory.decodeFile(RealPathUtil.getRealPath(getApplicationContext(), Uri.parse(uri)));
        if (uri != null && !uri.equals("null")) {
            try {
                imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 1024, 1024, true));
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "No encontrado", Toast.LENGTH_LONG).show();
            }

        } else {
            imageView.setImageBitmap(null);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {

        switch (requestCode) {
            case RESULTADO_FOTO:
                if (resultCode == Activity.RESULT_OK) {

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inDither = true;
                    bitmap = BitmapFactory.decodeFile(fileImagen.getPath());
                    imagen.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 1024, 1024, true));
                    pathArrastrar = fileImagen.getPath();
                    break;
                } else
                    Toast.makeText(this, "Error en captura", Toast.LENGTH_LONG).show();
                break;
            case RESULTADO_GALERIA:
                if (resultCode == Activity.RESULT_OK) {
                    pathGaleria = data.getDataString();
                    ponerFoto(imagen, pathGaleria);
                    pathArrastrar = pathGaleria;

                } else
                    Toast.makeText(this, "Foto no cargada", Toast.LENGTH_LONG).show();
                break;

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_secuencia, menu);
        getSupportActionBar().setCustomView(R.layout.menu_secuencia_titulo_editar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.guardarsecuencia) {
            guardar(editar);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean comprobarImg() {
        boolean nolleno = false;
        for (int j = 0; j < numeroImg; j++) {
            if (imagenes.get(j).equals("") || imagenes.get(j) == null || imagenes.isEmpty()) {
                nolleno = true;
            }
        }

        if (nolleno == true) {
            return false;
        } else {
            return true;
        }
    }

    private void guardar(boolean edit) {


        if (edit == true && comprobarImg()) {
            for (int i = 0; i < numeroImg; i++) {
                secuencia = new Secuencia();//instanciar
                secuencia.setIdSecuencia(secuenciaList.get(i).getIdSecuencia());
                secuencia.setImagenSecuencia(imagenes.get(i));
                secuencia.setOrdenImagenSecuencia((i));
                secuencia.setDescripcionImagenSecuencia(descripcionImagenes.get(i));
                secuencia.setIdHistoria(historia.getIdHistoria());

                secuenciaDAO.update(secuencia);
            }
            Toast.makeText(this,"La secuencia ha sido modificada", Toast.LENGTH_SHORT).show();
            finish();

        } else if (edit == false && comprobarImg()) {

            for (int i = 0; i < numeroImg; i++) {
                secuencia = new Secuencia();
                secuencia.setImagenSecuencia(imagenes.get(i));
                secuencia.setOrdenImagenSecuencia((i));
                secuencia.setDescripcionImagenSecuencia(descripcionImagenes.get(i));
                secuencia.setIdHistoria(historia.getIdHistoria());
                secuenciaDAO.create(secuencia);


            }
            Toast.makeText(this,"La secuencia ha sido modificada", Toast.LENGTH_SHORT).show();
            finish();

        } else {
            Toast.makeText(this, "Revisa si estan todas las imágenes!", Toast.LENGTH_SHORT).show();
        }

    }

    public void llamarmenu(View view) {
        Intent intent = new Intent(getApplicationContext(), MenuInicialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }


}