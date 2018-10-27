package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.filter.Filter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import mta.epn.ginghogam.com.mitaller.utilidades.MyTableViewModel;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static java.security.AccessController.getContext;

public class TablaResultadosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private TableView mTableView;
    private MyTableViewAdapter mTableAdapter;
    private SesionDAO sesionDAO;
    List<Sesion> sesionList;
    private Filter tableViewFilter ;
    MenuItem menuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_resultados);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mTableView = findViewById(R.id.my_TableView);
        TextView name = findViewById(R.id.nombreestudiantetabla);

        initializeTableView(mTableView);
        tableViewFilter = new Filter(mTableView);


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
                sesion.setNombretutor(cursor.getString(4));
                sesion.setNombreEstudiate(cursor.getString(5));
                sesion.setNombrehistoria(cursor.getString(6));
                sesion.setAciertos(cursor.getInt(8));
                sesion.setFallos(cursor.getInt(9));
                sesion.setTiempo(cursor.getLong(10));
                sesion.setLogro(cursor.getString(11));
                sesion.setObservacion(cursor.getString(12));
                sesion.setIdEstudiante(cursor.getInt(13));


                sesionList.add(sesion);


            } while (cursor.moveToNext());
        }


        cursor.close();
        sesionDAO.close();

        mTableAdapter.setUserList(sesionList);
    }


    private void initializeTableView(TableView tableView){
        mTableAdapter = new MyTableViewAdapter(this);
        tableView.setAdapter(mTableAdapter);
        tableView.setTableViewListener(new MyTableViewListener(tableView));
        tableViewFilter = new Filter(mTableView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabla_estudiantes, menu);
        getSupportActionBar().setCustomView(R.layout.menu_tabla_titulo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        menuItem = menu.findItem(R.id.buscar);
       // SearchView searchView = (SearchView) menuItem.getActionView();
       // searchView.setOnQueryTextListener(this);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.buscar) {
            exportarXls();

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

    public void exportarXls() {
        validaPermiso();
        try {
            Bundle extra = getIntent().getExtras();
            Estudiante estudiante;
            estudiante=extra.getParcelable("estudiante");
            Cursor cursor = sesionDAO.retrieveExport(estudiante.getIdEstudiante());
            cursor.moveToFirst();
            int rowcount = 0;
            int colcount = 0;
            File sdCardDir = Environment.getExternalStorageDirectory();
            String filename = estudiante.getNombreEstudiate()+".csv";
            // the name of the file to export with
            File saveFile = new File(sdCardDir, filename);
            FileWriter fw = new FileWriter(saveFile);

            BufferedWriter bw = new BufferedWriter(fw);
            rowcount = cursor.getCount();
            colcount = cursor.getColumnCount();
            if (rowcount > 0) {
                cursor.moveToFirst();

                for (int i = 0; i < colcount; i++) {
                    if (i != colcount - 1) {

                        bw.write(cursor.getColumnName(i) + ",");

                    } else {

                        bw.write(cursor.getColumnName(i));

                    }
                }
                bw.newLine();

                for (int i = 0; i < rowcount; i++) {
                    cursor.moveToPosition(i);

                    for (int j = 0; j < colcount; j++) {
                        if (j != colcount - 1)
                            bw.write(cursor.getString(j) + ",");
                        else
                            bw.write(cursor.getString(j));
                    }
                    bw.newLine();
                }
                bw.flush();
                Toast.makeText(getApplicationContext(), "Exportado satisfactoriamente ", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception ex) {

                Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();



        } finally {

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

    @Override
    public boolean onQueryTextSubmit(String query) {

        tableViewFilter.set(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }
}
