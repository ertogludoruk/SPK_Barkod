package com.spk.spkbarkoduygulamas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spk.spkbarkoduygulamas.omdb.DepoHaraketi;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyDepoHaraketiAdapter extends RecyclerView.Adapter<MyDepoHaraketiAdapter.MyViewHolder> {
    private List<DepoHaraketi> mDataset;

    public MyDepoHaraketiAdapter(List<DepoHaraketi> dataSet){
        mDataset = dataSet;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_haraket,parent,false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DepoHaraketi depoHaraketi = mDataset.get(position);
        holder.tvAdres.setText(depoHaraketi.adres);
        holder.tvTarih.setText(depoHaraketi.tarih.toString());
        holder.tvTarih.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(depoHaraketi.tarih));
        holder.tvLotKodu.setText(depoHaraketi.lotKodu.toString());
        holder.tvAdet.setText(depoHaraketi.adet.toString());

        if(depoHaraketi.haraket == 0){
            holder.tvMusteri.setText("");
            holder.ivHaraket.setImageResource(R.drawable.ic_depo_giris);
        }
        else{
            holder.tvMusteri.setText(depoHaraketi.musteri);
            holder.ivHaraket.setImageResource(R.drawable.ic_depo_cikis);
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvAdres;
        public TextView tvTarih;
        public TextView tvLotKodu;
        public TextView tvAdet;
        public TextView tvMusteri;
        public ImageView ivHaraket;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAdres = itemView.findViewById(R.id.textView_list_adres);
            tvTarih = itemView.findViewById(R.id.textView_list_tarih);
            tvLotKodu = itemView.findViewById(R.id.textView_list_lotkodu);
            tvAdet = itemView.findViewById(R.id.textView_list_adet);
            tvMusteri = itemView.findViewById(R.id.textView_list_musteri);
            ivHaraket = itemView.findViewById(R.id.imageView_list_haraket);
        }
    }

}
