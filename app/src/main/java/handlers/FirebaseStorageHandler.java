package handlers;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class FirebaseStorageHandler {

    private static final String TAG_Image = "Image saved";

    public interface ImageSaved{
        void onComplete(Uri url);
    }

    public static void savePicture(byte[] image, final ImageSaved callback){
        final UploadTask put = FirebaseStorage.getInstance().getReference().child(UUID.randomUUID() + ".png")
                .putBytes(image);

        Task<Uri> urlTask = put.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return put.getResult().getStorage().getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.d(TAG_Image, "onComplete: " + downloadUri.toString());
                    callback.onComplete(downloadUri);
                } else {
                    Log.w(TAG_Image, "Error saving image");
                }
            }
        });

    }

}
