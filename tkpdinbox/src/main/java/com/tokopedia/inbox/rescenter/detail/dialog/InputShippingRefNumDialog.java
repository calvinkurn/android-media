package com.tokopedia.inbox.rescenter.detail.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.R;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.inbox.rescenter.detail.customadapter.ShippingSpinnerAdapter;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.ResCenterKurir;
import com.tokopedia.inbox.rescenter.utils.LocalCacheManager;

import java.util.List;

/**
 * Created by hangnadi on 3/4/16.
 */
public class InputShippingRefNumDialog {

    private final Context context;
    private final Dialog dialog;
    private final String resolutionID;
    private final ResCenterKurir resCenterKurirList;
    private final List<ResCenterKurir.Kurir> kurirList;
    private EditText referenceNumber;
    private Spinner shippingSpinner;
    private TextView errorShippingSpinner;
    private View confirmButton;
    private View scanButton;
    private ShippingSpinnerAdapter shippingAdapter;
    private LocalCacheManager.ReturnPackage cache;

    public interface Listener {
        void onSubmitButtonClick();
        void onScanButtonClick();
    }

    public InputShippingRefNumDialog(Context context, String resolutionID) {
        this.context = context;
        this.dialog = new Dialog(context);
        this.resolutionID = resolutionID;
        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        resCenterKurirList =
                CacheUtil.convertStringToModel(globalCacheManager.getValueString(resolutionID),
                        new TypeToken<ResCenterKurir>() {}.getType());

        this.kurirList = resCenterKurirList.getList();

    }

    public static InputShippingRefNumDialog Builder(Context context, String resolutionID) {
        return new InputShippingRefNumDialog(context, resolutionID);
    }

    public InputShippingRefNumDialog initView() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_ref_rescenter);

        referenceNumber = (EditText) dialog.findViewById(R.id.ref_number);
        shippingSpinner = (Spinner) dialog.findViewById(R.id.spinner_kurir);
        errorShippingSpinner = (TextView) dialog.findViewById(R.id.error_spinner);
        confirmButton = dialog.findViewById(R.id.confirm_button);
        scanButton = dialog.findViewById(R.id.scan);

        shippingAdapter = new ShippingSpinnerAdapter(context, android.R.layout.simple_spinner_item, this.kurirList);
        shippingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shippingSpinner.setAdapter(shippingAdapter);

        return this;
    }

    public InputShippingRefNumDialog initPrevious() {
        cache = LocalCacheManager.ReturnPackage.Builder(resolutionID).getCache();

        for (ResCenterKurir.Kurir kurir : resCenterKurirList.getList()) {
            if (kurir.getShipmentId().equals(cache.getShippingID())) {
                shippingSpinner.setSelection(resCenterKurirList.getList().indexOf(kurir) + 1);
            }
        }

        referenceNumber.setText(cache.getShippingRefNum());
        return this;
    }

    public InputShippingRefNumDialog initValue(final Listener listener) {
        scanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (shippingSpinner.getSelectedItemPosition() > 0) {
                    LocalCacheManager.ReturnPackage.Builder(resolutionID)
                            .getCache()
                            .setShippingID(((ResCenterKurir.Kurir) shippingSpinner.getItemAtPosition(shippingSpinner.getSelectedItemPosition() - 1)).getShipmentId())
                            .setShippingRefNum(referenceNumber.getText().toString())
                            .save();

                    dialog.dismiss();
                    listener.onScanButtonClick();
                } else {
                    errorShippingSpinner.setVisibility(View.VISIBLE);
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                errorShippingSpinner.setVisibility(View.GONE);
                referenceNumber.setError(null);

                boolean valid = true;
                if (referenceNumber.getText().toString().replaceAll("\\s+","").length() == 0) {
                    valid = false;
                    referenceNumber.setError(context.getString(R.string.error_field_required));
                }
                if (referenceNumber.getText().toString().equals(cache.getShippingRefNum())) {
                    valid = false;
                    referenceNumber.setError(context.getString(R.string.error_update_receipt_number));
                }
                if (referenceNumber.length() < 8 || referenceNumber.length() > 17) {
                    valid = false;
                    referenceNumber.setError(context.getString(R.string.error_receipt_number));
                }
                if (shippingSpinner.getSelectedItemPosition() == 0) {
                    valid = false;
                    errorShippingSpinner.setVisibility(View.VISIBLE);
                }
                if (valid) {
                    LocalCacheManager.ReturnPackage.Builder(resolutionID)
                            .getCache()
                            .setShippingID(((ResCenterKurir.Kurir) shippingSpinner.getItemAtPosition(shippingSpinner.getSelectedItemPosition() - 1)).getShipmentId())
                            .setShippingRefNum(referenceNumber.getText().toString())
                            .save();
                    listener.onSubmitButtonClick();
                    dialog.dismiss();
                }
            }
        });
        return this;
    }

    public void show() {
        dialog.show();
    }
}
