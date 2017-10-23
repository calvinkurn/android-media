package com.tokopedia.flight.airport.data.source.db;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.flight.common.database.TkpdFightDatabase;

/**
 * @author sebastianuskh on 4/13/17.
 */
@Table(database = TkpdFightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightAirportDB extends BaseModel implements ItemType {

    private static final int TYPE = 12345;

    @PrimaryKey()
    @Column(name = "country_id")
    String countryId;

    @PrimaryKey()
    @Column(name = "city_id")
    String cityId;

    @PrimaryKey()
    @Column(name = "airport_id")
    String airportId;

    @Override
    public int getType() {
        return TYPE;
    }
}
