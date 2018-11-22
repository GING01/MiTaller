package mta.epn.ginghogam.com.mitaller.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.activities.EleccionEstudianteEntrenamientoActivity;
import mta.epn.ginghogam.com.mitaller.activities.EstudiantesActivity;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.listener.RecyclerItemClickListener;

public class EstudianteEntrenamientoListAdapter extends RecyclerView.Adapter<EstudianteEntrenamientoListAdapter.ContactHolder> implements View.OnClickListener {

    private List<Estudiante> estudianteList;
    private Context context;
    EleccionEstudianteEntrenamientoActivity estudianteActivity;

    private RecyclerItemClickListener recyclerItemClickListener;
    protected View.OnClickListener onClickListener;

    public EstudianteEntrenamientoListAdapter(Context context) {
        this.context = context;
        this.estudianteList = new ArrayList<>();
        estudianteActivity = (EleccionEstudianteEntrenamientoActivity) context;
    }

    private void add(Estudiante item) {
        estudianteList.add(item);
        notifyItemInserted(estudianteList.size() - 1);
    }

    public void addAll(List<Estudiante> vocabularioList) {
        for (Estudiante item : vocabularioList) {
            add(item);
        }
    }

    public void remove(Estudiante item) {
        int position = estudianteList.indexOf(item);
        if (position > -1) {
            estudianteList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public Estudiante getItem(int position) {
        return estudianteList.get(position);
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_estudiantes, parent, false);
        view.setOnClickListener(onClickListener);
        final ContactHolder contactHolder = new ContactHolder(view);

        contactHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPos = contactHolder.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    if (recyclerItemClickListener != null) {
                        recyclerItemClickListener.onItemClick(adapterPos, contactHolder.itemView);
                    }
                }

            }
        });
        return contactHolder;
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        final Estudiante estudiante = estudianteList.get(position);


        holder.nombre.setText(estudiante.getNombreEstudiate());
        holder.apellido.setText(estudiante.getApellidoEstudiante());
        holder.edad.setText(Integer.toString(estudiante.getEdadEstudiante()));


        File file = new File(estudiante.getFotoEstudiante());
        if (!file.exists()) {
            holder.imagen.setImageResource(R.drawable.no_foto);
        } else {
            File fileImagen = new File(estudiante.getFotoEstudiante().toString());
            Bitmap newBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(fileImagen.getPath()), 250,
                    250, true);

            holder.imagen.setImageBitmap(newBitmap);
        }

    }

    @Override
    public int getItemCount() {
        return estudianteList.size();
    }

    public void setOnItemClickListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if(onClickListener!=null){
            onClickListener.onClick(v);
        }
    }

    class ContactHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView nombre, apellido, edad;

        public ContactHolder(View itemView) {
            super(itemView);
            imagen = (ImageView) itemView.findViewById(R.id.imgEstudiante);
            nombre = (TextView) itemView.findViewById(R.id.nombreE);
            apellido = (TextView) itemView.findViewById(R.id.apellidoE);
            edad = (TextView) itemView.findViewById(R.id.edadE);

        }

    }

    public void setFilter(List<Estudiante> newlist){
        this.estudianteList=new ArrayList<>();
        this.estudianteList.addAll(newlist);
        notifyDataSetChanged();
    }
}