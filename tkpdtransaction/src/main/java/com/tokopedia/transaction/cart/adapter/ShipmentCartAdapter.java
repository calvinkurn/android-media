package com.tokopedia.transaction.cart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.transaction.cart.model.calculateshipment.Shipment;

import java.util.ArrayList;

/**
 * @author  by alvarisi on 11/11/16.
 */

public class ShipmentCartAdapter extends ArrayAdapter<Shipment> {
    private ArrayList<Shipment> mShipments;
    private static class ViewHolder {
        TextView shipment;
    }

    public static ShipmentCartAdapter newInstance(Context context){
        ArrayList<Shipment> shipments = new ArrayList<>();
        return new ShipmentCartAdapter(context, shipments);
    }

    private ShipmentCartAdapter(Context context, ArrayList<Shipment> shipments){
        super(context, android.R.layout.simple_spinner_dropdown_item, shipments);
        this.mShipments = shipments;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Shipment shipment = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
            viewHolder.shipment = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.shipment.setText(shipment.getShipmentName());

        return convertView;
    }

    public void setAdapterData(ArrayList<Shipment> shipments){
        this.mShipments.clear();
        this.mShipments.addAll(shipments);
        notifyDataSetChanged();
    }
}
