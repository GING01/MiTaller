package mta.epn.ginghogam.com.mitaller.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import mta.epn.ginghogam.com.mitaller.constantes.ConstanteDB;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;

public class SQLiteDB extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MiTaller.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String INTEGER_ = " INTEGER";


    //Tabla TALLER
    private static final String SQL_CREATE_TALLER =
            "CREATE TABLE " + ConstanteDB.TABLE_TALLER + " (" +
                    ConstanteDB.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ConstanteDB.COLUMN_NOMBRE + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_DESCRIPCION + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_IMAGEN + TEXT_TYPE + " )";

    private static final String SQL_DELETE_TALLER =
            "DROP TABLE IF EXISTS " + ConstanteDB.TABLE_TALLER;

    //Tabla HISTORIA
    private static final String SQL_CREATE_HISTORIA =
            "CREATE TABLE " + ConstanteDB.TABLE_HISTORIA + " (" +
                    ConstanteDB.COLUMN_ID_HISTORIA + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ConstanteDB.COLUMN_NOMBRE_HISTORIA + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_DESCRIPCION_HISTORIA + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_IMAGEN_HISTORIA + TEXT_TYPE +  COMMA_SEP +
                    ConstanteDB.COLUMN_NUM_LAMINAS + TEXT_TYPE +  COMMA_SEP +
                    ConstanteDB.COLUMN_DIFICULTAD + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_ID_TALLER + INTEGER_ + " )";

    private static final String SQL_DELETE_HISTORIA =
            "DROP TABLE IF EXISTS " + ConstanteDB.TABLE_HISTORIA;

    //Tabla VOCABULARIO
    private static final String SQL_CREATE_VOCABULARIO =
            "CREATE TABLE " + ConstanteDB.TABLE_VOCABULARIO + " (" +
                    ConstanteDB.COLUMN_ID_PALABRA + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ConstanteDB.COLUMN_PALABRA + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_IMAGEN_PALABRA + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_SONIDO_PALABRA + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_TIPO_PALABRA + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_ID_TALLER_PK_VOCABULARIO + INTEGER_ + " )";

    private static final String SQL_DELETE_VOCABULARIO =
            "DROP TABLE IF EXISTS " + ConstanteDB.TABLE_VOCABULARIO;

    //Tabla SECUENCIA
    private static final String SQL_CREATE_SECUENCIA =
            "CREATE TABLE " + ConstanteDB.TABLE_SECUENCIA + " (" +
                    ConstanteDB.COLUMN_ID_SECUENCIA + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ConstanteDB.COLUMN_IMAGEN_SECUENCIA + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_IMAGEN_ORDEN + INTEGER_ + COMMA_SEP +
                    ConstanteDB.COLUMN_ID_HISTORIA_PK + INTEGER_ + " )";

    private static final String SQL_DELETE_SECUENCIA =
            "DROP TABLE IF EXISTS " + ConstanteDB.TABLE_SECUENCIA;

    //Tabla TUTOR
    private static final String SQL_CREATE_TUTOR =
            "CREATE TABLE " + ConstanteDB.TABLE_TUTOR + " (" +
                    ConstanteDB.COLUMN_ID_TUTOR + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ConstanteDB.COLUMN_NOMBRE_TUTOR + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_APELLIDO_TUTOR + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_CI_TUTOR+ TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_USUARIO_TUTOR + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_CONTRASEÑA_TUTOR + TEXT_TYPE +" )";

    private static final String SQL_DELETE_TUTOR =
            "DROP TABLE IF EXISTS " + ConstanteDB.TABLE_TUTOR;

    //Tabla ESTUDIANTE
    private static final String SQL_CREATE_ESTUDIANTE =
            "CREATE TABLE " + ConstanteDB.TABLE_ESTUDIANTE + " (" +
                    ConstanteDB.COLUMN_ID_ESTUDIANTE + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ConstanteDB.COLUMN_NOMBRE_ESTUDIANTE + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_APELLIDO_ESTUDIANTE + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_EDAD_ESTUDIANTE+ INTEGER_ + COMMA_SEP +
                    ConstanteDB.COLUMN_FOTO_ESTUDIANTE + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_GENERO_ESTUDIANTE + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_PERFIL_ESTUDIANTE + TEXT_TYPE + COMMA_SEP +
                    ConstanteDB.COLUMN_ID_TUTOR_PK + INTEGER_ + " )";

    private static final String SQL_DELETE_ESTUDIANTE =
            "DROP TABLE IF EXISTS " + ConstanteDB.TABLE_ESTUDIANTE;


    public SQLiteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ESTUDIANTE);
        db.execSQL(SQL_CREATE_SECUENCIA);
        db.execSQL(SQL_CREATE_HISTORIA);
        db.execSQL(SQL_CREATE_VOCABULARIO);
        db.execSQL(SQL_CREATE_TALLER);
        db.execSQL(SQL_CREATE_TUTOR);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ESTUDIANTE);
        db.execSQL(SQL_DELETE_SECUENCIA);
        db.execSQL(SQL_DELETE_HISTORIA);
        db.execSQL(SQL_DELETE_VOCABULARIO);
        db.execSQL(SQL_DELETE_TALLER);
        db.execSQL(SQL_DELETE_TUTOR);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
