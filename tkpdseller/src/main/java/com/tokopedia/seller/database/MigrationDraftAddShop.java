package com.tokopedia.seller.database;

import android.content.Context;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.language.Update;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.product.edit.data.source.db.model.ProductDraftDataBase;
import com.tokopedia.seller.product.edit.data.source.db.model.ProductDraftDataBase_Table;

/**
 * Created by hendry on 6/22/2017.
 */

/**
 * From version 4 to 5, add column userId and tm
 */
@Migration(version = 5, database = TkpdSellerDatabase.class)
public class MigrationDraftAddShop extends AlterTableMigration<ProductDraftDataBase> {

    public MigrationDraftAddShop(Class<ProductDraftDataBase> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();
        addColumn(SQLiteType.TEXT, ProductDraftDataBase.COLUMN_SHOP_ID);
    }
}
