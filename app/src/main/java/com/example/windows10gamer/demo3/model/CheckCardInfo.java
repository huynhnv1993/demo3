package com.example.windows10gamer.demo3.model;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * Created by Windows 10 Gamer on 06/07/2017.
 */

public class CheckCardInfo extends AsyncTask<String, Void, JSONObject> {
    static String url = "http://micropay.vn";
    static String db = "2pay";
    JSONObject jsonObject = null;
    int uid =1;
    static String password = "!qwertY777";

    protected JSONObject doInBackground(String... params) {
        final String data = params[0];
        final String login = params[1];
        final String name = params[2];
        Log.d("login",login);
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
                    asList(asList(asList("login","=",login))),
                    new HashMap() {{
                        put("fields", asList( "id", "name","partner_id","street","mobile","balance","email","image_medium"));
                    }}
            )));
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }

        if (record.size()>0){
            jsonObject = new JSONObject((Map) record.get(0));
        }else {
            try {
                final Integer id = (Integer)objClient.execute("execute_kw", asList(
                        db, uid, password,
                        "res.users", "create",
                        asList(new HashMap() {{
                            put("name", name);
                            put("login",login);
                            put("x_dataread", data);
                        }})
                ));
                try {
                    record = asList((Object[])objClient.execute("execute_kw", asList(
                            db, uid, password,
                            "res.users", "search_read",
                            asList(asList(asList("x_dataread","=",data))),
                            new HashMap() {{
                                put("fields", asList( "id", "name","partner_id","street","mobile","balance","email"));
                            }}
                    )));
                } catch (XmlRpcException e) {
                    e.printStackTrace();
                }
                jsonObject = new JSONObject((Map) record.get(0));
            } catch (XmlRpcException e) {
                e.printStackTrace();
            }
        }

        return jsonObject;
    }
}
