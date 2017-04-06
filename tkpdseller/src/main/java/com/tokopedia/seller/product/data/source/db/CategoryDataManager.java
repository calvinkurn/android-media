package com.tokopedia.seller.product.data.source.db;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.seller.product.data.source.db.model.CategoryDataBase;
import com.tokopedia.seller.product.data.source.db.model.CategoryDataBase_Table;
import com.tokopedia.seller.product.di.scope.CategoryPickerScope;
import com.tokopedia.seller.product.domain.model.CategoryDomainModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/3/17.
 */
@CategoryPickerScope
public class CategoryDataManager {

    @Inject
    public CategoryDataManager() {
    }

    public void clearDatabase() {
        new Delete().from(CategoryDataBase.class).execute();

    }

    public Observable<List<CategoryDataBase>> fetchCategoryFromParent(int parentId) {
        ConditionGroup conditionGroup = ConditionGroup.clause()
                .and(CategoryDataBase_Table.parentId.eq(parentId));
        return Observable.just(new Select()
                .from(CategoryDataBase.class)
                .where(conditionGroup)
                .orderBy(CategoryDataBase_Table.weight, true)
                .queryList()
        );
    }

    public Observable<List<CategoryDataBase>> fetchFromDatabase() {
        return Observable.just(new Select().from(CategoryDataBase.class).queryList());
    }

    public void storeData(List<CategoryDataBase> categoryDataBases) {

        DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
        database.beginTransaction();
        try{
            for(CategoryDataBase categoryDataBase : categoryDataBases){
                categoryDataBase.save();
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

    }
}
