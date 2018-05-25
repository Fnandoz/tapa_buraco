package lfernando.tapaburaco.Fragments;


import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
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
import lfernando.tapaburaco.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends Fragment{
    List<Buraco> buracoList;
    private DatabaseReference mDatabase;
    GoogleMap gMap;

    MapView mapView;
    Switch aSwitch;
    android.support.v4.app.FragmentTransaction transaction;
    ListarBuracosFragment listaFragment;

    Location currentLocation = null;
    public MapaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        transaction = getActivity().getSupportFragmentManager().beginTransaction();


        mapView = view.findViewById(R.id.mapaBuracosView);
        aSwitch = view.findViewById(R.id.mapaSwitch);
        aSwitch.setChecked(false);
        listaFragment = new ListarBuracosFragment();

        buracoList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        getData();

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentoLayout, listaFragment);
                }
                //transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                gMap = googleMap;
                gMap.setMyLocationEnabled(true);
                LatLng belem = new LatLng(-1.385851, -48.4614937);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(belem, 11));

                gMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        currentLocation = googleMap.getMyLocation();
                        LatLng meuLocal = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(meuLocal, 17));

                        return true;
                    }
                });

                gMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                    @Override
                    public void onMyLocationClick(@NonNull Location location) {
                        currentLocation = location;
                        Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
                    }
                });



            }
        });


        mapView.onCreate(savedInstanceState);
        mapView.onResume();
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
                    Marker marker = gMap.addMarker(new MarkerOptions().position(new LatLng(b.getLat(), b.getLon())).title(b.getDescricao()));
                    marker.setTag(b.getId());

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
