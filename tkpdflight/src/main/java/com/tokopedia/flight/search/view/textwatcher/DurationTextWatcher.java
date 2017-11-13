package com.tokopedia.flight.search.view.textwatcher;

import android.text.Editable;
import android.widget.EditText;

import com.tokopedia.design.text.watcher.AfterTextWatcher;
import com.tokopedia.flight.R;

/**
 * Created by User on 11/10/2017.
 */

public class DurationTextWatcher extends AfterTextWatcher {

    public static final int MINUTE_PER_DAY = 1440; // 60*24
    public static final int MINUTE_PER_HOUR = 60; // 60*24

    private EditText editText;
    public DurationTextWatcher (EditText editText){
        this.editText = editText;
    }
    @Override
    public void afterTextChanged(Editable s) {
        try {
            int duration = Integer.parseInt(s.toString());
            int day = duration / MINUTE_PER_DAY;
            int durationModDay = duration - day * MINUTE_PER_DAY;
            int hour = durationModDay / MINUTE_PER_HOUR;
            int minute = durationModDay - (hour * MINUTE_PER_HOUR);
            editText.removeTextChangedListener(this);
            if (day > 0) {
                editText.setText(editText.getContext().getString(R.string.duration_flight_dd_hh_mm, day, hour, minute));
            } else {
                editText.setText(editText.getContext().getString(R.string.duration_flight_hh_mm, hour, minute));
            }
            editText.addTextChangedListener(this);
        } catch (Exception e) {
            // no op
        }
    }
}