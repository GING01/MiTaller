package mta.epn.ginghogam.com.mitaller.activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import mta.epn.ginghogam.com.mitaller.R;


public class SeleccionDificultadActivity extends AppCompatActivity {

    private SeekBar seekBarDificultad;
    private TextView dificultad;
    private int seekbarvalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_dificultad);
        seekBarDificultad=findViewById(R.id.seekbardificultad);
        dificultad=findViewById(R.id.dificultad);
        seekBarDificultad.setMax(9);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBarDificultad.setMin(1);
        }
        seekBarDificultad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress>=1 && progress <=3){
                    seekbarvalue=progress;
                    dificultad.setText("FACIL: "+progress+ " laminas");
                }
                if(progress>3 && progress <=6){
                    dificultad.setText("MEDIO: "+progress+ " laminas");
                }
                if(progress>6 && progress <=9){
                    dificultad.setText("DIFICIL: "+progress+ " laminas");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void pasar(View view) {
    }
}
