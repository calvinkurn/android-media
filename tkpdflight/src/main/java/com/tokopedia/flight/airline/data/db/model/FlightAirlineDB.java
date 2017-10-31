package com.tokopedia.flight.airline.data.db.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.flight.airline.data.cloud.model.AirlineData;
import com.tokopedia.flight.common.database.TkpdFlightDatabase;

/**
 * @author sebastianuskh on 4/13/17.
 */
@Table(database = TkpdFlightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightAirlineDB extends BaseModel {

    @PrimaryKey
    @Column(name = "id")
    String id;

    @Column(name = "name")
    String name;

    @Column(name = "logo")
    String logo;

    public FlightAirlineDB(){

    }
    public FlightAirlineDB(AirlineData airlineData) {
        this.id = airlineData.getId();
        this.name = airlineData.getAttributes().getName();
        this.logo = airlineData.getAttributes().getLogo();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }
}
