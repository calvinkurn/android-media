package com.tokopedia.session.register.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nisie on 1/27/17.
 */

public class AutoCompleteTextAdapter extends ArrayAdapter<String> {

    public interface onTextSelectedListener{
        void onSelected(String selected);
    }

    private onTextSelectedListener listener;

    public AutoCompleteTextAdapter(Context context, int resource, List<String> objects, onTextSelectedListener listener) {
        super(context, resource, objects);
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        final TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            listener.onSelected(textView.getText().toString());
            }
        });
        return view;
    }
}
