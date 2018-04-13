package com.tokopedia.events.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokopedia.events.data.entity.response.AddressDetail;
import com.tokopedia.events.data.entity.response.EventLocationResponseEntity;
import com.tokopedia.events.data.entity.response.EventResponseEntity;
import com.tokopedia.events.data.entity.response.EventsDetailsEntity;
import com.tokopedia.events.data.entity.response.HomeResponseEntity;
import com.tokopedia.events.data.entity.response.Schedule;
import com.tokopedia.events.domain.model.EventDetailsDomain;
import com.tokopedia.events.domain.model.EventLocationDomain;
import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.events.domain.model.ScheduleDomain;
import com.tokopedia.events.domain.model.ScheduleDetailDomain;

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
        List<EventLocationDomain> eventLocationList = new ArrayList<>();
        EventLocationDomain eventLocationDomain;
        for (EventLocationResponseEntity eventLocationResponseEntity : eventLocationResponseEntities) {
            eventLocationDomain = new EventLocationDomain();
            eventLocationDomain.setId(eventLocationResponseEntity.getId());
            eventLocationDomain.setCategoryId(eventLocationResponseEntity.getCategoryId());
            eventLocationDomain.setName(eventLocationResponseEntity.getName());
            eventLocationDomain.setCountry(eventLocationResponseEntity.getCountry());
            eventLocationDomain.setState(eventLocationResponseEntity.getState());
            eventLocationDomain.setDistrict(eventLocationResponseEntity.getDistrict());
            eventLocationDomain.setIcon(eventLocationResponseEntity.getIcon());
            eventLocationDomain.setStatus(eventLocationResponseEntity.getStatus());
            eventLocationDomain.setSearchName(eventLocationResponseEntity.getSearchName());

            eventLocationList.add(eventLocationDomain);
        }

        return eventLocationList;
    }

    public EventDetailsDomain tranformEventDetails(EventsDetailsEntity eventsDetailsEntity) {
        EventDetailsDomain eventDetailsDomain = new EventDetailsDomain();
        eventDetailsDomain.setId(eventsDetailsEntity.getId());
        eventDetailsDomain.setCityName(eventsDetailsEntity.getCityName());
        eventDetailsDomain.setCategoryId(eventsDetailsEntity.getCategoryId());
        eventDetailsDomain.setConvenienceFee(eventsDetailsEntity.getConvenienceFee());
        eventDetailsDomain.setDisplayName(eventsDetailsEntity.getDisplayName());
        eventDetailsDomain.setDuration(eventsDetailsEntity.getDuration());
        eventDetailsDomain.setImageApp(eventsDetailsEntity.getImageApp());
        eventDetailsDomain.setHasSeatLayout(eventsDetailsEntity.getHasSeatLayout());
        eventDetailsDomain.setIsFoodAvailable(eventsDetailsEntity.getIsFoodAvailable());
        eventDetailsDomain.setIsPromo(eventsDetailsEntity.getIsPromo());
        eventDetailsDomain.setTnc(eventsDetailsEntity.getTnc());
        eventDetailsDomain.setTitle(eventsDetailsEntity.getTitle());
        eventDetailsDomain.setSalesPrice(eventsDetailsEntity.getSalesPrice());
        eventDetailsDomain.setShortDesc(eventsDetailsEntity.getShortDesc());
        eventDetailsDomain.setLongRichDesc(eventsDetailsEntity.getLongRichDesc());
        eventDetailsDomain.setDateRange(eventsDetailsEntity.getDateRange());
        eventDetailsDomain.setMinStartDate(eventsDetailsEntity.getMinStartDate());
        eventDetailsDomain.setMaxEndDate(eventsDetailsEntity.getMaxEndDate());
        eventDetailsDomain.setUrl(eventsDetailsEntity.getUrl());
        eventDetailsDomain.setThumbnailApp(eventsDetailsEntity.getThumbnailApp());
        eventDetailsDomain.setOfferText(eventsDetailsEntity.getOfferText());
        eventDetailsDomain.setDisplayTags(eventsDetailsEntity.getDisplayTags());
        eventDetailsDomain.setMrp(eventsDetailsEntity.getMrp());
        eventDetailsDomain.setPromotionText(eventsDetailsEntity.getPromotionText());
        eventDetailsDomain.setRating(eventsDetailsEntity.getRating());
        eventDetailsDomain.setIsFeatured(eventsDetailsEntity.getIsFeatured());
        eventDetailsDomain.setIsFoodAvailable(eventsDetailsEntity.getIsFoodAvailable());
        eventDetailsDomain.setGenre(eventsDetailsEntity.getGenre());
        eventDetailsDomain.setForms(eventsDetailsEntity.getForms());
        eventDetailsDomain.setSeatMapImage(eventsDetailsEntity.getSeatmapImage());
        List<ScheduleDomain> scheduleDomainList = new ArrayList<>();
        try {
            for (Schedule item : eventsDetailsEntity.getSchedules()) {
                ScheduleDomain scheduleDomain = new ScheduleDomain();
                ScheduleDetailDomain scheduleDetailDomain = new ScheduleDetailDomain();
                AddressDetail addressDetailDomain = new AddressDetail();

                scheduleDetailDomain.setId(item.getSchedule().getId());
                scheduleDetailDomain.setCreatedAt(item.getSchedule().getCreatedAt());
                scheduleDetailDomain.setEndDate(item.getSchedule().getEndDate());
                scheduleDetailDomain.setProductId(item.getSchedule().getProductId());
                scheduleDetailDomain.setProviderMetaData(item.getSchedule().getProviderMetaData());
                scheduleDetailDomain.setProviderScheduleId(item.getSchedule().getProviderScheduleId());
                scheduleDetailDomain.setStartDate(item.getSchedule().getStartDate());
                scheduleDetailDomain.setStatus(item.getSchedule().getStatus());
                scheduleDetailDomain.setTitle(item.getSchedule().getTitle());
                scheduleDetailDomain.setTnc(item.getSchedule().getTnc());
                scheduleDetailDomain.setUpdatedAt(item.getSchedule().getUpdatedAt());
                scheduleDomain.setSchedule(scheduleDetailDomain);

                addressDetailDomain.setId(item.getAddressDetail().getId());
                addressDetailDomain.setAddress(item.getAddressDetail().getAddress());
                addressDetailDomain.setCity(item.getAddressDetail().getCity());
                addressDetailDomain.setCreatedAt(item.getAddressDetail().getCreatedAt());
                addressDetailDomain.setDistrict(item.getAddressDetail().getDistrict());
                addressDetailDomain.setLatitude(item.getAddressDetail().getLatitude());
                addressDetailDomain.setLongitude(item.getAddressDetail().getLongitude());
                addressDetailDomain.setName(item.getAddressDetail().getName());
                addressDetailDomain.setProductId(item.getAddressDetail().getProductId());
                addressDetailDomain.setProductScheduleId(item.getAddressDetail().getProductScheduleId());
                addressDetailDomain.setProductSchedulePackageId(item.getAddressDetail().getProductSchedulePackageId());
                addressDetailDomain.setState(item.getAddressDetail().getState());
                addressDetailDomain.setStatus(item.getAddressDetail().getStatus());
                addressDetailDomain.setUpdatedAt(item.getAddressDetail().getUpdatedAt());

                scheduleDomain.setAddressDetail(addressDetailDomain);
                scheduleDomain.setGroups(item.getGroups());

                scheduleDomainList.add(scheduleDomain);

            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        eventDetailsDomain.setSchedules(scheduleDomainList);

        return eventDetailsDomain;
    }
}
