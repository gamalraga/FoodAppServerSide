package gamal.myappnew.serverforclientapp.ui.MyAcount;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.MainActivity;
import gamal.myappnew.serverforclientapp.Moduel.User;
import gamal.myappnew.serverforclientapp.R;

import static android.app.Activity.RESULT_OK;

public class MyAccountFragment extends Fragment {
    ImageView imageprofile,done,close;
    EditText username,phone,bio,address;
    TextView changephoto;
    StorageReference storageReference;
    Uri imageuri;
    StorageTask uploadtask;
    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_myaccount, container, false);
        imageprofile=view.findViewById(R.id.edit_imageprofile);
        username=view.findViewById(R.id.edit_username);
        phone=view.findViewById(R.id.edit_phone);
        bio=view.findViewById(R.id.edit_bio);
        address=view.findViewById(R.id.address);
        done=view.findViewById(R.id.save);
        close=view.findViewById(R.id.close);
        alertDialog=new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        changephoto=view.findViewById(R.id.edit_changephoto);
        loadinfofromfirbase();
        changephoto.setOnClickListener(new View.OnClickListener() {
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
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("username",username.getText().toString());
                hashMap.put("phone",phone.getText().toString());
                hashMap.put("bio",bio.getText().toString());
                hashMap.put("address",address.getText().toString());
                FirebaseDatabase.getInstance().getReference(Common.USERS_REF)
                        .child(Common.CURRENT_USER.getId())
                        .updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        alertDialog.dismiss();
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getContext(), "Changed Done!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), MainActivity.class));
                        }
                    }
                });
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        return  view;
    }

    private void opengallery() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    private void loadinfofromfirbase() {
        FirebaseDatabase.getInstance().getReference(Common.USERS_REF)
                .child(Common.CURRENT_USER.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.getValue(User.class);
                        username.setText(user.getUsername());
                        bio.setText(user.getBio());
                        phone.setText(user.getPhone());
                        Glide.with(getContext()).load(user.getImageurl()).into(imageprofile);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                    if (task.isSuccessful())
                    {
                        Uri downloaduri=task.getResult();
                        String muri=downloaduri.toString();
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("imageurl",muri);
                        Glide.with(getContext()).load(muri).into(imageprofile);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(Common.CURRENT_USER.getId())
                                .updateChildren(hashMap);
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