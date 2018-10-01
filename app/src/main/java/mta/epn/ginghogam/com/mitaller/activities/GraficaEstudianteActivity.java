package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.db.SesionDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;

public class GraficaEstudianteActivity extends AppCompatActivity {

    private TextView nombreApellido;
    private RadioButton aciertosFallos, tiempo;
    private Button verTabla, exportar;
    private GraphView graph;

    private Estudiante estudiante;
    private Tutor tutor;
    private SesionDAO sesionDAO;
    List<Date> fechaList;

    private String nombreEstudiante;
    SimpleDateFormat sdf = new SimpleDateFormat("M");


    LineGraphSeries<DataPoint> series;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica_estudiante);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        Bundle extra = getIntent().getExtras();
        estudiante=extra.getParcelable("estudiante");

//        parametros();
//        iniciarComponentes();
//        llamarTabla();
//        consultarDatosSesion();

        graph = findViewById(R.id.graph);



        exqButton();

//        grafica();
//        nombreApellido.setText(nombreEstudiante);


    }

    private void exqButton() {

        series = new LineGraphSeries<>(


                new DataPoint[]{
                        new DataPoint(0, 1),
                        new DataPoint(1, 1),
                        new DataPoint(2, 1),
                        new DataPoint(3, 1),
                        new DataPoint(4, 1),
                        new DataPoint(5, 1),
                        new DataPoint(6, 1),
                        new DataPoint(7, 5),
                        new DataPoint(8, 0)
                }


        );
        graph.addSeries(series);
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        graph.getGridLabelRenderer().setHumanRounding(false);

    }

    public void verTabla(View view) {
        Intent intent = new Intent(GraficaEstudianteActivity.this, TablaResultadosActivity.class);
                intent.putExtra("tutor", tutor);
                intent.putExtra("estudiante", estudiante);
                startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_secuencia, menu);
        getSupportActionBar().setCustomView(R.layout.menu_grafica_titulo);
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





//    private void consultarDatosSesion() {
//        sesionDAO = new SesionDAO(this);
//        Cursor cursor = sesionDAO.retrieve(estudiante.getIdEstudiante());
//        fechaList = new ArrayList<Date>();
//
//        if (cursor.moveToFirst()) {
//            do {
//
//
//                String fecha = cursor.getString(1);
//                Date date1 = new Date();
//                SimpleDateFormat format = new SimpleDateFormat();
//
//                try {
//                    date1 = format.parse(fecha);
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//
//                fechaList.add(date1);
//
//
//            } while (cursor.moveToNext());
//        }
//
//        Toast.makeText(getApplicationContext(),""+fechaList,Toast.LENGTH_SHORT).show();
//
//    }
//
//    private void grafica() {
//
//
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
//
//
//                new DataPoint(2, 2),
//                new DataPoint(4, 5),
//                new DataPoint(6, 0)
//
//        });
//
//
//        series.setColor(Color.rgb(244, 140, 70));
//        series.setThickness(3);
//        series.setDrawBackground(true);
//        series.setBackgroundColor(Color.argb(60, 95, 226, 156));
//        series.setDrawDataPoints(true);
//        series.setDataPointsRadius(10);
//
//
//        graph.addSeries(series);
//
//
////        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
//
////        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
////            @Override
////            public String formatLabel(double value, boolean isValueX) {
////                if(isValueX){
////                    return  sdf.format(new Date((long)value));
////                }else{
////                    return super.formatLabel(value, isValueX);
////                }
////
////            }
////        });
//
//
////        graph.getGridLabelRenderer().setHumanRounding(false);
////
////        graph.getViewport().setScrollable(true); // enables horizontal scrolling
////        graph.getViewport().setScrollableY(true); // enables vertical scrolling
////        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
////        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
//
//
//    }
//
//
//    private DataPoint[] getData() {
//
//        sesionDAO = new SesionDAO(this);
//
//        Cursor cursor = sesionDAO.retrieve(estudiante.getIdEstudiante());
//        DataPoint[] dp = new DataPoint[cursor.getCount()];
//        for (int i = 0; i < cursor.getCount(); i++) {
//            cursor.moveToNext();
//            dp[i] = new DataPoint(i, cursor.getInt(7));
//
//        }
//
//
//        return dp;
//    }
//
//    private void parametros() {
//        Bundle extra = getIntent().getExtras();
//        estudiante = extra.getParcelable("estudiante");
//        tutor = extra.getParcelable("tutor");
//    }
//
//    private void llamarTabla() {
//        verTabla.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(GraficaEstudianteActivity.this, TablaResultadosActivity.class);
//                intent.putExtra("tutor", tutor);
//                intent.putExtra("estudiante", estudiante);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void iniciarComponentes() {
//        nombreApellido = findViewById(R.id.nombreEstu);
//        aciertosFallos = findViewById(R.id.rbAciertosyFallos);
//        verTabla = findViewById(R.id.btnVerTabla);
//        exportar = findViewById(R.id.btnExportar);
//        tiempo = findViewById(R.id.rbTiempo);
//        graph = findViewById(R.id.graph);
//    }
//}
