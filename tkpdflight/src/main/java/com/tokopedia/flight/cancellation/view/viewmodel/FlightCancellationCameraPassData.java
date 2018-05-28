package com.tokopedia.flight.cancellation.view.viewmodel;

import android.content.Intent;

/**
 * @author  by alvarisi on 3/28/18.
 */

public class FlightCancellationCameraPassData {
    private Intent destinationIntent;
    private String imagePathLoc;

    public FlightCancellationCameraPassData() {
    }

    public Intent getDestinationIntent() {
        return destinationIntent;
    }

    public void setDestinationIntent(Intent destinationIntent) {
        this.destinationIntent = destinationIntent;
    }

    public String getImagePathLoc() {
        return imagePathLoc;
    }

    public void setImagePathLoc(String imagePathLoc) {
        this.imagePathLoc = imagePathLoc;
    }
}
