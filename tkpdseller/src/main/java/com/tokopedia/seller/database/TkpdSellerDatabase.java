package com.tokopedia.seller.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Nathaniel on 11/24/2016.
 */

@Database(name = TkpdSellerDatabase.NAME, version = TkpdSellerDatabase.VERSION, foreignKeysSupported = true)
public class TkpdSellerDatabase {
    public static final String NAME = "tkpd_seller";

    public static final int VERSION = 6;
}