package com.tokopedia.flight.search.view.textwatcher;

import android.text.Editable;
import android.widget.EditText;

import com.tokopedia.design.text.watcher.AfterTextWatcher;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.util.DurationUtil;
import com.tokopedia.flight.search.view.model.Duration;

/**
 * Created by User on 11/10/2017.
 */

public class DurationTextWatcher extends AfterTextWatcher {

    private EditText editText;
    public DurationTextWatcher (EditText editText){
        this.editText = editText;
    }
    @Override
    public void afterTextChanged(Editable s) {
        try {
            int durationMinute = Integer.parseInt(s.toString());
            Duration duration = DurationUtil.convertFormMinute(durationMinute);
            editText.removeTextChangedListener(this);
            editText.setText(DurationUtil.getReadableString(editText.getContext(), duration));
            editText.addTextChangedListener(this);
        } catch (Exception e) {
            // no op
        }
    }
}