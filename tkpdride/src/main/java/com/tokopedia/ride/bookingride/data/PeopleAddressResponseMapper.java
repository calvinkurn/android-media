package com.tokopedia.ride.bookingride.data;

import com.tokopedia.ride.bookingride.data.entity.PeopleAddressEntity;
import com.tokopedia.ride.bookingride.data.entity.PeopleAddressPagingEntity;
import com.tokopedia.ride.bookingride.data.entity.PeopleAddressResponse;
import com.tokopedia.ride.bookingride.domain.model.Paging;
import com.tokopedia.ride.bookingride.domain.model.PeopleAddress;
import com.tokopedia.ride.bookingride.domain.model.PeopleAddressWrapper;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by alvarisi on 4/6/17.
 */

public class PeopleAddressResponseMapper implements Func1<PeopleAddressResponse, PeopleAddressWrapper> {
    @Override
    public PeopleAddressWrapper call(PeopleAddressResponse peopleAddressResponse) {
        return transformPeopleAddressWrapper(peopleAddressResponse);
    }

    private PeopleAddressWrapper transformPeopleAddressWrapper(PeopleAddressResponse response) {
        PeopleAddressWrapper wrapper = new PeopleAddressWrapper();
        wrapper.setAddresses(transformAddresses(response.getList()));
        wrapper.setPaging(transformPaging(response.getPaging()));
        return wrapper;
    }

    private Paging transformPaging(PeopleAddressPagingEntity entity) {
        Paging paging = null;
        if (entity != null){
            paging = new Paging();
            paging.setNextUrl(entity.getUriNext());
        }
        return paging;
    }

    private List<PeopleAddress> transformAddresses(List<PeopleAddressEntity> list) {
        List<PeopleAddress> peopleAddresses = new ArrayList<>();
        PeopleAddress peopleAddress;
        for (PeopleAddressEntity entity : list){
            peopleAddress = transformAddress(entity);
            if (peopleAddress != null)
                peopleAddresses.add(peopleAddress);
        }
        return peopleAddresses;
    }

    private PeopleAddress transformAddress(PeopleAddressEntity entity) {
        PeopleAddress peopleAddress = null;
        if (entity != null){
            peopleAddress = new PeopleAddress();
            peopleAddress.setAddressId(entity.getAddressId());
            peopleAddress.setAddressName(entity.getAddressName());
            peopleAddress.setAddressStatus(entity.getAddressStatus());
            peopleAddress.setAddressStreet(entity.getAddressStreet());
            peopleAddress.setCityId(entity.getCityId());
            peopleAddress.setCityName(entity.getCityName());
            peopleAddress.setCountryName(entity.getCountryName());
            peopleAddress.setDistrictName(entity.getDistrictName());
            peopleAddress.setDistrictId(entity.getDistrictId());
            peopleAddress.setLatitude(entity.getLatitude());
            peopleAddress.setLongitude(entity.getLongitude());
            peopleAddress.setAddressName(entity.getAddressName());
            peopleAddress.setPostalCode(entity.getPostalCode());
            peopleAddress.setProvinceId(entity.getProvinceId());
            peopleAddress.setProvinceName(entity.getProvinceName());
            peopleAddress.setReceiverName(entity.getReceiverName());
            peopleAddress.setReceiverPhone(entity.getReceiverPhone());
        }
        return peopleAddress;
    }
}
