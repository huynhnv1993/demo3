package com.example.windows10gamer.demo3.model;

import android.os.AsyncTask;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * Created by Windows 10 Gamer on 07/07/2017.
 */

public class GetBalance extends AsyncTask<String, Object, Integer> {
    static String url = "http://micropay.vn";
    static String db = "2pay";
    static int uid =1;
    static String password = "!qwertY777";
    Integer result = -1;
    @Override
    protected Integer doInBackground(String... params) {
        int partner_id = Integer.parseInt(params[0]);
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
                    "res.partner", "search_read",
                    asList(asList(asList("id","=",partner_id))),
                    new HashMap() {{
                        put("fields", asList( "balance"));
                    }}
            )));
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }

        if (record.size()>0){
            JSONObject jsonObject = new JSONObject((Map) record.get(0));
            try {
                result = jsonObject.getInt("balance");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return result;

    }
}
