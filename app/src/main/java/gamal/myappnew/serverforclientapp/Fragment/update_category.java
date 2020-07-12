package gamal.myappnew.serverforclientapp.Fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.MainActivity;
import gamal.myappnew.serverforclientapp.Moduel.CategoryModuel;
import gamal.myappnew.serverforclientapp.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class update_category extends Fragment {
ImageView image_menu,done,clos;
TextView changephoto;
EditText nameofcategory;
String menuid;
    StorageReference storageReference;
    Uri imageuri;
    StorageTask uploadtask;
     AlertDialog alertDialog;

    public update_category() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_update_category, container, false);
        image_menu=view.findViewById(R.id.image_menu);
        done=view.findViewById(R.id.post);
        clos=view.findViewById(R.id.close);
        storageReference= FirebaseStorage.getInstance().getReference("CategoryUpdate");

        changephoto=view.findViewById(R.id.changephoto);
        alertDialog=new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        nameofcategory=view.findViewById(R.id.menu_name);
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        menuid= sharedPreferences.getString("menuidupdate","none");
        Toast.makeText(getContext(), ""+menuid, Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference(Common.GETAGORY_REF)
                .child(menuid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (isAdded()) {
                            CategoryModuel moduel = dataSnapshot.getValue(CategoryModuel.class);
                            Glide.with(getContext()).load(moduel.getImage()).into(image_menu);
                            nameofcategory.setText(moduel.getName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("name",nameofcategory.getText().toString());
                FirebaseDatabase.getInstance().getReference(Common.GETAGORY_REF)
                        .child(menuid)
                        .updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            alertDialog.dismiss();
                            Toast.makeText(getContext(), "Done!", Toast.LENGTH_SHORT).show();
                        }else {
                            alertDialog.dismiss();
                        }
                    }
                });
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });
        clos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),MainActivity.class));
            }
        });
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

        return  view;
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
                    Toast.makeText(getContext(), "Can't change image Category..", Toast.LENGTH_SHORT)
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
                        hashMap.put("image",muri);
                        Glide.with(getContext()).load(muri).into(image_menu);
                        FirebaseDatabase.getInstance().getReference(Common.GETAGORY_REF)
                                .child(menuid)
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
