package com.example.foodplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.foodplanner.databinding.ActivityMainBinding;
import com.example.utils.NetworkConnectivityObserver;
import com.example.utils.NetworkStatus;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;


    public   NetworkConnectivityObserver connectivityObserver ;


    private Observable<Object> fragmentObservable;

   private NavHostFragment navHostFragment;

   public  boolean internetState;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        connectivityObserver  = new NetworkConnectivityObserver(getApplicationContext());
        setupNavigation();
        currentFragmentObservable();
        connectivityObserve();
    }



    private void showBottomNavigation(Boolean show)
    {
        if (show) binding.bottomNavigationView.setVisibility(View.GONE);
    }

    private void connectivityObserve()
    {

        Observable.combineLatest(connectivityObserver.Observe(), fragmentObservable, (networkStatus, fragmentStatus) -> networkStatus).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Object status) {

                NetworkStatus networkStatus = (NetworkStatus)status;
                switch (networkStatus){
                    case Lost:
                    case Unavailable: {
                        binding.bottomNavigationView.setVisibility(View.GONE);
                        internetState = false;
                        break;
                    }

                    case Available:{
                        if (mAuth.getCurrentUser() != null)
                        {
                            internetState = true;
                            binding.bottomNavigationView.setVisibility(View.VISIBLE);
                            showBottomNavigation(isInSplash());
                            break;
                        }
                    }


                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void currentFragmentObservable()
    {
       fragmentObservable =  Observable.create(emitter -> {
         navHostFragment.getNavController().addOnDestinationChangedListener((navController, navDestination, bundle) -> {
             emitter.onNext(navHostFragment.getNavController().getCurrentDestination().getId() == R.id.splashFragment);
          });
        }).observeOn(AndroidSchedulers.mainThread());
    }

    private void setupNavigation()
    {
         navHostFragment =  ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment));
        NavigationUI.setupWithNavController(binding.bottomNavigationView,navHostFragment.getNavController());
    }


    private boolean isInSplash(){
        return  navHostFragment.getNavController().getCurrentDestination().getId() == R.id.splashFragment ;
    }


}