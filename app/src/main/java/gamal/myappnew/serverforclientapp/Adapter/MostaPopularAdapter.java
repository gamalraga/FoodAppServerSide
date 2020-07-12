package gamal.myappnew.serverforclientapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import gamal.myappnew.serverforclientapp.DeatailsFood;
import gamal.myappnew.serverforclientapp.Moduel.MostPopular;
import gamal.myappnew.serverforclientapp.R;


public class MostaPopularAdapter extends RecyclerView.Adapter<MostaPopularAdapter.ViewHolder> {
   List<MostPopular> mostPopularList;
   Context context;

    public MostaPopularAdapter(List<MostPopular> mostPopularList, Context context) {
        this.mostPopularList = mostPopularList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.popular_food_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView.setText(mostPopularList.get(position).getName());
        Glide.with(context).load(mostPopularList.get(position).getImage()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+mostPopularList.get(position).getFood_id(), Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, DeatailsFood.class)
                        .putExtra("foodid",mostPopularList.get(position).getFood_id())
                        .addFlags(Intent. FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mostPopularList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
 ImageView imageView;
 TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.food_image);
            textView=itemView.findViewById(R.id.text_food_name);

        }
    }
}
