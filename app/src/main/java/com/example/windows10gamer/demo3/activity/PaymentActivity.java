package com.example.windows10gamer.demo3.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows10gamer.demo3.R;
import com.example.windows10gamer.demo3.model.CreateOrderModel;
import com.example.windows10gamer.demo3.model.GetBalance;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static java.util.Arrays.asList;

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
    public ProgressDialog progressDialog;
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
        Log.d("balance " , String.valueOf(balance));

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
                table.removeView(row_qty);
                JSONObject obj1 = new JSONObject();
                try {
                    obj1.put("type", type);
                    obj1.put("cardTypeId", getIntent().getIntExtra("id",0));
                    obj1.put("phone", getIntent().getStringExtra("phone"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                data = new JSONArray();
                data.put(obj1);
                Log.d("data", data.toString());
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
                btnconfirm.setBackgroundResource(R.drawable.button_yellow_clicked);
                if (total > balance){
                    TextView textView1 = (TextView) alertbox.findViewById(R.id.textview_message);
                    TextView textView2 = (TextView) alertbox.findViewById(R.id.title_dialog);
                    textView2.setText("THÔNG BÁO");
                    textView1.setText("Số dư tài khoản không đủ để thực hiện giao dịch!\nVui lòng nạp tiền vào tài khoản!\nCảm ơn!");
                    alertbox.show();
                    btnconfirm.setBackgroundResource(R.drawable.button_yellow);
                }else {
                    if (data != null){
                        progressDialog = ProgressDialog.show(PaymentActivity.this, "GIAO DỊCH", "Quá trình giao dịch đang thực hiện. Xin chờ trong giây lát.", true);
                        new Thread() {
                            public void run() {
                                try {
                                    ActionBuyCard();
                                } catch (Exception e) {
                                }
                                progressDialog.dismiss();
                            }
                        }.start();
                        btnconfirm.setBackgroundResource(R.drawable.button_yellow);
                    }else {
                        btnconfirm.setBackgroundResource(R.drawable.button_yellow);
                        Log.d("Error","Loi data");
                    }
                }
            }
        });
    }

    private void ActionBuyCard() {
        CreateOrderModel myTask = new CreateOrderModel();
        myTask.execute(String.valueOf(partner_id),String.valueOf(getIntent().getIntExtra("id",0)), String.valueOf(getIntent().getIntExtra("qty",0)),String.valueOf(getIntent().getStringExtra("phone")));
        try {
            order_id = myTask.get();
            if (order_id != -1){
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
                intent.putExtra("phone",getIntent().getStringExtra("phone"));
                startActivity(intent);
            }else {
                Toast bread = Toast.makeText(getApplicationContext(), "Lỗi tạo order", Toast.LENGTH_LONG);
                bread.show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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
