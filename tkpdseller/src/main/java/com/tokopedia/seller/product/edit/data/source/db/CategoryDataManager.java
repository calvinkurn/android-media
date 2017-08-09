package com.tokopedia.seller.product.edit.data.source.db;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tokopedia.seller.database.TkpdSellerDatabase;
import com.tokopedia.seller.product.edit.data.source.db.model.CategoryDataBase;
import com.tokopedia.seller.product.edit.data.source.db.model.CategoryDataBase_Table;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;


/**
 * @author sebastianuskh on 4/3/17.
 */
public class CategoryDataManager {

    @Inject
    public CategoryDataManager() {

    }

    public void clearDatabase() {
        new Delete().from(CategoryDataBase.class).execute();
    }

    public List<CategoryDataBase> fetchCategoryFromParent(long categoryId) {
        ConditionGroup conditionGroup = ConditionGroup.clause().and(CategoryDataBase_Table.parentId.eq(categoryId));
        return new Select()
                .from(CategoryDataBase.class)
                .where(conditionGroup)
                .orderBy(CategoryDataBase_Table.weight, true)
                .queryList();
    }

    public List<CategoryDataBase> fetchFromDatabase() {
        return new Select().from(CategoryDataBase.class).queryList();
    }

    public Observable<String> getCategoryName(long categoryId){
        CategoryDataBase categoryDataBase =
                new Select().from(CategoryDataBase.class)
                .where(CategoryDataBase_Table.id.is(categoryId))
                .querySingle();
        if (categoryDataBase != null) {
            return Observable.just(categoryDataBase.getName());
        } else {
            return null;
        }
    }

    public void storeData(List<CategoryDataBase> categoryDataBases) {

        DatabaseWrapper database = FlowManager.getDatabase(TkpdSellerDatabase.NAME).getWritableDatabase();
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

    public CategoryDataBase fetchCategoryWithId(long categoryId) {
        CategoryDataBase categoryDataBase = new Select()
                .from(CategoryDataBase.class)
                .where(CategoryDataBase_Table.id.like(categoryId))
                .querySingle();
        if (categoryDataBase != null) {
            return categoryDataBase;
        } else {
            throw new RuntimeException("No category found");
        }
    }
}
