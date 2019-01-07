package com.tokopedia.tkpdpdp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.tokopedia.core.product.facade.NetworkParam;
import com.tokopedia.core.product.model.etalase.Etalase;
import com.tokopedia.core.util.ValidationTextUtil;
import com.tokopedia.tkpdpdp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author Angga.Prasetiyo on 17/11/2015.
 */
public class DialogToEtalase extends Dialog {
    private final List<Etalase> etalaseList;
    private final Context context;
    private final int productId;

    TextView tvYes;
    Spinner spinner;
    EditText etNew;

    private final Listener listener;

    DialogToEtalase(Context context, int productId, List<Etalase> etalases, Listener listener) {
        super(context);
        this.context = context;
        this.etalaseList = etalases;
        this.etalaseList.add(0, new Etalase(null, context.getString(R.string.choose_etalase)));
        this.etalaseList.add(new Etalase(0, context.getString(R.string.add_etalase)));
        this.productId = productId;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(com.tokopedia.core2.R.layout.dialog_move_to_etalase);
        tvYes = (TextView) findViewById(R.id.ok_button);
        spinner = (Spinner) findViewById(R.id.spinner_etalase);
        etNew = (EditText) findViewById(R.id.etalase_name);
        setCancelable(true);
        ArrayAdapter<Etalase> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_item, etalaseList
        );

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etNew.setVisibility(position == etalaseList.size() - 1 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = etNew.getText().toString().trim();
                final Etalase selected = (Etalase) spinner.getSelectedItem();
                Map<String, String> param;
                if (selected.getEtalaseId() != null) {
                    if (selected.getEtalaseId() == 0 & newName.isEmpty()) {
                        etNew.setError(context.getString(com.tokopedia.core2.R.string.error_field_required));
                        return;
                    } else if (selected.getEtalaseId() == 0 & !newName.isEmpty()) {
                        if (!ValidationTextUtil.isValidText(3, newName)) {
                            etNew.setError(context.getString(com.tokopedia.core2.R.string.error_min_3_character));
                            return;
                        } else if (isAvailableEtalase(newName)) {
                            etNew.setError(context.getString(com.tokopedia.core2.R.string.error_etalase_exist));
                            return;
                        }
                    }
                } else {
                    listener.onNotSelected();
                    return;
                }

                if (selected.getEtalaseId() == 0) {
                    param = NetworkParam.paramToNewEtalase(productId, newName);
                } else {
                    param = NetworkParam.paramToEtalase(productId, selected);
                }
                listener.onRequestAction(param, productId);
                dismiss();
            }
        });
    }

    private boolean isAvailableEtalase(String newName) {
        for (Etalase etalase : etalaseList) {
            if (etalase.getEtalaseName().contains(newName)) return true;
        }
        return false;
    }


    public interface Listener {

        void onNotSelected();

        void onRequestAction(Map<String, String> param, int productId);
    }


    public static class Builder {
        private Context context;
        private int productId;
        private Listener listener;
        private List<Etalase> etalaseList = new ArrayList<>();

        private Builder() {
        }

        public static Builder aDialogToEtalase() {
            return new Builder();
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setProductId(int productId) {
            this.productId = productId;
            return this;
        }

        public Builder setListener(Listener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setEtalases(List<Etalase> etalases) {
            this.etalaseList = etalases;
            return this;
        }

        public Builder but() {
            return aDialogToEtalase()
                    .setContext(context)
                    .setProductId(productId)
                    .setListener(listener)
                    .setEtalases(etalaseList);
        }

        public DialogToEtalase build() {
            return new DialogToEtalase(context, productId,
                    etalaseList, listener);
        }
    }
}