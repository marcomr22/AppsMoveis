package handlers;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import models.Advert;
import models.User;

public class FirestoreHandler {

    private static final String TAG_WRITING_FAILURE = "Failure Write";
    private static final String TAG_READING_FAILURE = "Failure reading";
    private static final String TAG_READ_SUCCESSFUL = "Read Successfully";
    private static final String TAG_WRITE_SUCCESSFUL = "Write Successfully";

    private Activity activity;
    private Advert.Category category;
    private OrderBy order;
    private FirebaseFirestore bd;
    private Query query;

    public FirestoreHandler(Activity activity, Advert.Category category, OrderBy order) {
        this.activity = activity;
        this.category = category;
        this.order = order;
        this.bd = FirebaseFirestore.getInstance();
        this.query = this.bd.collectionGroup("adverts")
                .whereEqualTo("category", category.toString())
                /*.orderBy(order.toString())*/
                .limit(1);
    }

    public static void saveUser(User user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference doc = db.collection("users").document(user.getuID());
        doc.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG_WRITE_SUCCESSFUL, "User added to bd.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG_WRITING_FAILURE, "User failed write on db.");
                    }
                });

    }

    public interface UserCallback{
        void onCallback(User user);
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
                        Log.w(TAG_READING_FAILURE, "User from db");
                    }
                });
    }

    public static void saveAdvert(Advert advert){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference doc = db.document("/user/uid0/adverts/"+ advert.getId());
        doc.set(advert)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG_WRITE_SUCCESSFUL, "Advert added to bd.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG_WRITING_FAILURE, "Advert failed write on db.");
                    }
                });
    }

    public interface QueryCallback{
        void onCallback(List<Advert> list);
    }

    public void getAdverts(final QueryCallback callback){
        query.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG_READING_FAILURE, "Advert query" + e.toString());

                    }
                })
                .addOnSuccessListener(activity, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG_READ_SUCCESSFUL, "Advert query");
                        List<DocumentSnapshot> snapList = queryDocumentSnapshots.getDocuments();
                        query = query.startAfter(snapList.get(snapList.size() - 1));
                        List<Advert> list = new ArrayList<>();
                        for(DocumentSnapshot d: snapList)
                            list.add(d.toObject(Advert.class));
                        callback.onCallback(list);

                    }
                });
    }

    public enum OrderBy{
        Price,
    }
}
