package mta.epn.ginghogam.com.mitaller.activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.adaptadores.EstudianteListAdapter;
import mta.epn.ginghogam.com.mitaller.adaptadores.HistoriaListAdapter;
import mta.epn.ginghogam.com.mitaller.db.EstudianteDAO;
import mta.epn.ginghogam.com.mitaller.db.HistoriaDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;
import mta.epn.ginghogam.com.mitaller.listener.RecyclerItemClickListener;

public class EstudiantesActivity extends AppCompatActivity implements RecyclerItemClickListener {

    private RecyclerView recyclerEstudiante;


    private EstudianteListAdapter estudianteListAdapter;
    private LinearLayoutManager linearLayoutManager;


    private EstudianteDAO estudianteDAO;
    private Tutor tutor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiantes);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");

        Toast.makeText(this,tutor.getNombreTutor(), Toast.LENGTH_LONG).show();


        recyclerEstudiante = (RecyclerView) findViewById(R.id.recyclerEstudiante);


        linearLayoutManager = new LinearLayoutManager(this);
        estudianteListAdapter = new EstudianteListAdapter(this);
        estudianteListAdapter.setOnItemClickListener(this);

        recyclerEstudiante.setLayoutManager(linearLayoutManager);
        recyclerEstudiante.setAdapter(estudianteListAdapter);
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


        estudianteListAdapter.clear();
        estudianteListAdapter.addAll(estudianteList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_estudiante, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nuevaHistoria) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position, View view) {

    }
}
