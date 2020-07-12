package gamal.myappnew.serverforclientapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.DeatailsFood;
import gamal.myappnew.serverforclientapp.Moduel.FoodModuel;
import gamal.myappnew.serverforclientapp.R;


public class MyFoodListAdapter extends RecyclerView.Adapter<MyFoodListAdapter.ViewHolder>  {
    List<FoodModuel> list;
    Context context;
    List<FoodModuel> searchlist;
    public MyFoodListAdapter(List<FoodModuel> list, Context context) {
        this.list = list;
        this.context = context;
        searchlist=new ArrayList<>(list);
    }

    @NonNull
    @Override
    public MyFoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyFoodListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_food_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {

//        SharedPreferences preferences=context.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
//        menuid=preferences.getString("menuid","none");
        holder.name.setText(list.get(position).getFood());
        holder.price.setText(list.get(position).getPrice() + "$");
        Glide.with(context).load(list.get(position).getImage()).into(holder.imagefood);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "" + list.get(position).getFoodId(), Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, DeatailsFood.class)
                        .putExtra("foodid", list.get(position).getFoodId())
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference(Common.FOODS)
                        .child(list.get(position).getFoodId())
                        .removeValue();
                notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

  public class ViewHolder extends RecyclerView.ViewHolder{
    TextView price,name;
    ImageView imagefood,delete;
      public ViewHolder(@NonNull View itemView) {
          super(itemView);
          price=itemView.findViewById(R.id.text_food_price);
          name=itemView.findViewById(R.id.text_food_name);
          delete=itemView.findViewById(R.id.delete);
          imagefood=itemView.findViewById(R.id.image_food_list);
      }
  }




}
