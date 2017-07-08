package com.example.windows10gamer.demo3.model;

import android.os.AsyncTask;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by Windows 10 Gamer on 05/07/2017.
 */

public class CreateOrderModel extends AsyncTask<String, Void, Integer> {
    static String url = "http://micropay.vn";
    static String db = "2pay";
    static int uid = 1;
    static String password = "!qwertY777";
    @Override
    protected Integer doInBackground(String... params) {
        final int partner_id = Integer.parseInt(params[0]);
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
        try {
            order_id = (Integer)objClient.execute("execute_kw", asList(
                    db, uid, password,
                    "sale.order", "create",
                    asList(new HashMap() {{ put("partner_id", partner_id); }})
            ));
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
        return order_id;
    }
}
