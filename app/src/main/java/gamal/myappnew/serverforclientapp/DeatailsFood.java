package gamal.myappnew.serverforclientapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.Moduel.BestDealModuel;
import gamal.myappnew.serverforclientapp.Moduel.FoodModuel;
import gamal.myappnew.serverforclientapp.Moduel.Rating;

public class DeatailsFood extends AppCompatActivity {
    String foodid;
    TextView food_name_selected,food_price_selected,food_description_selected;
    ImageView image_food_selected;
    CollapsingToolbarLayout collpsing;
    FloatingActionButton delet,update;
    FoodModuel moduel;
    StorageReference storageReference;
    Uri imageuri;
    StorageTask uploadtask;
    android.app.AlertDialog alertDialog;
    ImageView imageView;
    String muri,menuid;
    Button showcomments;
    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatails_food);
        if (getIntent()!=null)
        {
            foodid=getIntent().getStringExtra("foodid");
        }
        ratingBar=findViewById(R.id.reatingbar);
        storageReference= FirebaseStorage.getInstance().getReference("FoodUpdate");
        showcomments=findViewById(R.id.show_comments);
        showcomments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CommentActivity.class)
                        .putExtra("FoodId",foodid));
            }
        });

        food_description_selected=findViewById(R.id.food_description_selected);
        food_name_selected=findViewById(R.id.food_name_selected);
        food_price_selected=findViewById(R.id.food_price_selected);
        image_food_selected=findViewById(R.id.image_food_selected);
        collpsing=findViewById(R.id.collpsing);
        delet=findViewById(R.id.delete);
        update=findViewById(R.id.update);
        if (!foodid.equals("")&& !foodid.isEmpty())
        {
            Loadinfofoodselected(foodid);
            getRagingfood(foodid);

        }

        delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference(Common.FOODS)
                        .child(foodid)
                .removeValue();
                Toast.makeText(DeatailsFood.this,"Deleted is Done !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DeatailsFood.this,MainActivity.class));
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlertDialoge(foodid);
            }
        });
    }

    private void ShowAlertDialoge(final String foodid) {

        AlertDialog.Builder alertdialoge  = new AlertDialog.Builder(this);
            alertdialoge.setTitle("Update category");
            alertdialoge.setMessage("Change name and photo of food");
            View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.update_food,null);
              imageView=view.findViewById(R.id.image_food);
            final EditText foodname=view.findViewById(R.id.food_name);
        final EditText foodprice=view.findViewById(R.id.food_price);
        final EditText fooddescription=view.findViewById(R.id.food_description);
        final EditText fooddiscount=view.findViewById(R.id.food_discount);
        TextView changephoto=view.findViewById(R.id.changephoto);
        changephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.
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
        alertdialoge.setView(view);

                        Glide.with(getApplicationContext()).load(moduel.getImage()).into(imageView);
                        fooddescription.setText(moduel.getDescription());
                        fooddiscount.setText(moduel.getDiscount());
                        foodprice.setText(moduel.getPrice());
                        foodname.setText(moduel.getFood());


        alertdialoge.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("Food",foodname.getText().toString());
                hashMap.put("Description",fooddescription.getText().toString());
                hashMap.put("Discount",fooddiscount.getText().toString());
                hashMap.put("Price",foodprice.getText().toString());
                hashMap.put("Image",muri);
                FirebaseDatabase.getInstance().getReference(Common.FOODS)
                        .child(foodid)
                        .updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            startActivity(new Intent(getApplicationContext(),DeatailsFood.class)
                            .putExtra("foodid",foodid));
                            finish();
                            Toast.makeText(DeatailsFood.this, "Done", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });
        alertdialoge.setNegativeButton("Cancle",null);

        alertdialoge.show();


    }

    private void opengallery() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    private void Loadinfofoodselected(String foodid) {
        FirebaseDatabase.getInstance().getReference()
                .child(Common.FOODS).child(foodid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    moduel = dataSnapshot.getValue(FoodModuel.class);
                    muri=moduel.getImage();
                    food_name_selected.setText(moduel.getFood());
                    food_description_selected.setText(moduel.getDescription());
                    food_price_selected.setText(moduel.getPrice() + "");
                    menuid=moduel.getMenuId();
                    Glide.with(getApplicationContext()).load(moduel.getImage()).into(image_food_selected);
                    collpsing.setTitle(moduel.getFood());
                }
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
                    Toast.makeText(getApplicationContext(), "Can't change image food..", Toast.LENGTH_SHORT)
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
                Toast.makeText(getApplicationContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            }else {
                uploadimage();
            }

        }

    }
    private String getfileextensions(Uri uri)
    {
        ContentResolver contentResolver=this.getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadimage()
    {
        final android.app.AlertDialog pd;
        pd=new SpotsDialog.Builder().setCancelable(false).setContext(this).build();
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
                        String imageurl=downloaduri.toString();
                        Glide.with(getApplicationContext()).load(imageurl).into(imageView);
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("Image",imageurl);
                        muri=imageurl;
                        FirebaseDatabase.getInstance().getReference(Common.FOODS)
                                .child(foodid)
                                .updateChildren(hashMap);
                        pd.dismiss();
                    }else {
                        Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }else {
            Toast.makeText(getApplicationContext(), "No Image Selected !.", Toast.LENGTH_SHORT).show();
        }
    }
    private void getRagingfood(String foodid) {
        // i  will rating food with get all rating from users and sum average and ther sum finaly i wil get it for food;
        FirebaseDatabase.getInstance().getReference(Common.RATING)
                .orderByChild("foodid").equalTo(foodid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int count=0;int sum=0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            Rating rating=snapshot.getValue(Rating.class);
                            Log.i("jwfiuwenf",rating.getRatevalue());
                            sum+=Integer.parseInt(rating.getRatevalue());
                            count++;
                        }
                        if (count!=0) {
                            float average = sum / count;
                            ratingBar.setRating(average);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
