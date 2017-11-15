package com.tokopedia.events.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokopedia.events.data.entity.EventResponseEntity;
import com.tokopedia.events.data.entity.HomeResponseEntity;
import com.tokopedia.events.domain.model.CategoryEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ashwanityagi on 15/11/17.
 */

public class EventEntityMaper {

    public EventEntityMaper() {
    }

    public List<CategoryEntity> tranform(EventResponseEntity eventResponseEntity) {

        HomeResponseEntity homeResponseEntity = eventResponseEntity.getHome();
        JsonObject layout = homeResponseEntity.getLayout();
        List<CategoryEntity> categoryEntities = new ArrayList<>();

        CategoryEntity categoryEntity;
        for (Map.Entry<String, JsonElement> entry : layout.entrySet()) {
            categoryEntity = new CategoryEntity();
            JsonObject obj = entry.getValue().getAsJsonObject();
            categoryEntity = new Gson().fromJson(obj, CategoryEntity.class);
            categoryEntities.add(categoryEntity);
        }
        return categoryEntities;
    }

}
