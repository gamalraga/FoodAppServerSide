package gamal.myappnew.serverforclientapp.Fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
import gamal.myappnew.serverforclientapp.Adapter.MyFoodListAdapter;
import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.DeatailsFood;
import gamal.myappnew.serverforclientapp.Moduel.BestDealModuel;
import gamal.myappnew.serverforclientapp.Moduel.FoodModuel;
import gamal.myappnew.serverforclientapp.R;

import static android.app.Activity.RESULT_OK;

public class show_foodFragment extends Fragment {
    RecyclerView recyclerView;
    List<FoodModuel> categoryModuels;
    LayoutAnimationController layoutAnimationController;
    String menuid;
    MyFoodListAdapter adapter;
    ImageView add_food,imageView,image_food;
    StorageReference storageReference;
    Uri imageuri;
    StorageTask uploadtask;
    String imageURL;
    List<BestDealModuel> list;
    public show_foodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

          View view=inflater.inflate(R.layout.fragment_show_food, container, false);
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("PREFS",Context.MODE_PRIVATE);
       menuid= sharedPreferences.getString("menuid","none");
        recyclerView=view.findViewById(R.id.recycle_food_list);
        categoryModuels=new ArrayList<>();
        recyclerView.setHasFixedSize(true);

        EditText search=view.findViewById(R.id.search_food);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchFood(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        storageReference= FirebaseStorage.getInstance().getReference("Foods");
    list=new ArrayList<>();
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layoutitem_from_left);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new MyFoodListAdapter(categoryModuels,getContext());
        recyclerView.setAdapter(adapter);
        if (menuid!=null && !menuid.isEmpty())
        {
            if (isAdded()) {
                LoadMenu(menuid);
            }
        }else {
            Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
        }
        add_food=view.findViewById(R.id.add_food);
        add_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   ShowAlertDialoge(FirebaseDatabase.getInstance().getReference(Common.FOODS).push().getKey());
            }
        });

        return  view;
    }
    private void ShowAlertDialoge(final String foodid) {

        AlertDialog.Builder alertdialoge  = new AlertDialog.Builder(getContext());
        alertdialoge.setTitle("Update category");
        alertdialoge.setMessage("Selecte name and photo of food");
        View view= LayoutInflater.from(getContext()).inflate(R.layout.update_food,null);
        imageView=view.findViewById(R.id.image_food);
        final EditText foodname=view.findViewById(R.id.food_name);
        final EditText foodprice=view.findViewById(R.id.food_price);
        final EditText fooddescription=view.findViewById(R.id.food_description);
        final EditText fooddiscount=view.findViewById(R.id.food_discount);
        TextView changephoto=view.findViewById(R.id.changephoto);
        changephoto.setText("Selected Image!");
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
        alertdialoge.setView(view);


        alertdialoge.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final HashMap<String,Object> hashMap=new HashMap<>();
                if (imageURL.isEmpty())
                {
                    Toast.makeText(getContext(), "Please Slecte photo frist!..", Toast.LENGTH_SHORT).show();
                }else {
                    hashMap.put("Food",foodname.getText().toString());
                    hashMap.put("Description",fooddescription.getText().toString());
                    hashMap.put("Discount",fooddiscount.getText().toString());
                    hashMap.put("Price",foodprice.getText().toString());
                    hashMap.put("MenuId",menuid);
                    hashMap.put("Image",imageURL);
                    FirebaseDatabase.getInstance().getReference(Common.FOODS)
                            .push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getContext(), "Done!", Toast.LENGTH_SHORT).show();
                            }
                            double discount=Double.parseDouble(fooddiscount.getText().toString());
                            if (discount>=25)
                            {
                                BestDealModuel bestDealModuel=new BestDealModuel();
                                bestDealModuel.setImage(imageURL);
                                bestDealModuel.setFood_id(foodid);
                                bestDealModuel.setMenu_id(menuid);
                                bestDealModuel.setName(foodname.getText().toString());
                                FirebaseDatabase.getInstance().getReference(Common.DESTDEAL)
                                        .push().setValue(bestDealModuel);
                            }


                                                   }
                    });
                }



            }
        });
        alertdialoge.setNegativeButton("Cancle",null);

        alertdialoge.show();


    }


    private void LoadMenu(final String menuid) {


            FirebaseDatabase.getInstance().getReference(Common.FOODS)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            categoryModuels.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    FoodModuel foodModuel=  snapshot.getValue(FoodModuel.class);
                                    if (foodModuel.getMenuId().equals(menuid)) {
                                        foodModuel.setFoodId(snapshot.getKey());
                                        categoryModuels.add(foodModuel);
                                    }

                                }

                                adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
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
                    Toast.makeText(getContext(), "Can't upload image food..", Toast.LENGTH_SHORT)
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
        final android.app.AlertDialog pd;
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
                         imageURL=downloaduri.toString();
                 Glide.with(getContext()).load(imageURL).into(imageView);
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
    private void SearchFood(String s) {
        FirebaseDatabase.getInstance().getReference(Common.FOODS)
                .orderByChild("Food")
                .startAt(s)
                .endAt(s+"\uf8ff")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        categoryModuels.clear();
                        for (DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            FoodModuel foodModuel=snapshot.getValue(FoodModuel.class);
                            if (foodModuel.getMenuId().equals(menuid)) {
                                foodModuel.setFoodId(snapshot.getKey());
                                categoryModuels.add(foodModuel);
                            }
                        }

                        adapter.notifyDataSetChanged();
                        recyclerView.scheduleLayoutAnimation();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}
