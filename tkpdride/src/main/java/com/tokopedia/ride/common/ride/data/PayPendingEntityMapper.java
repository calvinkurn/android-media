package com.tokopedia.ride.common.ride.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokopedia.ride.common.ride.domain.model.PayPending;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * Created by Vishal on 11/10/17.
 */

public class PayPendingEntityMapper {


    public PayPendingEntityMapper() {
    }

    public PayPending transform(JsonObject entity) {
        PayPending payPending = null;

        if (entity != null) {
            payPending = new PayPending();
            payPending.setUrl(entity.get("url").getAsString());
            
            StringBuffer stringBuffer = new StringBuffer();
            if (entity != null) {
                boolean isFirstKey = true;
                Set<Map.Entry<String, JsonElement>> set = entity.entrySet();
                for (Map.Entry<String, JsonElement> entry : set) {
                    try {
                        String key = entry.getKey();
                        JsonElement value = entry.getValue();

                        //skip url key from response data
                        if (key.equalsIgnoreCase("url")) {
                            continue;
                        }

                        if (!isFirstKey) {
                            stringBuffer.append("&");
                        } else {
                            isFirstKey = false;
                        }

                        if (value.isJsonArray()) {
                            stringBuffer.append(transform(key, value.getAsJsonArray()));
                        } else {
                            stringBuffer.append(URLEncoder.encode(key, "UTF-8"));
                            stringBuffer.append("=");
                            stringBuffer.append(URLEncoder.encode(value.getAsString(), "UTF-8"));
                        }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            }
            payPending.setPostData(stringBuffer.toString());
        }

        return payPending;
    }

    private String transform(String key, JsonArray array) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            if (array != null && array.size() > 0) {
                int length = array.size();
                for (int index = 0; index < length; index++) {
                    JsonElement element = array.get(index);

                    stringBuffer.append(URLEncoder.encode(key, "UTF-8"));
                    stringBuffer.append("=");
                    stringBuffer.append(URLEncoder.encode(element.getAsString(), "UTF-8"));

                    if (length < index + 1) {
                        stringBuffer.append("&");
                    }
                }
            }
        } catch (Exception ex) {
        }

        return stringBuffer.toString();
    }
}
