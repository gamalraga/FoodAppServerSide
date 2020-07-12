package gamal.myappnew.serverforclientapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.Moduel.IOnClickListListener;

public class ShipperAdapter extends RecyclerView.Adapter<ShipperAdapter.ViewHolder> {
    List<Shipper> list;
    Context context;

    public ShipperAdapter(List<Shipper> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_create_shipper,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
           holder.shippername.setText(list.get(position).name);
           holder.shipperphone.setText(list.get(position).phone);
           holder.rmove.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   FirebaseDatabase.getInstance().getReference(Common.SHIPPER)
                           .child(list.get(position).phone)
                           .removeValue();
                   Toast.makeText(context, "Remove Done!", Toast.LENGTH_SHORT).show();
                   notifyDataSetChanged();

               }
           });
           holder.edit.setOnClickListener(v -> {
               if (viewItemInterface != null) {
                   viewItemInterface.onItemClick(position);
               }
//ShowAlertDialoge();

//ShowAlertDialoge();
           });
    }

    private void ShowAlertDialoge() {
        androidx.appcompat.app.AlertDialog.Builder alertdialoge= new androidx.appcompat.app.AlertDialog.Builder(context);
        alertdialoge.setTitle("Edit Shipper");
        alertdialoge.setMessage("Please, Enter all infromation !");
        alertdialoge.setIcon(R.drawable.nav_shipper);
        View view= LayoutInflater.from(context).inflate(R.layout.item_create_shipper,null);
        final EditText name,phone,passsword;
        name=view.findViewById(R.id.ed_shhiper_name);
        phone=view.findViewById(R.id.ed_shhiper_phone);
        passsword=view.findViewById(R.id.ed_shhiper_password);
        alertdialoge.setView(view);
        alertdialoge.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (name.getText().toString().isEmpty()||phone.getText().toString().isEmpty()||name.getText().toString().isEmpty())
                {
                    Toast.makeText(context, "Please ,Enter all information ", Toast.LENGTH_SHORT).show();

                }else {
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
                                Toast.makeText(context, "Shipper is edited Succefull!!", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(context, "" + task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });
        alertdialoge.setNegativeButton("Cancle",null);
        alertdialoge.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shippername,shipperphone,edit,rmove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shippername=itemView.findViewById(R.id.shipper_name);
            shipperphone=itemView.findViewById(R.id.shipper_phone);
            edit=itemView.findViewById(R.id.shipper_edit);
            rmove=itemView.findViewById(R.id.shipper_remove);

        }

    }
    private IOnClickListListener viewItemInterface;

    public void setViewItemInterface(IOnClickListListener viewItemInterface) {
        this.viewItemInterface = viewItemInterface;
    }

}
