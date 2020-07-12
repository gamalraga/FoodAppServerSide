package gamal.myappnew.serverforclientapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.Moduel.Rating;
import gamal.myappnew.serverforclientapp.Moduel.User;
import gamal.myappnew.serverforclientapp.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
   List<Rating> list;
   Context context;

    public CommentAdapter(List<Rating> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment_list,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
       holder.comment.setText(list.get(position).getCommont());
       holder.ratingBar.setRating(Float.parseFloat(list.get(position).getRatevalue()));
        FirebaseDatabase.getInstance().getReference(Common.USERS_REF)
                .child(list.get(position).getUserid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.getValue(User.class);
                        Glide.with(context).load(user.getImageurl()).into(holder.imageView);
                        holder.username.setText(user.getUsername());
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
        CircleImageView imageView;
        TextView username,comment;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_profile);
            username=itemView.findViewById(R.id.text_comment_name);
            comment=itemView.findViewById(R.id.text_comment_text);
            ratingBar=itemView.findViewById(R.id.ratingbar);
        }
    }
}
