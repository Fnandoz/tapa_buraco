package lfernando.tapaburaco;

import android.app.Activity;
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
        View view = activity.getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);
        Buraco buraco = buracos.get(i);
        TextView texto1 = view.findViewById(R.id.text1);
        TextView texto2 = view.findViewById(R.id.text2);

        texto1.setText(buraco.getDescricao());
        texto2.setText(buraco.getImpacto());

        return view;
    }
}
