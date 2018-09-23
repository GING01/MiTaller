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
import mta.epn.ginghogam.com.mitaller.activities.TallerActivity;
import mta.epn.ginghogam.com.mitaller.entidades.Taller;
import mta.epn.ginghogam.com.mitaller.listener.RecyclerItemClickListener;

public class TallerListAdapter extends RecyclerView.Adapter<TallerListAdapter.ContactHolder> implements View.OnClickListener {

    private List<Taller> tallerList;
    private Context context;
    TallerActivity tallerActivity;

    private RecyclerItemClickListener recyclerItemClickListener;
    protected View.OnClickListener onClickListener;

    public TallerListAdapter(Context context) {
        this.context = context;
        this.tallerList = new ArrayList<>();
        tallerActivity=(TallerActivity) context;

    }
    private void add(Taller item) {
        tallerList.add(item);
        notifyItemInserted(tallerList.size() - 1);
    }
    public void addAll(List<Taller> contactList) {
        for (Taller contact : contactList) {
            add(contact);
        }
    }
    public void remove(Taller item) {
        int position = tallerList.indexOf(item);
        if (position > -1) {
            tallerList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }
    public Taller getItem(int position) {
        return tallerList.get(position);
    }
    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_talleres, null, false);
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
        final Taller taller = tallerList.get(position);

        holder.nombreTaller.setText(taller.getNombreTaller());


        if(taller != null) {
            File file = new File(taller.getImagenTaller());
            if (!file.exists()) {
                Toast.makeText(context, "no Exist", Toast.LENGTH_LONG).show();
                holder.imgTaller.setImageResource(R.drawable.no_foto);
            } else {
                holder.imgTaller.setImageBitmap(BitmapFactory.decodeFile(taller.getImagenTaller().toString()));
            }
        }
        if(!tallerActivity.visible){
            holder.checkBox.setVisibility(View.GONE);
        }
        else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return tallerList.size();
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

    public class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgTaller;
        TextView nombreTaller;
        CheckBox checkBox;

        public ContactHolder(View itemView) {
            super(itemView);
            imgTaller = (ImageView) itemView.findViewById(R.id.img_taller);
            nombreTaller = (TextView) itemView.findViewById(R.id.nombre_taller);
            checkBox=(CheckBox) itemView.findViewById(R.id.seleccionar);
            checkBox.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            tallerActivity.prepararSeleccion(v,getAdapterPosition());
        }
    }
}