package com.tokopedia.inbox.rescenter.create.customview;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.tokopedia.core2.R;
import com.tokopedia.inbox.rescenter.create.customadapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.create.listener.ChooseSolutionListener;
import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;


/**
 * Created on 7/5/16.
 */
public class AttachmentSectionCreateResCenterView extends BaseView<ActionParameterPassData, ChooseSolutionListener> {

    private View viewUploadProof;
    private RecyclerView attachmentRecyclerView;

    public AttachmentSectionCreateResCenterView(Context context) {
        super(context);
    }

    public AttachmentSectionCreateResCenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        setVisibility(GONE);
    }

    @Override
    public void setListener(ChooseSolutionListener chooseSolutionListener) {
        this.listener = chooseSolutionListener;
    }

    @Override
    public void renderData(@NonNull ActionParameterPassData data) {

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
        attachmentRecyclerView = view.findViewById(R.id.list_upload_proof);
        viewUploadProof = view.findViewById(R.id.view_upload_proof);
    }
}
