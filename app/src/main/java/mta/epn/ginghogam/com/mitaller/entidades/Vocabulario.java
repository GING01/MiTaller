package mta.epn.ginghogam.com.mitaller.entidades;

import android.os.Parcel;
import android.os.Parcelable;


public class Vocabulario implements Parcelable {

    private Integer idpalabra;
    private String palabra;
    private String imagenPalabra;
    private String sonidoPalabra;
    private String tipoPalabra;
    private Integer idTaller;


    public Vocabulario() {
    }

    protected Vocabulario(Parcel in) {
        if (in.readByte() == 0) {
            idpalabra = null;
        } else {
            idpalabra = in.readInt();
        }
        palabra = in.readString();
        imagenPalabra = in.readString();
        sonidoPalabra = in.readString();
        tipoPalabra = in.readString();
        if (in.readByte() == 0) {
            idTaller = null;
        } else {
            idTaller = in.readInt();
        }
    }

    public static final Creator<Vocabulario> CREATOR = new Creator<Vocabulario>() {
        @Override
        public Vocabulario createFromParcel(Parcel in) {
            return new Vocabulario(in);
        }

        @Override
        public Vocabulario[] newArray(int size) {
            return new Vocabulario[size];
        }
    };

    public Integer getIdpalabra() {
        return idpalabra;
    }

    public void setIdpalabra(Integer idpalabra) {
        this.idpalabra = idpalabra;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public String getImagenPalabra() {
        return imagenPalabra;
    }

    public void setImagenPalabra(String imagenPalabra) {
        this.imagenPalabra = imagenPalabra;
    }

    public String getSonidoPalabra() {
        return sonidoPalabra;
    }

    public void setSonidoPalabra(String sonidoPalabra) {
        this.sonidoPalabra = sonidoPalabra;
    }

    public String getTipoPalabra() {
        return tipoPalabra;
    }

    public void setTipoPalabra(String tipoPalabra) {
        this.tipoPalabra = tipoPalabra;
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
        if (idpalabra == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idpalabra);
        }
        dest.writeString(palabra);
        dest.writeString(imagenPalabra);
        dest.writeString(sonidoPalabra);
        dest.writeString(tipoPalabra);
        if (idTaller == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idTaller);
        }
    }
}
