package com.tokopedia.seller.shop.setting.data.mapper;

import com.tokopedia.core.database.model.DistrictDataDb;
import com.tokopedia.seller.shop.setting.domain.model.RecomendationDistrictDomainModel;
import com.tokopedia.seller.shop.setting.domain.model.RecommendationDistrictItemDomainModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by sebastianuskh on 3/22/17.
 */

public class DistrictDataDomainMapper implements Func1<List<DistrictDataDb>, RecomendationDistrictDomainModel> {
    private final String stringTyped;

    public DistrictDataDomainMapper(String stringTyped) {
        this.stringTyped = stringTyped;
    }

    @Override
    public RecomendationDistrictDomainModel call(List<DistrictDataDb> districtDataDbs) {


        RecomendationDistrictDomainModel domainModel = new RecomendationDistrictDomainModel();

        domainModel.setStringTyped(stringTyped);
        domainModel.setItems(mapItems(districtDataDbs));

        return domainModel;
    }

    private List<RecommendationDistrictItemDomainModel> mapItems(
            List<DistrictDataDb> districtDataDbs
    ) {
        List<RecommendationDistrictItemDomainModel> items = new ArrayList<>();
        for(DistrictDataDb dataDb : districtDataDbs){
            RecommendationDistrictItemDomainModel domainModel = mapDomainModel(dataDb);
            items.add(domainModel);
        }
        return items;
    }

    private RecommendationDistrictItemDomainModel mapDomainModel(DistrictDataDb dataDb) {
        RecommendationDistrictItemDomainModel domainModel =
                new RecommendationDistrictItemDomainModel();
        domainModel.setDistrictId(dataDb.getDistrictId());
        domainModel.setDistrictString(dataDb.getDistrictString());
        return domainModel;
    }
}
