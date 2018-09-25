package mta.epn.ginghogam.com.mitaller.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mta.epn.ginghogam.com.mitaller.constantes.ConstanteDB;
import mta.epn.ginghogam.com.mitaller.entidades.Sesion;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;

public class SesionDAO extends SQLiteDB {
    public SesionDAO(Context context) {
        super(context);
    }
    public void create(Sesion sesion){
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ConstanteDB.COLUMN_FECHA, String.valueOf(sesion.getFechaSesion()));
        values.put(ConstanteDB.COLUMN_NOMBRE_TALLER, sesion.getNombretaller());
        values.put(ConstanteDB.COLUMN_NOMBRE_TUTOR_RESULTADOS, sesion.getNombretutor());
        values.put(ConstanteDB.COLUMN_NOMBRE_ESTUDIANTE_RESULTADOS, sesion.getNombreEstudiate());
        values.put(ConstanteDB.COLUMN_NOMBRE_HISTORIA_RESULTADOS, sesion.getNombrehistoria());
        values.put(ConstanteDB.COLUMN_ACIERTOS, sesion.getAciertos());
        values.put(ConstanteDB.COLUMN_FALLOS, sesion.getFallos());
        values.put(ConstanteDB.COLUMN_TIEMPO_EJERCICIO, sesion.getTiempo());
        values.put(ConstanteDB.COLUMN_RESULTADO_EJERCICIO, String.valueOf(sesion.getLogro()));
        values.put(ConstanteDB.COLUMN_OBSERVACION_RESULTADO, sesion.getObservacion());
        values.put(ConstanteDB.COLUMN_ID_ESTUDIANTE_FK, sesion.getIdEstudiante());



        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ConstanteDB.TABLE_SESION,
                null,
                values);
    }

    public Cursor retrieve(long id){
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + ConstanteDB.TABLE_SESION + " WHERE "
                + ConstanteDB.COLUMN_ID_ESTUDIANTE_FK + " = " + id ;

        Cursor c = db.rawQuery(selectQuery, null);

        return c;
    }


}
