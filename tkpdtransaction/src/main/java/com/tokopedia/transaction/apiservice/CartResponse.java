package com.tokopedia.transaction.apiservice;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author anggaprasetiyo on 24/01/18.
 */

public class CartResponse {
    private boolean isNullData;
    private boolean isError;
    private String status;
    private String strResponse;
    private String stringData = "";
    private JSONObject jsonData;
    private List<String> errorMessages = new ArrayList<>();

    private Gson gson = new GsonBuilder().disableHtmlEscaping()
            .setPrettyPrinting().create();
    private Object objData;

    public static CartResponse factory(String strResponse) {
        List<String> msgError = new ArrayList<>();
        List<String> msgStatus = new ArrayList<>();
        boolean isNullData = false;
        boolean isError = false;
        String status = "";
        JSONObject jsonResponse;
        JSONObject jsonData;
        try {
            jsonResponse = new JSONObject(strResponse);
            status = jsonResponse.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


        try {
            if (!jsonResponse.isNull("message_error")) {
                JSONArray jArray = jsonResponse.getJSONArray("message_error");
                if (jArray != null) {
                    for (int i = 0; i < jArray.length(); i++) {
                        msgError.add(jArray.get(i).toString());
                    }
                    isError = true;
                }
            } else {
                msgError.add("");
                isError = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (!jsonResponse.isNull("data")) {
                jsonData = jsonResponse.getJSONObject("data");
            } else {
                jsonData = null;
            }

            isNullData = jsonData == null;
        } catch (JSONException e) {
            e.printStackTrace();
            jsonData = null;
        }
        if (jsonData == null) {
            isError = true;
            if (msgError.isEmpty()) msgError.add("Data Tidak Ditemukan");
        }

        try {
            if (!jsonResponse.isNull("message_status")) {
                JSONArray jArray = jsonResponse.getJSONArray("message_status");
                if (jArray != null) {
                    for (int i = 0; i < jArray.length(); i++) {
                        msgStatus.add(jArray.get(i).toString());
                    }
                }
            } else {
                msgStatus.add("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CartResponse cartResponse = new CartResponse();
        if (!isNullData & jsonData != null) cartResponse.setJsonData(jsonData);
        cartResponse.setErrorMessages(msgError);
        cartResponse.setIsError(isError || isNullData);
        cartResponse.setStatus(status);
        cartResponse.setIsNullData(isNullData);
        cartResponse.setStrResponse(strResponse);
        return cartResponse;
    }


    public boolean isNullData() {
        return isNullData;
    }

    private void setIsNullData(boolean isNullData) {
        this.isNullData = isNullData;
    }

    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    public String getStringData() {
        return stringData;
    }

    private void setStringData(String stringData) {
        if (!stringData.isEmpty()) {
            gson.toJson(stringData);
        }
        this.stringData = stringData;
    }

    public JSONObject getJsonData() {
        return jsonData;
    }

    public String getStrResponse() {
        return strResponse;
    }

    private void setStrResponse(String strResponse) {
        this.strResponse = strResponse;
    }

    private void setJsonData(@NonNull JSONObject jsonData) {
        this.stringData = jsonData.toString();
        this.jsonData = jsonData;
    }

    public boolean isError() {
        return isError;
    }

    private void setIsError(boolean isError) {
        this.isError = isError;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    private void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public String getErrorMessageJoined() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!errorMessages.isEmpty()) {
            for (int i = 0, statusMessagesSize = errorMessages.size(); i < statusMessagesSize; i++) {
                String string = errorMessages.get(i);
                stringBuilder.append(string);
                if (i != errorMessages.size() - 1
                        && !errorMessages.get(i).equals("")
                        && !errorMessages.get(i + 1).equals("")) {
                    stringBuilder.append("\n");
                }
            }
        }
        return stringBuilder.toString();
    }


    @SuppressWarnings("unchecked")
    public <T> T convertDataObj(Class<T> clazz) {
        if (objData == null) {
            try {
                this.objData = gson.fromJson(stringData, clazz);
                return (T) objData;
            } catch (ClassCastException | JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return (T) objData;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> convertDataList(Class<T[]> clazz) {
        if (objData == null) {
            try {
                this.objData = Arrays.asList((T[]) (this.objData = gson.fromJson(stringData, clazz)));
                return (List<T>) objData;
            } catch (ClassCastException | JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return (List<T>) objData;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T convertToObj(Class<T> clazz) {
        try {
            return gson.fromJson(stringData, clazz);
        } catch (ClassCastException | JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
