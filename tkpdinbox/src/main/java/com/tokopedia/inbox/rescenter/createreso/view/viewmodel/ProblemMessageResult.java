package com.tokopedia.inbox.rescenter.createreso.view.viewmodel;

import org.json.JSONObject;

/**
 * Created by yoasfs on 18/08/17.
 */

public class ProblemMessageResult {
    public String remark;

    public JSONObject writeToJson() {
        JSONObject object = new JSONObject();
        try {
            if (!remark.equals("")) {
                object.put("remark", remark);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
