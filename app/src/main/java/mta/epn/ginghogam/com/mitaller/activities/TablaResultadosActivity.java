package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.filter.Filter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.adaptadores.MyTableViewAdapter;
import mta.epn.ginghogam.com.mitaller.db.EstudianteDAO;
import mta.epn.ginghogam.com.mitaller.db.SesionDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Sesion;
import mta.epn.ginghogam.com.mitaller.utilidades.MyTableViewListener;

import static java.security.AccessController.getContext;

public class TablaResultadosActivity extends AppCompatActivity {


    private TableView mTableView;
    private MyTableViewAdapter mTableAdapter;
    private SesionDAO sesionDAO;
    List<Sesion> sesionList;
    private Filter tableViewFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_resultados);
        mTableView = findViewById(R.id.my_TableView);
       TextView name = findViewById(R.id.nombreestudiantetabla);


        initializeTableView(mTableView);

        sesionDAO = new SesionDAO(this);



        sesionList = new ArrayList<>();
        Bundle extra = getIntent().getExtras();
        Estudiante estudiante;
        estudiante=extra.getParcelable("estudiante");
        name.setText(estudiante.getNombreEstudiate().toUpperCase()+" "+estudiante.getApellidoEstudiante().toUpperCase());




        Cursor cursor = sesionDAO.retrieve(estudiante.getIdEstudiante());
        cursor.moveToFirst();
        Sesion sesion;
        if (cursor.moveToFirst()) {
            do {

                sesion = new Sesion();
                sesion.setIdSesion(cursor.getInt(0));
                sesion.setFechaSesion(cursor.getString(1));
                sesion.setNombretaller(cursor.getString(2));
                sesion.setNombretutor(cursor.getString(3));
                sesion.setNombreEstudiate(cursor.getString(4));
                sesion.setNombrehistoria(cursor.getString(5));
                sesion.setAciertos(cursor.getInt(6));
                sesion.setFallos(cursor.getInt(7));
                sesion.setTiempo(cursor.getLong(8));
                sesion.setLogro(cursor.getString(9));
                sesion.setObservacion(cursor.getString(10));
                sesion.setIdEstudiante(cursor.getInt(11));


                sesionList.add(sesion);


            } while (cursor.moveToNext());
        }


        mTableAdapter.setUserList(sesionList);

    }
    private void initializeTableView(TableView tableView){

        // Create TableView Adapter
        mTableAdapter = new MyTableViewAdapter(this);
        tableView.setAdapter(mTableAdapter);

        // Create listener
        tableView.setTableViewListener(new MyTableViewListener(tableView));
        tableViewFilter = new Filter(mTableView);
        filterTableForGender("si");
    }

    public void filterTableForGender(String genderFilterKeyword) {
        tableViewFilter.set(genderFilterKeyword);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_secuencia, menu);
        getSupportActionBar().setCustomView(R.layout.menu_tabla_titulo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public void llamarmenu(View view){
        Intent intent = new Intent(getApplicationContext(), MenuInicialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }
}
