package gamal.myappnew.serverforclientapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.MainActivity;
import gamal.myappnew.serverforclientapp.Moduel.CategoryModuel;
import gamal.myappnew.serverforclientapp.R;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private static final int IMG_REQUEST =1 ;
    List<CategoryModuel> categoryModuelList;
     Context context;
    public CategoryAdapter(Context context, List<CategoryModuel> categoryModuelList) {
        this.categoryModuelList = categoryModuelList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

           holder.textView.setText(categoryModuelList.get(position).getName());
        Glide.with(context).load(categoryModuelList.get(position).getImage()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.opengalery(position);
                context.startActivity(new Intent(context, MainActivity.class)
                .putExtra("menuid",categoryModuelList.get(position).getMenuid())
                .putExtra("menuname",categoryModuelList.get(position).getName()));
                Toast.makeText(context, ""+categoryModuelList.get(position).getMenuid(), Toast.LENGTH_LONG).show();

            }
        });


holder.move_it.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        android.widget.PopupMenu popupMenu= new android.widget.PopupMenu(context,v);
        popupMenu.inflate(R.menu.popo_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.update)
                {
                    SharedPreferences.Editor editor=context.getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                    editor.putString("menuidupdate", categoryModuelList.get(position).getMenuid());
                    editor.apply();
                   MainActivity.navController.navigate(R.id.update_category);
                }
                else if (item.getItemId()==R.id.delete)
                {
                    FirebaseDatabase.getInstance().getReference(Common.GETAGORY_REF)
                            .child(categoryModuelList.get(position).getMenuid())
                            .removeValue();
                    //don't forget delete all food in this category'
                    FirebaseDatabase.getInstance().getReference(Common.FOODS)
                            .orderByChild("MenuId").equalTo(categoryModuelList.get(position).getMenuid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot:dataSnapshot.getChildren())
                                    {
                                        snapshot.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                }
                return false;
            }
        });
        popupMenu.show();
    }
});



    }


    private void ShowAlerDialoge(final String menuid) {
        AlertDialog.Builder alertdialoge=new AlertDialog.Builder(context);
        alertdialoge.setTitle("Update category");
        alertdialoge.setMessage("Change name and photo of gategory");
        View view=LayoutInflater.from(context).inflate(R.layout.update_category,null);
        final ImageView imageView=view.findViewById(R.id.image_category);
        final EditText editText=view.findViewById(R.id.menu_name);
        Button changephoto=view.findViewById(R.id.changephoto);
        FirebaseDatabase.getInstance().getReference(Common.GETAGORY_REF)
                .child(menuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CategoryModuel moduel=dataSnapshot.getValue(CategoryModuel.class);
                Glide.with(context).load(moduel.getImage()).into(imageView);
                editText.setText(moduel.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        changephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
         alertdialoge.setView(view);
         alertdialoge.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {

             }
         });
         alertdialoge.setNegativeButton("Cancle", null);
         alertdialoge.show();
    }


    @Override
    public int getItemCount() {
        return categoryModuelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
           ImageView imageView,move_it;
           TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_category);
            textView=itemView.findViewById(R.id.tex_category);
            move_it=itemView.findViewById(R.id.move_it);
        }


    }

    OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void opengalery(int postion);
    }

}
