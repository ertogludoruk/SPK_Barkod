package com.spk.spkbarkoduygulamas;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spk.spkbarkoduygulamas.helpers.TugkanHelper;
import com.spk.spkbarkoduygulamas.omdb.DepoHaraketi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DepoGosterFragment extends Fragment {

    TextView tvDepoAdi, tvDepoAdet;
    RecyclerView recyclerView1;
    RecyclerView recyclerView2;
    Context context;
    String depoAdi;
    RadioGroup radioGroup;

    List<DepoHaraketi> veriler;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public DepoGosterFragment(){
    }

    public static DepoGosterFragment newInstance(){
        return new DepoGosterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_depo_goster, container, false);

        Bundle bd = this.getArguments();
        depoAdi = bd.getString("keyData");
        veriler = Arrays.asList(MainActivity.dbMain.userDao().getDepoHaraketsFromDepoAdi(depoAdi));

        tvDepoAdi = view.findViewById(R.id.textViewDepoAdi);
        tvDepoAdet = view.findViewById(R.id.textViewDepoAdet);

        Integer toplam = TugkanHelper.GetDepoAdet(veriler);
        tvDepoAdi.setText(depoAdi);
        tvDepoAdet.setText(toplam.toString());


        recyclerView2 = view.findViewById(R.id.recylerViewGosterHarakets);
        radioGroup = view.findViewById(R.id.radioGroupGoster);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButton1){
                    doMyThing(0);
                }
                else if(checkedId==R.id.radioButton2){
                    doMyThing(1);
                }
                else if(checkedId==R.id.radioButton3){
                    doMyThing(2);
                }

            }
        });

        doMyThing(0);

        return view;
    }

    private void doMyThing(int haraketEksi1){
        if(haraketEksi1 == 0){
            MyDepoHaraketiAdapter adapter = new MyDepoHaraketiAdapter(veriler);
            recyclerView2.setLayoutManager(new LinearLayoutManager(context));
            recyclerView2.setAdapter(adapter);
        }
        else if(haraketEksi1 == 1){
            List<DepoHaraketi> filteredHarakets = new ArrayList<>();
            for(DepoHaraketi haraket:veriler){
                if(haraket.haraket==0){
                    filteredHarakets.add(haraket);
                }
            }
            MyDepoHaraketiAdapter adapter = new MyDepoHaraketiAdapter(filteredHarakets);
            recyclerView2.setLayoutManager(new LinearLayoutManager(context));
            recyclerView2.setAdapter(adapter);
        }
        else if(haraketEksi1 == 2){
            List<DepoHaraketi> filteredHarakets = new ArrayList<>();
            for(DepoHaraketi haraket:veriler){
                if(haraket.haraket==1){
                    filteredHarakets.add(haraket);
                }
            }
            MyDepoHaraketiAdapter adapter = new MyDepoHaraketiAdapter(filteredHarakets);
            recyclerView2.setLayoutManager(new LinearLayoutManager(context));
            recyclerView2.setAdapter(adapter);
        }
    }
}
