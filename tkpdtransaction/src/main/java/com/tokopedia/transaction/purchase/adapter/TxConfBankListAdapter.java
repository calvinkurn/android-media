package com.tokopedia.transaction.purchase.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.core.database.model.Bank;

import java.util.List;

/**
 * @author Angga.Prasetiyo on 23/06/2016.
 */
public class TxConfBankListAdapter extends ArrayAdapter<Bank> {
    private final int resourceId;
    private final LayoutInflater inflater;

    public TxConfBankListAdapter(Context context, int resource, List<Bank> objects) {
        super(context, resource, objects);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) convertView = inflater.inflate(resourceId, parent, false);
        if (convertView instanceof TextView)
            ((TextView) convertView).setText(getItem(position).getBankName());
        return convertView;
    }
}
