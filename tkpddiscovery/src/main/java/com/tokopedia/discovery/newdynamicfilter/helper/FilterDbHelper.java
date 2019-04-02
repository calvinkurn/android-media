package com.tokopedia.discovery.newdynamicfilter.helper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by henrypriyono on 11/27/17.
 */

public class FilterDbHelper {
    public static void storeLocationFilterOptions(List<Option> optionList) {

        Type listType = new TypeToken<List<Option>>() {}.getType();
        Gson gson = new Gson();
        String optionData = gson.toJson(optionList, listType);

        DynamicFilterDbManager cache = new DynamicFilterDbManager();
        cache.setFilterID(Filter.TEMPLATE_NAME_LOCATION);
        cache.setFilterData(optionData);
        cache.store();
    }

    public static List<Option> loadLocationFilterOptions() {
        String data = new DynamicFilterDbManager()
                .getValueString(Filter.TEMPLATE_NAME_LOCATION);
        Type listType = new TypeToken<List<Option>>() {}.getType();
        return new Gson().fromJson(data, listType);
    }
}
