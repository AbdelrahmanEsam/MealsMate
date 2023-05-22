package com.example.foodplanner.authenticationandaccount.presentation.login.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodplanner.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {


    private FragmentLoginBinding binding;
    private NavController controller ;
    private FirebaseAuth mAuth;
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
        mAuth = FirebaseAuth.getInstance();

        binding.signUp.setOnClickListener(view1 -> {
            controller.navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment());
        });


        binding.signInButton.setOnClickListener(view1 -> {
           // controller.navigate(LoginFragmentDirections.actionLoginFragmentToCategoriesFragment());
            controller.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment());
        });


        mAuth.signInWithEmailAndPassword("email", "password")
                .addOnCompleteListener(requireActivity(), task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();
                      //  updateUI(user);
                    } else {

                      //  updateUI(null);
                    }
                });

    }
}