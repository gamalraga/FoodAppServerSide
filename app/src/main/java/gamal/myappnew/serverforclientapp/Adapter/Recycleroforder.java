package gamal.myappnew.serverforclientapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import gamal.myappnew.serverforclientapp.DeatailsFood;
import gamal.myappnew.serverforclientapp.Moduel.FoodModuel;
import gamal.myappnew.serverforclientapp.Moduel.FoodofRequest;
import gamal.myappnew.serverforclientapp.R;

public class Recycleroforder extends RecyclerView.Adapter<Recycleroforder.ViewHolder> {
  List<FoodofRequest> foodofRequestList;
  Context context;

    public Recycleroforder(List<FoodofRequest> foodofRequestList, Context context) {
        this.foodofRequestList = foodofRequestList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_of_food_request,parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        Glide.with(context).load(foodofRequestList.get(position).getImageFood()).into(holder.imageView);
        holder.textView.setText(foodofRequestList.get(position).getFoodName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DeatailsFood.class)
                        .putExtra("foodid",foodofRequestList.get(position).getFoodId()));
            }
        });

    }


    @Override
    public int getItemCount() {
        return foodofRequestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        LinearLayout lener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.order_image);
            textView=itemView.findViewById(R.id.text_order_name);
            lener=itemView.findViewById(R.id.lener);
        }

    }
}
