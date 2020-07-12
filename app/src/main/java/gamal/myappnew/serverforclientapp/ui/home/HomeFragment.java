package gamal.myappnew.serverforclientapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import gamal.myappnew.serverforclientapp.Adapter.MostaPopularAdapter;
import gamal.myappnew.serverforclientapp.Adapter.MyBestDealAdapter;
import gamal.myappnew.serverforclientapp.Common.Common;
import gamal.myappnew.serverforclientapp.Moduel.BestDealModuel;
import gamal.myappnew.serverforclientapp.Moduel.MostPopular;
import gamal.myappnew.serverforclientapp.R;
import gamal.myappnew.serverforclientapp.Server.Token;

public class HomeFragment extends Fragment {
    RecyclerView recycle_popular;
    LoopingViewPager viewpager;
    List<MostPopular> mostPopularList;
    List<BestDealModuel> bestDealModuelList;
    MostaPopularAdapter popularadapter;
    MyBestDealAdapter bestdealadapter;
    LayoutAnimationController layoutAnimationController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recycle_popular=root.findViewById(R.id.recycle_categories);
        mostPopularList=new ArrayList<>();
        recycle_popular.setHasFixedSize(true);
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layoutitem_from_left);
        recycle_popular.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        recycle_popular.setLayoutAnimation(layoutAnimationController);
        popularadapter=new MostaPopularAdapter(mostPopularList,getContext());
        recycle_popular.setAdapter(popularadapter);
        if (isAdded())
        {
            LoodPopularfood();
            viewpager=root.findViewById(R.id.viewpager);
            bestDealModuelList = new ArrayList<>();
            bestdealadapter=new MyBestDealAdapter(getContext(),bestDealModuelList,true);
            viewpager.setAdapter(bestdealadapter);
            LoodBestDeal();
        }

        return root;
    }
    private void LoodBestDeal() {
        FirebaseDatabase.getInstance().getReference(Common.DESTDEAL)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            bestDealModuelList.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                BestDealModuel moduel = snapshot.getValue(BestDealModuel.class);
                                bestDealModuelList.add(moduel);
                            }
                            bestdealadapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private void LoodPopularfood() {
        FirebaseDatabase.getInstance().getReference(Common.MOSTPOPULAR)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            MostPopular popular=snapshot.getValue(MostPopular.class);
                            mostPopularList.add(popular);
                        }
                        popularadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    @Override
    public void onPause() {
        super.onPause();
        viewpager.pauseAutoScroll();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewpager.resumeAutoScroll();
    }
}