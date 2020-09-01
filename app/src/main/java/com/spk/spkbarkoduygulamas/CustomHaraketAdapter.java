package com.spk.spkbarkoduygulamas;

import android.content.Context;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.spk.spkbarkoduygulamas.helpers.DataHaraket;
import com.spk.spkbarkoduygulamas.helpers.Haraket;

import java.util.ArrayList;



public class CustomHaraketAdapter extends ArrayAdapter<DataHaraket> {
    private ArrayList<DataHaraket> dataset;
    private Context mContext;

    private static class ViewHolder {
        TextView tvAdres;
        TextView tvZaman;
        TextView tvAdet;
        TextView tvLot;
        TextView tvMusteri;
        View viewIslem;
    }

    public CustomHaraketAdapter(ArrayList<DataHaraket> dataset, Context context) {
        super(context, R.layout.list_view_haraket);
        this.dataset = dataset;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return dataset.size();
    }
    @Override
    public DataHaraket getItem(int position) {
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
        viewHolder.viewIslem = convertView.findViewById(R.id.view_list_islemdurumu);
        viewHolder.tvAdet = convertView.findViewById(R.id.textView_list_adet);
        viewHolder.tvLot = convertView.findViewById(R.id.textView_list_lotkodu);
        viewHolder.tvMusteri = convertView.findViewById(R.id.textView_list_musteri);
        DataHaraket haraket = dataset.get(position);
        viewHolder.tvAdres.setText(haraket.getAdres());
        viewHolder.tvZaman.setText(haraket.getZaman().toString());
        viewHolder.tvAdet.setText(haraket.getAdet().toString() );
        viewHolder.tvLot.setText(haraket.getLot().toString());
        viewHolder.tvMusteri.setText("ERTOGLU INŞAAT");
        if(haraket.getHareket() == Haraket.GIRIS){
            viewHolder.viewIslem.setBackground(mContext.getResources().getDrawable(R.drawable.back_green));
        }
        else{
            viewHolder.viewIslem.setBackground(mContext.getResources().getDrawable(R.drawable.back_red));
        }

        return convertView;
    }
}
