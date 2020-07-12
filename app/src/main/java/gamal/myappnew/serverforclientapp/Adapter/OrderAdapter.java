package gamal.myappnew.serverforclientapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.MainActivity;
import gamal.myappnew.serverforclientapp.MapsActivity;
import gamal.myappnew.serverforclientapp.Moduel.Request;
import gamal.myappnew.serverforclientapp.R;
import gamal.myappnew.serverforclientapp.Remote.APIService;
import gamal.myappnew.serverforclientapp.Server.MyResponse;
import gamal.myappnew.serverforclientapp.Server.Notification;
import gamal.myappnew.serverforclientapp.Server.Sender;
import gamal.myappnew.serverforclientapp.Server.Token;
import gamal.myappnew.serverforclientapp.Shipper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    List<Request> list;
    APIService mservec;
    Context context;
    Recycleroforder adapter;
   MaterialSpinner spinner;
    public OrderAdapter(List<Request> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.lauout_item_request,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Request request=list.get(position);
       adapter=new Recycleroforder(request.getFoods(),context);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        holder.recyclerView.setAdapter(adapter);

        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.layoutitem_from_left);
       holder.recyclerView.setLayoutAnimation(layoutAnimationController);
        adapter.notifyDataSetChanged();
        holder.address.setText(request.getAdress());
        holder.phone.setText(request.getPhone());
        holder.status.setText(Common.ConvertStatusts(request.getStatus()));
        holder.total.setText(request.getTotal());
        Glide.with(context).load(request.getImageurl()).into(holder.imageprofile);
                                      holder.username.setText(request.getName());
        TextDrawable drawable = TextDrawable.builder()
                .buildRound("" + request.getFoods().size(), Color.DKGRAY);
        holder.count.setImageDrawable(drawable);
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Update....", Toast.LENGTH_SHORT).show();
              ShowAlerDialogeforupdate(request.getRequest_id(),request,position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference(Common.REQUEST)
                        .child(request.getRequest_id())
                        .removeValue();
                Toast.makeText(context, "Deleted, Done!", Toast.LENGTH_SHORT).show();
            }
        });
        holder.direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              context.startActivity(new Intent(context, MapsActivity.class));
              Common.CURRENTREQUEST=request;
            }
        });
     holder.comment.setText(list.get(position).getComment());
     long date=Long.parseLong(request.getRequest_id());

     holder.date.setText(Common.getDate(date));

    }

    private void ShowAlerDialogeforupdate(final String request_id, final Request request,final int postion) {

        AlertDialog.Builder alertdialoge  = new AlertDialog.Builder(context);
        alertdialoge.setTitle("Update Order");
        alertdialoge.setMessage("Change Status of Orders");
        View view= LayoutInflater.from(context).inflate(R.layout.update_request_item,null);
          spinner=view.findViewById(R.id.statusspinner);
           final MaterialSpinner shipper=view.findViewById(R.id.shipperpinner);
     final List<String> shipperList=new ArrayList<>();
     FirebaseDatabase.getInstance().getReference(Common.SHIPPER)
             .addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     shipperList.clear();
                     for (DataSnapshot snapshot1:snapshot.getChildren())
                         shipperList.add(snapshot1.getKey());
                         shipper.setItems(shipperList);
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             });
        spinner.setItems("Placed","On My Way","Shipping");
          alertdialoge.setView(view);
          alertdialoge.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
                  request.setStatus(String.valueOf(spinner.getSelectedIndex()));
                  if (request.getStatus().equals("2"))
                 {
                     FirebaseDatabase.getInstance().getReference(Common.ORDER_NEW_SGIP_TABLE)
                             .child(shipper.getItems().get(shipper.getSelectedIndex()).toString())
                             .child(request_id)
                             .setValue(request);
                     request.setStatus(String.valueOf(spinner.getSelectedIndex()));
                     FirebaseDatabase.getInstance().getReference(Common.REQUEST)
                             .child(request_id).setValue(request);
                     Toast.makeText(context, "Shipper recive the order... ", Toast.LENGTH_SHORT).show();
                 }else {
                     FirebaseDatabase.getInstance().getReference(Common.REQUEST)
                             .child(request_id).setValue(request);
                     Toast.makeText(context, "Waiting response from customer!", Toast.LENGTH_SHORT).show();
                     //SendOrderStatusTouser(request);
                 }
              }
          });
          alertdialoge.setNegativeButton("Cancle",null);
          alertdialoge.show();

    }

    private void SendOrderStatusTouser(final Request request) {
         mservec=Common.getFCMServec();

        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference(Common.TOKENS);
        final Query data=tokens.orderByChild("serverToken").equalTo(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Notification notification = new Notification("your order" +
                            request.getRequest_id() + "was updated to " + Common.ConvertStatusts(request.getStatus()), "Update order ");
                    Sender content = new Sender(token.getToken(), notification);
                    mservec.sendNotification(content).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {

                                if (response.body().success == 1) {
                                    Toast.makeText(context, "Update sucessfull!", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(context, "Failed!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Log.e("ERROR", t.getMessage());
                        }

                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
            }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageprofile,count;
        TextView phone,address,status,total,username,comment,date;
        RecyclerView recyclerView;
        TextView update,delete,direction;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comment=itemView.findViewById(R.id.comment);
            date=itemView.findViewById(R.id.date_order);
            recyclerView=itemView.findViewById(R.id.recycle_food_list);
            phone=itemView.findViewById(R.id.phone);
            address=itemView.findViewById(R.id.address);
            status=itemView.findViewById(R.id.status);
            imageprofile=itemView.findViewById(R.id.imageprofile);
            total=itemView.findViewById(R.id.total);
            username=itemView.findViewById(R.id.user);
            count=itemView.findViewById(R.id.count_order);
            update=itemView.findViewById(R.id.edit_order);
            delete=itemView.findViewById(R.id.delet_order);
            direction=itemView.findViewById(R.id.Direction);

        }
    }

}
