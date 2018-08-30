package mta.epn.ginghogam.com.mitaller.entidades;

import android.os.Parcel;
import android.os.Parcelable;

public class Secuencia implements Parcelable{

    private Integer idSecuencia;
    private String imagenSecuencia;
    private Integer ordenImagenSecuencia;
    private Integer idHistoria;

    public Secuencia() { }

    protected Secuencia(Parcel in) {
        if (in.readByte() == 0) {
            idSecuencia = null;
        } else {
            idSecuencia = in.readInt();
        }
        imagenSecuencia = in.readString();
        if (in.readByte() == 0) {
            idHistoria = null;
        } else {
            idHistoria = in.readInt();
        }
    }

    public static final Creator<Secuencia> CREATOR = new Creator<Secuencia>() {
        @Override
        public Secuencia createFromParcel(Parcel in) {
            return new Secuencia(in);
        }

        @Override
        public Secuencia[] newArray(int size) {
            return new Secuencia[size];
        }
    };

    public Integer getIdSecuencia() {
        return idSecuencia;
    }

    public void setIdSecuencia(Integer idSecuencia) {
        this.idSecuencia = idSecuencia;
    }

    public String getImagenSecuencia() {
        return imagenSecuencia;
    }

    public void setImagenSecuencia(String imagenSecuencia) {
        this.imagenSecuencia = imagenSecuencia;
    }

    public Integer getIdHistoria() {
        return idHistoria;
    }

    public void setIdHistoria(Integer idHistoria) {
        this.idHistoria = idHistoria;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (idSecuencia == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idSecuencia);
        }
        dest.writeString(imagenSecuencia);
        if (idHistoria == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idHistoria);
        }
    }

    public Integer getOrdenImagenSecuencia() {
        return ordenImagenSecuencia;
    }

    public void setOrdenImagenSecuencia(Integer ordenImagenSecuencia) {
        this.ordenImagenSecuencia = ordenImagenSecuencia;
    }
}
