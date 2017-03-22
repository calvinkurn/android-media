package com.tokopedia.seller.shop.setting.data.mapper;

import com.tokopedia.core.database.model.DistrictDataDb;
import com.tokopedia.seller.shop.setting.domain.model.RecomendationDistrictDomainModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by sebastianuskh on 3/22/17.
 */

public class DistrictDataDomainMapper implements Func1<List<DistrictDataDb>, List<RecomendationDistrictDomainModel>> {
    @Override
    public List<RecomendationDistrictDomainModel> call(List<DistrictDataDb> districtDataDbs) {

        List<RecomendationDistrictDomainModel> domainModelList = new ArrayList<>();
        for(DistrictDataDb dataDb : districtDataDbs){
            RecomendationDistrictDomainModel domainModel = mapDomainModel(dataDb);
            domainModelList.add(domainModel);

        }
        return domainModelList;
    }

    private RecomendationDistrictDomainModel mapDomainModel(DistrictDataDb dataDb) {
        RecomendationDistrictDomainModel domainModel = new RecomendationDistrictDomainModel();
        domainModel.setDistrictId(dataDb.getDistrictId());
        domainModel.setDistrictString(dataDb.getDistrictString());
        return domainModel;
    }
}
