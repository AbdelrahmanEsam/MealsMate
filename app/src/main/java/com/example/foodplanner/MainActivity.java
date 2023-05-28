package com.example.foodplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.example.foodplanner.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       NavHostFragment navHostFragment =  ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment));
       NavigationUI.setupWithNavController(binding.bottomNavigationView,navHostFragment.getNavController());
    }
}