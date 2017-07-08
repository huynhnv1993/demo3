package com.example.windows10gamer.demo3.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows10gamer.demo3.R;
import com.example.windows10gamer.demo3.model.CheckCardInfo;
import com.example.windows10gamer.demo3.model.CheckLogin;
import com.samilcts.media.State;
import com.samilcts.sdk.mpaio.MpaioManager;
import com.samilcts.sdk.mpaio.command.MpaioCommand;
import com.samilcts.sdk.mpaio.error.ResponseError;
import com.samilcts.sdk.mpaio.ext.dialog.RxConnectionDialog;
import com.samilcts.sdk.mpaio.message.MpaioMessage;
import com.samilcts.util.android.Converter;
import com.samilcts.util.android.Logger;
import com.samilcts.util.android.ToastUtil;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import rx.Subscriber;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "LoginActivity";
    private RxConnectionDialog connectionDialog;
    private MpaioManager mpaioManager;
    private Logger logger = new Logger();
    private Context mContext;
    private String savedata="";
    private Dialog alertbox,alertboxsign;

    private static String username;
    private static String password;
    private Button ok,checkCard;
    private EditText editTextUsername,editTextPassword;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    ProgressDialog progressDialog;
    JSONObject objects;
    Toolbar toolbar;
    String data="123456";
    public LoginActivity() throws ExecutionException, InterruptedException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View rootView = ((Activity)LoginActivity.this).getWindow().getDecorView().findViewById(android.R.id.content);

        mContext = getApplicationContext();
        mpaioManager = new MpaioManager(getApplicationContext());
        connectionDialog = new RxConnectionDialog(this, mpaioManager);
        alertbox = showDialogcustom();

        if ( !mpaioManager.isConnected()) {
            connectionDialog.show();
        }
        ok = (Button)findViewById(R.id.btnLogin);
        ok.setOnClickListener(this);
        checkCard = (Button)findViewById(R.id.btnLoginCard);
        checkCard.setOnClickListener(this);
        editTextUsername = (EditText)findViewById(R.id.edtUserName);
        editTextPassword = (EditText)findViewById(R.id.edtPassword);
        saveLoginCheckBox = (CheckBox)findViewById(R.id.checkBoxSave);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            editTextUsername.setText(loginPreferences.getString("username", ""));
            editTextPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        mpaioManager.onReceived()
                .subscribe(new Subscriber<byte[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(byte[] bytes) {

                    }
                });

        mpaioManager.onStateChanged()
                .subscribe(new Subscriber<State>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(State state) {
                        invalidateOptionsMenu();
                        ToastUtil.show(mContext, "state changed : " + state.getValue());
                    }
                });
        mpaioManager.onReadMsCard()//Receive notify of MS card data
                .mergeWith(mpaioManager.onReadEmvCard())
                .mergeWith(mpaioManager.onReadRfidCard())
                .subscribe(new Subscriber<MpaioMessage>() {
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
                        byte[] data1 = new byte[0];
                        logger.i(TAG, "AID : " + Converter.toInt(mpaioMessage.getAID())
                                + " CMD : " + Converter.toHexString(mpaioMessage.getCommandCode())
                                + " Data : " + Converter.toHexString(mpaioMessage.getData()));
                        if (data!=null){
                            savedata = Converter.toHexString(data);
                            data1 = data;
                        }
                        try {
                            mpaioManager.rxSyncRequest(mpaioManager.getNextAid(), new MpaioCommand(MpaioCommand.STOP).getCode(), new byte[0])
                                    .subscribe(getMessageSubscriber());
                        }catch (NumberFormatException e) {
                            ToastUtil.show(getApplicationContext(), "Error STOP");
                            e.printStackTrace();
                        }

                        Log.d("senddata2" , savedata);
                        Log.d("senddata2" , new String(data));
                        String s = new String(data1);
                        String[] sArray = s.split("\\^")[1].split("\\s{2,}");
                        String login= s.split("\\^")[0].split("B")[1];
                        String name = sArray[0];
                        if (alertbox.isShowing()){
                            alertbox.dismiss();
                        }
                        CheckCardInfo myTask = new CheckCardInfo();
                        myTask.execute(String.valueOf(savedata),login,name);
                        try {
                            objects = myTask.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        Log.d("partner " , String.valueOf(objects));
                        if (objects != null){
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("profile", String.valueOf(objects));
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }else {
                            ToastUtil.show(getApplicationContext(),"Chưa có tài khoản!",5000);
                        }


                    }
                });
        mpaioManager
                .onBarcodeRead()
