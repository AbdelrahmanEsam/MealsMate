package com.example.foodplanner.authenticationandaccount.presentation.login.view;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class LoginFragment extends Fragment {


    private FragmentLoginBinding binding;
    private NavController controller ;
    private FirebaseAuth mAuth;

    private FirebaseFirestore firebaseFirestore ;
    private LoginPresenter presenter;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    //Log.d("google",mAuth.getCurrentUser().getDisplayName());
                  googleLoginSuccess(account.getIdToken());

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
        firebaseFirestore = FirebaseFirestore.getInstance();
        if (savedInstanceState == null){

            presenter = new LoginPresenter();
        }else{
            presenter = savedInstanceState.getParcelable(getString(R.string.presenter));
            binding.emailInputLayout.getEditText().setText(presenter.getEmail());
            binding.passwordInputLayout.getEditText().setText(presenter.getPassword());
        }
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

        emailTextWatcher();
        passwordTextWatcher();




    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.presenter),presenter);
    }

    private void userIsAuthenticated()
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if ( currentUser != null)
        {
            loginSuccess(currentUser.getDisplayName());
        }
    }


    private void emailTextWatcher()
    {
        binding.emailInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence email, int i, int i1, int i2) {
                presenter.setEmail(email.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void passwordTextWatcher()
    {
        binding.passwordInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence password, int i, int i1, int i2) {
                presenter.setPassword(password.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
                .requestIdToken(getString(R.string.web_id))
                .requestEmail()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(requireContext(),gso);
        signInLauncher.launch(client.getSignInIntent());
    }

    private void googleLoginSuccess(String idToken)
    {
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                       createNewCollectionForUser(user);

                    }
                });
    }


    private void createNewCollectionForUser(FirebaseUser user) {
        Map<String, Object> data = new HashMap<>();
        data.put(getString(R.string.breakfast), new ArrayList<>());
        data.put(getString(R.string.launch), new ArrayList<>());
        data.put(getString(R.string.dinner), new ArrayList<>());
        data.put(getString(R.string.favourites), new ArrayList<>());

        firebaseFirestore.collection(getString(R.string.users)).document(user.getUid()).set(data)
                .addOnSuccessListener(documentReference -> {
                    loginSuccess(user.getDisplayName());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(),"error "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loginSuccess(String username )
    {
        if (!((MainActivity)requireActivity()).connectivityObserver.networkStatus()){

        Toast.makeText(getContext(), getString(R.string.welcome)+" "+username, Toast.LENGTH_SHORT).show();
        controller.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment());
        }else{
            Toast.makeText(getContext(), getString(R.string.you_need_internet_to_login_as_guest), Toast.LENGTH_SHORT).show();
        }
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