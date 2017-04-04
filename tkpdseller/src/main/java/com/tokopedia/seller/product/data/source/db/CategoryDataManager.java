package com.tokopedia.seller.product.data.source.db;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.tokopedia.seller.product.data.source.db.model.CategoryDataBase;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class CategoryDataManager {
    public void clearDatabase() {
         new Delete().from(CategoryDataBase.class).execute();

    }
}
