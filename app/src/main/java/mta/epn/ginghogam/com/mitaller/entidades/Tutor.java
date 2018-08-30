package mta.epn.ginghogam.com.mitaller.entidades;

import android.os.Parcel;
import android.os.Parcelable;

public class Tutor implements Parcelable {
    Integer idTutor;
    String nombreTutor;
    String apellidoTutor;
    String ciTutor;
    String usuarioTutor;
    String contraseñaTutor;

    public Tutor(Integer idTutor, String nombreTutor, String apellidoTutor, String ciTutor, String usuarioTutor, String contraseñaTutor) {
        this.idTutor = idTutor;
        this.nombreTutor = nombreTutor;
        this.apellidoTutor = apellidoTutor;
        this.ciTutor = ciTutor;
        this.usuarioTutor = usuarioTutor;
        this.contraseñaTutor = contraseñaTutor;
    }

    public Tutor() {
    }

    protected Tutor(Parcel in) {
        if (in.readByte() == 0) {
            idTutor = null;
        } else {
            idTutor = in.readInt();
        }
        nombreTutor = in.readString();
        apellidoTutor = in.readString();
        ciTutor = in.readString();
        usuarioTutor = in.readString();
        contraseñaTutor = in.readString();
    }

    public static final Creator<Tutor> CREATOR = new Creator<Tutor>() {
        @Override
        public Tutor createFromParcel(Parcel in) {
            return new Tutor(in);
        }

        @Override
        public Tutor[] newArray(int size) {
            return new Tutor[size];
        }
    };

    public Integer getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(Integer idTutor) {
        this.idTutor = idTutor;
    }

    public String getNombreTutor() {
        return nombreTutor;
    }

    public void setNombreTutor(String nombreTutor) {
        this.nombreTutor = nombreTutor;
    }

    public String getApellidoTutor() {
        return apellidoTutor;
    }

    public void setApellidoTutor(String apellidoTutor) {
        this.apellidoTutor = apellidoTutor;
    }

    public String getCiTutor() {
        return ciTutor;
    }

    public void setCiTutor(String ciTutor) {
        this.ciTutor = ciTutor;
    }

    public String getUsuarioTutor() {
        return usuarioTutor;
    }

    public void setUsuarioTutor(String usuarioTutor) {
        this.usuarioTutor = usuarioTutor;
    }

    public String getContraseñaTutor() {
        return contraseñaTutor;
    }

    public void setContraseñaTutor(String contraseñaTutor) {
        this.contraseñaTutor = contraseñaTutor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (idTutor == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idTutor);
        }
        dest.writeString(nombreTutor);
        dest.writeString(apellidoTutor);
        dest.writeString(ciTutor);
        dest.writeString(usuarioTutor);
        dest.writeString(contraseñaTutor);
    }
}