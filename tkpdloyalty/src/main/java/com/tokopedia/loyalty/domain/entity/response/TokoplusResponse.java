package com.tokopedia.loyalty.domain.entity.response;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ResponseErrorException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public class TokoplusResponse {
    private static final String KEY_DATA = "data";
    private static final String KEY_HEADER = "header";
    private static final String DEFAULT_ERROR_MESSAGE_DATA_NULL = "Tidak ada data";

    private JsonElement jsonElementData;
    private TokoplusHeaderResponse tokoplusHeaderResponse;
    private Object objData;
    private String strData;
    private String strResponse;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static TokoplusResponse factory(String strResponse) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        TokoplusResponse tokoplusResponse = new TokoplusResponse();
        JsonElement jsonElement = new JsonParser().parse(strResponse);
        JsonObject jsonResponse = jsonElement.getAsJsonObject();
        String strData;

        TokoplusHeaderResponse tokoplusHeaderResponse = gson.fromJson(
                jsonResponse.get(KEY_HEADER).getAsString(), TokoplusHeaderResponse.class
        );


        if ((!jsonResponse.has(KEY_DATA) || jsonResponse.get(KEY_DATA).isJsonNull())
                && (tokoplusHeaderResponse != null && tokoplusHeaderResponse.getErrorCode() != null
                && tokoplusHeaderResponse.getMessage() != null)) {
            if (!TextUtils.isEmpty(tokoplusHeaderResponse.getMessageFormatted()))
                throw new ResponseErrorException(tokoplusHeaderResponse.getMessageFormatted());
            else
                throw new ResponseErrorException();
        } else if (jsonResponse.has(KEY_DATA) && jsonResponse.get(KEY_DATA).isJsonObject()) {
            strData = jsonResponse.get(KEY_DATA).getAsJsonObject().toString();
        } else if (jsonResponse.has(KEY_DATA) && jsonResponse.get(KEY_DATA).isJsonArray()) {
            strData = jsonResponse.get(KEY_DATA).getAsJsonArray().toString();
        } else {
            throw new ResponseDataNullException(DEFAULT_ERROR_MESSAGE_DATA_NULL);
        }
        tokoplusResponse.setJsonElementData(jsonResponse.get(KEY_DATA));
        tokoplusResponse.setStrData(strData);
        tokoplusResponse.setTokoplusHeaderResponse(tokoplusHeaderResponse);
        tokoplusResponse.setStrResponse(strResponse);
        return tokoplusResponse;
    }

    public JsonElement getJsonElementData() {
        return jsonElementData;
    }

    public void setJsonElementData(JsonElement jsonElementData) {
        this.jsonElementData = jsonElementData;
    }

    public TokoplusHeaderResponse getTokoplusHeaderResponse() {
        return tokoplusHeaderResponse;
    }

    public void setTokoplusHeaderResponse(TokoplusHeaderResponse tokoplusHeaderResponse) {
        this.tokoplusHeaderResponse = tokoplusHeaderResponse;
    }

    public Object getObjData() {
        return objData;
    }

    public String getStrResponse() {
        return strResponse;
    }

    public void setStrResponse(String strResponse) {
        this.strResponse = strResponse;
    }

    public void setObjData(Object objData) {
        this.objData = objData;
    }

    public String getStrData() {
        return strData;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    @SuppressWarnings("unchecked")
    public <T> T convertDataObj(Class<T> clazz) {
        if (objData == null) {
            try {
                this.objData = gson.fromJson(strData, clazz);
                return (T) objData;
            } catch (ClassCastException e) {
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
                this.objData = Arrays.asList((T[]) (this.objData = gson.fromJson(strData, clazz)));
                return (List<T>) objData;
            } catch (ClassCastException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return (List<T>) objData;
        }
    }
}
