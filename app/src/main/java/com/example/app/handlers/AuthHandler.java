package com.example.app.handlers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.app.FullListShort;
import com.example.app.Login;
import com.example.app.Menu;
import com.example.app.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;

import com.example.app.models.User;

public class AuthHandler {
    private static final String TAG = "AuthHandler";
    private FirebaseAuth mAuth;

    public void CreateUser(final EditText username, final EditText email, EditText phoneNumber, final EditText password, final Context context){
        final String email_aux = email.getText().toString().trim();
        String password_aux = password.getText().toString().trim();
        final String phoneNumber_aux = phoneNumber.getText().toString().trim();
        final String username_aux = username.getText().toString().trim();

        mAuth = FirebaseAuth.getInstance();

        if (username_aux.isEmpty()){
            username.setError("Please enter an username");
            username.requestFocus();
        }

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

        if (phoneNumber_aux.isEmpty()){
            phoneNumber.setError("Please insert your phone number");
            phoneNumber.requestFocus();
        }

        if(!Patterns.PHONE.matcher(phoneNumber_aux).matches()){
            phoneNumber.setError("Please insert a valid phone number");
            phoneNumber.requestFocus();
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
                                Log.d(TAG,mAuth.getCurrentUser().getUid());
                                username.setText("");
                                email.setText("");
                                password.setText("");
                                mAuth.signOut();
                                Intent i = new Intent(context, Login.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                            } else {
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    final User user = new User(mAuth.getCurrentUser().getUid(), username_aux, email_aux,"", 0.0, phoneNumber_aux);
                    FirestoreHandler.saveUser(user);
                }
                else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void SignInUser (final EditText email, final EditText password, final Context context){

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
                        Intent i = new Intent(context, FullListShort.class);
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

    public static FirebaseUser getUser (){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user;
    }

    public void deleteUser(EditText password, final Context context) {
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        final String uid_aux = user.getUid();

        Log.d(TAG, "Delete User");
        String password_aux = password.getText().toString().trim();

        if (password_aux.isEmpty()) {
            password.setError("Please enter your password");
            password.requestFocus();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password_aux);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "User re-authenticated.");
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirestoreHandler.deleteUser(uid_aux);
                                Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(context, Login.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                            }
                            else{
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteUserGoogle(final GoogleSignInAccount acct, final Context context) {
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        final String uid_aux = user.getUid();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "User re-authenticated.");
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirestoreHandler.deleteUser(uid_aux);
                                GoogleSignIn.getClient(
                                        context,
                                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                                ).signOut();
                                Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(context, Login.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                            }
                            else{
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void ChangeEmail (final EditText email, final EditText email2, final EditText password, final Context context){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = getUser();
        String password_aux = password.getText().toString().trim();
        final String email_aux = email.getText().toString().trim();
        String email2_aux = email2.getText().toString().trim();

        if (email_aux.isEmpty()){
            email.setError("Please enter an email");
            email.requestFocus();
            return;
        }

        if (email2_aux.isEmpty()){
            email2.setError("Please enter an email");
            email2.requestFocus();
            return;
        }

        if (!email2_aux.equals(email_aux)){
            email2.setError("The emails do not match");
            email2.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email_aux).matches()){
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }

        if (password_aux.isEmpty()) {
            password.setError("Please enter your password");
            password.requestFocus();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password_aux);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "User re-authenticated.");
                    mAuth.getCurrentUser().updateEmail(email_aux).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            email.setText("");
                                            email2.setText("");
                                            password.setText("");
                                            SignOut(context);
                                            Intent i = new Intent(context, Login.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(i);
                                            Toast.makeText(context, "Please check your email for verification and login again", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
                else{
                    Log.d(TAG, task.getException().getMessage());
                }
            }
        });
    }


    public void RecoverPassword (String email, final Context context){
        mAuth = FirebaseAuth.getInstance();

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    SignOut(context);
                    Toast.makeText(context, "Please check your email for the instructions", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, Login.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }

                else{
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void SignOut (Context context){
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser().getIdToken(false).getResult().getSignInProvider().equals("google.com")) {
            mAuth.signOut();
            GoogleSignIn.getClient(
                    context,
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            ).signOut();
            Log.d(TAG, "Google Logout successful");
        }
        mAuth.signOut();
        Toast.makeText(context, "Log Out successful", Toast.LENGTH_SHORT).show();
    }

    public void ChangePassword (final Context context, final EditText OldPassword, final EditText NewPassword, final EditText NewPassword2){
        String OldPasswordAux = OldPassword.getText().toString().trim();
        final String NewPasswordAux = NewPassword.getText().toString().trim();
        String NewPassword2Aux = NewPassword2.getText().toString().trim();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (OldPasswordAux.isEmpty()){
            OldPassword.setError("Please insert your old Password");
            OldPassword.requestFocus();
            return;
        }

        if (NewPasswordAux.isEmpty()){
            NewPassword.setError("Please insert your new Password");
            NewPassword.requestFocus();
            return;
        }

        if (NewPassword.length() <6){
            NewPassword.setError("Please insert a password with at least 6 characters");
            NewPassword.requestFocus();
            return;
        }

        if (NewPassword2Aux.isEmpty()){
            NewPassword2.setError("Please confirm your new Password");
            NewPassword2.requestFocus();
            return;
        }

        if (!NewPassword2Aux.equals(NewPasswordAux)){
            NewPassword2.setError("The two passwords do not match");
            NewPassword2.requestFocus();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), OldPasswordAux);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User re-authenticated.");
                    user.updatePassword(NewPasswordAux).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                OldPassword.setText("");
                                NewPassword.setText("");
                                NewPassword2.setText("");
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
                   OldPassword.setText("");
                   NewPassword.setText("");
                   NewPassword2.setText("");

                } else {
                    Log.d(TAG, task.getException().getMessage());
                }
            }
            });
    }


    public void firebaseAuthWithGoogle(final GoogleSignInAccount acct, final Context context) {
        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        Log.d(TAG, acct.getEmail());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Log.d(TAG, FirebaseAuth.getInstance().getCurrentUser().getUid());
                            Intent i = new Intent(context, FullListShort.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            final String username = acct.getEmail().split("@")[0];
                            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                            final User user = new User(uid, username, email, "", 0.0, "");
                            FirestoreHandler.saveUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                         }
                    }
                });
    }
}
