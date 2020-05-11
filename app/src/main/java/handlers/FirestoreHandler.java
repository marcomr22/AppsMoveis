package handlers;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import models.Advert;
import models.User;

public class FirestoreHandler {

    private static final String TAG_WRITE_FAILURE = "Write Failure";
    private static final String TAG_READ_FAILURE = "Read Failure";
    private static final String TAG_READ_SUCCESSFUL = "Read Successfully";
    private static final String TAG_WRITE_SUCCESSFUL = "Write Successfully";

    private Activity activity;
    private FirebaseFirestore bd;
    private Advert.Category category;
    private String user;
    private DocumentSnapshot docSnapshot;


    public FirestoreHandler(Activity activity, Advert.Category category, String user) {
        this.activity = activity;
        this.category = category;
        this.user = user;
        this.bd = FirebaseFirestore.getInstance();
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
                        Log.w(TAG_WRITE_FAILURE, "User failed write on db.");
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
                        Log.w(TAG_READ_FAILURE, "User from db");
                    }
                });
    }

    public static void deleteUser(final String uID){
        FirebaseFirestore.getInstance().collection("users").document(uID)
                .delete().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG_WRITE_FAILURE, "Error deleting user" + uID);
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG_WRITE_SUCCESSFUL, uID + " user deleted");
            }
        });
    }

    public static void saveAdvert(Advert advert){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.document("/users/"+ advert.getOwnerID() + "/adverts/"+ advert.getId());
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
                        Log.w(TAG_WRITE_FAILURE, "Advert failed write on db.");
                    }
                });
    }

    public static void deleteAdvert(final Advert advert){
        FirebaseFirestore.getInstance().document("/users/"+ advert.getOwnerID() + "/adverts/"+ advert.getId())
                .delete().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG_WRITE_FAILURE, "Error deleting Advert" + advert.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG_WRITE_SUCCESSFUL, advert.toString() + " deleted");
            }
        });
    }

    public interface QueryCallback{
        void onCallback(List<Advert> list);
    }

    private Query createQuery(){
        Query query = bd.collectionGroup("adverts")
                .orderBy("voteCount", Query.Direction.DESCENDING)
                .orderBy("rating", Query.Direction.DESCENDING)
                .limit(1);
        //Todas de um utilizador; Categoria todos ou uma especifico
        if(docSnapshot != null){
            if(user!=null){
                query = bd.collectionGroup("adverts")
                        .whereEqualTo("uID", user)
                        .orderBy("voteCount", Query.Direction.DESCENDING)
                        .orderBy("rating", Query.Direction.DESCENDING)
                        .startAfter(docSnapshot)
                        .limit(1);
            }else if(this.category.equals(Advert.Category.ALL)){
                query = bd.collectionGroup("adverts")
                        .orderBy("voteCount", Query.Direction.DESCENDING)
                        .orderBy("rating", Query.Direction.DESCENDING)
                        .startAfter(docSnapshot)
                        .limit(1);
            }else{
                query = bd.collectionGroup("adverts")
                        .whereEqualTo("category", this.category)
                        .orderBy("voteCount", Query.Direction.DESCENDING)
                        .orderBy("rating", Query.Direction.DESCENDING)
                        .startAfter(docSnapshot)
                        .limit(1);
            }
        }else{
            if(user!=null){
                Log.d("create query", "user" + user);
                query = bd.collectionGroup("adverts")
                        .whereEqualTo("uID", user)
                        .orderBy("voteCount", Query.Direction.DESCENDING)
                        .orderBy("rating", Query.Direction.DESCENDING)
                        .limit(1);
            }else if( this.category.equals(Advert.Category.ALL)){
                query = bd.collectionGroup("adverts")
                        .orderBy("voteCount", Query.Direction.DESCENDING)
                        .orderBy("rating", Query.Direction.DESCENDING)
                        .limit(1);
            }else{
                query = bd.collectionGroup("adverts")
                        .whereEqualTo("category", this.category)
                        .orderBy("voteCount", Query.Direction.DESCENDING)
                        .orderBy("rating", Query.Direction.DESCENDING)
                        .limit(1);
            }
        }
        return query;
    }

    public void getAdverts(final QueryCallback callback){

        this.createQuery().get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG_READ_FAILURE, "Advert query" + e.toString());

                    }
                })
                .addOnSuccessListener(activity, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG_READ_SUCCESSFUL, "Advert query" + queryDocumentSnapshots.toString());
                        List<DocumentSnapshot> snapList = queryDocumentSnapshots.getDocuments();
                        if(!snapList.isEmpty()) {
                            Log.d("test_fh", snapList.toString());
                            docSnapshot = snapList.get(snapList.size()-1);
                        }
                        List<Advert> list = new ArrayList<>();
                        for(DocumentSnapshot d: snapList)
                            list.add(d.toObject(Advert.class));
//                        Log.d("test fh", query.toString());
                        callback.onCallback(list);

                    }
                });
    }

}
