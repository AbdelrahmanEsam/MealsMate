package com.example.foodplanner.authenticationandaccount.presentation.login.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.foodplanner.MainActivity;
import com.example.foodplanner.R;
import com.example.foodplanner.authenticationandaccount.presentation.login.presenter.LoginPresenter;
import com.example.foodplanner.authenticationandaccount.presentation.login.presenter.LoginPresenterInterface;
import com.example.foodplanner.databinding.FragmentLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {


    private FragmentLoginBinding binding;
    private NavController controller ;
    private FirebaseAuth mAuth;
    private LoginPresenter presenter;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                  loginSuccess(account.getDisplayName());
                } catch (ApiException e) {

                    enableInteraction();
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

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
        presenter = new LoginPresenter();
        userIsAuthenticated();

        binding.signUp.setOnClickListener(view1 -> {
            controller.navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment());
        });


        binding.signInButton.setOnClickListener(view1 -> {
              signIn();
        });

        binding.googleButton.setOnClickListener(view1 -> {
            signInWithGoogle();
        });

        binding.asGuest.setOnClickListener(view1 -> {
            loginSuccess(getString(R.string.guest));
        });




    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void userIsAuthenticated()
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if ( currentUser != null)
        {
            loginSuccess(currentUser.getDisplayName());
        }
    }

    private void signIn()
    {

        disableInteraction();
        String email = binding.emailInputLayout.getEditText().getText().toString();
        String password = binding.passwordInputLayout.getEditText().getText().toString();

        if (!presenter.isValidEmail(email))
        {
            enableInteraction();
            Toast.makeText(requireContext(), getString(R.string.please_insert_valid_email), Toast.LENGTH_SHORT).show();
            return;
        }


        if (!presenter.isValidPassword(password))
        {
            enableInteraction();
            Toast.makeText(requireContext(), getString(R.string.your_password_should_have_number_capital_letter_small_letter_and_special_character), Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email
                        , password)
                .addOnCompleteListener(requireActivity(), task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {

                            loginSuccess(user.getDisplayName());
                        }
                    } else {


                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        //  updateUI(null);
                    }
                    enableInteraction();
                });
    }

    private void signInWithGoogle()
    {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(requireContext(),gso);
        signInLauncher.launch(client.getSignInIntent());
    }

    private void loginSuccess(String username )
    {
        Toast.makeText(getContext(), getString(R.string.welcome)+" "+username, Toast.LENGTH_SHORT).show();
        controller.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment());
    }

    private void enableInteraction()
    {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }


    private void disableInteraction()
    {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }
}