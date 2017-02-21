package com.tokopedia.seller.topads.view.helper;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;

import java.util.Locale;

/**
 * @author normansyahputa on 2/16/17.
 */
public class NumberOfChooseFooterHelper {

    public static final String numberOfChoosenFormat = "%d produk terpilih";
    private static final Locale locale = new Locale("in", "ID");
    private View view;
    private TextView btSelectionNumber;
    private TextView btSelectionDescription;

    public NumberOfChooseFooterHelper(View view) {
        this.view = view;

        initViews();
    }

    public static String getNumberOfChoose(int numberOfChoosen) {
        return String.format(locale, numberOfChoosenFormat, numberOfChoosen);
    }

    private void initViews() {
        btSelectionNumber = (TextView) view.findViewById(R.id.bt_selection_number);
        btSelectionDescription = (TextView) view.findViewById(R.id.bt_selection_description);
    }

    public void bindData(int numberOfChoosen, View.OnClickListener onExpandClickListener) {
        btSelectionNumber.setText(
                getNumberOfChoose(numberOfChoosen)
        );

        if (onExpandClickListener != null)
            view.setOnClickListener(onExpandClickListener);
    }
}
