package com.example.windows10gamer.demo3.model;

import android.os.AsyncTask;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.json.JSONArray;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by Windows 10 Gamer on 04/07/2017.
 */

public class GetBrandProduct extends AsyncTask<String, Object, JSONArray> {
    static String url = "http://micropay.vn";
    static String db = "2pay";
    static int uid =1;
    static String password = "!qwertY777";
    JSONArray jsonArray = null;
//    Return brand [{id,logo,name}]
    @Override
    protected JSONArray doInBackground(String... params) {
        String brand_type = params[0];
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
                    "product.brand", "search_read",
                    asList(asList(asList("x_brand_type","=",brand_type))),
                    new HashMap() {{
                        put("fields", asList( "discount","logo","name"));
                    }}
            )));
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }

        if (record!=null){
            jsonArray = new JSONArray(record);
        }
        return jsonArray;

    }
}
