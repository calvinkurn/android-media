package com.tokopedia.inbox.rescenter.edit.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.rescenter.edit.customadapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.edit.listener.BuyerEditSolutionListener;

import butterknife.BindView;

/**
 * Created on 8/26/16.
 */
public class EditAttachmentView extends BaseView<Object, BuyerEditSolutionListener> {

    @BindView(R2.id.view_upload_proof)
    View viewUploadProof;
    @BindView(R2.id.list_upload_proof)
    RecyclerView attachmentRecyclerView;

    public EditAttachmentView(Context context) {
        super(context);
    }

    public EditAttachmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(BuyerEditSolutionListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_create_res_center_attachment_section;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull Object data) {

    }

    public void attachAdapter(AttachmentAdapter attachmentAdapter) {
        LinearLayoutManager horizontal = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        attachmentRecyclerView.setLayoutManager(horizontal);
        attachmentRecyclerView.setAdapter(attachmentAdapter);
    }
}
