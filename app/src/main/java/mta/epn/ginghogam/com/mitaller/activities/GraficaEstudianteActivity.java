package mta.epn.ginghogam.com.mitaller.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica_estudiante);
        parametros();
        iniciarComponentes();
        llamarTabla();
        consultarDatosSesion();


        grafica();
        nombreApellido.setText(nombreEstudiante);


    }

    private void consultarDatosSesion() {
        sesionDAO = new SesionDAO(this);
        Cursor cursor = sesionDAO.retrieve(estudiante.getIdEstudiante());
        fechaList = new ArrayList<Date>();

        if (cursor.moveToFirst()) {
            do {


                String fecha = cursor.getString(1);
                Date date1 = new Date();
                SimpleDateFormat format = new SimpleDateFormat();

                try {
                    date1 = format.parse(fecha);

                } catch (ParseException e) {
                    e.printStackTrace();
                }


                fechaList.add(date1);


            } while (cursor.moveToNext());
        }

        Toast.makeText(getApplicationContext(),""+fechaList,Toast.LENGTH_SHORT).show();

    }

    private void grafica() {


        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{


                new DataPoint(2, 2),
                new DataPoint(4, 5),
                new DataPoint(6, 0)

        });


        series.setColor(Color.rgb(244, 140, 70));
        series.setThickness(3);
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.argb(60, 95, 226, 156));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);


        graph.addSeries(series);

//        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
//            @Override
//            public String formatLabel(double value, boolean isValueX) {
//                if(isValueX){
//                    return  sdf.format(new Date((long)value));
//                }else{
//                    return super.formatLabel(value, isValueX);
//                }
//
//            }
//        });


//        graph.getGridLabelRenderer().setHumanRounding(false);
//
//        graph.getViewport().setScrollable(true); // enables horizontal scrolling
//        graph.getViewport().setScrollableY(true); // enables vertical scrolling
//        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
//        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling


    }


    private DataPoint[] getData() {

        sesionDAO = new SesionDAO(this);

        Cursor cursor = sesionDAO.retrieve(estudiante.getIdEstudiante());
        DataPoint[] dp = new DataPoint[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            dp[i] = new DataPoint(i, cursor.getInt(7));

        }


        return dp;
    }

    private void parametros() {
        Bundle extra = getIntent().getExtras();
        estudiante = extra.getParcelable("estudiante");
        tutor = extra.getParcelable("tutor");
    }

    private void llamarTabla() {
        verTabla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraficaEstudianteActivity.this, TablaResultadosActivity.class);
                intent.putExtra("tutor", tutor);
                intent.putExtra("estudiante", estudiante);
                startActivity(intent);
            }
        });
    }

    private void iniciarComponentes() {
        nombreApellido = findViewById(R.id.nombreEstu);
        aciertosFallos = findViewById(R.id.rbAciertosyFallos);
        verTabla = findViewById(R.id.btnVerTabla);
        exportar = findViewById(R.id.btnExportar);
        tiempo = findViewById(R.id.rbTiempo);
        graph = findViewById(R.id.graph);
    }
}
