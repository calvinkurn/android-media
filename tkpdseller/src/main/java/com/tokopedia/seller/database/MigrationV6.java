package com.tokopedia.seller.database;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.tokopedia.seller.product.edit.data.source.db.model.ProductDraftDataBase;

/**
 * Created by zulfikarrahman on 2/5/18.
 */

/**
 * From version 5 to 6, add column version
 */
@Migration(version = 6, database = TkpdSellerDatabase.class)
public class MigrationV6 extends AlterTableMigration<ProductDraftDataBase> {
    public MigrationV6(Class<ProductDraftDataBase> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();
        addColumn(SQLiteType.INTEGER, ProductDraftDataBase.COLUMN_VERSION);
    }
}
