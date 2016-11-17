package com.tokopedia.transaction.cart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.tokopedia.transaction.cart.model.calculateshipment.ShipmentPackage;

import java.util.ArrayList;

/**
 * @author by alvarisi on 11/11/16.
 */

public class ShipmentPackageCartAdapter extends ArrayAdapter<ShipmentPackage> {
    private ArrayList<ShipmentPackage> mShipmentPackages;

    private static class ViewHolder {
        TextView shipment;
    }

    public static ShipmentPackageCartAdapter newInstance(Context context) {
        ArrayList<ShipmentPackage> shipmentPackages = new ArrayList<>();
        return new ShipmentPackageCartAdapter(context, shipmentPackages);
    }

    private ShipmentPackageCartAdapter(Context context, ArrayList<ShipmentPackage> shipmentPackages) {
        super(context, android.R.layout.simple_spinner_dropdown_item, shipmentPackages);
        mShipmentPackages = shipmentPackages;
    }

    @NonNull
    @Override
    public View getView(int position, View convertnView, @NonNull ViewGroup parent) {
        ShipmentPackage shipment = getItem(position);
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

        viewHolder.shipment.setText(shipment.getName() != null ? shipment.getName() : "");

        return convertView;
    }

    public void setAdapterData(ArrayList<ShipmentPackage> shipmentPackages) {
        this.mShipmentPackages.clear();
        this.mShipmentPackages.addAll(shipmentPackages);
        notifyDataSetChanged();
    }
}
