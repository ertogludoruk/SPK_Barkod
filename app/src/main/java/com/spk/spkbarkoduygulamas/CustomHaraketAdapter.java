package com.spk.spkbarkoduygulamas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spk.spkbarkoduygulamas.helpers.DepoHaraketi;
import com.spk.spkbarkoduygulamas.helpers.Haraket;

import java.util.ArrayList;



public class CustomHaraketAdapter extends ArrayAdapter<DepoHaraketi> {
    private ArrayList<DepoHaraketi> dataset;
    private Context mContext;

    private static class ViewHolder {
        TextView tvAdres;
        TextView tvZaman;
        TextView tvAdet;
        TextView tvLot;
        TextView tvMusteri;
        ImageView viewIslem;
    }

    public CustomHaraketAdapter(ArrayList<DepoHaraketi> dataset, Context context) {
        super(context, R.layout.list_view_haraket);
        this.dataset = dataset;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return dataset.size();
    }
    @Override
    public DepoHaraketi getItem(int position) {
        return dataset.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.list_view_haraket,parent,false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tvAdres = convertView.findViewById(R.id.textView_list_adres);
        viewHolder.tvZaman = convertView.findViewById(R.id.textView_list_tarih);
        viewHolder.viewIslem = convertView.findViewById(R.id.imageView_list_haraket);
        viewHolder.tvAdet = convertView.findViewById(R.id.textView_list_adet);
        viewHolder.tvLot = convertView.findViewById(R.id.textView_list_lotkodu);
        viewHolder.tvMusteri = convertView.findViewById(R.id.textView_list_musteri);

        DepoHaraketi haraket = dataset.get(position);

        viewHolder.tvAdres.setText(haraket.getAdres());
        viewHolder.tvZaman.setText(haraket.getTarih().toString());
        viewHolder.tvAdet.setText(haraket.getMiktar().toString() );
        viewHolder.tvLot.setText(haraket.getLot().toString());
        viewHolder.tvMusteri.setText(haraket.getMusteri());

        if(haraket.getHaraket() == Haraket.GIRIS){
            viewHolder.viewIslem.setImageResource(R.drawable.ic_depo_giris);
        }
        else{
            viewHolder.viewIslem.setImageResource(R.drawable.ic_depo_cikis);
        }

        return convertView;
    }
}
