package com.tokopedia.inbox.rescenter.edit.customview;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.tokopedia.core2.R;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.rescenter.edit.customadapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.edit.listener.SellerEditResCenterListener;

/**
 * Created on 8/30/16.
 */
public class EditAttachmentSellerView extends BaseView<Object, SellerEditResCenterListener> {

    private View viewUploadProof;
    private RecyclerView attachmentRecyclerView;

    public EditAttachmentSellerView(Context context) {
        super(context);
    }

    public EditAttachmentSellerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(SellerEditResCenterListener listener) {

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
    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        viewUploadProof = view.findViewById(R.id.view_upload_proof);
        attachmentRecyclerView = view.findViewById(R.id.list_upload_proof);
    }
}
