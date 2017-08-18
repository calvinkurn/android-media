package com.tokopedia.inbox.rescenter.createreso.view.viewmodel;

import org.json.JSONObject;

/**
 * Created by yoasfs on 18/08/17.
 */

public class ProblemOrderResult {
    public ProblemOrderDetailResult detail;

    public JSONObject writeToJson() {
        JSONObject object = new JSONObject();
        try {
            if (detail != null) {
                object.put("detail", detail.writeToJson());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
