package gamal.myappnew.serverforclientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.Moduel.IOnClickListListener;

public class CreateShipper extends AppCompatActivity {
 RecyclerView recyclerView;
 FloatingActionButton fab;
 ShipperAdapter adapter;
 List<Shipper> list;
    TextView empty;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shipper);
        recyclerView=findViewById(R.id.recyclerview_shipper);
        fab=findViewById(R.id.fab);
        list=new ArrayList<>();
         empty=findViewById(R.id.empty);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter=new ShipperAdapter(list,getApplicationContext());
        recyclerView.setAdapter(adapter);
        LoadAllShipper();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertdialoge = new AlertDialog.Builder(CreateShipper.this);
                alertdialoge.setTitle("Create Shipper");
                alertdialoge.setMessage("Please, Enter all infromation !");
                alertdialoge.setIcon(R.drawable.nav_shipper);
                View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_add_shipper,null);
                final EditText name,phone,passsword;
                name=view.findViewById(R.id.ed_shhiper_name);
                phone=view.findViewById(R.id.ed_shhiper_phone);
                passsword=view.findViewById(R.id.ed_shhiper_password);
                alertdialoge.setView(view);
                alertdialoge.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (name.getText().toString().isEmpty() | phone.getText().toString().isEmpty() | passsword.getText().toString().isEmpty()) {
                            Toast.makeText(CreateShipper.this, "All Fileds is requerid!!", Toast.LENGTH_SHORT).show();
                        } else
                        {
                            Shipper shipper = new Shipper();
                        shipper.setName(name.getText().toString());
                        shipper.setPhone(phone.getText().toString());
                        shipper.setPassword(passsword.getText().toString());
                        FirebaseDatabase.getInstance().getReference(Common.SHIPPER)
                                .child(phone.getText().toString())
                                .setValue(shipper).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    Toast.makeText(CreateShipper.this, "Shipper is created!!", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(CreateShipper.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    }
                });
                alertdialoge.setNegativeButton("Cancle",null);
                alertdialoge.show();

            }
        });
        adapter.setViewItemInterface(position -> {
            ShowAlertDialoge(list.get(position).getName(),list.get(position).getPhone(),list.get(position).getPassword());
        });

    }

    private void LoadAllShipper() {
        FirebaseDatabase.getInstance().getReference(Common.SHIPPER)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         list.clear();
                         for (DataSnapshot snapshot:dataSnapshot.getChildren())
                         {
                             Shipper shipper=snapshot.getValue(Shipper.class);
                             list.add(shipper);
                         }
                         adapter.notifyDataSetChanged();
                         if (list.isEmpty())
                         {
                             empty.setVisibility(View.VISIBLE);

                         }else {
                             empty.setVisibility(View.GONE);
                         }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void ShowAlertDialoge(String sname,String sphone,String spassword) {
        AlertDialog.Builder alertdialoge = new AlertDialog.Builder(CreateShipper.this);
        alertdialoge.setTitle("Edit Shipper");
        alertdialoge.setMessage("Please, Enter all infromation !");
        alertdialoge.setIcon(R.drawable.nav_shipper);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_add_shipper, null);
        final EditText name, phone, passsword;
        name = view.findViewById(R.id.ed_shhiper_name);
        phone = view.findViewById(R.id.ed_shhiper_phone);
        passsword = view.findViewById(R.id.ed_shhiper_password);
        name.setText(sname);
        phone.setText(sphone);
        passsword.setText(spassword);
        alertdialoge.setView(view);
        alertdialoge.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (name.getText().toString().isEmpty() || phone.getText().toString().isEmpty() || name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please ,Enter all information ", Toast.LENGTH_SHORT).show();

                } else {
                    Shipper shipper = new Shipper();
                    shipper.setName(name.getText().toString());
                    shipper.setPhone(phone.getText().toString());
                    shipper.setPassword(passsword.getText().toString());
                    FirebaseDatabase.getInstance().getReference(Common.SHIPPER)
                            .child(phone.getText().toString())
                            .setValue(shipper).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(getApplicationContext(), "Shipper is edited Succefull!!", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(), "" + task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });
        alertdialoge.setNegativeButton("Cancle", null);
        alertdialoge.show();

    }

}
