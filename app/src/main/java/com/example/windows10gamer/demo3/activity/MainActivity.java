package com.example.windows10gamer.demo3.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows10gamer.demo3.R;
import com.example.windows10gamer.demo3.model.GetBalance;
import com.example.windows10gamer.demo3.model.GetBrandProduct;
import com.samilcts.sdk.mpaio.MpaioManager;
import com.samilcts.sdk.mpaio.command.MpaioCommand;
import com.samilcts.sdk.mpaio.error.ResponseError;
import com.samilcts.sdk.mpaio.ext.dialog.RxConnectionDialog;
import com.samilcts.sdk.mpaio.message.MpaioMessage;
import com.samilcts.util.android.Converter;
import com.samilcts.util.android.Logger;
import com.samilcts.util.android.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

import rx.Subscriber;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    public static RxConnectionDialog connectionDialog;
    public static MpaioManager mpaioManager;
    private Logger logger = new Logger();
    private Context mContext;
    private TextView name,phonenumber,txtbalance,uname,uaddress;
    double balance;
    ProgressDialog progressDialog;
    String cardphone,image_person ="";
    public static JSONObject profile=null;
    ImageView imgCard,imgBarcode,img_Baohiem,img_thanhtoandien,img_chuyentien,img_thanhtoaninternet,img_muavexemphim,img_person;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    Dialog alertbox;

    JSONArray  partner = null;
    int partner_id=1;
    public static int uid=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("mpaio1", String.valueOf(mpaioManager));
        mpaioManager = LoginActivity.mpaioManager;
        connectionDialog = new RxConnectionDialog(this, mpaioManager);

        try {
            profile = new JSONObject(getIntent().getStringExtra("profile"));
            uid = profile.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("profile", String.valueOf(profile));
        Log.d("uid", String.valueOf(uid));
        Mapping();
        ActionImage();

    }

    private void ActionImage() {
        imgCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCard.setBackgroundColor(Color.parseColor("#dedede"));
                Intent intent = new Intent(getApplicationContext(),PrepaidActivity.class);
                startActivity(intent);
            }
        });

        imgBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mpaioManager.rxSyncRequest(mpaioManager.getNextAid(), new MpaioCommand(MpaioCommand.READ_BARCODE).getCode(), new byte[0])
                            .subscribe(); //Command for activating Barcode Mode
                }catch (NumberFormatException e) {
                    ToastUtil.show(getApplicationContext(), "Input valid number");
                    e.printStackTrace();
                }
            }
        });

        img_Baohiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView1 = (TextView) alertbox.findViewById(R.id.textview_message);
                TextView textView2 = (TextView) alertbox.findViewById(R.id.title_dialog);
                textView2.setText("THÔNG BÁO");
                textView1.setText("Dịch vụ đang phát triển!\nCảm ơn!");
                alertbox.show();
            }
        });
        img_thanhtoandien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView1 = (TextView) alertbox.findViewById(R.id.textview_message);
                TextView textView2 = (TextView) alertbox.findViewById(R.id.title_dialog);
                textView2.setText("THÔNG BÁO");
                textView1.setText("Dịch vụ đang phát triển!\nCảm ơn!");
                alertbox.show();
            }
        });
        img_chuyentien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView1 = (TextView) alertbox.findViewById(R.id.textview_message);
                TextView textView2 = (TextView) alertbox.findViewById(R.id.title_dialog);
                textView2.setText("THÔNG BÁO");
                textView1.setText("Dịch vụ đang phát triển!\nCảm ơn!");
                alertbox.show();
            }
        });
        img_thanhtoaninternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView1 = (TextView) alertbox.findViewById(R.id.textview_message);
                TextView textView2 = (TextView) alertbox.findViewById(R.id.title_dialog);
                textView2.setText("THÔNG BÁO");
                textView1.setText("Dịch vụ đang phát triển!\nCảm ơn!");
                alertbox.show();
            }
        });
        img_muavexemphim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView1 = (TextView) alertbox.findViewById(R.id.textview_message);
                TextView textView2 = (TextView) alertbox.findViewById(R.id.title_dialog);
                textView2.setText("THÔNG BÁO");
                textView1.setText("Dịch vụ đang phát triển!\nCảm ơn!");
                alertbox.show();
            }
        });

    }

    private void Mapping() {
        alertbox = showDialogcustom();
        img_Baohiem = (ImageView) findViewById(R.id.image_baohiem);
        img_thanhtoandien = (ImageView) findViewById(R.id.image_diennuoc);
        img_chuyentien = (ImageView) findViewById(R.id.image_chuyentien);
        img_thanhtoaninternet = (ImageView) findViewById(R.id.image_internet);
        img_muavexemphim = (ImageView) findViewById(R.id.image_vexemphim);
        imgBarcode = (ImageView) findViewById(R.id.image_barcode);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Trang chủ");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        View header=navigationView.getHeaderView(0);
        uname = (TextView) header.findViewById(R.id.txtname);
        uaddress = (TextView) header.findViewById(R.id.textView);
        img_person = (ImageView) header.findViewById(R.id.imageView);


        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        imgCard = (ImageView) findViewById(R.id.image_prepaid);
        name = (TextView) findViewById(R.id.text_user);
        phonenumber = (TextView) findViewById(R.id.text_phone);
        txtbalance = (TextView) findViewById(R.id.text_balance);

        try {
            image_person = profile.getString("image_medium");
            name.setText(profile.getString("name"));
            uname.setText(profile.getString("name"));
            if (profile.getString("mobile")!="false"){
                phonenumber.setText(profile.getString("mobile"));
            }else if (profile.getString("email")!="false"){
                phonenumber.setText(profile.getString("email"));
            }else {phonenumber.setText("");}

            if (profile.getString("email")!="false"){
                uaddress.setText(profile.getString("email"));
            }else {
                uaddress.setText(profile.getString("phone"));
            }
            partner= new JSONArray(profile.getString("partner_id"));
            partner_id = partner.getInt(0);

            txtbalance.setText(decimalFormat.format(profile.getDouble("balance")) + " đ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (image_person != null){
            byte[] decodedString = Base64.decode(image_person, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            img_person.setImageBitmap(decodedByte);
        }
        GetBalance myTask2 = new GetBalance();
        myTask2.execute(String.valueOf(partner_id));
        try {
            balance = myTask2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if ( mpaioManager != null) {
            boolean isConnected = mpaioManager.isConnected();
            MenuItem item = menu.findItem(R.id.action_connect);
            item.setTitle(isConnected ? "Disconnect" : "Connect");
            return true;
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_connect:
                if ( mpaioManager.isConnected()) {
                    mpaioManager.disconnect();
                    return true;
                } else {
                    connectionDialog.show();
                }
                break;
            case R.id.action_settings:
                break;
            case R.id.action_logout:

                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                MainActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        imgCard.setBackgroundResource(R.drawable.border_box);
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

