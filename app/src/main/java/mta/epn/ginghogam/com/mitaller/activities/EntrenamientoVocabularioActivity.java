package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.adaptadores.PalabraEntrenamientoListAdapter;
import mta.epn.ginghogam.com.mitaller.adaptadores.PalabraListAdapter;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.db.VocabularioDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;
import mta.epn.ginghogam.com.mitaller.entidades.Vocabulario;
import mta.epn.ginghogam.com.mitaller.listener.RecyclerItemClickListener;

public class EntrenamientoVocabularioActivity extends AppCompatActivity implements RecyclerItemClickListener {

    private RecyclerView recyclerPalabra;


    private PalabraEntrenamientoListAdapter palabraEntrenamientoListAdapter;
    private LinearLayoutManager linearLayoutManager;


    private VocabularioDAO vocabularioDAO;

    private Taller taller;
    private Tutor tutor;
    private Estudiante estudiante;
    private List<Vocabulario> vocabularioList;
    private MediaPlayer mp;
    String dificultadSeleccionada;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenamiento_vocabulario);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle extras = getIntent().getExtras();
        taller = extras.getParcelable("taller");
        estudiante = extras.getParcelable("estudiante");
        tutor = extras.getParcelable("tutor");
        dificultadSeleccionada = extras.getString("dificultad");

        Toast.makeText(this,dificultadSeleccionada, Toast.LENGTH_LONG).show();

        recyclerPalabra = (RecyclerView) findViewById(R.id.recyclerPalabraEntrenamiento);


        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        palabraEntrenamientoListAdapter = new PalabraEntrenamientoListAdapter(this);
        palabraEntrenamientoListAdapter.setOnItemClickListener(this);

        recyclerPalabra.setLayoutManager(linearLayoutManager);
        recyclerPalabra.setAdapter(palabraEntrenamientoListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    void loadData(){

        vocabularioDAO = new VocabularioDAO(this);

         vocabularioList = new ArrayList<>();
        long params = taller.getIdTaller();
        Cursor cursor = vocabularioDAO.retrieve(params);

        Vocabulario vocabulario;

        if (cursor.moveToFirst()) {
            do {

                vocabulario = new Vocabulario();

                vocabulario.setIdpalabra(cursor.getInt(0));
                vocabulario.setPalabra(cursor.getString(1));
                vocabulario.setImagenPalabra(cursor.getString(2));
                vocabulario.setSonidoPalabra(cursor.getString(3));
                vocabulario.setTipoPalabra(cursor.getString(4));
                vocabulario.setIdTaller(cursor.getInt(5));
                vocabularioList.add(vocabulario);


            }while (cursor.moveToNext());
        }



        palabraEntrenamientoListAdapter.clear();
        palabraEntrenamientoListAdapter.addAll(vocabularioList);
    }

    @Override
    public void onItemClick(int position, View view) {

       String sonido =  vocabularioList.get(position).getSonidoPalabra().toString().trim();
        mp = new MediaPlayer();
        try {
//fileName is global string. it contains the Uri to the recently recorded audio.
            mp.setDataSource(sonido);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }

    }

    public void pasar(View view) {
        Intent intent = new Intent(EntrenamientoVocabularioActivity.this, SeleccionHistoriaEntrenamientoActivity.class);
        intent.putExtra("tutor", tutor);
        intent.putExtra("estudiante", estudiante);
        intent.putExtra("dificultad", dificultadSeleccionada);
        intent.putExtra("taller", taller);
        startActivity(intent);
    }
}
