package handlers;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import models.User;

public class FirestoreHandler {

    public static final String TAG_FAILURE_WRITE = "Failure Write";
    public static final String TAG_FAILURE_READING = "Failure reading";
    public static final String TAG_READ_SUCCESSFUL = "Read Successfully";
    private static String TAG_SUCCESS_WRITE = "Write Successfully";

    public static void saveUser(User user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference doc = db.collection("users").document(user.getuID());
        doc.set(user.dbModel())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG_SUCCESS_WRITE, "User added to bd.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG_FAILURE_WRITE, "User failed write on db.");
                    }
                });

    }

    public interface UserCallback{
        public void onCallback(User user);
    }

    public static void getUser(String uID, final UserCallback callback){
        DocumentReference docRefUser = FirebaseFirestore.getInstance().document("users/"+uID);
        docRefUser.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            Log.d(TAG_READ_SUCCESSFUL, "User from db");
                            callback.onCallback(documentSnapshot.toObject(User.class));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG_FAILURE_READING, "User from db");
                    }
                });
    }

}
