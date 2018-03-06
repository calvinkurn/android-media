package com.tokopedia.shop.product.view.customview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.FrameLayout;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.shop.R;

public class CustomContentBottomActionView extends BottomSheetView {

    FrameLayout customContentView;
    @LayoutRes
    private int customContentLayout;

    public CustomContentBottomActionView(Context context) {
        super(context);
    }

    public CustomContentBottomActionView(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
        init(context);
    }

    public CustomContentBottomActionView(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    protected void init(Context context) {
        super.init(context);
        customContentView = findViewById(R.id.custom_content);
    }

    public void setCustomContentLayout(int customContentLayout) {
        this.customContentLayout = customContentLayout;
        customContentView.addView(getLayoutInflater().inflate(customContentLayout, null));
    }

    public void setCustomContentLayout(View customContentLayout) {
        customContentView.addView(customContentLayout);
    }


    protected int getLayout() {
        return R.layout.custom_content_bottom_sheet;
    }
}
