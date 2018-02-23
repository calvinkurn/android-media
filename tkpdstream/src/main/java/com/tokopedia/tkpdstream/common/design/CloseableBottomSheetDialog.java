package com.tokopedia.tkpdstream.common.design;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tokopedia.tkpdstream.R;

/**
 * @author by nisie on 2/23/18.
 */

public class CloseableBottomSheetDialog extends BottomSheetDialog {

    Context context;
    private CloseClickedListener closeListener;

    public interface CloseClickedListener {
        void onCloseDialog();
    }

    private CloseableBottomSheetDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    private CloseableBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
        init(context);
    }

    protected CloseableBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public static CloseableBottomSheetDialog createInstance(Context context) {
        final CloseableBottomSheetDialog closeableBottomSheetDialog = new CloseableBottomSheetDialog
                (context);
        closeableBottomSheetDialog.setListener(new CloseClickedListener() {
            @Override
            public void onCloseDialog() {
                closeableBottomSheetDialog.dismiss();
            }
        });
        return closeableBottomSheetDialog;
    }

    public static CloseableBottomSheetDialog createInstance(Context context, CloseClickedListener
            closeListener) {
        CloseableBottomSheetDialog closeableBottomSheetDialog = new CloseableBottomSheetDialog
                (context);
        closeableBottomSheetDialog.setListener(closeListener);
        return closeableBottomSheetDialog;
    }

    private void setListener(CloseClickedListener closeListener) {
        this.closeListener = closeListener;
    }

    private void init(Context context) {
        this.context = context;
    }

    @Override
    public void setContentView(View view) {
        View contentView = inflateCustomView(view);
        super.setContentView(contentView);

    }

    private View inflateCustomView(View view) {
        View contentView = ((Activity) context).getLayoutInflater().inflate(R.layout
                .closeable_bottom_sheet_dialog, null);
        FrameLayout frameLayout = contentView.findViewById(R.id.container);
        frameLayout.addView(view);
        ImageView closeButton = contentView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                closeListener.onCloseDialog();
            }
        });
        return contentView;
    }

    @Override
    public void setContentView(int layoutResId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View contentView = ((Activity) context).getLayoutInflater().inflate(R.layout
                    .closeable_bottom_sheet_dialog, null);
            FrameLayout frameLayout = contentView.findViewById(R.id.container);
            View view = inflater.inflate(layoutResId, null);
            frameLayout.addView(view);
            super.setContentView(contentView);
        }

        super.setContentView(layoutResId);
    }
}
