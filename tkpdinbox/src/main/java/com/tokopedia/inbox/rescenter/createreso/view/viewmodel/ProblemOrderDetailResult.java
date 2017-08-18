package com.tokopedia.inbox.rescenter.createreso.view.viewmodel;

import org.json.JSONObject;

/**
 * Created by yoasfs on 18/08/17.
 */

public class ProblemOrderDetailResult {
    public int id;

    public JSONObject writeToJson() {
        JSONObject object = new JSONObject();
        try {
            if (id != 0) {
                object.put("id", id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
