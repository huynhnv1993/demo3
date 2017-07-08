package com.example.windows10gamer.demo3.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.windows10gamer.demo3.R;
import com.example.windows10gamer.demo3.model.CheckCardInfo;
import com.example.windows10gamer.demo3.model.CheckLogin;
import com.example.windows10gamer.demo3.model.CreateOrderLineModel;
import com.example.windows10gamer.demo3.model.CreateOrderModel;
import com.example.windows10gamer.demo3.model.GetBalance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class PaymentActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageView;
    TextView txtbalance;
    TextView txtphone,txtnamecard,txtprice,txtqty,txtdiscount,txttotal;
    TableLayout table;
    TableRow row_phone,row_qty;
    JSONObject profile;
    double balance, total;
    Button btnconfirm;
    Dialog alertbox;
    String type;
    JSONArray data = null;
    JSONArray  partner = null;
    int partner_id=1;
    int order_id = -1;
    int order_line_id = -1;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

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
        Log.d("partner " , String.valueOf(balance));

        Mapping();
        type = getIntent().getStringExtra("type");
        switch (type){
            case "PREPAIDCARD" :
                table.removeView(row_phone);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("type", type);
                    obj.put("cardTypeId", getIntent().getIntExtra("id",0));
                    obj.put("qty", getIntent().getIntExtra("qty",0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                data = new JSONArray();
                data.put(obj);
                Log.d("data", data.toString());
                break;
            case "TOPUP" :
                break;
            default:
                return;
        }
        ActionToolbar();
        ActionButton();
    }

    private void ActionButton() {
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (total > balance){
                    TextView textView1 = (TextView) alertbox.findViewById(R.id.textview_message);
                    TextView textView2 = (TextView) alertbox.findViewById(R.id.title_dialog);
                    textView2.setText("THÔNG BÁO");
                    textView1.setText("Số dư tài khoản không đủ để thực hiện giao dịch!\nVui lòng nạp tiền vào tài khoản!\nCảm ơn!");
                    alertbox.show();
                }else {
                    if (data != null){

                        ActionBuyCard();
                    }
                }
            }
        });
    }

    private void ActionBuyCard() {
//        progressDialog = ProgressDialog.show(PaymentActivity.this, "GIAO DỊCH","Quá trình giao dịch đang thực hiện. Xin chờ trong giây lát.", true);


        CreateOrderModel myTask = new CreateOrderModel();
        myTask.execute(String.valueOf(partner_id));
        try {
            order_id = myTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (order_id != -1){
            CreateOrderLineModel myTask2 = new CreateOrderLineModel();
            myTask2.execute(String.valueOf(order_id), String.valueOf(getIntent().getIntExtra("id",0)), String.valueOf(getIntent().getIntExtra("qty",0)));
            try {
                order_line_id = myTask2.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (order_line_id != -1){
                Intent intent = new Intent(getApplicationContext(),ConfirmationActivity.class);
                intent.putExtra("order_id",order_id);
                intent.putExtra("uid",MainActivity.uid);
                intent.putExtra("profile", String.valueOf(MainActivity.profile));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("discount",getIntent().getDoubleExtra("discount",0.00));
                intent.putExtra("name",getIntent().getStringExtra("name"));
                intent.putExtra("price",getIntent().getIntExtra("price",0));
                intent.putExtra("qty",getIntent().getIntExtra("qty",0));
                intent.putExtra("type",type);
                startActivity(intent);
            }else {
                Toast bread = Toast.makeText(getApplicationContext(), "Lỗi tạo order line", Toast.LENGTH_LONG);
                bread.show();
            }
        }else {
            Toast bread = Toast.makeText(getApplicationContext(), "Lỗi tạo order", Toast.LENGTH_LONG);
            bread.show();
        }


    }

    private void Mapping() {
        alertbox = showDialogcustom();
        table = (TableLayout) findViewById(R.id.tableLayout);
        row_phone = (TableRow)findViewById(R.id.row_phone);
        row_qty = (TableRow) findViewById(R.id.row_qty);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        imageView = (ImageView) findViewById(R.id.image_type);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 150);
//        params.setMargins(10,10,10,10);
//        imageView.setLayoutParams(params);
        txtdiscount = (TextView) findViewById(R.id.textView_discount);
        Double discount = getIntent().getDoubleExtra("discount",0.00) / 100;
        txtdiscount.setText(getIntent().getDoubleExtra("discount",0.00) + "%");
        txtnamecard = (TextView) findViewById(R.id.textView_namecard);
        txtnamecard.setText(getIntent().getStringExtra("name"));
        txtprice = (TextView) findViewById(R.id.textView_price);
        txtprice.setText(decimalFormat.format(getIntent().getIntExtra("price",0)) + " đ");
        txtqty = (TextView) findViewById(R.id.textView_qty);
        txtqty.setText(String.valueOf(getIntent().getIntExtra("qty",0)));
        txtphone = (TextView) findViewById(R.id.textView_phone);
        txtphone.setText(getIntent().getStringExtra("phone"));
        txttotal = (TextView) findViewById(R.id.textView_total);
        total = getIntent().getIntExtra("price",0) * getIntent().getIntExtra("qty",0) * (1- discount);
        txttotal.setText(decimalFormat.format(total) + " đ");
        txtbalance = (TextView) findViewById(R.id.text_balance);

        txtbalance.setText(decimalFormat.format(balance) + " đ");
        btnconfirm = (Button) findViewById(R.id.button_confirm);

    }

    private void ActionToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_payment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Dialog showDialogcustom(){
        final Dialog aDialog = new Dialog(this);
        aDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        aDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        aDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        aDialog.setContentView(R.layout.dialog_custom);
        aDialog.setCancelable(false);
        Button btn_close = (Button) aDialog.findViewById(R.id.close_button);
        Button btn_OK = (Button) aDialog.findViewById(R.id.buttonOK);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aDialog.dismiss();
            }
        });
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aDialog.dismiss();
            }
        });

        return aDialog;
    }
}
