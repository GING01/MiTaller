package mta.epn.ginghogam.com.mitaller.activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

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


        initializeTableView(mTableView);

        sesionDAO = new SesionDAO(this);



        sesionList = new ArrayList<>();
        Bundle extra = getIntent().getExtras();
        Estudiante estudiante;
        estudiante=extra.getParcelable("estudiante");




        Cursor cursor = sesionDAO.retrieve(estudiante.getIdEstudiante());
        cursor.moveToFirst();
        Sesion sesion;
        if (cursor.moveToFirst()) {
            do {

                sesion = new Sesion();
                String fecha = cursor.getString(1);
                Date date = null;
                Date date1=new Date();
               SimpleDateFormat format= new SimpleDateFormat(" MM/dd/yyyy hh:mm:ss aa");
                SimpleDateFormat src = new SimpleDateFormat("yyyy/MM/dd");
                SimpleDateFormat dest = new SimpleDateFormat("dd/MM/yyyy");



                try {
                    date1=format.parse(fecha);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

//               String result = dest.format(date);
                sesion.setIdSesion(cursor.getInt(0));
                sesion.setFechaSesion(src.format(date1));
                sesion.setNombretaller(cursor.getString(2));
                sesion.setNombretutor(cursor.getString(3));
                sesion.setNombreEstudiate(cursor.getString(4));
                sesion.setNombrehistoria(cursor.getString(5));
                sesion.setAciertos(cursor.getInt(6));
                sesion.setFallos(cursor.getInt(7));
                sesion.setTiempo(cursor.getLong(8));
                sesion.setLogro(Boolean.valueOf(cursor.getString(9)));
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
        filterTableForGender("true");
    }

    public void filterTableForGender(String genderFilterKeyword) {
        tableViewFilter.set(genderFilterKeyword);
    }
}
