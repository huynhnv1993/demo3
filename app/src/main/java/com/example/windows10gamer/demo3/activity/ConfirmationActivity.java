package com.example.windows10gamer.demo3.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.windows10gamer.demo3.R;
import com.example.windows10gamer.demo3.model.GetBalance;
import com.example.windows10gamer.demo3.model.GetOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class ConfirmationActivity extends AppCompatActivity {
    Button btnbackhome;
    TextView txtphone,txtcardname,txtprice,txtqty,txtdiscount,txttotal,txtbalance,txtcard,txtmessage,txtorderid;
    TableLayout table;
    TableRow row_phone,row_card,row_qty;
    String string,message,type,namecard ="", cardcontent = "";
    JSONObject data,order,item,card;
    JSONArray itemorder,resultcard;
    DecimalFormat decimalFormat;
    double balance, total;
    JSONObject profile;
    JSONArray  partner = null;
    int partner_id=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        profile = MainActivity.profile;
        try {
            partner= new JSONArray(profile.getString("partner_id"));
            partner_id = partner.getInt(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GetBalance myTask = new GetBalance();
        myTask.execute(String.valueOf(partner_id));
        try {
            balance = myTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        GetOrder myTask3 = new GetOrder();
        myTask3.execute(String.valueOf(getIntent().getIntExtra("order_id",0)));
        try {
            JSONObject state = myTask3.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Mapping();
        type = getIntent().getStringExtra("type");
        switch (type){
            case "PREPAIDCARD" :
                table.removeView(row_phone);
                break;
            case "TOPUP" :
                table.removeView(row_qty);
                table.removeView(row_card);
                break;
            default:
                return;
        }
        ProcessBuy();
        ActionButton();
    }

    private void ActionButton() {
        btnbackhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnbackhome.setBackgroundResource(R.drawable.button_yellow_clicked);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("profile", getIntent().getStringExtra("profile"));
                startActivity(intent);
                ConfirmationActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        btnbackhome.setBackgroundResource(R.drawable.button_yellow);
        super.onResume();
    }

    private void ProcessBuy() {

    }

    private void Mapping() {
        decimalFormat = new DecimalFormat("###,###,###");
        txtorderid = (TextView) findViewById(R.id.textView_orderid);
        txtorderid.setText(getIntent().getIntExtra("order_id",0) + "");
        txtmessage = (TextView) findViewById(R.id.textview_message);
        txtphone = (TextView) findViewById(R.id.textView_phone);
        txtphone.setText(getIntent().getStringExtra("phone"));
        txtcardname = (TextView) findViewById(R.id.textView_namecard);
        txtcardname.setText(getIntent().getStringExtra("name"));
        txtprice = (TextView) findViewById(R.id.textView_price);
        txtprice.setText(decimalFormat.format(getIntent().getIntExtra("price",0)) + " đ");
        txtqty = (TextView) findViewById(R.id.textView_qty);
        txtqty.setText(String.valueOf(getIntent().getIntExtra("qty",0)));
        txtdiscount = (TextView) findViewById(R.id.textView_discount);
        Double discount = getIntent().getDoubleExtra("discount",0.00) / 100;
        txtdiscount.setText(getIntent().getDoubleExtra("discount",0.00) + "%");
        txttotal = (TextView) findViewById(R.id.textView_total);
        total = getIntent().getIntExtra("price",0) * getIntent().getIntExtra("qty",0) * (1- discount);
        txttotal.setText(decimalFormat.format(total) + " đ");
        txtbalance = (TextView) findViewById(R.id.textView_balance);
        txtbalance.setText(decimalFormat.format(balance) + " đ");
        txtcard = (TextView) findViewById(R.id.textView_card);
        table = (TableLayout) findViewById(R.id.tableLayout);
        row_card = (TableRow) findViewById(R.id.row_card);
        row_qty = (TableRow) findViewById(R.id.row_qty);
        row_phone = (TableRow) findViewById(R.id.row_phone);
        btnbackhome = (Button) findViewById(R.id.button_backhome);
    }
}
