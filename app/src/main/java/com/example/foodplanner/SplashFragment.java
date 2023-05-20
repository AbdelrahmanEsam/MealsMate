package com.example.foodplanner;

import android.animation.Animator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.databinding.FragmentSplashBinding;


public class SplashFragment extends Fragment {

  //  private FragmentJavaPracticeBinding binding;
    private FragmentSplashBinding binding;

    private NavController controller ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
        binding.motionLayout.transitionToStart();
      binding.lottieSplash.addAnimatorListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(@NonNull Animator animator) {

          }

          @Override
          public void onAnimationEnd(@NonNull Animator animator) {
              controller.navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment());
          }

          @Override
          public void onAnimationCancel(@NonNull Animator animator) {

          }

          @Override
          public void onAnimationRepeat(@NonNull Animator animator) {

          }
      });



    }
}