package mta.epn.ginghogam.com.mitaller.constantes;

import android.content.Intent;

public class ConstanteDB {

    //Tabla TALLER
    public static final String TABLE_TALLER = "taller";
    public static final String COLUMN_ID = "taller_id";
    public static final String COLUMN_NOMBRE = "taller_nombre";
    public static final String COLUMN_DESCRIPCION = "taller_descripcion";
    public static final String COLUMN_IMAGEN = "taller_imagen";

    //Tabla HISTORIA
    public static final String TABLE_HISTORIA = "historia";
    public static final String COLUMN_ID_HISTORIA = "historia_id";
    public static final String COLUMN_NOMBRE_HISTORIA = "historia_nombre";
    public static final String COLUMN_DESCRIPCION_HISTORIA = "historia_descripcion";
    public static final String COLUMN_IMAGEN_HISTORIA = "historia_imagen";
    public static final String COLUMN_NUM_LAMINAS = "num_laminas";
    public static final String COLUMN_DIFICULTAD = "dificultad";
    public static final String COLUMN_ID_TALLER = "id_taller_fk";

    //Tabla VOCABULARIO
    public static final String TABLE_VOCABULARIO = "vocabulario";
    public static final String COLUMN_ID_PALABRA = "palabra_id";
    public static final String COLUMN_PALABRA = "palabra";
    public static final String COLUMN_IMAGEN_PALABRA = "palabra_imagen";
    public static final String COLUMN_SONIDO_PALABRA = "palabra_sonido";
    public static final String COLUMN_TIPO_PALABRA = "palabra_tipo";
    public static final String COLUMN_ID_TALLER_PK_VOCABULARIO = "vocabulario_taller_pk_id";

    //Tabla SECUENCIA
    public static final String TABLE_SECUENCIA = "secuencia";
    public static final String COLUMN_ID_SECUENCIA = "secuencia_id";
    public static final String COLUMN_IMAGEN_SECUENCIA = "secuencia_imagen";
    public static final String COLUMN_IMAGEN_ORDEN = "secuencia_imagen_orden";
    public static final String COLUMN_IMAGEN_DESCRIPCION = "secuencia_imagen_descripcion";
    public static final String COLUMN_ID_HISTORIA_PK = "historia_pk_id";

    //Tabla TUTOR
    public static final String TABLE_TUTOR= "tutor";
    public static final String COLUMN_ID_TUTOR = "tutor_id";
    public static final String COLUMN_NOMBRE_TUTOR = "nombre_tutor";
    public static final String COLUMN_APELLIDO_TUTOR = "apellido_tutor";
    public static final String COLUMN_CI_TUTOR = "ci_tutor";
    public static final String COLUMN_USUARIO_TUTOR = "usuario_tutor";
    public static final String COLUMN_CONTRASEÑA_TUTOR = "contraseña_tutor";

    //Tabla ESTUDIANTE
    public static final String TABLE_ESTUDIANTE= "estudiante";
    public static final String COLUMN_ID_ESTUDIANTE = "estudiante_id";
    public static final String COLUMN_NOMBRE_ESTUDIANTE = "nombre_estudiante";
    public static final String COLUMN_APELLIDO_ESTUDIANTE = "apellido_estudiante";
    public static final String COLUMN_FOTO_ESTUDIANTE = "foto_estudiante";
    public static final String COLUMN_GENERO_ESTUDIANTE = "genero_estudiante";
    public static final String COLUMN_PERFIL_ESTUDIANTE = "perfil_estudiante";
    public static final String COLUMN_EDAD_ESTUDIANTE = "edad_estudiante";
    public static final String COLUMN_ID_TUTOR_PK = "estudiante_id_pk";

    //Tabla SESION
    public static final String TABLE_SESION= "sesion";
    public static final String COLUMN_ID_SESION = "sesion_id";
    public static final String COLUMN_FECHA = "fecha_sesion";
    public static final String COLUMN_NOMBRE_TALLER = "nombre_taller_sesion";
    public static final String COLUMN_ID_TALLER_SESION = "id_taller_sesion";
    public static final String COLUMN_NOMBRE_TUTOR_RESULTADOS= "nombre_tutor_resultados";
    public static final String COLUMN_NOMBRE_ESTUDIANTE_RESULTADOS = "nombre_estudiante_resultados";
    public static final String COLUMN_NOMBRE_HISTORIA_RESULTADOS = "nombre_historia_resultados";
    public static final String COLUMN_ID_HISTORIA_SESION = "id_historia_sesion";
    public static final String COLUMN_ACIERTOS = "aciertos";
    public static final String COLUMN_FALLOS = "fallos";
    public static final String COLUMN_TIEMPO_EJERCICIO = "tiempo";
    public static final String COLUMN_RESULTADO_EJERCICIO= "resultado_ejercicio";
    public static final String COLUMN_OBSERVACION_RESULTADO = "observacion";
    public static final String COLUMN_ID_ESTUDIANTE_FK = "id_estudiante_fk";









}
