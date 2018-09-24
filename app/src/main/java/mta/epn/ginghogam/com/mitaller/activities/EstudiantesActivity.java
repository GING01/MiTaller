package mta.epn.ginghogam.com.mitaller.activities;

import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.adaptadores.EstudianteListAdapter;
import mta.epn.ginghogam.com.mitaller.db.EstudianteDAO;
import mta.epn.ginghogam.com.mitaller.db.SQLiteDB;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;
import mta.epn.ginghogam.com.mitaller.listener.RecyclerItemClickListener;

public class EstudiantesActivity extends AppCompatActivity implements RecyclerItemClickListener, SearchView.OnQueryTextListener {

    private RecyclerView recyclerEstudiante;


    private EstudianteListAdapter estudianteListAdapter;
    private LinearLayoutManager linearLayoutManager;


    private EstudianteDAO estudianteDAO;
    private Tutor tutor;
    public boolean visible = false;
    ArrayList<Estudiante> seleccion = new ArrayList<>();
    List<Estudiante> estudianteList;


    //Menu
    MenuItem menuItem;
    MenuItem cancelarMenu;
    MenuItem ok;
    MenuItem nuevo;
    MenuItem borrar;
    boolean isClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiantes);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        Bundle extras = getIntent().getExtras();
        tutor = extras.getParcelable("tutor");

        Toast.makeText(this, tutor.getNombreTutor(), Toast.LENGTH_LONG).show();


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

    void loadData() {

        estudianteDAO = new EstudianteDAO(this);

        estudianteList = new ArrayList<>();


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
        getSupportActionBar().setCustomView(R.layout.menu_estudiantes_titulo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        menuItem = menu.findItem(R.id.buscar);
        cancelarMenu = menu.findItem(R.id.accion_cancelar);
        ok = menu.findItem(R.id.accion_ok);
        nuevo = menu.findItem(R.id.nuevoEstudiante);
        borrar = menu.findItem(R.id.borrar);


        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nuevoEstudiante) {
            EdicionEstudianteActivity.startE(EstudiantesActivity.this, tutor);
            return true;
        }
        if (id == R.id.borrar) {
            visible = true;
            estudianteListAdapter.notifyDataSetChanged();

            menuItem.setVisible(false);
            nuevo.setVisible(false);
            borrar.setVisible(false);
            ok.setVisible(true);
            cancelarMenu.setVisible(true);

            isClicked = true;
        }

        if (id == R.id.accion_ok) {
            aceptar(null);

        }
        if (id == R.id.accion_cancelar) {
            cancelar(null);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position, View view) {
        if (!isClicked) {
            EdicionEstudianteActivity.startE(this, estudianteListAdapter.getItem(position), tutor);
        }
    }

    public void aceptar(View view) {
        borrarElementos();
        loadData();
        menu();
        isClicked = false;
        visible = false;

    }

    public void cancelar(View view) {
        estudianteListAdapter.notifyDataSetChanged();
        menu();
        visible = false;
        isClicked = false;

    }

    private void menu() {
        menuItem.setVisible(true);
        nuevo.setVisible(true);
        borrar.setVisible(true);
        ok.setVisible(false);
        cancelarMenu.setVisible(false);
    }

    public void prepararSeleccion(View view, int position) {
        if (((CheckBox) view).isChecked()) {
            seleccion.add(estudianteListAdapter.getItem(position));
        } else {
            seleccion.remove(estudianteListAdapter.getItem(position));
        }

    }

    public void borrarElementos() {
        estudianteDAO = new EstudianteDAO(this);
        for (int i = 0; i < seleccion.size(); i++) {
            estudianteDAO.delete((Integer) seleccion.get(i).getIdEstudiante());
        }

        // recreate();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        List<Estudiante> newlist = new ArrayList<>();
        for (Estudiante estudiante : estudianteList) {
            if (estudiante.getNombreEstudiate().toLowerCase().contains(userInput)) {
                newlist.add(estudiante);
            }
        }
        estudianteListAdapter.setFilter(newlist);
        return true;
    }

    public void llamarmenu(View view) {

        finish();
    }
}
