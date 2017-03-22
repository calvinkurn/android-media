package com.tokopedia.seller.shop.setting.data.source.cache.db;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.model.DistrictDataDb;
import com.tokopedia.core.database.model.DistrictDataDb_Table;
import com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict.DistrictModel;
import com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict.OpenShopDistrictServiceModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/21/17.
 */

public class DistrictDataManager {

    @Inject
    public DistrictDataManager() {
    }

    public void storeDistrictData(OpenShopDistrictServiceModel serviceModel) {

        DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
        database.beginTransaction();
        try {
            processServiceModel(serviceModel);
            database.setTransactionSuccessful();
        } catch (Exception e){
            throw new DistrictDataManagerException();
        } finally{
            database.endTransaction();
        }

    }

    private void processServiceModel(OpenShopDistrictServiceModel serviceModel) {
        for (DistrictModel districtModel : serviceModel.getData().getDistrictModels()) {
            String districtString = "";
            int id = 0;
            processDistrictModel(districtModel, id, districtString);
        }
    }

    private void processDistrictModel(DistrictModel districtModel, int id, String districtString) {
        districtString += districtModel.getName();
        id = districtModel.getId();
        if (districtModel.getChild() != null && !districtModel.getChild().isEmpty()) {
            districtString += ", ";
            for (DistrictModel child : districtModel.getChild()) {
                processDistrictModel(child, id, districtString);
            }
        } else {
            storeDistrictDatabase(id, districtString);
        }
    }

    private void storeDistrictDatabase(int id, String name) {
        DistrictDataDb dataDb = new DistrictDataDb();
        dataDb.setDistrictId(id);
        dataDb.setDistrictString(name);
        dataDb.save();
    }

    public boolean checkDataIsEmpty() {
        if (new Select().from(DistrictDataDb.class).count() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void clearData() {
        Delete.table(DistrictDataDb.class);
    }

    public Observable<List<DistrictDataDb>> getRecommendationDistrict(String typedString) {
        ConditionGroup conditionalGroup = generateConditionGroup(typedString);
        return Observable.just(new Select()
                .from(DistrictDataDb.class)
                .where(conditionalGroup)
                .limit(15)
                .queryList());
    }

    private ConditionGroup generateConditionGroup(String typedString) {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        String[] words = typedString.split("\\W+");
        for (String word : words){
            conditionGroup.and(DistrictDataDb_Table
                    .districtString.like("%" + word + "%"));
        }
        return conditionGroup;
    }
}
