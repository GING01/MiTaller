package mta.epn.ginghogam.com.mitaller.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.adaptadores.EstudianteEntrenamientoListAdapter;
import mta.epn.ginghogam.com.mitaller.db.EstudianteDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;
import mta.epn.ginghogam.com.mitaller.listener.RecyclerItemClickListener;

public class EleccionEstudianteEntrenamientoActivity extends AppCompatActivity implements RecyclerItemClickListener, SearchView.OnQueryTextListener{


    private RecyclerView recyclerEstudiante;
    private EstudianteEntrenamientoListAdapter estudianteEntrenamientoListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private EstudianteDAO estudianteDAO;
    private Tutor tutor;

    private String m_Text = "";
    List<Estudiante> estudianteList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleccion_estudiante_entrenamiento);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");

        Toast.makeText(this,tutor.getNombreTutor(), Toast.LENGTH_LONG).show();

        recyclerEstudiante = (RecyclerView) findViewById(R.id.recyclerEstudianteEntrenamiento);


        linearLayoutManager = new LinearLayoutManager(this);
        estudianteEntrenamientoListAdapter = new EstudianteEntrenamientoListAdapter(this);
        estudianteEntrenamientoListAdapter.setOnItemClickListener(this);

        recyclerEstudiante.setLayoutManager(linearLayoutManager);
        recyclerEstudiante.setAdapter(estudianteEntrenamientoListAdapter);



    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    void loadData(){

        estudianteDAO = new EstudianteDAO(this);




        long params = tutor.getIdTutor();
        estudianteList = new ArrayList<>();
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


        estudianteEntrenamientoListAdapter.clear();
        estudianteEntrenamientoListAdapter.addAll(estudianteList);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selecion_estudiante, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView =(SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }




    @Override
    public void onItemClick(final int position, final View view) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingresa tu password para continuar");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();

                if(m_Text.equals(tutor.getContraseñaTutor())){
                    Intent intent = new Intent(EleccionEstudianteEntrenamientoActivity.this, SeleccionDificultadActivity.class);
                    intent.putExtra("tutor", tutor);
                    intent.putExtra("estudiante", estudianteEntrenamientoListAdapter.getItem(position));
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Password incorrecto", Toast.LENGTH_LONG).show();
                }
            }
        });


        builder.show();

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput= newText.toLowerCase();
        List<Estudiante> newlist = new ArrayList<>();
        for(Estudiante estudiante: estudianteList){
            if(estudiante.getNombreEstudiate().toLowerCase().contains(userInput)){
                newlist.add(estudiante);
            }
        }
        estudianteEntrenamientoListAdapter.setFilter(newlist);
        return true;
    }
}
