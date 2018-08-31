package mta.epn.ginghogam.com.mitaller.entidades;

import android.os.Parcel;
import android.os.Parcelable;

public class Estudiante implements Parcelable {

    private Integer idEstudiante;
    private String nombreEstudiate;
    private String apellidoEstudiante;
    private String fotoEstudiante;
    private String generoEstudiante;
    private String perfilEstudiante;
    private Integer edadEstudiante;
    private Integer idTutor;

    public Estudiante() {
    }

    protected Estudiante(Parcel in) {
        if (in.readByte() == 0) {
            idEstudiante = null;
        } else {
            idEstudiante = in.readInt();
        }
        nombreEstudiate = in.readString();
        apellidoEstudiante = in.readString();
        fotoEstudiante = in.readString();
        generoEstudiante = in.readString();
        perfilEstudiante = in.readString();
        if (in.readByte() == 0) {
            edadEstudiante = null;
        } else {
            edadEstudiante = in.readInt();
        }
        if (in.readByte() == 0) {
            idTutor = null;
        } else {
            idTutor = in.readInt();
        }
    }

    public static final Creator<Estudiante> CREATOR = new Creator<Estudiante>() {
        @Override
        public Estudiante createFromParcel(Parcel in) {
            return new Estudiante(in);
        }

        @Override
        public Estudiante[] newArray(int size) {
            return new Estudiante[size];
        }
    };

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getNombreEstudiate() {
        return nombreEstudiate;
    }

    public void setNombreEstudiate(String nombreEstudiate) {
        this.nombreEstudiate = nombreEstudiate;
    }

    public String getApellidoEstudiante() {
        return apellidoEstudiante;
    }

    public void setApellidoEstudiante(String apellidoEstudiante) {
        this.apellidoEstudiante = apellidoEstudiante;
    }

    public String getFotoEstudiante() {
        return fotoEstudiante;
    }

    public void setFotoEstudiante(String fotoEstudiante) {
        this.fotoEstudiante = fotoEstudiante;
    }

    public String getGeneroEstudiante() {
        return generoEstudiante;
    }

    public void setGeneroEstudiante(String generoEstudiante) {
        this.generoEstudiante = generoEstudiante;
    }

    public String getPerfilEstudiante() {
        return perfilEstudiante;
    }

    public void setPerfilEstudiante(String perfilEstudiante) {
        this.perfilEstudiante = perfilEstudiante;
    }

    public Integer getEdadEstudiante() {
        return edadEstudiante;
    }

    public void setEdadEstudiante(Integer edadEstudiante) {
        this.edadEstudiante = edadEstudiante;
    }

    public Integer getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(Integer idTutor) {
        this.idTutor = idTutor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (idEstudiante == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idEstudiante);
        }
        dest.writeString(nombreEstudiate);
        dest.writeString(apellidoEstudiante);
        dest.writeString(fotoEstudiante);
        dest.writeString(generoEstudiante);
        dest.writeString(perfilEstudiante);
        if (edadEstudiante == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(edadEstudiante);
        }
        if (idTutor == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idTutor);
        }
    }
}
