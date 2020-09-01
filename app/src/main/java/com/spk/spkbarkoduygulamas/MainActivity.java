package com.spk.spkbarkoduygulamas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/* CREATED BY Doruk Ertoğlu and
 Tuğkan Söğüt 2020 */

public class MainActivity extends AppCompatActivity {
    static Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navigationFragment);
        Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        if(fragment instanceof DepoCikisFragment){
            ((DepoCikisFragment)fragment).myOnKeyDown(keyCode);
        }
        else if(fragment instanceof DepoGirisFragment){
            ((DepoGirisFragment)fragment).myOnKeyDown(keyCode);
        }

        return super.onKeyDown(keyCode, event);
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}