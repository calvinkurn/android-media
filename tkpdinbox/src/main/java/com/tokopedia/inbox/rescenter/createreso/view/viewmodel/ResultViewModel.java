package com.tokopedia.inbox.rescenter.createreso.view.viewmodel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 18/08/17.
 */

public class ResultViewModel implements Serializable {
    public List<ProblemResult> problem = new ArrayList<>();
    public int solution;
    public int refundAmount;
    public ProblemMessageResult message;
    public int attachmentCount;

    public JSONObject writeToJson() {
        JSONObject object = new JSONObject();
        try {
            if (problem.size() != 0) {
                JSONArray problemArray = new JSONArray();
                for (ProblemResult problemResult : problem) {
                    problemArray.put(problemResult.writeToJson());
                }
                object.put("problem", problemArray);
            }
            if (solution != 0) {
                object.put("solution", solution);
            }
            if (refundAmount != 0) {
                object.put("refundAmount", refundAmount);
            }
            if (attachmentCount != 0) {
                object.put("attachmentCount", attachmentCount);
            }
            if (message != null) {
                object.put("message", message.writeToJson());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
