package com.tokopedia.inbox.rescenter.detail.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core2.R;

/**
 * Created by hangnadi on 3/7/16.
 */
public class ConfirmationDialog {

    private final Dialog dialog;
    private int stepCreate;
    private TextView messageDialog;
    private View submit;

    public ConfirmationDialog(Context context) {
        this.dialog = new Dialog(context);
        stepCreate++;
    }

    public interface Listener {
        void onSubmitButtonClick();
    }

    public static ConfirmationDialog Builder(@NonNull Context context) {
        return new ConfirmationDialog(context);
    }

    public ConfirmationDialog initView() {
        if (stepCreate != 1) {
            throw new RuntimeException("Call this after builder()");
        } else {
            stepCreate++;
        }
        dialog.setTitle(R.string.title_confirm);
        dialog.setContentView(R.layout.dialog_confirmation_detail_res_center);
        messageDialog = (TextView) dialog.findViewById(R.id.message);
        submit = dialog.findViewById(R.id.submit);

        return this;
    }

    public ConfirmationDialog initValue(String messageDialog) {
        if (stepCreate != 2) {
            throw new RuntimeException("Call this after initView()");
        } else {
            stepCreate++;
        }
        this.messageDialog.setText(messageDialog);
        return this;
    }

    public ConfirmationDialog initListener(final Listener listener) {
        if (stepCreate != 3) {
            throw new RuntimeException("Call this after initValue()");
        } else {
            stepCreate++;
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                listener.onSubmitButtonClick();
            }
        });

        return this;
    }

    public ConfirmationDialog show() {
        if (stepCreate != 4) {
            throw new RuntimeException("Call this after initListener()");
        } else {
            stepCreate++;
        }
        dialog.show();
        return this;
    }
}
