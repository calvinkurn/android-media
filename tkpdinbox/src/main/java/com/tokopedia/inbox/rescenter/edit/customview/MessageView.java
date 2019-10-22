package com.tokopedia.inbox.rescenter.edit.customview;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.core2.R;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.rescenter.edit.listener.BuyerEditSolutionListener;


/**
 * Created on 8/29/16.
 */
public class MessageView extends BaseView<Object, BuyerEditSolutionListener> {

    private EditText messageBox;

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

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        messageBox = view.findViewById(R.id.message_box);
    }
}
