package mta.epn.ginghogam.com.mitaller.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mta.epn.ginghogam.com.mitaller.constantes.ConstanteDB;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;

public class TallerDAO extends SQLiteDB {
    public TallerDAO(Context context) {
        super(context);
    }
    public void create(Taller taller){
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ConstanteDB.COLUMN_NOMBRE, taller.getNombreTaller());
        values.put(ConstanteDB.COLUMN_DESCRIPCION, taller.getDescripcionTaller());
        values.put(ConstanteDB.COLUMN_IMAGEN, taller.getImagenTaller());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ConstanteDB.TABLE_TALLER,
                null,
                values);
    }

    public Cursor retrieve(){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ConstanteDB.COLUMN_ID,
                ConstanteDB.COLUMN_NOMBRE,
                ConstanteDB.COLUMN_DESCRIPCION,
                ConstanteDB.COLUMN_IMAGEN};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ConstanteDB.COLUMN_NOMBRE + " ASC";

        Cursor c = db.query(
                ConstanteDB.TABLE_TALLER,                    // The table to query
                projection,                                 // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sortOrder                                   // The sort order
        );

        return c;
    }

    public void update(Taller taller){
        SQLiteDatabase db = getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(ConstanteDB.COLUMN_NOMBRE, taller.getNombreTaller());
        values.put(ConstanteDB.COLUMN_DESCRIPCION, taller.getDescripcionTaller());
        values.put(ConstanteDB.COLUMN_IMAGEN, taller.getImagenTaller());

        // Which row to update, based on the ID
        String selection = ConstanteDB.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(taller.getIdTaller()) };

        int count = db.update(
                ConstanteDB.TABLE_TALLER,
                values,
                selection,
                selectionArgs);
    }

    public void delete(int id){
        SQLiteDatabase db = getReadableDatabase();

        // Define 'where' part of query.
        String selection = ConstanteDB.COLUMN_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(id) };
        // Issue SQL statement.
        db.delete(ConstanteDB.TABLE_TALLER, selection, selectionArgs);
    }
}
