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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;



import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    ImageView imageView;

    private GraphView graph;

    private Estudiante estudiante;
    private Tutor tutor;
    private SesionDAO sesionDAO;
    List<String> fechaList;

    private String nombreEstudiante, nombreTaller;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


    LineGraphSeries<DataPoint> series;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica_estudiante);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        parametros();
        iniciarComponentes();
        llamarTabla();
        consultarDatosSesion();


        grafica();
        nombreApellido.setText(nombreEstudiante);

        imageView = findViewById(R.id.imageView);
        exportar.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                exportarGrafica();
            }
        });


    }

    private void consultarDatosSesion() {
        sesionDAO = new SesionDAO(this);
        Cursor cursor = sesionDAO.retrieve(estudiante.getIdEstudiante());
        fechaList = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            do {


                String fecha = cursor.getString(1);
                nombreTaller = cursor.getString(2);
                nombreEstudiante = cursor.getString(4);
                fechaList.add(fecha.toString());


            } while (cursor.moveToNext());
        }

        Toast.makeText(getApplicationContext(), "" + fechaList, Toast.LENGTH_SHORT).show();

    }

    private void grafica() {


        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(getData());

        series.setColor(Color.rgb(244, 140, 70));
        series.setThickness(1);
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
        series2.setThickness(1);
        series2.setDrawBackground(true);
        //series2.setBackgroundColor(Color.argb(60, 95, 226, 156));
        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(5);
        series2.setTitle("Aciertos");



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

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Estudiante: "+nombreEstudiante);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
        graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(12);
        graph.getGridLabelRenderer().setTextSize(14);


        graph.setTitle("Taller: " + nombreTaller);
        graph.setTitleTextSize(25);
        graph.setTitleColor(Color.RED);


        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
//
//        graph.getViewport().setYAxisBoundsManual(true);
//        graph.getViewport().setXAxisBoundsManual(true);

//        graph.getViewport().setMaxX(fechaList.size());
//        graph.getViewport().setMaxY(fechaList.size());
//
//        graph.getViewport().setMinY(0);
//        graph.getViewport().setMinX(0);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        graph.addSeries(series2);


    }

    private DataPoint[] getDataAcieros() {
        sesionDAO = new SesionDAO(this);

        Cursor cursor = sesionDAO.retrieve(estudiante.getIdEstudiante());
        DataPoint[] dp = new DataPoint[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            dp[i] = new DataPoint(new Date(fechaList.get(i)), cursor.getInt(6));

        }

        return dp;
    }


    private DataPoint[] getData() {

        sesionDAO = new SesionDAO(this);

        Cursor cursor = sesionDAO.retrieve(estudiante.getIdEstudiante());
        DataPoint[] dp = new DataPoint[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            dp[i] = new DataPoint(new Date(fechaList.get(i)), cursor.getInt(7));

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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void exportarGrafica() {
        graph.buildDrawingCache();
        Bitmap bitmap = graph.getDrawingCache();

        String path = Environment.getExternalStorageDirectory().toString();
        File file = new File(path, "example1" + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.

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

        File root = new File(Environment.getExternalStorageDirectory(), "PDF Folder 12");
        if (!root.exists()) {
            root.mkdir();
        }

        File file1 = new File(root, "picture2.pdf");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file1);
            pdfDocument.writeTo(fileOutputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
        pdfDocument.close();

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
