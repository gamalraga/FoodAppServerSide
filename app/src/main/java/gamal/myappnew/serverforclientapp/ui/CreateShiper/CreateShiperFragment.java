package gamal.myappnew.serverforclientapp.ui.CreateShiper;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import gamal.myappnew.serverforclientapp.MainActivity;
import gamal.myappnew.serverforclientapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateShiperFragment extends Fragment {


    public CreateShiperFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_create_shiper, container, false);
        MainActivity.fab.setVisibility(View.GONE);
        return view;
    }

}
