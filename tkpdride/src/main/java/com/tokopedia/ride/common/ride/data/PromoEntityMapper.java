package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.bookingride.domain.model.Promo;
import com.tokopedia.ride.common.ride.data.entity.PromoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 3/31/17.
 */

public class PromoEntityMapper {

    public PromoEntityMapper() {
    }


    public List<Promo> transform(List<PromoEntity> entities) {
        List<Promo> promos = new ArrayList<>();
        Promo promo;
        for (PromoEntity entity : entities) {
            promo = transform(entity);
            if (promo != null) {
                promos.add(promo);
            }
        }
        return promos;
    }

    private Promo transform(PromoEntity entity) {
        Promo promo = null;
        if (entity != null) {
            promo = new Promo();
            promo.setCode(entity.getCode());
            promo.setOffer(entity.getOffer());
            promo.setUrl(entity.getUrl());
        }
        return promo;
    }
}
