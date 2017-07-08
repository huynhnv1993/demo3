package com.example.windows10gamer.demo3.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows10gamer.demo3.R;
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
    private RxConnectionDialog connectionDialog;
    private MpaioManager mpaioManager;
    private Logger logger = new Logger();
    private Context mContext;

    private TextView name,phonenumber,balance;
    ProgressDialog progressDialog;
    String cardphone;
    public static JSONObject profile=null;
    ImageView imgCard;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    JSONArray brand = null;
    public static int uid=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mpaioManager = new MpaioManager(getApplicationContext());
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
                progressDialog = ProgressDialog.show(MainActivity.this, "LOAD DATA","Quá trình LOAD sẽ kết thúc sau 1 tiếng =]]].", true);
                GetBrandProduct myTask2 = new GetBrandProduct();
                myTask2.execute("MobileCard");
                try {
                    brand = myTask2.get();
                    Log.d("brand ", String.valueOf(brand));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (brand!=null){
                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(),PrepaidActivity.class);
                    intent.putExtra("brandcard", String.valueOf(brand));
                    startActivity(intent);
                }else {
                    progressDialog.dismiss();
                    Toast bread = Toast.makeText(getApplicationContext(), "Không có dữ liệu", Toast.LENGTH_LONG);
                    bread.show();
                }
            }
        });
    }

    private void Mapping() {
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

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        imgCard = (ImageView) findViewById(R.id.image_prepaid);
        name = (TextView) findViewById(R.id.text_user);
        phonenumber = (TextView) findViewById(R.id.text_phone);
        balance = (TextView) findViewById(R.id.text_balance);
        try {
            name.setText(profile.getString("name"));
            if (profile.getString("mobile")!="false"){
                phonenumber.setText(profile.getString("mobile"));
            }else{
                phonenumber.setText(profile.getString("email"));
            }

            balance.setText(decimalFormat.format(profile.getDouble("balance")) + " đ");
        } catch (JSONException e) {
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
                Log.d("code", new MpaioCommand(MpaioCommand.READ_MS_CARD).getCode().toString());
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

    private Subscriber<MpaioMessage> getMessageSubscriber() {

        return new Subscriber<MpaioMessage>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                e.printStackTrace();
                String msg = null == e.getMessage() ? e.toString() : e.getMessage();
                ToastUtil.show(mContext, "ERROR : " + msg);
            }

            @Override
            public void onNext(MpaioMessage mpaioMessage) {

                byte[] data = mpaioMessage.getData();

                if (ResponseError.fromCode(data[0]) == ResponseError.NO_ERROR) {
                    //response ok
                }


                logger.i(TAG, "AID : " + Converter.toInt(mpaioMessage.getAID())
                        + " CMD : " + Converter.toHexString(mpaioMessage.getCommandCode())
                        + " Data : " + Converter.toHexString((byte[]) mpaioMessage.getData()));

                ToastUtil.show(mContext, "received data part : " + Converter.toHexString(data));
                ToastUtil.show(mContext, "(string) : " + (data == null ? "" : new String(data)));

            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if ( null != connectionDialog && connectionDialog.isShowing()) {
            //this is for updating connection dialog state.
            connectionDialog.onRequestPermissionResult(requestCode, permissions, grantResults);
        }

    }
}

