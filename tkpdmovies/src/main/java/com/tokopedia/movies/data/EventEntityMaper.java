package com.tokopedia.movies.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokopedia.movies.data.entity.response.AddressDetail;
import com.tokopedia.movies.data.entity.response.EventLocationResponseEntity;
import com.tokopedia.movies.data.entity.response.EventResponseEntity;
import com.tokopedia.movies.data.entity.response.EventsDetailsEntity;
import com.tokopedia.movies.data.entity.response.HomeResponseEntity;
import com.tokopedia.movies.data.entity.response.MovieSeatResponseEntity;
import com.tokopedia.movies.data.entity.response.Schedule;
import com.tokopedia.movies.data.entity.response.SeatLayoutItem;
import com.tokopedia.movies.data.entity.response.seatlayoutresponse.SeatLayoutResponse;
import com.tokopedia.movies.domain.model.EventDetailsDomain;
import com.tokopedia.movies.domain.model.EventLocationDomain;
import com.tokopedia.movies.domain.model.EventsCategoryDomain;
import com.tokopedia.movies.domain.model.ScheduleDomain;
import com.tokopedia.movies.domain.model.ScheduleDomain_;
import com.tokopedia.movies.view.viewmodel.SeatLayoutItemViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EventEntityMaper {

    public EventEntityMaper() {
    }

    public List<EventsCategoryDomain> tranform(EventResponseEntity eventResponseEntity) {

        HomeResponseEntity homeResponseEntity = eventResponseEntity.getHome();
        JsonObject layout = homeResponseEntity.getLayout();
        List<EventsCategoryDomain> categoryEntities = new ArrayList<>();

        EventsCategoryDomain moviesCategoryDomain;
        for (Map.Entry<String, JsonElement> entry : layout.entrySet()) {
            JsonObject obj = entry.getValue().getAsJsonObject();
            moviesCategoryDomain = new Gson().fromJson(obj, EventsCategoryDomain.class);
            categoryEntities.add(moviesCategoryDomain);
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

    public EventDetailsDomain tranformEventDetails(EventsDetailsEntity moviesDetailsEntity) {
        EventDetailsDomain eventDetailsDomain = new EventDetailsDomain();
        eventDetailsDomain.setId(moviesDetailsEntity.getId());
        eventDetailsDomain.setCityName(moviesDetailsEntity.getCityName());
        eventDetailsDomain.setCategoryId(moviesDetailsEntity.getCategoryId());
        eventDetailsDomain.setConvenienceFee(moviesDetailsEntity.getConvenienceFee());
        eventDetailsDomain.setDisplayName(moviesDetailsEntity.getDisplayName());
        eventDetailsDomain.setDuration(moviesDetailsEntity.getDuration());
        eventDetailsDomain.setImageApp(moviesDetailsEntity.getImageApp());
        eventDetailsDomain.setThumbnailApp(moviesDetailsEntity.getThumbnailApp());
        eventDetailsDomain.setHasSeatLayout(moviesDetailsEntity.getHasSeatLayout());
        eventDetailsDomain.setIsFoodAvailable(moviesDetailsEntity.getIsFoodAvailable());
        eventDetailsDomain.setIsPromo(moviesDetailsEntity.getIsPromo());
        eventDetailsDomain.setTnc(moviesDetailsEntity.getTnc());
        eventDetailsDomain.setTitle(moviesDetailsEntity.getTitle());
        eventDetailsDomain.setSalesPrice(moviesDetailsEntity.getSalesPrice());
        eventDetailsDomain.setShortDesc(moviesDetailsEntity.getShortDesc());
        eventDetailsDomain.setLongRichDesc(moviesDetailsEntity.getLongRichDesc());
        eventDetailsDomain.setDateRange(moviesDetailsEntity.getDateRange());
        eventDetailsDomain.setMinStartDate(moviesDetailsEntity.getMinStartDate());
        eventDetailsDomain.setMaxEndDate(moviesDetailsEntity.getMaxEndDate());
        eventDetailsDomain.setUrl(moviesDetailsEntity.getUrl());
        eventDetailsDomain.setThumbnailApp(moviesDetailsEntity.getThumbnailApp());
        eventDetailsDomain.setOfferText(moviesDetailsEntity.getOfferText());
        eventDetailsDomain.setDisplayTags(moviesDetailsEntity.getDisplayTags());
        eventDetailsDomain.setMrp(moviesDetailsEntity.getMrp());
        eventDetailsDomain.setPromotionText(moviesDetailsEntity.getPromotionText());
        eventDetailsDomain.setRating(moviesDetailsEntity.getRating());
        eventDetailsDomain.setIsFeatured(moviesDetailsEntity.getIsFeatured());
        eventDetailsDomain.setIsFoodAvailable(moviesDetailsEntity.getIsFoodAvailable());
        eventDetailsDomain.setCustomText1(moviesDetailsEntity.getCustomText1());
        eventDetailsDomain.setCustomText2(moviesDetailsEntity.getCustomText2());
        eventDetailsDomain.setCustomText3(moviesDetailsEntity.getCustomText3());
        eventDetailsDomain.setGenre(moviesDetailsEntity.getGenre());
        eventDetailsDomain.setForms(moviesDetailsEntity.getForms());
        List<ScheduleDomain> scheduleDomainList = new ArrayList<>();
        try {
            for (Schedule item : moviesDetailsEntity.getSchedules()) {
                ScheduleDomain scheduleDomain = new ScheduleDomain();
                ScheduleDomain_ scheduleDomain_ = new ScheduleDomain_();
                AddressDetail addressDetailDomain = new AddressDetail();

                scheduleDomain_.setId(item.getSchedule().getId());
                scheduleDomain_.setCreatedAt(item.getSchedule().getCreatedAt());
                scheduleDomain_.setEndDate(item.getSchedule().getEndDate());
                scheduleDomain_.setProductId(item.getSchedule().getProductId());
                scheduleDomain_.setProviderMetaData(item.getSchedule().getProviderMetaData());
                scheduleDomain_.setProviderScheduleId(item.getSchedule().getProviderScheduleId());
                scheduleDomain_.setStartDate(item.getSchedule().getStartDate());
                scheduleDomain_.setStatus(item.getSchedule().getStatus());
                scheduleDomain_.setTitle(item.getSchedule().getTitle());
                scheduleDomain_.setTnc(item.getSchedule().getTnc());
                scheduleDomain_.setUpdatedAt(item.getSchedule().getUpdatedAt());
                scheduleDomain.setSchedule(scheduleDomain_);

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
