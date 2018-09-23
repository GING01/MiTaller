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
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.listener.RecyclerItemClickListener;

public class SecuenciaListAdapter extends RecyclerView.Adapter<SecuenciaListAdapter.ContactHolder>{

    private List<Historia> historiaList;
    private Context context;

    private RecyclerItemClickListener recyclerItemClickListener;

    public SecuenciaListAdapter(Context context) {
        this.context = context;
        this.historiaList = new ArrayList<>();
    }
    private void add(Historia item) {
        historiaList.add(item);
        notifyItemInserted(historiaList.size() - 1);
    }
    public void addAll(List<Historia> contactList) {
        for (Historia contact : contactList) {
            add(contact);
        }
    }
    public void remove(Historia item) {
        int position = historiaList.indexOf(item);
        if (position > -1) {
            historiaList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }
    public Historia getItem(int position) {
        return historiaList.get(position);
    }
    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_secuencias, parent, false);

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
        final Historia historia = historiaList.get(position);

        holder.nombreHistoria.setText(historia.getNombreHistoria());

        File file = new File(historia.getImagenHistoria());
        if (!file.exists()) {
            Toast.makeText(context,"no Exist", Toast.LENGTH_LONG).show();
            holder.imgHistoria.setImageResource(R.drawable.no_foto);
        }else {
            holder.imgHistoria.setImageBitmap(BitmapFactory.decodeFile(historia.getImagenHistoria().toString()));
        }
    }

    @Override
    public int getItemCount() {
        return historiaList.size();
    }

    public void setOnItemClickListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    static class ContactHolder extends RecyclerView.ViewHolder {
        ImageView imgHistoria;
        TextView nombreHistoria, descripcionHistoria, numLaminas, dificultad;

        public ContactHolder(View itemView) {
            super(itemView);
            imgHistoria = (ImageView) itemView.findViewById(R.id.img_historia);
            nombreHistoria = (TextView) itemView.findViewById(R.id.nombre_historia);
        }
    }
}