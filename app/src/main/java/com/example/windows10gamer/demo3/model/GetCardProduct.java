package com.example.windows10gamer.demo3.model;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * Created by Windows 10 Gamer on 04/07/2017.
 */

public class GetCardProduct extends AsyncTask<String, Object, JSONArray> {
    static String url = "http://micropay.vn";
    static String db = "2pay";
    static int uid =1;
    static String password = "!qwertY777";
    JSONArray jsonArray = null;
    @Override
    protected JSONArray doInBackground(String... params) {
        int brand_id = Integer.parseInt(params[0]);
        final XmlRpcClient objClient = new XmlRpcClient();
        final XmlRpcClientConfigImpl objStartConfig = new XmlRpcClientConfigImpl();
        try {
            objStartConfig.setServerURL(
                    new URL(String.format("%s/xmlrpc/2/object", url)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        objClient.setConfig(objStartConfig);

        //Get product
        List record = null;

        try {
            record = asList((Object[])objClient.execute("execute_kw", asList(
                    db, uid, password,
                    "product.template", "search_read",
                    asList(asList(
                            asList("website_published", "=", true),
                            asList("product_brand_id.id","=",brand_id)
                    )),
                    new HashMap() {{
                        put("fields", asList("discount_price","lst_price","name","product_variant_ids"));
                    }}
            )));
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }

        if (record!=null){

            Collections.sort(record, new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    JSONObject j1 = new JSONObject((Map) o1);
                    JSONObject j2 = new JSONObject((Map) o2);
                    try {
                        return j1.getInt("lst_price") - j2.getInt("lst_price") ;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });
            jsonArray = new JSONArray(record);
        }
        return jsonArray;
    }
}
