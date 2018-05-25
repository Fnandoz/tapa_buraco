package lfernando.tapaburaco.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lfernando.tapaburaco.Buraco;
import lfernando.tapaburaco.BuracoAdapter;
import lfernando.tapaburaco.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListarBuracosFragment extends Fragment {
    List<Buraco> buracoList;
    ListView lista;
    BuracoAdapter adapter;

    private DatabaseReference mDatabase;
    Switch aSwitch;
    android.support.v4.app.FragmentTransaction transaction;
    MapaFragment mapaFragment;


    public ListarBuracosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listar_buracos, container, false);

        lista = view.findViewById(R.id.listaBuracos);
        buracoList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        adapter = new BuracoAdapter(buracoList, getActivity());
        lista.setAdapter(adapter);

        getData();

        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        mapaFragment = new MapaFragment();

        aSwitch = view.findViewById(R.id.listaSwitch);
        aSwitch.setChecked(true);


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentoLayout, mapaFragment);
                }
                transaction.commit();
            }
        });
        return view;
    }

    public void getData() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("LIST", dataSnapshot.child("buracos").getValue().toString());
                HashMap<Object, Object> buracos = (HashMap<Object, Object>) dataSnapshot.child("buracos").getValue();
                Log.d("LIST", "" + buracos.size());
                for (Map.Entry<Object, Object> buraco : buracos.entrySet()) {
                    String key = buraco.getKey().toString();
                    HashMap<String, String> valores = (HashMap<String, String>) buraco.getValue();
                    Buraco b = new Buraco(key, valores.get("descricao"), valores.get("imagem"), Integer.parseInt(valores.get("impacto")),
                            Float.parseFloat(valores.get("lat")), Float.parseFloat(valores.get("lon")));

                    buracoList.add(b);

                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
