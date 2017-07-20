package com.example.windows10gamer.demo3.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.windows10gamer.demo3.activity.PaymentActivity;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by Windows 10 Gamer on 05/07/2017.
 */

public class CreateOrderModel extends AsyncTask<String, Void, Integer> {
    String url = "http://micropay.vn";
    String db = "2pay";
    int uid = 1;
    String password = "!qwertY777";

    @Override
    protected Integer doInBackground(String... params) {
        final int partner_id = Integer.parseInt(params[0]);
        final int product_id = Integer.parseInt(params[1]);
        final int qty = Integer.parseInt(params[2]);
        final String phone = params[3];
        final XmlRpcClient objClient = new XmlRpcClient();
        final XmlRpcClientConfigImpl objStartConfig = new XmlRpcClientConfigImpl();
        try {
            objStartConfig.setServerURL(
                    new URL(String.format("%s/xmlrpc/2/object", url)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        objClient.setConfig(objStartConfig);
        Integer order_id = -1;

        final Object[] list = new Object[3];
        list[0]=0;
        list[1]=0;
        list[2]= new HashMap() {{ put("product_id", product_id);put("product_uom_qty", qty);put("target", phone); }};

        Log.d("orderxx", String.valueOf(asList(list)));
        try {
            order_id = (Integer)objClient.execute("execute_kw", asList(
                    db, uid, password,
                    "sale.order", "create",
                    asList(new HashMap() {{
                        put("partner_id", partner_id);
                        put("order_line",asList(asList(list)));
                    }})
            ));
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return order_id;
    }

    @Override
    protected void onPostExecute(Integer integer) {

        super.onPostExecute(integer);
        Log.d("progressLog","logvvv");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("progressLog","log");

    }
}