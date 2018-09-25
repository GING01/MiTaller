package mta.epn.ginghogam.com.mitaller.entidades;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Sesion implements Parcelable {

    private Integer idSesion;
    private Date fechaSesion;
    private String nombretaller;
    private String nombretutor;
    private String nombreEstudiate;
    private String nombrehistoria;
    private Integer aciertos;
    private Integer fallos;
    private Integer tiempo;
    private Boolean logro;
    private String observacion;
    private Integer idEstudiante;

    public Sesion() {
    }

    protected Sesion(Parcel in) {
        if (in.readByte() == 0) {
            idSesion = null;
        } else {
            idSesion = in.readInt();
        }
        nombretaller = in.readString();
        nombretutor = in.readString();
        nombreEstudiate = in.readString();
        nombrehistoria = in.readString();
        if (in.readByte() == 0) {
            aciertos = null;
        } else {
            aciertos = in.readInt();
        }
        if (in.readByte() == 0) {
            fallos = null;
        } else {
            fallos = in.readInt();
        }
        if (in.readByte() == 0) {
            tiempo = null;
        } else {
            tiempo = in.readInt();
        }
        byte tmpLogro = in.readByte();
        logro = tmpLogro == 0 ? null : tmpLogro == 1;
        observacion = in.readString();
        if (in.readByte() == 0) {
            idEstudiante = null;
        } else {
            idEstudiante = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (idSesion == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idSesion);
        }
        dest.writeString(nombretaller);
        dest.writeString(nombretutor);
        dest.writeString(nombreEstudiate);
        dest.writeString(nombrehistoria);
        if (aciertos == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(aciertos);
        }
        if (fallos == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(fallos);
        }
        if (tiempo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(tiempo);
        }
        dest.writeByte((byte) (logro == null ? 0 : logro ? 1 : 2));
        dest.writeString(observacion);
        if (idEstudiante == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idEstudiante);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Sesion> CREATOR = new Creator<Sesion>() {
        @Override
        public Sesion createFromParcel(Parcel in) {
            return new Sesion(in);
        }

        @Override
        public Sesion[] newArray(int size) {
            return new Sesion[size];
        }
    };

    public Integer getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(Integer idSesion) {
        this.idSesion = idSesion;
    }

    public Date getFechaSesion() {
        return fechaSesion;
    }

    public void setFechaSesion(Date fechaSesion) {
        this.fechaSesion = fechaSesion;
    }

    public String getNombretaller() {
        return nombretaller;
    }

    public void setNombretaller(String nombretaller) {
        this.nombretaller = nombretaller;
    }

    public String getNombretutor() {
        return nombretutor;
    }

    public void setNombretutor(String nombretutor) {
        this.nombretutor = nombretutor;
    }

    public String getNombreEstudiate() {
        return nombreEstudiate;
    }

    public void setNombreEstudiate(String nombreEstudiate) {
        this.nombreEstudiate = nombreEstudiate;
    }

    public String getNombrehistoria() {
        return nombrehistoria;
    }

    public void setNombrehistoria(String nombrehistoria) {
        this.nombrehistoria = nombrehistoria;
    }

    public Integer getAciertos() {
        return aciertos;
    }

    public void setAciertos(Integer aciertos) {
        this.aciertos = aciertos;
    }

    public Integer getFallos() {
        return fallos;
    }

    public void setFallos(Integer fallos) {
        this.fallos = fallos;
    }

    public Integer getTiempo() {
        return tiempo;
    }

    public void setTiempo(Integer tiempo) {
        this.tiempo = tiempo;
    }

    public Boolean getLogro() {
        return logro;
    }

    public void setLogro(Boolean logro) {
        this.logro = logro;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }
}