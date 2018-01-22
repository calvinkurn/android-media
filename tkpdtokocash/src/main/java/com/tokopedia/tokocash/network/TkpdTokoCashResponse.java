package com.tokopedia.tokocash.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nabillasabbaha on 1/12/18.
 */

public class TkpdTokoCashResponse {

    private static final String TAG = TkpdTokoCashResponse.class.getSimpleName();

    private static final String KEY_DATA = "data";
    private static final String KEY_INCLUDED = "included";
    private static final String KEY_META = "meta";
    private static final String KEY_ERROR = "errors";
    private static final String DEFAULT_ERROR_MESSAGE_DATA_NULL = "Tidak ada data";


    private JsonElement jsonElementData;
    private JsonElement jsonElementIncluded;
    private JsonElement jsonElementMeta;
    private Object objData;
    private Object objIncluded;
    private Object objMeta;
    private String message;
    private String strResponse;
    private String strData;
    private String strIncluded;
    private String strMeta;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static TkpdTokoCashResponse factory(String strResponse) throws IOException {
        Log.d(TAG, strResponse);
        TkpdTokoCashResponse tkpdTokoCashResponse = new TkpdTokoCashResponse();
        JsonElement jsonElement = new JsonParser().parse(strResponse);
        JsonObject jsonResponse = jsonElement.getAsJsonObject();
        String strData;
        String strIncluded;
        String strMeta;
        if (!jsonResponse.has(KEY_DATA) || jsonResponse.get(KEY_DATA).isJsonNull()) {
            if (jsonResponse.has(KEY_ERROR)) {
                try {
                    TokoCashErrorResponse tokoCashErrorResponse =
                            new Gson().fromJson(strResponse, TokoCashErrorResponse.class);
                    throw new ResponseErrorException(
                            tokoCashErrorResponse.getTokoCashErrorMessageFormatted()
                    );
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    throw new ResponseErrorException();
                }
            }
            throw new ResponseDataNullException(DEFAULT_ERROR_MESSAGE_DATA_NULL);
        } else if (jsonResponse.has(KEY_DATA) && jsonResponse.get(KEY_DATA).isJsonObject()) {
            strData = jsonResponse.get(KEY_DATA).getAsJsonObject().toString();
        } else if (jsonResponse.has(KEY_DATA) && jsonResponse.get(KEY_DATA).isJsonArray()) {
            strData = jsonResponse.get(KEY_DATA).getAsJsonArray().toString();
        } else {
            throw new ResponseDataNullException(DEFAULT_ERROR_MESSAGE_DATA_NULL);
        }

        if (!jsonResponse.has(KEY_INCLUDED) || jsonResponse.get(KEY_INCLUDED).isJsonNull()) {
            strIncluded = null;
        } else if (jsonResponse.has(KEY_INCLUDED) && jsonResponse.get(KEY_INCLUDED).isJsonObject()) {
            strIncluded = jsonResponse.get(KEY_INCLUDED).getAsJsonObject().toString();
        } else if (jsonResponse.has(KEY_INCLUDED) && jsonResponse.get(KEY_INCLUDED).isJsonArray()) {
            strIncluded = jsonResponse.get(KEY_INCLUDED).getAsJsonArray().toString();
        } else {
            strIncluded = null;
        }

        if (!jsonResponse.has(KEY_META) || jsonResponse.get(KEY_META).isJsonNull()) {
            strMeta = null;
        } else if (jsonResponse.has(KEY_META) && jsonResponse.get(KEY_META).isJsonObject()) {
            strMeta = jsonResponse.get(KEY_META).getAsJsonObject().toString();
        } else if (jsonResponse.has(KEY_META) && jsonResponse.get(KEY_META).isJsonArray()) {
            strMeta = jsonResponse.get(KEY_META).getAsJsonArray().toString();
        } else {
            strMeta = null;
        }
        tkpdTokoCashResponse.setJsonElementData(jsonResponse.get(KEY_DATA));
        tkpdTokoCashResponse.setJsonElementIncluded(jsonResponse.get(KEY_INCLUDED));
        tkpdTokoCashResponse.setJsonElementMeta(jsonResponse.get(KEY_META));
        tkpdTokoCashResponse.setMessage("");
        tkpdTokoCashResponse.setStrData(strData);
        tkpdTokoCashResponse.setStrIncluded(strIncluded);
        tkpdTokoCashResponse.setStrResponse(strResponse);
        tkpdTokoCashResponse.setStrMeta(strMeta);
        return tkpdTokoCashResponse;
    }

