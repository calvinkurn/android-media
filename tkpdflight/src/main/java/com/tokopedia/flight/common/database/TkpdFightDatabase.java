package com.tokopedia.flight.common.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Nathaniel on 11/24/2016.
 */

@Database(name = TkpdFightDatabase.NAME, version = TkpdFightDatabase.VERSION, foreignKeysSupported = true)
public class TkpdFightDatabase {
    public static final String NAME = "tkpd_flight";

    public static final int VERSION = 1;
}