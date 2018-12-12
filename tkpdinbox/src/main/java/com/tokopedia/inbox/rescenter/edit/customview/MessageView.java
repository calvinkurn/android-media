package com.tokopedia.inbox.rescenter.edit.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.EditText;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.rescenter.edit.listener.BuyerEditSolutionListener;

import butterknife.BindView;

/**
 * Created on 8/29/16.
 */
public class MessageView extends BaseView<Object, BuyerEditSolutionListener> {


    @BindView(R2.id.message_box)
    EditText messageBox;

    public MessageView(Context context) {
        super(context);
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(BuyerEditSolutionListener listener) {

    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_edit_rescenter_message;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull Object data) {
        messageBox.requestFocus();
    }

    public EditText getMessageBox() {
        return messageBox;
    }
}
