package lfernando.tapaburaco;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BuracoAdapter extends BaseAdapter {
    private List<Buraco> buracos;
    private Activity activity;
    Geocoder geocoder;
    List<Address> addresses;

    public BuracoAdapter(List<Buraco> buracos, Activity contexto) {
        this.buracos = buracos;
        this.activity = contexto;
    }

    @Override
    public int getCount() {
        return buracos.size();
    }

    @Override
    public Object getItem(int i) {
        return buracos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder = null;





        if (convertView == null) {
            view = LayoutInflater.from(activity)
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
            holder = new ViewHolder(view);
        } else {
            view = convertView;
        }

        Buraco buraco = buracos.get(i);

        geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(buraco.getLat(), buraco.getLon(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        assert holder != null;
        if (buraco.getDescricao().isEmpty() || buraco.getDescricao()==null){
            holder.texto1.setText("Buraco");
        }else{
            holder.texto1.setText(buraco.getDescricao());
        }

        holder.texto2.setText(address);

        return view;
    }
    static class ViewHolder{
        TextView texto1;
        TextView texto2;

        public ViewHolder(View view){
            texto1 = view.findViewById(android.R.id.text1);
            texto2 = view.findViewById(android.R.id.text2);
        }
    }
}


