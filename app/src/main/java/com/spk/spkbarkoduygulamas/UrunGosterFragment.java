package com.spk.spkbarkoduygulamas;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spk.spkbarkoduygulamas.helpers.TugkanHelper;
import com.spk.spkbarkoduygulamas.omdb.DepoHaraketi;
import com.spk.spkbarkoduygulamas.omdb.DepoYeri;
import com.spk.spkbarkoduygulamas.omdb.Stok;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UrunGosterFragment extends Fragment {

    TextView tvAdi,tvStokkodu;
    RecyclerView recyclerView1,recyclerView2;
    Context context;
    String stok_kodu;
    RadioGroup radioGroup;
    RadioButton radioButtonT;

    List<DepoHaraketi> veriler;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public UrunGosterFragment(){
    }

    public static UrunGosterFragment newInstance(){
        return new UrunGosterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_urun_goster, container, false);


        Bundle bd = this.getArguments();
        assert bd != null;
        stok_kodu = bd.getString("keyData");



        Stok stok = MainActivity.dbMain.userDao().findStokByStokKodu(stok_kodu);
        tvStokkodu.setText(stok_kodu);
        tvAdi.setText(stok.stokIsim);

        veriler = Arrays.asList(MainActivity.dbMain.userDao().getDepoHaraketsFromStoKod(stok_kodu));


        tvAdi = view.findViewById(R.id.textViewStokAdi);
        tvStokkodu = view.findViewById(R.id.textViewStokKodu);

        recyclerView1 = view.findViewById(R.id.recylerViewGoster);
        recyclerView2 = view.findViewById(R.id.recylerViewGosterHarakets);
        radioGroup = view.findViewById(R.id.radioGroupGoster);

        radioButtonT = view.findViewById(R.id.radioButtonT);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButtonT){
                    doMyThing(0);
                }
                else if(checkedId==R.id.radioButton1){
                    doMyThing(1);
                }
                else if(checkedId==R.id.radioButton2){
                    doMyThing(2);
                }
                else if(checkedId==R.id.radioButton3){
                    doMyThing(3);
                }

            }
        });

        doMyThing(0);

        return view;
    }

    private void doMyThing(int haraketEksi1){

        if(haraketEksi1==0){
            List<String[]> gosterilenDepoLar = calculateEachDepo(veriler);
            MyOkumaAdapter adapter1 = new MyOkumaAdapter(gosterilenDepoLar);
            recyclerView1.setLayoutManager(new LinearLayoutManager(context));
            recyclerView1.setAdapter(adapter1);
            recyclerView1.setVisibility(View.VISIBLE);
            recyclerView2.setVisibility(View.GONE);
        }
        else if(haraketEksi1==1){
            MyDepoHaraketiAdapter adapter = new MyDepoHaraketiAdapter(veriler);
            recyclerView2.setLayoutManager(new LinearLayoutManager(context));
            recyclerView2.setAdapter(adapter);
            recyclerView1.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.VISIBLE);
        }
        else if(haraketEksi1 == 2){
            List<DepoHaraketi> filteredHarakets = new ArrayList<>();
            for(DepoHaraketi haraket:veriler){
                if(haraket.haraket==0){
                    filteredHarakets.add(haraket);
                }
            }

            MyDepoHaraketiAdapter adapter = new MyDepoHaraketiAdapter(filteredHarakets);
            recyclerView2.setLayoutManager(new LinearLayoutManager(context));
            recyclerView2.setAdapter(adapter);
            recyclerView1.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.VISIBLE);
        }
        else if(haraketEksi1 == 3){
            List<DepoHaraketi> filteredHarakets = new ArrayList<>();
            for(DepoHaraketi haraket:veriler){
                if(haraket.haraket==1){
                    filteredHarakets.add(haraket);
                }
            }

            MyDepoHaraketiAdapter adapter = new MyDepoHaraketiAdapter(filteredHarakets);
            recyclerView2.setLayoutManager(new LinearLayoutManager(context));
            recyclerView2.setAdapter(adapter);
            recyclerView1.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.VISIBLE);
        }
    }

    private List<String[]> calculateEachDepo(List<DepoHaraketi> veriler){
        DepoYeri[] yerler = MainActivity.dbMain.userDao().getButunDepoYerleri();
        Map<String,Integer> map = TugkanHelper.GetEachDepo(yerler,veriler);
        List<String[]> datas = new ArrayList<>();

        for(DepoYeri yer:yerler){
            Integer aktifSayi = map.get(yer.adresAdi);
            if(aktifSayi>0){
                datas.add(new String[]{yer.adresAdi,aktifSayi.toString()});
            }
            else if(aktifSayi<0){
                datas.add(new String[]{yer.adresAdi,"!HATALI SAYIM!: "+aktifSayi.toString()});
            }
        }
        return datas;
    }
}