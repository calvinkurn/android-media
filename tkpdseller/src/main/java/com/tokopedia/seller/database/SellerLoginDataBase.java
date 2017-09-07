package com.tokopedia.seller.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.core.util.SessionHandler;

/**
 * @author sebastianuskh on 4/13/17.
 */
@Table(database = TkpdSellerDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class SellerLoginDataBase extends BaseModel{

    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    @PrimaryKey
    @Column (name = COLUMN_USER_ID)
    long userId;

    @Column (name = COLUMN_TIMESTAMP)
    long timeStamp;

    public long getUserId() {
        return userId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
