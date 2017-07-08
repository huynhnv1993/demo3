package com.example.windows10gamer.demo3.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.windows10gamer.demo3.R;
import com.example.windows10gamer.demo3.model.GridCardModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Windows 10 Gamer on 04/07/2017.
 */

public class GridCardAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GridCardModel> arrayList;

    public GridCardAdapter(Context context, ArrayList<GridCardModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.card_price,null);
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        TextView price = (TextView) gridView.findViewById(R.id.price);
        price.setText(decimalFormat.format(arrayList.get(position).getPrice()) + " đ");
        TextView sale_price = (TextView) gridView.findViewById(R.id.sale_price);
        sale_price.setText("Giá: "+decimalFormat.format(arrayList.get(position).getSale_price()) + " đ");
        if(position == 0){
            price.setTextColor(Color.WHITE);
            gridView.setBackgroundResource(R.drawable.border_box_blue);
        }else {
            gridView.setBackgroundResource(R.drawable.border_box);
            price.setTextColor(Color.parseColor("#2d2c2c"));
        }
        return gridView;
    }
}
