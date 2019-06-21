package com.tokopedia.discovery.newdynamicfilter.helper;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.discovery.common.data.Filter;
import com.tokopedia.discovery.common.data.Option;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by henrypriyono on 11/27/17.
 */

public class FilterDbHelper {
    public static void storeLocationFilterOptions(Context context, List<Option> optionList) {

        Type listType = new TypeToken<List<Option>>() {}.getType();
        Gson gson = new Gson();
        String optionData = gson.toJson(optionList, listType);

        DynamicFilterDbManager.store(context, Filter.TEMPLATE_NAME_LOCATION, optionData);
    }

    public static List<Option> loadLocationFilterOptions(Context context) {
        String data = DynamicFilterDbManager.getFilterData(context, Filter.TEMPLATE_NAME_LOCATION);
        Type listType = new TypeToken<List<Option>>() {}.getType();
        return new Gson().fromJson(data, listType);
    }
}
