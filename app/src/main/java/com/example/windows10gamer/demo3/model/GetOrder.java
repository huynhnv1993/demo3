package com.example.windows10gamer.demo3.model;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * Created by Windows 10 Gamer on 12/07/2017.
 */

public class GetOrder extends AsyncTask<String, Object, JSONObject> {
    static String url = "http://micropay.vn";
    static String db = "2pay";
    static int uid =1;
    static String password = "!qwertY777";
    String state = "";
    JSONObject jsonObject = null;
    @Override
    protected JSONObject doInBackground(String... params) {
        final int order_id = Integer.parseInt(params[0]);
        final XmlRpcClient objClient = new XmlRpcClient();
        final XmlRpcClientConfigImpl objStartConfig = new XmlRpcClientConfigImpl();
        try {
            objStartConfig.setServerURL(
                    new URL(String.format("%s/xmlrpc/2/object", url)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        objClient.setConfig(objStartConfig);
        List record = null;

        try {
            record = asList((Object[])objClient.execute("execute_kw", asList(
                    db, uid, password,
                    "sale.order", "search_read",
                    asList(asList(asList("id","=",order_id))),
                    new HashMap() {{
                        put("fields", asList( "state","order_line"));
                    }}
            )));
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }

        if (record!=null){
            jsonObject = new JSONObject((Map) record.get(0));
        }
        Log.d("state", String.valueOf(jsonObject));
        return jsonObject;
    }
}
