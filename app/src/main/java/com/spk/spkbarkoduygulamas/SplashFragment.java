package com.spk.spkbarkoduygulamas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SplashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SplashFragment extends Fragment {

    ImageView splashImage;

    public SplashFragment() {
        // Required empty public constructor
    }


    public static SplashFragment newInstance() {
        SplashFragment fragment = new SplashFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        splashImage = view.findViewById(R.id.imageView_splashLogo);

        Animation scaleup = AnimationUtils.loadAnimation(getContext(),R.anim.animation_splash);
        splashImage.startAnimation(scaleup);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Navigation.findNavController(getActivity(),R.id.navigationFragment).navigate(R.id.action_splashFragment_to_mainMenuFragment);
            }
        };

        splashImage.postOnAnimationDelayed(runnable,1000);
        return view;
    }
}