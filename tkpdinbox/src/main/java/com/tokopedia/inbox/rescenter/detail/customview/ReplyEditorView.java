package com.tokopedia.inbox.rescenter.detail.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.core2.R;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.inbox.rescenter.create.customview.BaseView;
import com.tokopedia.inbox.rescenter.detail.customadapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.detail.listener.DetailResCenterView;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.inbox.rescenter.utils.LocalCacheManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 8/13/16.
 */
public class ReplyEditorView extends BaseView<DetailResCenterData.Detail, DetailResCenterView> {


    private RecyclerView attachmentRecyclerView;
    private View actionAttachment;
    private View actionSend;
    private EditText boxComment;

    private List<AttachmentResCenterVersion2DB> attachmentData;
    private AttachmentAdapter adapter;
    private LocalCacheManager.MessageConversation cache;

    public ReplyEditorView(Context context) {
        super(context);
    }

    public ReplyEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(DetailResCenterView detailResCenterView) {
        this.listener = detailResCenterView;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_reply_editor;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull DetailResCenterData.Detail data) {
        isCanConversation(data.getResolutionCanConversation());
        renderAttachmentAdapter();
        actionSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setOnSendClickListener(String.valueOf(boxComment.getText()));
            }
        });
        actionAttachment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getItemCount() < 5) {
                    listener.setOnAttachmentClickListener();
                } else {
                    listener.showToastMessage(getContext().getString(R.string.max_upload_detail_res_center));
                }
            }
        });
    }

    private void renderAttachmentAdapter() {
        // set adapter
        attachmentData = new ArrayList<>();
        adapter = new AttachmentAdapter(listener, attachmentData);

        // set layout manager
        LinearLayoutManager horizontal = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        // attach adapter and layout manager into recyclerview
        attachmentRecyclerView.setLayoutManager(horizontal);
        attachmentRecyclerView.setAdapter(adapter);

        setAttachmentVisibility(false);
    }

    public void setAttachmentVisibility(boolean isVisible) {
        attachmentRecyclerView.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void isCanConversation(Integer canConversation) {
        setVisibility(canConversation == 1 ? VISIBLE : GONE);
    }

    public List<AttachmentResCenterVersion2DB> getAttachmentList() {
        return adapter.getItemList();
    }

    public void setError(String param) {
        boxComment.setError(param);
    }

    public void renderAttachmentData(List<AttachmentResCenterVersion2DB> data) {
        if (data != null) {
            adapter.setDataSet(data);
        }
    }

    public void setReplyBoxEmpty() {
        boxComment.setText(null);
    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        attachmentRecyclerView = view.findViewById(R.id.listview_attachment);
        actionAttachment = view.findViewById(R.id.attach_but);
        actionSend = view.findViewById(R.id.send_but);
        boxComment = view.findViewById(R.id.new_comment);
    }
}
