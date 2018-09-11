package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.adaptadores.EstudianteEstadisticaListAdapter;
import mta.epn.ginghogam.com.mitaller.db.EstudianteDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;
import mta.epn.ginghogam.com.mitaller.listener.RecyclerItemClickListener;

public class EleccionEstudianteEstadisticaActivity extends AppCompatActivity implements RecyclerItemClickListener {


    private RecyclerView recyclerEstudiante;
    private EstudianteEstadisticaListAdapter estudianteEstadisticaListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private EstudianteDAO estudianteDAO;
    private Tutor tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleccion_estudiante_estadistica);


        setContentView(R.layout.activity_eleccion_estudiante_entrenamiento);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");

        Toast.makeText(this,tutor.getNombreTutor(), Toast.LENGTH_LONG).show();

        recyclerEstudiante = (RecyclerView) findViewById(R.id.recyclerEstudianteEntrenamiento);


        linearLayoutManager = new LinearLayoutManager(this);
        estudianteEstadisticaListAdapter = new EstudianteEstadisticaListAdapter(this);
        estudianteEstadisticaListAdapter.setOnItemClickListener(this);

        recyclerEstudiante.setLayoutManager(linearLayoutManager);
        recyclerEstudiante.setAdapter(estudianteEstadisticaListAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    void loadData(){

        estudianteDAO = new EstudianteDAO(this);
        List<Estudiante> estudianteList = new ArrayList<>();



        long params = tutor.getIdTutor();

        Cursor cursor = estudianteDAO.retrieve(params);
        Estudiante estudiante;

        if (cursor.moveToFirst()) {
            do {

                estudiante = new Estudiante();

                estudiante.setIdEstudiante(cursor.getInt(0));
                estudiante.setNombreEstudiate(cursor.getString(1));
                estudiante.setApellidoEstudiante(cursor.getString(2));
                estudiante.setEdadEstudiante(cursor.getInt(3));
                estudiante.setFotoEstudiante(cursor.getString(4));
                estudiante.setGeneroEstudiante(cursor.getString(5));
                estudiante.setPerfilEstudiante(cursor.getString(6));
                estudiante.setIdTutor(cursor.getInt(7));
                estudianteList.add(estudiante);

            } while (cursor.moveToNext());
        }


        estudianteEstadisticaListAdapter.clear();
        estudianteEstadisticaListAdapter.addAll(estudianteList);
    }

    @Override
    public void onItemClick(int position, View view) {
        Intent intent = new Intent(EleccionEstudianteEstadisticaActivity.this, SeleccionTallerEntrenamientoActivity.class);
        intent.putExtra("tutor", tutor);
        intent.putExtra("estudiante", estudianteEstadisticaListAdapter.getItem(position));
        startActivity(intent);
    }
}
