package com.tokopedia.gm.common.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Nathaniel on 11/24/2016.
 */

@Database(name = TkpdGMDatabase.NAME, version = TkpdGMDatabase.VERSION, foreignKeysSupported = true)
public class TkpdGMDatabase {
    public static final String NAME = "tkpd_gm";

    public static final int VERSION = 1;
}