//                .mergeWith(mpaioManager.onReadMsCard())
//                .mergeWith(mpaioManager.onReadEmvCard())
//                .mergeWith(mpaioManager.onReadRfidCard())
                .mergeWith(mpaioManager.onPressPinPad())
                .mergeWith(mpaioManager.onNotifyPrepaidTransaction())
                .mergeWith(mpaioManager.onReadPrepaidTransactionLog())
                .subscribe(new Subscriber<MpaioMessage>() {
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

                        logger.i(TAG, "AID : " + Converter.toInt(mpaioMessage.getAID())
                                + " CMD : " + Converter.toHexString(mpaioMessage.getCommandCode())
                                + " Data : " + Converter.toHexString(mpaioMessage.getData()));

                        ToastUtil.show(mContext, "notify. data part : " + Converter.toHexString(data) );
                        ToastUtil.show(mContext, "(string) : " + (data == null ? "" : new String(data)));
                    }
                });
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
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        if (view == ok) {
            progressDialog = ProgressDialog.show(LoginActivity.this, "ĐĂNG NHẬP",
                    "Quá trình đăng nhập sẽ kết thúc sau 1 tiếng =]]].", true);

            username = editTextUsername.getText().toString();
            password = editTextPassword.getText().toString();

            if (saveLoginCheckBox.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", username);
                loginPrefsEditor.putString("password", password);
                loginPrefsEditor.commit();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }
            doSomethingElse();
        }else if (view == checkCard){
            doCheckCard(view);
        }
    }

    private void doCheckCard(View v) {
        if ( !mpaioManager.isConnected()) {
            connectionDialog.show();
        }else {


            TextView textView1 = (TextView) alertbox.findViewById(R.id.textview_message);
            TextView textView2 = (TextView) alertbox.findViewById(R.id.title_dialog);
            textView1.setText("INSERT CARD");
            textView2.setText("CARD");
            alertbox.show();
            try {
                mpaioManager.rxSyncRequest(mpaioManager.getNextAid(), new MpaioCommand(MpaioCommand.READ_MS_CARD).getCode(), new byte[0])
                        .subscribe(); //Command for activation MS card mode
                mpaioManager.rxSyncRequest(mpaioManager.getNextAid(), new MpaioCommand(MpaioCommand.READ_EMV_CARD).getCode(), new byte[0])
                        .subscribe(); //Command for activating EMV card mode
                mpaioManager.rxSyncRequest(mpaioManager.getNextAid(), new MpaioCommand(MpaioCommand.READ_RFID_CARD).getCode(),null)
                        .subscribe(); //Command for activating RFID card mode
            }catch (NumberFormatException e) {
                ToastUtil.show(getApplicationContext(), "Input valid number");
                e.printStackTrace();
            }
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);



        }
    }

    public void doSomethingElse() {
        CheckLogin myTask = new CheckLogin();
        myTask.execute(username, password);
        try {
            objects = myTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.d("partner " , String.valueOf(objects));
        if (objects != null){
            progressDialog.dismiss();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("password",password);
            intent.putExtra("profile", String.valueOf(objects));
            startActivity(intent);
            LoginActivity.this.finish();
        }else {
            progressDialog.dismiss();
            Toast bread = Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra lại tài khoản", Toast.LENGTH_LONG);
            bread.show();
        }



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
        btn_OK.setVisibility(View.INVISIBLE);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpaioManager.rxSyncRequest(mpaioManager.getNextAid(), new MpaioCommand(MpaioCommand.STOP).getCode(), new byte[0])
                        .subscribe(getMessageSubscriber());
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
