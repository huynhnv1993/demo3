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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows10gamer.demo3.R;
import com.example.windows10gamer.demo3.adapter.GridCardAdapter;
import com.example.windows10gamer.demo3.model.GetCardProduct;
import com.example.windows10gamer.demo3.model.GridCardModel;
import com.example.windows10gamer.demo3.model.SpinnerBrandModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Windows 10 Gamer on 04/07/2017.
 */

public class Fragment2 extends Fragment {
    private int qty = 1,price = 0,Id = 0;
    private double discount = 0;
    private String name = "Viettel";
    Boolean checkbuy = true;
    private String phone,uname;
    TextView txtname,txtphone;
    ImageView imageView;
    Button btn_continue1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.activity_fragment2, container, false);
        final PrepaidActivity activity = (PrepaidActivity) getActivity();
        try {
            uname = MainActivity.profile.getString("name");
            phone = MainActivity.profile.getString("mobile");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (activity.contactnumber!=null){
            uname = activity.contactname;
            phone = activity.contactnumber;
        }

        txtname = (TextView) view1.findViewById(R.id.text_name);
        txtname.setText(uname);
        txtphone = (TextView) view1.findViewById(R.id.text_phone);
        txtphone.setText(phone);
        imageView = (ImageView) view1.findViewById(R.id.image_contact);
        imageView.setBackgroundResource(R.drawable.border_box);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setBackgroundColor(Color.parseColor("#b43d2d2d"));
                Intent intent = new Intent(getContext(),AllContactActivity.class);
                startActivity(intent);
            }
        });


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


        final ArrayList<SpinnerBrandModel> list = new ArrayList<>();
        try {
            JSONArray jsonArray = activity.brandTopup;
            for (int i = 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = new JSONObject();
                jsonObject = jsonArray.getJSONObject(i);
                GetCardProduct myTask = new GetCardProduct();
                myTask.execute( String.valueOf(jsonObject.getInt("id")));
                try {
                    JSONArray objects = myTask.get();
                    list.add(new SpinnerBrandModel(jsonObject.getString("name"),jsonObject.getString("logo"),jsonObject.getInt("id"),jsonObject.getDouble("discount"),objects,jsonObject.getString("brand_mobile_code")));
//                    Log.d("brand_mobile_code",jsonObject.getString("brand_mobile_code"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i<list.size();i++){
            String []strings = list.get(i).getIdNumber().replaceAll("\\s+", "").split(",");
            String check = phone.substring(0,4);

            for (int j = 0;j<strings.length;j++){
                if (check.startsWith(strings[j])){
                    JSONArray objects = list.get(i).getProduct();
                    name = list.get(i).getName();
                    arrList.clear();
                    for (int k = 0; k<objects.length(); k++){
                        try {
                            JSONObject jsonObject = objects.getJSONObject(k);
                            JSONArray array = jsonObject.getJSONArray("product_variant_ids");
                            arrList.add(new GridCardModel(array.getInt(0),jsonObject.getString("name"),list.get(i).getDiscount(),jsonObject.getInt("discount_price"),jsonObject.getInt("lst_price")));
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
            }
        }


        btn_continue1 = (Button) view1.findViewById(R.id.btn_continue1);
        btn_continue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_continue1.setBackgroundResource(R.drawable.button_yellow_clicked);
                if (checkbuy){
                    qty = activity.minteger;
                    Intent intent = new Intent(getContext(),PaymentActivity.class);
                    intent.putExtra("phone",phone);
                    intent.putExtra("qty",qty);
                    intent.putExtra("id",Id);
                    intent.putExtra("name",name);
                    intent.putExtra("price",price);
                    intent.putExtra("discount",discount);
                    intent.putExtra("type","TOPUP");
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
        btn_continue1.setBackgroundResource(R.drawable.button_yellow);
        imageView.setBackgroundResource(R.drawable.border_box);
        super.onResume();
    }
}
