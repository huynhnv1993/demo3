package com.example.windows10gamer.demo3.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.windows10gamer.demo3.R;
import com.example.windows10gamer.demo3.model.SpinnerBrandModel;

import java.util.ArrayList;

/**
 * Created by Windows 10 Gamer on 04/07/2017.
 */

public class SpinnerBrandAdapter extends ArrayAdapter<SpinnerBrandModel> {
    int groupid;
    Context context;
    ArrayList<SpinnerBrandModel> list;
    LayoutInflater inflater;

    public SpinnerBrandAdapter(Context context, int groupid, int id, ArrayList<SpinnerBrandModel> list){
        super(context,id,list);
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid = groupid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(groupid,parent,false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.image_companylogo);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 150);
//        imageView.setLayoutParams(params);
        byte[] decodedString = Base64.decode(list.get(position).getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
        TextView company = (TextView) itemView.findViewById(R.id.company);
        TextView sub = (TextView) itemView.findViewById(R.id.sub);
        company.setText(list.get(position).getName());
        return itemView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position,convertView,parent);
    }
}
