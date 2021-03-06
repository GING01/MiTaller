package mta.epn.ginghogam.com.mitaller.entidades;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Sesion implements Parcelable {

    private Integer idSesion;
    private String fechaSesion;
    private String nombretaller;
    private String nombretutor;
    private String nombreEstudiate;
    private String nombrehistoria;
    private Integer aciertos;
    private Integer fallos;
    private Long tiempo;
    private String logro;
    private String observacion;
    private Integer idEstudiante;
    private Integer idTaller;
    private Integer idHistoria;

    public Sesion() {
    }

    protected Sesion(Parcel in) {
        if (in.readByte() == 0) {
            idSesion = null;
        } else {
            idSesion = in.readInt();
        }
        fechaSesion = in.readString();
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
            tiempo = in.readLong();
        }
        logro = in.readString();
        observacion = in.readString();
        if (in.readByte() == 0) {
            idEstudiante = null;
        } else {
            idEstudiante = in.readInt();
        }
        if (in.readByte() == 0) {
            idTaller = null;
        } else {
            idTaller = in.readInt();
        }
        if (in.readByte() == 0) {
            idHistoria = null;
        } else {
            idHistoria = in.readInt();
        }
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

    public String getFechaSesion() {
        return fechaSesion;
    }

    public void setFechaSesion(String fechaSesion) {
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

    public Long getTiempo() {
        return tiempo;
    }

    public void setTiempo(Long tiempo) {
        this.tiempo = tiempo;
    }

    public String getLogro() {
        return logro;
    }

    public void setLogro(String logro) {
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

    public Integer getIdTaller() {
        return idTaller;
    }

    public void setIdTaller(Integer idTaller) {
        this.idTaller = idTaller;
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
        if (idSesion == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idSesion);
        }
        dest.writeString(fechaSesion);
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
            dest.writeLong(tiempo);
        }
        dest.writeString(logro);
        dest.writeString(observacion);
        if (idEstudiante == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idEstudiante);
        }
        if (idTaller == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idTaller);
        }
        if (idHistoria == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idHistoria);
        }
    }
}