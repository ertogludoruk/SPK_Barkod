package com.spk.spkbarkoduygulamas;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.spk.spkbarkoduygulamas.helpers.DBManager;
import com.spk.spkbarkoduygulamas.omdb.DepoYeri;
import com.spk.spkbarkoduygulamas.omdb.Stok;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.CLIPBOARD_SERVICE;

public class UrunAraFragment extends Fragment {
    AutoCompleteTextView edit_text;
    Context context;
    Button buttonAra;
    Spinner spinner;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public UrunAraFragment(){
    }

    public static UrunAraFragment newInstance(){
        return new UrunAraFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); }

    public List<String> refreshList(String text){
        List<String> list = new ArrayList<>();
        Stok[] stoks = MainActivity.dbMain.userDao().getStokFromChars(text+"%");
        for(Stok stok:stoks){
            list.add(stok.stokKodu);
        }
        return list;
    }
    public List<String> refreshListAdres(){
        List<String> list = new ArrayList<>();
        DepoYeri[] depoYeris = MainActivity.dbMain.userDao().getButunDepoYerleri();
        for(DepoYeri yer:depoYeris){
            list.add(yer.adresAdi);
        }
        return list;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_urun_ara, container, false);
        edit_text = view.findViewById(R.id.editTextBarkod);
        buttonAra = view.findViewById(R.id.buttonAra);

        spinner = view.findViewById(R.id.spinner);
        String[] items = new String[]{"STOK KODU","ADRES"};
        ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adp);


        edit_text.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > before){
                    if(s.charAt(start) == '*' ){

                        String text = edit_text.getText().toString();
                        text = text.substring(0, text.length() - 1);

                        List<String> list = null;
                        if(spinner.getSelectedItem().toString().equals("ADRES")){
                            list = refreshListAdres();
                        }
                        else if(spinner.getSelectedItem().toString().equals("STOK KODU")){
                            list = refreshList(text);
                        }
                        ArrayAdapter<String> adapter =new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list);
                        MainActivity.hideKeyboard(getActivity());
                        edit_text.setText(text);
                        edit_text.setAdapter(adapter);
                        edit_text.setSelection(edit_text.getText().length());

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sp = "none";
                if(spinner.getSelectedItem().toString().equals("STOK KODU")){sp = "spinner_stokkodu";}
                else if (spinner.getSelectedItem().toString().equals("ADRES")){sp = "spinner_adres";}

                Bundle bd = new Bundle();
                bd.putString("key1", edit_text.getText().toString());
                bd.putString("key2", sp);

                Navigation.findNavController(getActivity(),R.id.navigationFragment).navigate(R.id.action_urunAraFragment_to_urunGosterFragment, bd);
            }
        });

        final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                String barcode = Objects.requireNonNull(clipboard.getPrimaryClip()).getItemAt(0).coerceToText(context).toString();
                if(barcode.equals(" ")){return;}
                if(barcode.contains("DEPO_")){
                    String depoAdi = barcode.substring(5);
                    spinner.setSelection(1);
                    edit_text.setText(depoAdi);
                }
                else{

                }

            }
        });

        return view;
    }

}
