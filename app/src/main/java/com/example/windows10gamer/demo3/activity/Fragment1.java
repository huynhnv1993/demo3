package com.example.windows10gamer.demo3.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows10gamer.demo3.R;
import com.example.windows10gamer.demo3.adapter.GridCardAdapter;
import com.example.windows10gamer.demo3.adapter.SpinnerBrandAdapter;
import com.example.windows10gamer.demo3.model.GetCardProduct;
import com.example.windows10gamer.demo3.model.GridCardModel;
import com.example.windows10gamer.demo3.model.SpinnerBrandModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Windows 10 Gamer on 04/07/2017.
 */

public class Fragment1 extends Fragment {
    private int qty = 0,price = 0,Id = 0;
    private double discount = 0;
    private String name = "Viettel";
    Boolean checkbuy = true;
    Button btn_continue1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.activity_fragment1, container, false);

        final GridView gridView;
        gridView = (GridView) view1.findViewById(R.id.gridView);
        final ArrayList<GridCardModel> arrList = new ArrayList<>();
        final GridCardAdapter gridAdapter = new GridCardAdapter(getContext(),arrList);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (gridView!= null && gridView.getChildAt(0) != null){
                    ((TextView) parent.getChildAt(position).findViewById(R.id.price)).setTextColor(Color.WHITE);
                    parent.getChildAt(position).findViewById(R.id.card_price).setBackgroundResource(R.drawable.border_box_blue);
                    for (int i = 0;i<parent.getCount();i++){
                        if (i!=position){
                            parent.getChildAt(i).findViewById(R.id.card_price).setBackgroundResource(R.drawable.border_box);
                            ((TextView) parent.getChildAt(i).findViewById(R.id.price)).setTextColor(Color.parseColor("#2d2c2c"));
                        }
                    }

                }
                price = arrList.get(position).getPrice();
                discount = arrList.get(position).getDiscount();
                Id = arrList.get(position).getId();
            }
        });

        final PrepaidActivity activity = (PrepaidActivity) getActivity();
        final ArrayList<SpinnerBrandModel> list = new ArrayList<>();
        try {
            JSONArray jsonArray = activity.brandCard;
            for (int i = 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = new JSONObject();
                jsonObject = jsonArray.getJSONObject(i);
                GetCardProduct myTask = new GetCardProduct();
                myTask.execute( String.valueOf(jsonObject.getInt("id")));
                try {
                    JSONArray objects = myTask.get();
                    list.add(new SpinnerBrandModel(jsonObject.getString("name"),jsonObject.getString("logo"),jsonObject.getInt("id"),jsonObject.getDouble("discount"),objects,jsonObject.getString("brand_mobile_code")));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Spinner sp = (Spinner) view1.findViewById(R.id.spinner);
        SpinnerBrandAdapter adapter = new SpinnerBrandAdapter(getContext(),R.layout.spinner_rows,R.id.company,list);
        sp.setAdapter(adapter);
        for (int position =0;position<adapter.getCount();position++){
            if (adapter.getItem(position).getId().equals(32) && arrList.size()==0){
                sp.setSelection(position);
            }
        }

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                name = list.get(position).getName();
                JSONArray objects = list.get(position).getProduct();
                arrList.clear();
                for (int i = 0; i<objects.length(); i++){
                    try {
                        JSONObject jsonObject = objects.getJSONObject(i);
                        JSONArray array = jsonObject.getJSONArray("product_variant_ids");
                        arrList.add(new GridCardModel(array.getInt(0),jsonObject.getString("name"),list.get(position).getDiscount(),jsonObject.getInt("discount_price"),jsonObject.getInt("lst_price")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                gridAdapter.notifyDataSetChanged();
                if (arrList.size()>0){
                    checkbuy = true;
                    Id = arrList.get(0).getId();
                    price = arrList.get(0).getPrice();
                    discount = arrList.get(0).getDiscount();
                }
                else{
                    checkbuy =false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0).findViewById(R.id.company)).setTextColor(Color.RED);
            }
        });
        btn_continue1 = (Button) view1.findViewById(R.id.btn_continue1);
        btn_continue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_continue1.setBackgroundResource(R.drawable.button_yellow_clicked);
                if (checkbuy){
                    qty = activity.minteger;
                    Intent intent = new Intent(getContext(),PaymentActivity.class);
                    intent.putExtra("qty",qty);
                    intent.putExtra("id",Id);
                    intent.putExtra("name",name);
                    intent.putExtra("price",price);
                    intent.putExtra("discount",discount);
                    intent.putExtra("type","PREPAIDCARD");
                    intent.putExtra("phone","");
                    Log.d(String.format("%d / %d / %s / %d / %s", qty, Id, name, price, discount), "qty / id / name / price / discount");
                    startActivity(intent);
                }else {
                    btn_continue1.setBackgroundResource(R.drawable.button_yellow);
                    Toast bread = Toast.makeText(activity, "Không có sản phẩm được chọn!", Toast.LENGTH_LONG);
                    bread.show();
                }
            }
        });

        return view1;
    }

    @Override
    public void onResume() {
        super.onResume();
        btn_continue1.setBackgroundResource(R.drawable.button_yellow);
    }
}
