package com.tokopedia.inbox.rescenter.createreso.view.viewmodel;

import org.json.JSONObject;

/**
 * Created by yoasfs on 18/08/17.
 */

public class ProblemResult {
    public int type;
    public int trouble;
    public int quantity;
    public ProblemOrderResult order;
    public String remark;
    public boolean isDelivered = false;
    public boolean canShowInfo = false;

    public ProblemResult() {
        order = new ProblemOrderResult();
    }

    public JSONObject writeToJson() {
        JSONObject object = new JSONObject();
        try {
            if (type != 0) {
                object.put("type", type);
            }
            if (trouble != 0) {
                object.put("trouble", trouble);
            }
            if (quantity != 0) {
                object.put("quantity", quantity);
            }
            if (order != null) {
                object.put("order", order.writeToJson());
            }
            if (!remark.equals("")) {
                object.put("remark", remark);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
