package mta.epn.ginghogam.com.mitaller.entidades;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Taller implements Parcelable {

    private Integer idTaller;
    private String nombreTaller;
    private String imagenTaller;
    private String descripcionTaller;

    public Taller(Integer idTaller, String nombreTaller, String imagenTaller, String descripcionTaller) {
        this.idTaller = idTaller;
        this.nombreTaller = nombreTaller;
        this.imagenTaller = imagenTaller;
        this.descripcionTaller = descripcionTaller;
    }

    protected Taller(Parcel in) {
        if (in.readByte() == 0) {
            idTaller = null;
        } else {
            idTaller = in.readInt();
        }
        nombreTaller = in.readString();
        imagenTaller = in.readString();
        descripcionTaller = in.readString();
    }

    public static final Creator<Taller> CREATOR = new Creator<Taller>() {
        @Override
        public Taller createFromParcel(Parcel in) {
            return new Taller(in);
        }

        @Override
        public Taller[] newArray(int size) {
            return new Taller[size];
        }
    };

    public Integer getIdTaller() {
        return idTaller;
    }

    public void setIdTaller(Integer idTaller) {
        this.idTaller = idTaller;
    }

    public Taller() {

    }

    public String getNombreTaller() {
        return nombreTaller;
    }

    public void setNombreTaller(String nombreTaller) {
        this.nombreTaller = nombreTaller;
    }

    public String getImagenTaller() {
        return imagenTaller;
    }

    public void setImagenTaller(String imagenTaller) {
        this.imagenTaller = imagenTaller;
    }

    public String getDescripcionTaller() {
        return descripcionTaller;
    }

    public void setDescripcionTaller(String descripcionTaller) {
        this.descripcionTaller = descripcionTaller;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (idTaller == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idTaller);
        }
        dest.writeString(nombreTaller);
        dest.writeString(imagenTaller);
        dest.writeString(descripcionTaller);
    }
}
