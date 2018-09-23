package mta.epn.ginghogam.com.mitaller.adaptadores;

import android.content.Context;
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
import mta.epn.ginghogam.com.mitaller.activities.HistoriaActivity;
import mta.epn.ginghogam.com.mitaller.entidades.Historia;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.listener.RecyclerItemClickListener;

public class HistoriaListAdapter extends RecyclerView.Adapter<HistoriaListAdapter.ContactHolder> implements View.OnClickListener{

    private List<Historia> historiaList;
    private Context context;
    HistoriaActivity historiaActivity;

    private RecyclerItemClickListener recyclerItemClickListener;
    protected View.OnClickListener onClickListener;

    public HistoriaListAdapter(Context context) {
        this.context = context;
        this.historiaList = new ArrayList<>();
        historiaActivity=(HistoriaActivity) context;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_historias, null, false);
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
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
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
        if(!historiaActivity.visible){
            holder.checkBox.setVisibility(View.GONE);
        }
        else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return historiaList.size();
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

    public class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgHistoria;
        TextView nombreHistoria, descripcionHistoria, numLaminas, dificultad;
        CheckBox checkBox;

        public ContactHolder(View itemView) {
            super(itemView);
            imgHistoria = (ImageView) itemView.findViewById(R.id.img_historia);
            nombreHistoria = (TextView) itemView.findViewById(R.id.nombre_historia);
            checkBox=(CheckBox) itemView.findViewById(R.id.seleccionar);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            historiaActivity.prepararSeleccion(v,getAdapterPosition());
        }
    }
}