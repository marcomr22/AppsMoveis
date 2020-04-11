package authentication;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.app.Login;
import com.example.app.Menu;
import com.example.app.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
                        Intent i = new Intent(context, Menu.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
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

    public void SignOut (Context context){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Toast.makeText(context, "Log Out successful", Toast.LENGTH_SHORT).show();
    }

    public void ChangePassword (final Context context, EditText OldPassword, EditText NewPassword, EditText NewPassword2){
        String OldPasswordAux = OldPassword.getText().toString().trim();
        String NewPasswordAux = NewPassword.getText().toString().trim();
        String NewPassword2Aux = NewPassword2.getText().toString().trim();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (OldPasswordAux.isEmpty()){
            OldPassword.setError("Please insert your old Password");
            OldPassword.requestFocus();
        }

        if (NewPasswordAux.isEmpty()){
            NewPassword.setError("Please insert your new Password");
            NewPassword.requestFocus();
        }

        if (NewPassword.length() <6){
            NewPassword.setError("Please insert a password with at least 6 characters");
            NewPassword.requestFocus();
        }

        if (NewPassword2Aux.isEmpty()){
            NewPassword2.setError("Please confirm your new Password");
            NewPassword2.requestFocus();
        }

        if (!NewPassword2Aux.equals(NewPasswordAux)){
            NewPassword2.setError("The two passwords do not match");
            NewPassword2.requestFocus();
        }

        if (user != null){
            ReAuthenticate(user, OldPasswordAux);
            user.updatePassword(NewPasswordAux).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(context, "Password change successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, Profile.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }

                    else{
                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void ReAuthenticate (FirebaseUser user, String password){

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "User re-authenticated.");
                }

                else{
                    Log.d(TAG, task.getException().getMessage());
                }
            }
        });
    }

}
