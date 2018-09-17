package mta.epn.ginghogam.com.mitaller.adaptadores;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.R;
import mta.epn.ginghogam.com.mitaller.activities.EntrenamientoVocabularioActivity;
import mta.epn.ginghogam.com.mitaller.activities.VocabularioActivity;
import mta.epn.ginghogam.com.mitaller.entidades.Vocabulario;
import mta.epn.ginghogam.com.mitaller.listener.RecyclerItemClickListener;

public class PalabraEntrenamientoListAdapter extends RecyclerView.Adapter<PalabraEntrenamientoListAdapter.ContactHolder> implements View.OnClickListener{

    private List<Vocabulario> vocabularioList;
    private Context context;
    EntrenamientoVocabularioActivity entrenamientoVocabularioActivity;

    private RecyclerItemClickListener recyclerItemClickListener;

    protected View.OnClickListener onClickListener;

    public PalabraEntrenamientoListAdapter(Context context) {
        this.context = context;
        this.vocabularioList = new ArrayList<>();
        entrenamientoVocabularioActivity=(EntrenamientoVocabularioActivity) context;
    }
    private void add(Vocabulario item) {
        vocabularioList.add(item);
        notifyItemInserted(vocabularioList.size() - 1);
    }
    public void addAll(List<Vocabulario> vocabularioList) {
        for (Vocabulario item : vocabularioList) {
            add(item);
        }
    }
    public void remove(Vocabulario item) {
        int position = vocabularioList.indexOf(item);
        if (position > -1) {
            vocabularioList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }
    public Vocabulario getItem(int position) {
        return vocabularioList.get(position);
    }
    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_palabras_entrenamiento, parent, false);
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
        final Vocabulario vocabulario = vocabularioList.get(position);


        String tipoPalabra = vocabulario.getTipoPalabra().toString().trim();

        if(tipoPalabra.equals("Alimento"))
            holder.lyColor.setBackgroundColor(Color.DKGRAY);
            if(tipoPalabra.equals("Peligro"))
                holder.lyColor.setBackgroundColor(Color.BLUE);
                if(tipoPalabra.equals("Limpieza"))
                    holder.lyColor.setBackgroundColor(Color.RED);


        holder.palabra.setText(vocabulario.getPalabra());


        File file = new File(vocabulario.getImagenPalabra());
        if (!file.exists()) {
            holder.imgPalabra.setImageResource(R.drawable.no_foto);
        }else {
            holder.imgPalabra.setImageBitmap(BitmapFactory.decodeFile(vocabulario.getImagenPalabra().toString()));
        }

    }

    @Override
    public int getItemCount() {
        return vocabularioList.size();
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

    class ContactHolder extends RecyclerView.ViewHolder{
        ImageView imgPalabra;
        TextView palabra;
        LinearLayout lyColor;

        public ContactHolder(View itemView) {
            super(itemView);
            imgPalabra = (ImageView) itemView.findViewById(R.id.img_palabra_V);
            palabra = (TextView) itemView.findViewById(R.id.palabra_);
            lyColor = (LinearLayout) itemView.findViewById(R.id.lyColor);

        }
    }
}