package gamal.myappnew.serverforclientapp.ui.addCategory;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.MainActivity;
import gamal.myappnew.serverforclientapp.R;

import static android.app.Activity.RESULT_OK;

public class AddCategory extends Fragment {

ImageView post,close,image_menu;
EditText name_menu;
TextView select_image;
    StorageReference storageReference;
    Uri imageuri;
    StorageTask uploadtask;
    String muri;
     AlertDialog alertDialog;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_category, container, false);
              post=root.findViewById(R.id.post);
        close=root.findViewById(R.id.close);
        image_menu=root.findViewById(R.id.image_menu);
        name_menu=root.findViewById(R.id.menu_name);

        alertDialog=new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        select_image=root.findViewById(R.id.selcted_image);
        storageReference= FirebaseStorage.getInstance().getReference("Categories");
               post.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       alertDialog.show();
                       HashMap<String,Object> hashMap=new HashMap<>();
                       hashMap.put("image",muri);
                       hashMap.put("name",name_menu.getText().toString());
                       hashMap.put("menuid",FirebaseDatabase.getInstance().getReference(Common.GETAGORY_REF)
                               .push().getKey());
                       FirebaseDatabase.getInstance().getReference(Common.GETAGORY_REF)
                               .push()
                               .setValue(hashMap);
                       Toast.makeText(getContext(), "Done!...", Toast.LENGTH_LONG).show();
                       startActivity(new Intent(getContext(), MainActivity.class));
                       alertDialog.dismiss();
                   }

               });
               close.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       startActivity(new Intent(getContext(), MainActivity.class)
                       .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                   }
               });
        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.
                            READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                        ,Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                , 1);
                    } else {

                            opengallery();




                    }
                }
            }
        });


        return root;
    }
    private void opengallery() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    opengallery();

                } else {
                    // Permission Denied
                    Toast.makeText(getContext(), "Can't change image profile..", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode== RESULT_OK && data!=null)
        {
            imageuri=data.getData();
            if (uploadtask!=null&&uploadtask.isInProgress())
            {
                Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            }else {
                uploadimage();
            }

        }

    }
    private String getfileextensions(Uri uri)
    {
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadimage()
    {
        final AlertDialog pd;
        pd=new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        pd.show();
        if (imageuri!=null)
        {
            final StorageReference filereference=storageReference.child(System.currentTimeMillis()+"."+getfileextensions(imageuri));
            uploadtask=filereference.putFile(imageuri);
            uploadtask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloaduri = task.getResult();
                         muri = downloaduri.toString();
                        Glide.with(getContext()).load(muri).into(image_menu);
                        pd.dismiss();

                    }else {
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }else {
            Toast.makeText(getContext(), "No Image Selected !.", Toast.LENGTH_SHORT).show();
        }
    }
}