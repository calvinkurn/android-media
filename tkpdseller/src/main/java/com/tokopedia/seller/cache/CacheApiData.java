package com.tokopedia.seller.cache;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.UniqueGroup;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.seller.database.TkpdSellerDatabase;

/**
 * Created by normansyahputa on 8/9/17.
 */
@Table(database = TkpdSellerDatabase.class,
        uniqueColumnGroups = {@UniqueGroup(groupNumber = 1, uniqueConflict = ConflictAction.REPLACE)})
public class CacheApiData extends BaseModel {
    @ContainerKey("unique_id")
    @Column
    @PrimaryKey(autoincrement = true)
    public long uniqueId;

    @Column
    public String host;

    @Column
    public String path;

    @Column
    public String method;

    @ContainerKey("request_param")
    @Column
    public String requestParam;

    @ContainerKey("response_body")
    @Column
    public String responseBody;


}
