package com.tokopedia.flight.airline.data.db.model;

import android.os.Parcel;
import android.os.Parcelable;

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
public class FlightAirlineDB extends BaseModel implements Parcelable {

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

    public FlightAirlineDB(String id, String name, String logo) {
        this.id = id;
        this.name = name;
        this.logo = logo;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.logo);
    }

    protected FlightAirlineDB(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.logo = in.readString();
    }

    public static final Parcelable.Creator<FlightAirlineDB> CREATOR = new Parcelable.Creator<FlightAirlineDB>() {
        @Override
        public FlightAirlineDB createFromParcel(Parcel source) {
            return new FlightAirlineDB(source);
        }

        @Override
        public FlightAirlineDB[] newArray(int size) {
            return new FlightAirlineDB[size];
        }
    };
}
