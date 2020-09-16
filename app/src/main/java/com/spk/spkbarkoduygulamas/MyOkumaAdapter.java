package com.spk.spkbarkoduygulamas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;

public class MyOkumaAdapter extends RecyclerView.Adapter<MyOkumaAdapter.MyViewHolder> {
    private Map<String,String> mDataset;

    public MyOkumaAdapter(Map<String,String> dataSet){

        mDataset = dataSet;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_okuma_sutun,parent,false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String key = (String) mDataset.keySet().toArray()[position];
        String value = mDataset.get(key);
        holder.column.setText(key);
        holder.value.setText(value);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView column;
        public TextView value;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            column = itemView.findViewById(R.id.tvOkuColumn);
            value = itemView.findViewById(R.id.tvOkuValue);
        }
    }

}
