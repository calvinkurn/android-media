package com.tokopedia.flight.common.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Nathaniel on 11/24/2016.
 */

@Database(name = TkpdFlightDatabase.NAME, version = TkpdFlightDatabase.VERSION, foreignKeysSupported = true)
public class TkpdFlightDatabase {
    public static final String NAME = "tkpd_flight";

    public static final int VERSION = 1;
}