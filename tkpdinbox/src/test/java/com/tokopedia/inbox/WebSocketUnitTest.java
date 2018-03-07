package com.tokopedia.inbox;

import com.tokopedia.core.network.constants.TkpdBaseURL;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by Hendri on 13/02/18.
 */

public class WebSocketUnitTest {
    WebSocket webSocket;
    CountDownLatch countDownLatch;
    WebListener wl;

    String json = "{\"code\":103,\"data\":{\"message_id\":60166378,\"message\":\" Bisa dikirim hari ini ga???? gmn sih tong?? \",\"start_time\":\"2018-02-13T05:55:16.466Z\"}}";
    @Test
    public void testCallWebSocket() throws Exception{
        wl = new WebListener();
//       OkHttpClient client = new OkHttpClient.Builder().pingInterval(5,TimeUnit.SECONDS).build();
       OkHttpClient client = new OkHttpClient();
        String url = "wss://chat.tokopedia.com/connect?os_type=1&device_id=d-fMlCUru9c:APA91bG53Z0mIJkMJ9tBtWswmhbj7EJzc_5gff5Z4vhuUcscUExA9_IraFWX_0fJFGBS9JDcLJtJvwPHOtSBrgdIKbsqnYhlWzywK-oZTx4MQSql3mQCsm5etuLVG18k1tw6ZkGBWj58&user_id=26696802";
        Request request = new Request.Builder().url(url)
                .header("Origin", TkpdBaseURL.WEB_DOMAIN)
                .build();
        webSocket = client.newWebSocket(request, wl);
//        for (int j=0;j<1000;j++){
//            countDownLatch = new CountDownLatch(1);
//            countDownLatch.await(10,TimeUnit.SECONDS);
//            if(wl.isOpen){
//                webSocket.send(json);
//            }
//        }


        countDownLatch = new CountDownLatch(1);
        countDownLatch.await(10,TimeUnit.SECONDS);
        String json2 = "{\"code\":103,\"data\":{\"message_id\":60166378,\"message\":\"halo selamat pagi\",\"start_time\":\"2017-08-28T04:12:23.915Z\",\"attachment_type\":3,\"product_id\":242546660}}";
        webSocket.send(json2);

        countDownLatch = new CountDownLatch(1);
        countDownLatch.await(1000, TimeUnit.SECONDS);
    }

    class WebListener extends WebSocketListener {
        public Boolean isOpen = false;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            System.out.println(sdf.format(new Date()));
            System.out.println("Open");
            super.onOpen(webSocket, response);
            isOpen = true;
            String json = "{\"code\":103,\"data\":{\"message_id\":60166378,\"message\":\" Bisa dikirim hari ini ga???? gmn sih tong?? \",\"start_time\":\"2018-02-13T05:55:16.466Z\"}}";
            webSocket.send(json);

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            System.out.println(sdf.format(new Date())+" --- "+text);
            super.onMessage(webSocket, text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            System.out.println(sdf.format(new Date()));
            System.out.println("Closing "+code);
            System.out.println(reason);
            super.onClosing(webSocket, code, reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            System.out.println(sdf.format(new Date()));
            System.out.println("Closed "+code);
            System.out.println(reason);
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            t.printStackTrace();
            super.onFailure(webSocket, t, response);
        }
    }
}
