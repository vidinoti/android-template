package com.vidinoti.tabapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.vidinoti.tabapp.databinding.ActivityMainBinding;
import com.vidinoti.vdarsdk.ScannerFragment;
import com.vidinoti.vdarsdk.WebFragment;

public class MainActivity extends AppCompatActivity {

    private final Fragment homeFragment = WebFragment.newInstance("file:///android_asset/info.html");
    private final Fragment mapFragment = new MapFragment();

    private final Fragment scanFragment = new ScannerFragment();
    private boolean mapFragmentAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.vidinoti.tabapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homeFragment)
                .commit();

        // We simply hide the map fragment instead of removing it. This way the web page does not
        // reload when switching between views

        binding.navView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                getSupportFragmentManager().beginTransaction()
                        .remove(scanFragment)
                        .add(R.id.fragment_container, homeFragment)
                        .hide(mapFragment)
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.navigation_scan) {
                getSupportFragmentManager().beginTransaction()
                        .remove(homeFragment)
                        .add(R.id.fragment_container, scanFragment)
                        .hide(mapFragment)
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.navigation_map) {
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .remove(homeFragment)
                        .remove(scanFragment);
                if (!mapFragmentAdded) {
                    transaction.add(R.id.fragment_container, mapFragment);
                    mapFragmentAdded = true;
                }
                transaction.show(mapFragment)
                        .commit();
                return true;
            }
            return false;
        });
    }

}