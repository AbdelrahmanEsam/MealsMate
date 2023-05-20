package com.example.foodplanner.authentication.presentation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.R;
import com.example.foodplanner.databinding.FragmentLoginBinding;
import com.example.foodplanner.databinding.FragmentSplashBinding;


public class LoginFragment extends Fragment {


    private FragmentLoginBinding binding;
    private NavController controller ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        binding.signUp.setOnClickListener(view1 -> {
            controller.navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment());
        });


        binding.signInButton.setOnClickListener(view1 -> {
            controller.navigate(LoginFragmentDirections.actionLoginFragmentToCategoriesFragment());
        });

    }
}