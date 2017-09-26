package com.tokopedia.seller.product.edit.data.source.db.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.seller.database.TkpdSellerDatabase;

/**
 * @author sebastianuskh on 4/13/17.
 */
@Table(database = TkpdSellerDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class ProductDraftDataBase extends BaseModel{
    public static final String COLUMN_IS_UPLOADING = "is_uploading";
    public static final String COLUMN_SHOP_ID = "shopId";

    @PrimaryKey(autoincrement = true)
    @Column
    long id;

    @Column
    String data;

    @Column (name = COLUMN_IS_UPLOADING)
    boolean isUploading;

    @Column (name = COLUMN_SHOP_ID)
    String shopId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isUploading() {
        return isUploading;
    }

    public void setUploading(boolean uploading) {
        isUploading = uploading;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
