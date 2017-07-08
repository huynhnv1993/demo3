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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class ConfirmationActivity extends AppCompatActivity {
    Button btnbackhome;
    TextView txtphone,txtcardname,txtprice,txtqty,txtdiscount,txttotal,txtbalance,txtcard,txtmessage,txtorderid;
    TableLayout table;
    TableRow row_phone,row_card,row_qty;
    String string,message,type,namecard ="", cardcontent = "";
    JSONObject data,order,item,card;
    JSONArray itemorder,resultcard;
    DecimalFormat decimalFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Mapping();
        ProcessBuy();
        ActionButton();
    }

    private void ActionButton() {
        btnbackhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("profile", getIntent().getStringExtra("profile"));
                startActivity(intent);
                ConfirmationActivity.this.finish();
            }
        });
    }

    private void ProcessBuy() {

    }

    private void Mapping() {
        decimalFormat = new DecimalFormat("###,###,###");
        txtorderid = (TextView) findViewById(R.id.textView_orderid);
        txtmessage = (TextView) findViewById(R.id.textview_message);
        txtphone = (TextView) findViewById(R.id.textView_phone);
        txtcardname = (TextView) findViewById(R.id.textView_namecard);
        txtprice = (TextView) findViewById(R.id.textView_price);
        txtqty = (TextView) findViewById(R.id.textView_qty);
        txtdiscount = (TextView) findViewById(R.id.textView_discount);
        txttotal = (TextView) findViewById(R.id.textView_total);
        txtbalance = (TextView) findViewById(R.id.textView_balance);
        txtcard = (TextView) findViewById(R.id.textView_card);
        table = (TableLayout) findViewById(R.id.tableLayout);
        row_card = (TableRow) findViewById(R.id.row_card);
        row_qty = (TableRow) findViewById(R.id.row_qty);
        row_phone = (TableRow) findViewById(R.id.row_phone);
        btnbackhome = (Button) findViewById(R.id.button_backhome);
    }
}
