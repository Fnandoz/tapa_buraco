package lfernando.tapaburaco;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BuracoAdapter extends BaseAdapter {
    private List<Buraco> buracos;
    private Activity activity;

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
        Log.d("LIST", "getView: "+buraco.getDescricao()+"\n"+buraco.getImpacto());

        assert holder != null;
        if (buraco.getDescricao().isEmpty() || buraco.getDescricao()==null){
            holder.texto1.setText("Buraco");
        }else{
            holder.texto1.setText(buraco.getDescricao());
        }

        holder.texto2.setText(String.valueOf(buraco.getImpacto()));//buraco.getImpacto());

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


