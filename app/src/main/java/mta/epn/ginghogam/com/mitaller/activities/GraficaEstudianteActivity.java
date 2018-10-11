package mta.epn.ginghogam.com.mitaller.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.db.HistoriaDAO;
import mta.epn.ginghogam.com.mitaller.db.SesionDAO;
import mta.epn.ginghogam.com.mitaller.db.TallerDAO;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.entidades.Tutor;

public class GraficaEstudianteActivity extends AppCompatActivity {

    private TextView nombreApellido;
    private RadioButton aciertosFallos, tiempo;
    private Button verTabla, exportar;
    private boolean checked;
    private Integer idTaller, idHistoria;


    ImageView imageView;

    private GraphView graph;

    private Estudiante estudiante;
    private Tutor tutor;
    private SesionDAO sesionDAO;
    List<String> fechaList;

    private String nombreEstudiante, nombreTaller, nombreHistoria;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


    LineGraphSeries<DataPoint> series;


    Spinner spinnerTaller;
    Spinner spinnerHistoria;

    ArrayList<Taller> listTalleres;
    ArrayList<Historia> listHistoria;

    ArrayList<String> tallerArrayList;
    ArrayList<String> historiaArrayList;


    TallerDAO tallerDAO;
    HistoriaDAO historiaDAO;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica_estudiante);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        parametros();
        iniciarComponentes();



        consultarTalleres();
        comboTaller();

//        consultarDatosSesion();
//        grafica();
        graph.setVisibility(View.GONE);


        imageView = findViewById(R.id.cap);


    }


    private void consultarDatosSesion() {
        sesionDAO = new SesionDAO(this);
//        Cursor cursor = sesionDAO.retrieve(estudiante.getIdEstudiante());
        Cursor cursor = sesionDAO.retrieveGrafica(estudiante.getIdEstudiante(), idTaller, idHistoria);
        fechaList = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            do {


                String fecha = cursor.getString(1);
                nombreTaller = cursor.getString(2);
                nombreEstudiante = cursor.getString(5);
                nombreHistoria = cursor.getString(6);
                fechaList.add(fecha.toString());



            } while (cursor.moveToNext());
        }

        nombreApellido.setText(nombreEstudiante);

        Toast.makeText(getApplicationContext()," id taller"+idTaller+"  "+idHistoria, Toast.LENGTH_SHORT).show();

    }

    private void grafica() {


        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(getData());

        series.setColor(Color.rgb(244, 140, 70));
        series.setThickness(3);
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.argb(60, 95, 226, 156));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(5);
        series.setTitle("Fallos");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        graph.addSeries(series);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(getDataAcieros());

        series2.setColor(Color.rgb(66, 134, 244));
        series2.setThickness(3);
        series2.setDrawBackground(true);
        //series2.setBackgroundColor(Color.argb(60, 95, 226, 156));
        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(5);
        series2.setTitle("Aciertos");

        graph.addSeries(series2);


        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return sdf.format(new Date((long) value));
                } else {
                    return "" + ((int) value);
                }

            }
        });


