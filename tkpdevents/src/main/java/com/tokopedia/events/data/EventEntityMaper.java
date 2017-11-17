package com.tokopedia.events.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokopedia.events.data.entity.response.EventLocationResponseEntity;
import com.tokopedia.events.data.entity.response.EventResponseEntity;
import com.tokopedia.events.data.entity.response.HomeResponseEntity;
import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.events.domain.model.EventLocationDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ashwanityagi on 15/11/17.
 */

public class EventEntityMaper {

    public EventEntityMaper() {
    }

    public List<EventsCategoryDomain> tranform(EventResponseEntity eventResponseEntity) {

        HomeResponseEntity homeResponseEntity = eventResponseEntity.getHome();
        JsonObject layout = homeResponseEntity.getLayout();
        List<EventsCategoryDomain> categoryEntities = new ArrayList<>();

        EventsCategoryDomain eventsCategoryDomain;
        for (Map.Entry<String, JsonElement> entry : layout.entrySet()) {
            JsonObject obj = entry.getValue().getAsJsonObject();
            eventsCategoryDomain = new Gson().fromJson(obj, EventsCategoryDomain.class);
            categoryEntities.add(eventsCategoryDomain);
        }
        return categoryEntities;
    }


    public List<EventLocationDomain> tranformLocationList(List<EventLocationResponseEntity> eventLocationResponseEntities) {
        List<EventLocationDomain> EventLocationList = new ArrayList<>();
        EventLocationDomain eventLocationDomain;
        for (EventLocationResponseEntity eventLocationResponseEntity:eventLocationResponseEntities) {
            eventLocationDomain =new EventLocationDomain();
            eventLocationDomain.setId(eventLocationResponseEntity.getId());
            eventLocationDomain.setCategoryId(eventLocationResponseEntity.getCategoryId());
            eventLocationDomain.setName(eventLocationResponseEntity.getName());
            eventLocationDomain.setCountry(eventLocationResponseEntity.getCountry());
            eventLocationDomain.setState(eventLocationResponseEntity.getState());
            eventLocationDomain.setDistrict(eventLocationResponseEntity.getDistrict());
            eventLocationDomain.setIcon(eventLocationResponseEntity.getIcon());
            eventLocationDomain.setStatus(eventLocationResponseEntity.getStatus());
            eventLocationDomain.setSearchName(eventLocationResponseEntity.getSearchName());

            EventLocationList.add(eventLocationDomain);
        }

        return EventLocationList;
    }
}
