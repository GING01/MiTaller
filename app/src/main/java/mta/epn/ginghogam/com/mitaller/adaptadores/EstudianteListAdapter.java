package mta.epn.ginghogam.com.mitaller.adaptadores;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.entidades.Estudiante;
import mta.epn.ginghogam.com.mitaller.listener.RecyclerItemClickListener;

public class EstudianteListAdapter extends RecyclerView.Adapter<EstudianteListAdapter.ContactHolder>{

    private List<Estudiante> estudianteList;
    private Context context;

    private RecyclerItemClickListener recyclerItemClickListener;

    public EstudianteListAdapter(Context context) {
        this.context = context;
        this.estudianteList = new ArrayList<>();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_palabras, parent, false);

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
        holder.edad.setText(estudiante.getEdadEstudiante());


        File file = new File(estudiante.getFotoEstudiante());
        if (!file.exists()) {
            Toast.makeText(context,"no Exist", Toast.LENGTH_LONG).show();
            holder.imgEstudiante.setImageResource(R.drawable.no_foto);
        }else {
            holder.imgEstudiante.setImageBitmap(BitmapFactory.decodeFile(estudiante.getFotoEstudiante().toString()));
        }
    }

    @Override
    public int getItemCount() {
        return estudianteList.size();
    }

    public void setOnItemClickListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    static class ContactHolder extends RecyclerView.ViewHolder {
        ImageView imgEstudiante;
        TextView nombre, apellido, edad;

        public ContactHolder(View itemView) {
            super(itemView);
            imgEstudiante = (ImageView) itemView.findViewById(R.id.imgEstudiante);
            nombre = (TextView) itemView.findViewById(R.id.nombreEstudiante);
            apellido = (TextView) itemView.findViewById(R.id.apellidoEstudiante);
            edad = (TextView) itemView.findViewById(R.id.edad);
        }
    }
}