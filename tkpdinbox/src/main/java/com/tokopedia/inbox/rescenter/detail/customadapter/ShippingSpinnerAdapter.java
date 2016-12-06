package com.tokopedia.inbox.rescenter.detail.customadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.ResCenterKurir;

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
            holder.textView.setText(getContext().getString(R.string.index_spinner_0_default));
        } else {
            holder.textView.setText(getItem(position - 1).getShipmentName());
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
            holderDropDown.textView.setText(getContext().getString(R.string.index_spinner_0_default));
        } else {
            holderDropDown.textView.setText(getItem(position - 1).getShipmentName());
        }

        return localView;
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }
}
