package com.spk.spkbarkoduygulamas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.spk.spkbarkoduygulamas.omdb.AppDatabase;
import com.spk.spkbarkoduygulamas.omdb.Hesap;

/* CREATED BY Doruk Ertoğlu and
 Tuğkan Söğüt 2020 */

public class MainActivity extends AppCompatActivity {
    static public Hesap giriliHesap;
    static public AppDatabase dbMain;
    TextView tvHesap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvHesap = findViewById(R.id.textViewHesap);
        cikisYap();
        dbMain = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-v01").allowMainThreadQueries().build();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navigationFragment);
        Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        if(fragment instanceof DepoGirisCikisFragment){
            ((DepoGirisCikisFragment)fragment).myOnKeyDown(keyCode);
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void girisYap(Hesap girilenHesap){
        giriliHesap = girilenHesap;
        Toast.makeText(this, "Giriş Yapıldı", Toast.LENGTH_SHORT).show();
        tvHesap.setText(giriliHesap.isim + " " + giriliHesap.soyisim);
    }

    public void cikisYap(){
        giriliHesap = null;
        tvHesap.setText("Giriş Yapınız");
    }
}