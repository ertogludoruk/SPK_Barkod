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

import com.spk.spkbarkoduygulamas.omdb.DepoHaraketi;
import com.spk.spkbarkoduygulamas.omdb.DepoYeri;
import com.spk.spkbarkoduygulamas.omdb.Stok;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrunGosterFragment extends Fragment {

    TextView tvAdi,tvStokkodu,tvAdi1,tvStokkodu1;
    RecyclerView recyclerView1;
    RecyclerView recyclerView2;
    Context context;
    String data;
    String spinnerValue;
    RadioGroup radioGroup;
    RadioButton radioButtonT;

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
        data = bd.getString("keyData");
        spinnerValue = bd.getString("keySpinnerChoice");

        tvAdi = view.findViewById(R.id.textViewStokAdi);
        tvStokkodu = view.findViewById(R.id.textViewStokKodu);

        tvAdi1 = view.findViewById(R.id.textViewStokAdi1);
        tvStokkodu1 = view.findViewById(R.id.textViewStokKodu1);

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
        List<DepoHaraketi> veriler = null;
        if (spinnerValue.equals("spinner_stokkodu")) {
            String stok_kodu = data;
            Stok stok = MainActivity.dbMain.userDao().findStokByStokKodu(stok_kodu);
            tvStokkodu.setText(stok_kodu);
            tvAdi.setText(stok.stokIsim);
            if(haraketEksi1==0){
                veriler = Arrays.asList(MainActivity.dbMain.userDao().getDepoHaraketsFromStoKod(stok_kodu));
                List<String[]> test1 = calculateEachDepo(veriler);
                MyOkumaAdapter adapter1 = new MyOkumaAdapter(test1);
                recyclerView1.setLayoutManager(new LinearLayoutManager(context));
                recyclerView1.setAdapter(adapter1);
                recyclerView1.setVisibility(View.VISIBLE);
                recyclerView2.setVisibility(View.GONE);
            }
            else if(haraketEksi1==1){
                veriler = Arrays.asList(MainActivity.dbMain.userDao().getDepoHaraketsFromStoKod(stok_kodu));
                MyDepoHaraketiAdapter adapter = new MyDepoHaraketiAdapter(veriler);
                recyclerView2.setLayoutManager(new LinearLayoutManager(context));
                recyclerView2.setAdapter(adapter);
                recyclerView1.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);
            }
            else if(haraketEksi1 == 2){
                veriler = Arrays.asList(MainActivity.dbMain.userDao().getDepoHaraketsFromStoKod0(stok_kodu));
                MyDepoHaraketiAdapter adapter = new MyDepoHaraketiAdapter(veriler);
                recyclerView2.setLayoutManager(new LinearLayoutManager(context));
                recyclerView2.setAdapter(adapter);
                recyclerView1.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);
            }
            else if(haraketEksi1 == 3){
                veriler = Arrays.asList(MainActivity.dbMain.userDao().getDepoHaraketsFromStoKod1(stok_kodu));
                MyDepoHaraketiAdapter adapter = new MyDepoHaraketiAdapter(veriler);
                recyclerView2.setLayoutManager(new LinearLayoutManager(context));
                recyclerView2.setAdapter(adapter);
                recyclerView1.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);
            }

        }
        else if(spinnerValue.equals("spinner_adres")){
            if(radioGroup.getCheckedRadioButtonId() == R.id.radioButtonT ){
                radioGroup.check(R.id.radioButton1);
                return;
            }
            String depoAdi = data;
            if(haraketEksi1==1){
                veriler = Arrays.asList(MainActivity.dbMain.userDao().getDepoHaraketsFromAdres(depoAdi));
                tvStokkodu.setText(calculateEachDepo(veriler).get(0)[1]);
            }
            else if(haraketEksi1==2){
                veriler = Arrays.asList(MainActivity.dbMain.userDao().getDepoHaraketsFromAdres0(depoAdi));
            }
            else if(haraketEksi1 == 3){
                veriler = Arrays.asList(MainActivity.dbMain.userDao().getDepoHaraketsFromAdres1(depoAdi));
            }

            radioButtonT.setVisibility(View.GONE);

            tvAdi.setText(depoAdi);


            tvAdi1.setText("Depo Adı:");
            tvStokkodu1.setText("Kalan Adet:");
            recyclerView1.setVisibility(View.GONE);

            MyDepoHaraketiAdapter adapter = new MyDepoHaraketiAdapter(veriler);
            recyclerView2.setLayoutManager(new LinearLayoutManager(context));
            recyclerView2.setAdapter(adapter);
        }
    }

    private List<String[]> calculateEachDepo(List<DepoHaraketi> veriler){
        Map<String,Integer> map = new HashMap<>();
        DepoYeri[] yerler = MainActivity.dbMain.userDao().getButunDepoYerleri();
        for(DepoYeri yer:yerler){
            map.put(yer.adresAdi,0);
        }

        for(DepoHaraketi haraket:veriler){
            String depoyeri = haraket.adres;
            Integer aktifSayi = map.get(depoyeri);
            if(haraket.haraket == 0){
                map.put(depoyeri,aktifSayi+haraket.adet);
            }
            else{
                map.put(depoyeri,aktifSayi-haraket.adet);
            }
        }
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
