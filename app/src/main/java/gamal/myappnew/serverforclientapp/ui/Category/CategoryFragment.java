package gamal.myappnew.serverforclientapp.ui.Category;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gamal.myappnew.serverforclientapp.Adapter.CategoryAdapter;
import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.Moduel.CategoryModuel;
import gamal.myappnew.serverforclientapp.R;

public class CategoryFragment extends Fragment {

    RecyclerView recyclerView;
    List<CategoryModuel> categoryModuels;
    LayoutAnimationController layoutAnimationController;
    CategoryAdapter adapter;
    CategoryModuel moduel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView=root.findViewById(R.id.recycler_category);
        categoryModuels=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layoutitem_from_left);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        adapter=new CategoryAdapter(getContext(),categoryModuels);
        recyclerView.setAdapter(adapter);
        LoadMenu();
 adapter.setOnItemClick(new CategoryAdapter.OnItemClick() {
     @Override
     public void opengalery(int postion) {

     }
 });

        return root;
    }
    private void LoadMenu() {

        FirebaseDatabase.getInstance().getReference(Common.GETAGORY_REF)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (isAdded()) {
                            categoryModuels.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                 moduel = snapshot.getValue(CategoryModuel.class);
                                moduel.setMenuid(snapshot.getKey());
                                categoryModuels.add(moduel);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}