package com.tokopedia.seller.product.data.source.db;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.seller.product.data.source.db.model.CategoryDataBase;
import com.tokopedia.seller.product.di.scope.CategoryPickerScope;

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

    public Observable<List<CategoryDataBase>> fetchFromDatabase() {
        return Observable.just(new Select().from(CategoryDataBase.class).queryList());
    }

    public void storeData(List<CategoryDataBase> categoryDataBases) {



    }
}
