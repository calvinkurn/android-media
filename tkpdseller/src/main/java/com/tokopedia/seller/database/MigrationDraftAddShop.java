package com.tokopedia.seller.database;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.tokopedia.seller.product.edit.data.source.db.model.ProductDraftDataBase;

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
