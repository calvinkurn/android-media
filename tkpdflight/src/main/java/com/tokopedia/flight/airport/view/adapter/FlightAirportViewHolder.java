package com.tokopedia.flight.airport.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightAirportViewHolder extends AbstractViewHolder<FlightAirportDB> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_airport;

    private TextView city;
    private TextView airport;

    private FilterTextListener filterTextListener;
    private ForegroundColorSpan boldColor;

    public interface FilterTextListener {
        String getFilterText();
    }

    public FlightAirportViewHolder(View itemView, FilterTextListener filterTextListener) {
        super(itemView);
        city = (TextView) itemView.findViewById(R.id.city);
        airport = (TextView) itemView.findViewById(R.id.airport);
        this.filterTextListener = filterTextListener;
        boldColor = new ForegroundColorSpan(ContextCompat.getColor(itemView.getContext(),R.color.font_black_primary_70));
    }

    @Override
    public void bind(FlightAirportDB flightAirportDB) {
        Context context = itemView.getContext();
        String filterText = filterTextListener.getFilterText();

        String cityStr = context.getString(R.string.flight_label_city,
                flightAirportDB.getCityName(), flightAirportDB.getCountryName());
        city.setText(getSpandableBoldText(cityStr, filterText));

        if (!TextUtils.isEmpty(flightAirportDB.getAirportId())) {
            String airportString = context.getString(R.string.flight_label_airport,
                    flightAirportDB.getAirportId(), flightAirportDB.getAirportName());
            airport.setText(getSpandableBoldText(airportString, filterText));
        } else {
            String airportString = context.getString(R.string.flight_labe_all_airport);
            airport.setText(airportString);
        }
    }

    private CharSequence getSpandableBoldText(String strToPut, String stringToBold) {
        int indexStartBold = -1;
        int indexEndBold = -1;
        if (TextUtils.isEmpty(stringToBold)) {
            return strToPut;
        }
        String strToPutLowerCase = strToPut.toLowerCase();
        String strToBoldLowerCase = stringToBold.toLowerCase();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(strToPut);
        indexStartBold = strToPutLowerCase.indexOf(strToBoldLowerCase);
        if (indexStartBold != -1) {
            indexEndBold = indexStartBold + stringToBold.length();
        }
        if (indexStartBold == -1) {
            return spannableStringBuilder;
        } else {
            spannableStringBuilder.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    indexStartBold, indexEndBold, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.setSpan(boldColor, indexStartBold, indexEndBold,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableStringBuilder;
        }

    }
}
