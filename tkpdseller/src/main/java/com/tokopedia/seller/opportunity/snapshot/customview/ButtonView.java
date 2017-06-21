package com.tokopedia.seller.opportunity.snapshot.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.snapshot.listener.SnapShotFragmentView;

/**
 * Created by hangnadi on 3/1/17.
 */

public class ButtonView extends BaseView<Object, SnapShotFragmentView> {

    TextView actionConfirm;

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutView(), this, true);

        actionConfirm = (TextView) findViewById(R.id.action_confirm);
    }

    public ButtonView(Context context) {
        super(context);
    }

    public ButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(SnapShotFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_snapshot_button;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull Object data) {
        actionConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onActionConfirmClicked();
            }
        });
    }
}