//        graph.getGridLabelRenderer().setHumanRounding(false, false);

        graph.getGridLabelRenderer().setVerticalAxisTitle("ACIERTOS Y FALLOS");
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(18);
        graph.getGridLabelRenderer().setVerticalLabelsAlign(Paint.Align.CENTER);
        graph.getGridLabelRenderer().setTextSize(14);

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Estudiante: " + nombreEstudiante);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
        graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(12);
        graph.getGridLabelRenderer().setTextSize(14);


        graph.setTitle("Taller: " + nombreTaller+"  -  "+nombreHistoria);
        graph.setTitleTextSize(25);
        graph.setTitleColor(Color.RED);


        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling


        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);

        graph.getGridLabelRenderer().setHorizontalLabelsAngle(135);

        graph.getGridLabelRenderer().setNumHorizontalLabels(5);


    }


    private DataPoint[] getDataAcieros() {
        sesionDAO = new SesionDAO(this);

//        Cursor cursor = sesionDAO.retrieve(estudiante.getIdEstudiante());
        Cursor cursor = sesionDAO.retrieveGrafica(estudiante.getIdEstudiante(), idTaller, idHistoria);

        DataPoint[] dp = new DataPoint[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            dp[i] = new DataPoint(new Date(fechaList.get(i)), cursor.getInt(8));

        }

        return dp;
    }


    private DataPoint[] getData() {

        consultarDatosSesion();


        sesionDAO = new SesionDAO(this);

//        Cursor cursor = sesionDAO.retrieve(estudiante.getIdEstudiante());
        Cursor cursor = sesionDAO.retrieveGrafica(estudiante.getIdEstudiante(), idTaller, idHistoria);

        DataPoint[] dp = new DataPoint[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            dp[i] = new DataPoint(new Date(fechaList.get(i)), cursor.getInt(9));

        }

        return dp;
    }

    private DataPoint[] getDataTiempo() {
        sesionDAO = new SesionDAO(this);

        consultarDatosSesion();

//        Cursor cursor = sesionDAO.retrieve(estudiante.getIdEstudiante());
        Cursor cursor = sesionDAO.retrieveGrafica(estudiante.getIdEstudiante(), idTaller, idHistoria);

        DataPoint[] dp = new DataPoint[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            dp[i] = new DataPoint(new Date(fechaList.get(i)), cursor.getInt(10));

        }

        return dp;
    }

    private void parametros() {
        Bundle extra = getIntent().getExtras();
        estudiante = extra.getParcelable("estudiante");
        tutor = extra.getParcelable("tutor");
    }


    int cont = 0;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void exportarGrafica() {
        Bitmap bitmap = null;

        cont++;

        graph.setDrawingCacheEnabled(true);

        bitmap = Bitmap.createBitmap(graph.getDrawingCache());//important to make copy of that bitmap.


        String path = Environment.getExternalStorageDirectory().toString();
        File file = new File(path, nombreEstudiante + new Date() + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }


        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();

        PdfDocument.Page page = pdfDocument.startPage(pi);


        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);

        pdfDocument.finishPage(page);

        File root = new File(Environment.getExternalStorageDirectory(), "Grafica estudiantes");
        if (!root.exists()) {
            root.mkdir();
        }

        File file1 = new File(root, nombreEstudiante + " " + nombreTaller + "_" + new Date() + ".pdf");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file1);
            pdfDocument.writeTo(fileOutputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "PDF generado", Toast.LENGTH_SHORT).show();
        pdfDocument.close();

        graph.destroyDrawingCache();


    }

    private void iniciarComponentes() {
        nombreApellido = findViewById(R.id.nombreEstu);
        aciertosFallos = findViewById(R.id.rbAciertosyFallos);
        tiempo = findViewById(R.id.rbTiempo);
        graph = findViewById(R.id.graph);
        spinnerTaller = findViewById(R.id.spnTaller);
        spinnerHistoria = findViewById(R.id.spnHistoria);
    }

    public void onClick(View view) {
        checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbAciertosyFallos:
                if (checked) {
                    graph.removeAllSeries();
                    graph.setVisibility(View.VISIBLE);
                    grafica();

                }

                break;
            case R.id.rbTiempo:
                if (checked) {
                    graph.removeAllSeries();
                    graph.setVisibility(View.VISIBLE);
                    graficaTiempo();
                }
                break;


        }

    }

    private void graficaTiempo() {

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(getDataTiempo());

        series.setColor(Color.rgb(65, 99, 64));
        series.setThickness(3);
        series.setDrawBackground(true);
//        series.setBackgroundColor(Color.argb(60, 95, 226, 156));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(5);
        series.setTitle("Tiempo");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        graph.addSeries(series);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);


        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return sdf.format(new Date((long) value));
                } else {
                    return "" + ((int) value);
                }

            }
        });


        graph.getGridLabelRenderer().setVerticalAxisTitle("Tiempo (segundos)");
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(18);
        graph.getGridLabelRenderer().setVerticalLabelsAlign(Paint.Align.CENTER);
        graph.getGridLabelRenderer().setTextSize(14);

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Estudiante: " + nombreEstudiante);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
        graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(12);
        graph.getGridLabelRenderer().setTextSize(14);


        graph.setTitle("Taller: " + nombreTaller+"  - "+nombreHistoria);
        graph.setTitleTextSize(25);
        graph.setTitleColor(Color.RED);


        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling


        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);

        graph.getGridLabelRenderer().setHorizontalLabelsAngle(135);

        graph.getGridLabelRenderer().setNumHorizontalLabels(5);


    }



    private void consultarTalleres() {

        Taller taller = null;
        tallerDAO = new TallerDAO(this);
        Cursor cursor = tallerDAO.retrieve();
        listTalleres = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                taller = new Taller();
                taller.setIdTaller(cursor.getInt(0));
                taller.setNombreTaller(cursor.getString(1));
                taller.setDescripcionTaller(cursor.getString(2));
                taller.setImagenTaller(cursor.getString(3));

                listTalleres.add(taller);

            } while (cursor.moveToNext());
        }
        obtenerListaTalleres();
    }

    private void obtenerListaTalleres() {

        tallerArrayList = new ArrayList<>();

        for (int i = 0; i < listTalleres.size(); i++) {
            tallerArrayList.add(listTalleres.get(i).getNombreTaller());
        }

    }

    private void consultarHistoria(int idTaller) {

        historiaDAO = new HistoriaDAO(this);
        Historia historia = null;
        Cursor cursor = historiaDAO.retrieve(idTaller);
        listHistoria = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                historia = new Historia();
                historia.setIdHistoria(cursor.getInt(0));
                historia.setNombreHistoria(cursor.getString(1));
                historia.setDescripcionHistoria(cursor.getString(2));
                historia.setImagenHistoria(cursor.getString(3));
                historia.setNumeroLaminas(cursor.getString(3));
                historia.setIdTaller(cursor.getInt(0));
                listHistoria.add(historia);
            } while (cursor.moveToNext());
        }
        obtenerListaHistoria();
    }

    private void obtenerListaHistoria() {

        historiaArrayList = new ArrayList<>();

        for (int i = 0; i < listHistoria.size(); i++) {
            historiaArrayList.add(listHistoria.get(i).getNombreHistoria());
        }

    }

    private void comboTaller() {

        ArrayAdapter<CharSequence> adatadorTaller = new ArrayAdapter(
                this, android.R.layout.simple_spinner_item, tallerArrayList);
        spinnerTaller.setAdapter(adatadorTaller);

        spinnerTaller.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    default:
                        idTaller = listTalleres.get(position).getIdTaller();
                        Toast.makeText(getApplicationContext(), "Id taller: " + idTaller, Toast.LENGTH_SHORT).show();

                        consultarHistoria(idTaller);
                        comboHistoria();

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void comboHistoria() {

        ArrayAdapter<CharSequence> adatadorHistoria = new ArrayAdapter(
                this, android.R.layout.simple_spinner_item, historiaArrayList);
        spinnerHistoria.setAdapter(adatadorHistoria);

        spinnerHistoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    default:
                        idHistoria = listHistoria.get(position).getIdHistoria();
//                        Toast.makeText(getApplicationContext(), "Id historia: " + idHistoria, Toast.LENGTH_SHORT).show();

                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grafica, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.verTabla) {
            Intent intent = new Intent(GraficaEstudianteActivity.this, TablaResultadosActivity.class);
            intent.putExtra("tutor", tutor);
            intent.putExtra("estudiante", estudiante);
            startActivity(intent);
            return true;
        }
        if (id == R.id.exportarGrafica) {
            exportarGrafica();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

