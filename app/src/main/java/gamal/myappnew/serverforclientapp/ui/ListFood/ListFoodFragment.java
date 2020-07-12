package gamal.myappnew.serverforclientapp.ui.ListFood;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gamal.myappnew.serverforclientapp.Adapter.MyFoodListAdapter;
import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.Moduel.FoodModuel;
import gamal.myappnew.serverforclientapp.R;

public class ListFoodFragment extends Fragment {

    RecyclerView recyclerView;
    List<FoodModuel> categoryModuels;
    LayoutAnimationController layoutAnimationController;
    MyFoodListAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_show_food, container, false);

      EditText search=root.findViewById(R.id.search_food);
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
        recyclerView = root.findViewById(R.id.recycle_food_list);
        categoryModuels = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyFoodListAdapter(categoryModuels, getContext());
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layoutitem_from_left);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.setAdapter(adapter);
        ImageView appBarLayout=root.findViewById(R.id.add_food);
        appBarLayout.setVisibility(View.GONE);
        LoadMenu();
        return root;
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

                            foodModuel.setFoodId(snapshot.getKey());
                            categoryModuels.add(foodModuel);
                        }

                        adapter.notifyDataSetChanged();
                        recyclerView.scheduleLayoutAnimation();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void LoadMenu() {

        if (isAdded())
        {
            FirebaseDatabase.getInstance().getReference(Common.FOODS)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            categoryModuels.clear();
                            for (DataSnapshot snapshot:dataSnapshot.getChildren())
                            {
                                FoodModuel foodModuel=snapshot.getValue(FoodModuel.class);

                                foodModuel.setFoodId(snapshot.getKey());
                                categoryModuels.add(foodModuel);


                            }

                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }
}