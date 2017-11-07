package com.tokopedia.flight.detail.util;

import android.content.Context;
import android.support.annotation.DrawableRes;

import com.tokopedia.flight.R;

/**
 * Created by zulfikarrahman on 11/6/17.
 */

public class FlightAirlineIconUtil {

    @DrawableRes
    public static int getImageResource(String idAirline){
        switch (idAirline){
            case  "JT" :
                return R.drawable.ic_lion_air;
            case "GA":
                return R.drawable.ic_garuda;
            case "QZ":
                return R.drawable.ic_airasia;
            case "SJ":
                return R.drawable.ic_sriwijaya;
            case "ID":
                return R.drawable.ic_batik_air;
            case "QG":
                return R.drawable.ic_citilink;
            default:
                return R.drawable.ic_airline_default;
        }
    }
}
