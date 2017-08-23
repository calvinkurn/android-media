package com.tokopedia.discovery.newdynamicfilter.helper;

import android.text.TextUtils;

import com.tokopedia.core.discovery.model.Option;

import java.util.HashMap;

/**
 * Created by henrypriyono on 8/22/17.
 */

public class OptionHelper {

    public static void saveOptionInputState(Option option,
                                            HashMap<String, Boolean> checkedState,
                                            HashMap<String, String> savedTextInput) {

        if (Option.INPUT_TYPE_CHECKBOX.equals(option.getInputType())) {
            saveCheckboxOptionInputState(option, checkedState);
        } else if (Option.INPUT_TYPE_TEXTBOX.equals(option.getInputType())) {
            saveTextboxOptionInputState(option, savedTextInput);
        }
    }

    private static void saveCheckboxOptionInputState(Option option,
                                                     HashMap<String, Boolean> checkedState) {

        if (!TextUtils.isEmpty(option.getInputState())) {
            checkedState.put(option.getUniqueId(), Boolean.parseBoolean(option.getInputState()));
        } else {
            checkedState.put(option.getUniqueId(), false);
        }
    }

    private static void saveTextboxOptionInputState(Option option,
                                                    HashMap<String, String> savedTextInput) {

        savedTextInput.put(option.getKey(), option.getInputState());
    }

    public static String loadOptionInputState(Option option,
                                            HashMap<String, Boolean> checkedState,
                                            HashMap<String, String> savedTextInput) {

        if (Option.INPUT_TYPE_CHECKBOX.equals(option.getInputType())) {
            return loadCheckboxOptionInputState(option, checkedState);
        } else if (Option.INPUT_TYPE_TEXTBOX.equals(option.getInputType())) {
            return loadTextboxOptionInputState(option, savedTextInput);
        } else {
            return "";
        }
    }

    private static String loadCheckboxOptionInputState(Option option,
                                                       HashMap<String, Boolean> checkedState) {

        Boolean isChecked = checkedState.get(option.getUniqueId());
        if (Boolean.TRUE.equals(isChecked)) {
            return Boolean.TRUE.toString();
        } else {
            return Boolean.FALSE.toString();
        }
    }

    private static String loadTextboxOptionInputState(Option option,
                                                      HashMap<String, String> savedTextInput) {

        String result = savedTextInput.get(option.getKey());
        return TextUtils.isEmpty(result) ? "" : result;
    }

    public static String parseKeyFromUniqueId(String uniqueId) {
        int separatorPos = uniqueId.indexOf("_");
        return uniqueId.substring(0, separatorPos);
    }

    public static String parseValueFromUniqueId(String uniqueId) {
        int separatorPos = uniqueId.indexOf("_");
        return uniqueId.substring(separatorPos + 1, uniqueId.length());
    }
}