    public JsonElement getJsonElementData() {
        return jsonElementData;
    }

    public String getMessage() {
        return message;
    }

    public String getStrResponse() {
        return strResponse;
    }

    public String getStrData() {
        return strData;
    }

    private void setJsonElementData(JsonElement jsonElementData) {
        this.jsonElementData = jsonElementData;
    }

    public Object getObjMeta() {
        return objMeta;
    }

    public void setObjMeta(Object objMeta) {
        this.objMeta = objMeta;
    }

    public String getStrMeta() {
        return strMeta;
    }

    public void setStrMeta(String strMeta) {
        this.strMeta = strMeta;
    }

    public JsonElement getJsonElementMeta() {
        return jsonElementMeta;
    }

    public void setJsonElementMeta(JsonElement jsonElementMeta) {
        this.jsonElementMeta = jsonElementMeta;
    }

    void setMessage(String message) {
        this.message = message;
    }

    private void setStrResponse(String strResponse) {
        this.strResponse = strResponse;
    }

    private void setStrData(String strData) {
        this.strData = strData;
    }

    public Object getObjIncluded() {
        return objIncluded;
    }

    public void setObjIncluded(Object objIncluded) {
        this.objIncluded = objIncluded;
    }

    public String getStrIncluded() {
        return strIncluded;
    }

    private void setStrIncluded(String strIncluded) {
        this.strIncluded = strIncluded;
    }

    public JsonElement getJsonElementIncluded() {
        return jsonElementIncluded;
    }

    private void setJsonElementIncluded(JsonElement jsonElementIncluded) {
        this.jsonElementIncluded = jsonElementIncluded;
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


    @SuppressWarnings("unchecked")
    public <T> T convertIncludedObj(Class<T> clazz) {
        if (objIncluded == null) {
            try {
                this.objIncluded = gson.fromJson(strIncluded, clazz);
                return (T) objIncluded;
            } catch (ClassCastException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return (T) objIncluded;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> convertIncludedList(Class<T[]> clazz) {
        if (objIncluded == null) {
            try {
                this.objIncluded = Arrays.asList((T[])
                        (this.objIncluded = gson.fromJson(strIncluded, clazz)));
                return (List<T>) objIncluded;
            } catch (ClassCastException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return (List<T>) objIncluded;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T convertMetaObj(Class<T> clazz) {
        if (objMeta == null) {
            try {
                this.objMeta = gson.fromJson(strMeta, clazz);
                return (T) objMeta;
            } catch (ClassCastException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return (T) objMeta;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> convertMetaList(Class<T[]> clazz) {
        if (objMeta == null) {
            try {
                this.objMeta = Arrays.asList((T[])
                        (this.objMeta = gson.fromJson(strMeta, clazz)));
                return (List<T>) objMeta;
            } catch (ClassCastException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return (List<T>) objMeta;
        }
    }


    public static class TokoCashErrorResponse {
        public static final int ERROR_TOKOCASH = 1;
        public static final int ERROR_SERVER = 2;

        @SerializedName("errors")
        @Expose
        private List<String> errors = new ArrayList<>();
        @SerializedName("code")
        @Expose
        private String code;

        public void setCode(String code) {
            this.code = code;
        }

        public int getTypeOfError() {
            if (!errors.isEmpty()) return ERROR_TOKOCASH;
            else return ERROR_SERVER;
        }

        public String getTokoCashErrorMessageFormatted() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < errors.size(); i++) {
                stringBuilder.append(errors.get(i));
                if (i < errors.size() - 1) {
                    stringBuilder.append(", ");
                }
            }
            return stringBuilder.toString().trim();
        }

        public static TokoCashErrorResponse factory(String errorBody, int code) {
            try {
                TokoCashErrorResponse tokoCashErrorResponse =
                        new Gson().fromJson(errorBody, TokoCashErrorResponse.class);
                tokoCashErrorResponse.setCode(String.valueOf(code));
                return tokoCashErrorResponse;
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                return TokoCashErrorResponse.factoryDefault(
                        ErrorNetMessage.MESSAGE_ERROR_DEFAULT, code
                );
            }
        }

        private static TokoCashErrorResponse factoryDefault(
                String messageErrorDefault, int errorCode) {
            TokoCashErrorResponse digitalErrorResponse = new TokoCashErrorResponse();
            List<String> errors = new ArrayList<>();
            errors.add(messageErrorDefault);
            digitalErrorResponse.setCode(String.valueOf(errorCode));
            return digitalErrorResponse;
        }
    }
}