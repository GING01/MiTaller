package mta.epn.ginghogam.com.mitaller.entidades;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Historia implements Parcelable{

    private Integer idHistoria;
    private String nombreHistoria;
    private String descripcionHistoria;
    private String imagenHistoria;
    private String dificultad;
    private String numeroLaminas;
    private Integer idTaller; //clave foranea



    public Historia(){}

    protected Historia(Parcel in) {
        if (in.readByte() == 0) {
            idHistoria = null;
        } else {
            idHistoria = in.readInt();
        }
        nombreHistoria = in.readString();
        descripcionHistoria = in.readString();
        imagenHistoria = in.readString();
        dificultad = in.readString();
        numeroLaminas = in.readString();
        if (in.readByte() == 0) {
            idTaller = null;
        } else {
            idTaller = in.readInt();
        }
    }

    public static final Creator<Historia> CREATOR = new Creator<Historia>() {
        @Override
        public Historia createFromParcel(Parcel in) {
            return new Historia(in);
        }

        @Override
        public Historia[] newArray(int size) {
            return new Historia[size];
        }
    };

    public Integer getIdHistoria() {
        return idHistoria;
    }

    public void setIdHistoria(Integer idHistoria) {
        this.idHistoria = idHistoria;
    }

    public String getNombreHistoria() {
        return nombreHistoria;
    }

    public void setNombreHistoria(String nombreHistoria) {
        this.nombreHistoria = nombreHistoria;
    }

    public String getDescripcionHistoria() {
        return descripcionHistoria;
    }

    public void setDescripcionHistoria(String descripcionHistoria) {
        this.descripcionHistoria = descripcionHistoria;
    }

    public String getImagenHistoria() {
        return imagenHistoria;
    }

    public void setImagenHistoria(String imagenHistoria) {
        this.imagenHistoria = imagenHistoria;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getNumeroLaminas() {
        return numeroLaminas;
    }

    public void setNumeroLaminas(String numeroLaminas) {
        this.numeroLaminas = numeroLaminas;
    }

    public Integer getIdTaller() {
        return idTaller;
    }

    public void setIdTaller(Integer idTaller) {
        this.idTaller = idTaller;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (idHistoria == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idHistoria);
        }
        dest.writeString(nombreHistoria);
        dest.writeString(descripcionHistoria);
        dest.writeString(imagenHistoria);
        dest.writeString(dificultad);
        dest.writeString(numeroLaminas);
        if (idTaller == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idTaller);
        }
    }
}
