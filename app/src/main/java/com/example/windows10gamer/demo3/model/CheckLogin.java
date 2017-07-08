package com.example.windows10gamer.demo3.model;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * Created by Windows 10 Gamer on 04/07/2017.
 */

public class CheckLogin extends AsyncTask<String, Void, JSONObject> {
    static String url = "http://micropay.vn";
    static String db = "2pay";
    JSONObject jsonObject = null;
//    static String username ="huynh.nguyen@bluecom.vn";
//    static String password = "sunbop101210";

    protected JSONObject doInBackground(String... params) {
        String username = params[0];
        String password = params[1];
        final XmlRpcClient authClient = new XmlRpcClient();
        final XmlRpcClientConfigImpl authStartConfig = new XmlRpcClientConfigImpl();
        try {
            authStartConfig.setServerURL(
                    new URL(String.format("%s/xmlrpc/2/common", url)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List configList = new ArrayList();
        Map paramMap = new HashMap();

        configList.add(db);
        configList.add(username);
        configList.add(password);
        configList.add(paramMap);

        int uid = -1;
        Object object = null;
        try {
            object= authClient.execute(
                    authStartConfig, "authenticate", configList);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
        if (object.equals(false)){

        }else {
            uid = (int) object;
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
                        "res.users", "search_read",
                        asList(asList(asList("id","=",uid))),
                        new HashMap() {{
                            put("fields", asList( "id", "name","partner_id","street","mobile","balance","email"));
                        }}
                )));
            } catch (XmlRpcException e) {
                e.printStackTrace();
            }

            if (record!=null){
                jsonObject = new JSONObject((Map) record.get(0));
            }
        }



        return jsonObject;
    }
}
