package com.tokopedia.inbox.rescenter.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by yfsx on 02/08/18.
 */
public class ResolutionResponse<T> {
    @SerializedName("data")
    private T data;

    @SerializedName("message_error")
    private List<String> error_messages = new ArrayList<>();

    @SerializedName("messageError")
    private List<String> errorMessages = new ArrayList<>();

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
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
        } else {
            for (int i = 0, statusMessagesSize = error_messages.size(); i < statusMessagesSize; i++) {
                String string = error_messages.get(i);
                stringBuilder.append(string);
                if (i != error_messages.size() - 1
                        && !error_messages.get(i).equals("")
                        && !error_messages.get(i + 1).equals("")) {
                    stringBuilder.append("\n");
                }
            }
        }
        return stringBuilder.toString();
    }

    public boolean isNullData() {
        return data == null;
    }
}
