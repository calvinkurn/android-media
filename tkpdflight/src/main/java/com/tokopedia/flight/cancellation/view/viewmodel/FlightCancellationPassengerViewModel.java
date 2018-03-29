package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by furqan on 23/03/18.
 */

public class FlightCancellationPassengerViewModel implements Parcelable{

    private String passengerId;
    private int type;
    private int title;
    private String titleString;
    private String firstName;
    private String lastName;

    protected FlightCancellationPassengerViewModel(Parcel in) {
        passengerId = in.readString();
        type = in.readInt();
        title = in.readInt();
        titleString = in.readString();
        firstName = in.readString();
        lastName = in.readString();
    }

    public static final Creator<FlightCancellationPassengerViewModel> CREATOR = new Creator<FlightCancellationPassengerViewModel>() {
        @Override
        public FlightCancellationPassengerViewModel createFromParcel(Parcel in) {
            return new FlightCancellationPassengerViewModel(in);
        }

        @Override
        public FlightCancellationPassengerViewModel[] newArray(int size) {
            return new FlightCancellationPassengerViewModel[size];
        }
    };

    public FlightCancellationPassengerViewModel() {
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getTitleString() {
        return titleString;
    }

    public void setTitleString(String titleString) {
        this.titleString = titleString;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(passengerId);
        parcel.writeInt(type);
        parcel.writeInt(title);
        parcel.writeString(titleString);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
    }
}
