package gamal.myappnew.serverforclientapp.ui.Requests;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gamal.myappnew.serverforclientapp.Adapter.CategoryAdapter;
import gamal.myappnew.serverforclientapp.Adapter.OrderAdapter;
import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.Moduel.Request;
import gamal.myappnew.serverforclientapp.R;

public class RequestsFragment extends Fragment {
  RecyclerView recyclerView;
  OrderAdapter orderAdapter;
  List<Request> list;
  LayoutAnimationController layoutAnimationController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_requests, container, false);
        recyclerView=root.findViewById(R.id.recyclerview);
        list=new ArrayList<>();
       recyclerView.setHasFixedSize(true);
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layoutitem_from_left);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter=new OrderAdapter(list,getContext());
        recyclerView.setAdapter(orderAdapter);
        if (isAdded())
        {
            LoadRequest();
        }

        return root;
    }

    private void LoadRequest() {
        FirebaseDatabase.getInstance().getReference(Common.REQUEST)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            Request request=snapshot.getValue(Request.class);
                            request.setRequest_id(snapshot.getKey());
                            Log.i("knwjkfnewkj",request.getRequest_id());
                            list.add(request);
                        }
                        orderAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}