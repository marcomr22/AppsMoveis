package handlers;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class FirebaseStorageHandler {

    private static final String TAG_Image = "Image saved";

    public interface ImageSaved{
        void onComplete(Uri url);
    }

    public static void savePicture(byte[] image, final ImageSaved callback){
        FirebaseStorage.getInstance().getReference(UUID.randomUUID() + ".png")
                .putBytes(image)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG_Image, "Saved new image");
                        callback.onComplete(taskSnapshot.getStorage().getDownloadUrl().getResult());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG_Image, "Error saving image");
            }
        });
    }

}
