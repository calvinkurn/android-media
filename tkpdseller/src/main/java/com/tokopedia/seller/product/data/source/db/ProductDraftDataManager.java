package com.tokopedia.seller.product.data.source.db;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.seller.product.data.source.db.model.ProductDraftDataBase;
import com.tokopedia.seller.product.data.source.db.model.ProductDraftDataBase_Table;

import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductDraftDataManager {

    @Inject
    public ProductDraftDataManager() {
    }

    public Observable<Long> saveDraft(String json, long draftId){
        ProductDraftDataBase productDraftDataBase = new ProductDraftDataBase();
        productDraftDataBase.setData(json);
        productDraftDataBase.setId(draftId);
        productDraftDataBase.save();
        return Observable.just(productDraftDataBase.getId());
    }

    public Observable<String> getDraft(long productId) {
        ProductDraftDataBase productDraftDatabase =
                new Select()
                        .from(ProductDraftDataBase.class)
                        .where(ProductDraftDataBase_Table.id.is(productId))
                        .querySingle();
        if (productDraftDatabase == null){
            throw new RuntimeException("Product draft not found in database");
        }
        return Observable.just(productDraftDatabase.getData());
    }

    public void clearAllDraft(){
        new Delete().from(ProductDraftDataBase.class).execute();
    }
}
