package authentication;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class FirebaseHandler {
    private static final String TAG = "FireBaseHandler";

    private FirebaseAuth mAuth;

    public void CreateUser(EditText email, EditText password, final Context context){
        String email_aux = email.getText().toString().trim();
        String password_aux = password.getText().toString().trim();

        mAuth = FirebaseAuth.getInstance();

        if (email_aux.isEmpty()){
            email.setError("Please enter an email");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email_aux).matches()){
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }

        if (password_aux.isEmpty()) {
            password.setError("Please enter a password");
            password.requestFocus();
            return;
        }

        if (password_aux.length() < 6){
            password.setError("Please insert a password with at least 6 characters");
            password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email_aux,password_aux).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "SignUp successful, please check your email for verification  ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void SignInUser (EditText email, EditText password, final Context context){

        String email_aux = email.getText().toString().trim();
        String password_aux = password.getText().toString().trim();

        mAuth = FirebaseAuth.getInstance();

        if (email_aux.isEmpty()){
            email.setError("Please enter an email");
            email.requestFocus();
            return;
        }

        if (password_aux.isEmpty()) {
            password.setError("Please enter a password");
            password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email_aux,password_aux).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (mAuth.getCurrentUser().isEmailVerified()) {
                        Toast.makeText(context, "SignIn successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Please verify your email", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
