package com.example.windows10gamer.demo3.model;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static java.util.Arrays.asList;

/**
 * Created by Windows 10 Gamer on 05/07/2017.
 */

public class CreateOrderLineModel extends AsyncTask<String, Void, Integer> {
    static String url = "http://micropay.vn";
    static String db = "2pay";
    static int uid = 1;
    static String password = "!qwertY777";
    @Override
    protected Integer doInBackground(String... params) {
        final int order_id = Integer.parseInt(params[0]);
        final int product_id = Integer.parseInt(params[1]);
        final int qty = Integer.parseInt(params[2]);
        final XmlRpcClient objClient = new XmlRpcClient();
        final XmlRpcClientConfigImpl objStartConfig = new XmlRpcClientConfigImpl();
        try {
            objStartConfig.setServerURL(
                    new URL(String.format("%s/xmlrpc/2/object", url)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        objClient.setConfig(objStartConfig);
        Integer order_line_id = -1;
        Log.d("order_id", String.valueOf(order_id));
        Log.d("product_id", String.valueOf(product_id));
        Log.d("qty", String.valueOf(qty));
        try {
            order_line_id = (Integer)objClient.execute("execute_kw", asList(
                    db, uid, password,
                    "sale.order.line", "create",
                    asList(new HashMap() {{
                        put("name", "Order from app Micropay");
                        put("product_id",product_id);
                        put("order_id", order_id);
                        put("product_uom_qty",qty);
                    }})
            ));
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
        return order_line_id;
    }
}
