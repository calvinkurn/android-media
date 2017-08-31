package com.tokopedia.seller.cache;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.annotation.UniqueGroup;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.seller.database.TkpdSellerDatabase;

/**
 * Created by normansyahputa on 8/9/17.
 */
@Table(database = TkpdSellerDatabase.class,
        uniqueColumnGroups = {@UniqueGroup(groupNumber = 1, uniqueConflict = ConflictAction.ABORT)})
public class CacheApiWhitelist extends BaseModel {
    @Column
    @Unique(unique = true)
    @PrimaryKey
    public String host;

    @Column
    @Unique(unique = true)
    @PrimaryKey
    public String path;

    @Column
    public long expiredTime;
}
