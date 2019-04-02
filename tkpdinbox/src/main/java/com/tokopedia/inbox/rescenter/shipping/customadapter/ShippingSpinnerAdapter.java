package com.tokopedia.inbox.rescenter.shipping.customadapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.inbox.rescenter.shipping.model.ResCenterKurir;

import java.util.List;

/**
 * Created on 3/4/16.
 */
public class ShippingSpinnerAdapter extends ArrayAdapter<ResCenterKurir.Kurir> {

    public class ViewHolder {
        public TextView textView;
    }

    public ShippingSpinnerAdapter(Context context, int resource, List<ResCenterKurir.Kurir> kurirList) {
        super(context, resource, kurirList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
            holder.textView = (TextView) convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            holder.textView.setText(getContext().getString(R.string.index_spinner_0_shipping));
        } else {
            holder.textView.setText(Html.fromHtml(getItem(position - 1).getShipmentName()).toString());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holderDropDown = new ViewHolder();
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        View localView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        holderDropDown.textView = (TextView) localView.findViewById(android.R.id.text1);

        if (position == 0) {
            holderDropDown.textView.setText(getContext().getString(R.string.index_spinner_0_shipping));
        } else {
            holderDropDown.textView.setText(Html.fromHtml(getItem(position - 1).getShipmentName()).toString());
        }

        return localView;
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }
}
