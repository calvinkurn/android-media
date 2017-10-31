package com.tokopedia.flight.detail.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.flight.R;
import com.tokopedia.flight.search.data.cloud.model.Amenity;

/**
 * Created by zulfikarrahman on 10/31/17.
 */

public class FlightDetailFacilityAmenityViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageAmenity;
    private TextView textAmenity;

    public FlightDetailFacilityAmenityViewHolder(View itemView) {
        super(itemView);
        imageAmenity = (ImageView) itemView.findViewById(R.id.image_amenity);
        textAmenity = (TextView) itemView.findViewById(R.id.text_amenity);
    }

    public void bindData(Amenity amenity) {
        textAmenity.setText(amenity.getLabel());
    }
}
