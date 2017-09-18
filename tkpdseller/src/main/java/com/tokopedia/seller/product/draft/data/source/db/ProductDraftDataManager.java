package com.tokopedia.seller.product.draft.data.source.db;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Update;
import com.tokopedia.seller.product.edit.data.source.db.model.ProductDraftDataBase;
import com.tokopedia.seller.product.edit.data.source.db.model.ProductDraftDataBase_Table;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductDraftDataManager {

    @Inject
    public ProductDraftDataManager() {
    }

    public Observable<Long> saveDraft(String json, long draftId, boolean isUploading){
        ProductDraftDataBase productDraftDataBase = new ProductDraftDataBase();
        productDraftDataBase.setData(json);
        productDraftDataBase.setId(draftId);
        productDraftDataBase.setUploading(isUploading);
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
            return Observable.error(new RuntimeException("Product draft not found in database"));
        }
        return Observable.just(productDraftDatabase.getData());
    }

    public Observable<List<ProductDraftDataBase>> getAllDraft() {
        return Observable.just( new Select()
                .from(ProductDraftDataBase.class)
                .where(ProductDraftDataBase_Table.is_uploading.is(false))
                .queryList());
    }

    public Observable<Long> getAllDraftCount() {
        return Observable.just( new Select(Method.count())
                .from(ProductDraftDataBase.class)
                .where(ProductDraftDataBase_Table.is_uploading.is(false))
                .count());
    }

    public Observable<Boolean> clearAllDraft(){
        new Delete().from(ProductDraftDataBase.class).execute();
        return Observable.just(true);
    }

    public Observable<Boolean> deleteDeraft(long productId) {
        ProductDraftDataBase productDraftDataBase = new Select()
                .from(ProductDraftDataBase.class)
                .where(ProductDraftDataBase_Table.id.is(productId))
                .querySingle();
        if (productDraftDataBase != null) {
            productDraftDataBase.delete();
            return Observable.just(true);
        }
        return Observable.just(false);
    }

    public Observable<Long> updateDraft(long productId, String draftData) {
        ProductDraftDataBase productDraftDataBase = new Select()
                .from(ProductDraftDataBase.class)
                .where(ProductDraftDataBase_Table.id.is(productId))
                .querySingle();
        if (productDraftDataBase != null){
            productDraftDataBase.setData(draftData);
            productDraftDataBase.save();
            return Observable.just(productDraftDataBase.getId());
        } else {
            throw new RuntimeException("Draft tidak ditemukan");
        }
    }

    public Observable<Long> updateDraft(long productId, String draftData, boolean isUploading) {
        ProductDraftDataBase productDraftDataBase = new Select()
                .from(ProductDraftDataBase.class)
                .where(ProductDraftDataBase_Table.id.is(productId))
                .querySingle();
        if (productDraftDataBase != null){
            productDraftDataBase.setData(draftData);
            productDraftDataBase.setUploading(isUploading);
            productDraftDataBase.save();
            return Observable.just(productDraftDataBase.getId());
        } else {
            throw new RuntimeException("Draft tidak ditemukan");
        }
    }

    public Observable<Boolean> updateUploadingStatusDraft(long productId, boolean isUploading) {
        if (productId != 0){
            ProductDraftDataBase productDraftDataBase = new Select()
                    .from(ProductDraftDataBase.class)
                    .where(ProductDraftDataBase_Table.id.is(productId))
                    .querySingle();
            if (productDraftDataBase != null){
                productDraftDataBase.setUploading(isUploading);
                productDraftDataBase.save();
                return Observable.just(true);
            } else {
                throw new RuntimeException("Draft tidak ditemukan");
            }
        } else { // update all isUploading
            new Update<>(ProductDraftDataBase.class)
                    .set(ProductDraftDataBase_Table.is_uploading.eq(isUploading))
                    .where(ProductDraftDataBase_Table.is_uploading.is(!isUploading))
                    .execute();
            return Observable.just(true);
        }
    }


}
