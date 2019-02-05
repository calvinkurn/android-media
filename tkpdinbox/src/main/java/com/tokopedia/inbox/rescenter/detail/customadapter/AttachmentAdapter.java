package com.tokopedia.inbox.rescenter.detail.customadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.inbox.rescenter.detail.listener.DetailResCenterView;

import java.io.File;
import java.util.List;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {

    private final DetailResCenterView listener;
    private List<AttachmentResCenterVersion2DB> dataSet;

    public AttachmentAdapter(DetailResCenterView view, List<AttachmentResCenterVersion2DB> list) {
        this.listener = view;
        this.dataSet = list;
    }

    public void setDataSet(List<AttachmentResCenterVersion2DB> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public List<AttachmentResCenterVersion2DB> getItemList() {
        return dataSet;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView attachment;
        View removeAttachment;
        View loadingAttachment;
        public ViewHolder(View itemView) {
            super(itemView);
            this.attachment = (ImageView) itemView.findViewById(R.id.attachment);
            this.removeAttachment = itemView.findViewById(R.id.remove);
            this.loadingAttachment = itemView.findViewById(R.id.loading);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setVisibility();
    }

    @Override
    public AttachmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_attachment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AttachmentAdapter.ViewHolder holder, int position) {
        setValue(holder, position);
        setListener(holder, position);
    }

    private void setValue(ViewHolder holder, int position) {
        File imgFile = new  File(dataSet.get(position).imagePath);
        ImageHandler.loadImageFromFile(holder.itemView.getContext(), holder.attachment, imgFile);
    }

    private void setListener(ViewHolder holder, int position) {
        holder.removeAttachment.setOnClickListener(OnRemoveAttachment(holder));
        holder.attachment.setOnClickListener(OnRemoveAttachment(holder));
    }

    private View.OnClickListener OnRemoveAttachment(final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSet.get(holder.getAdapterPosition()).delete();
                dataSet.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                setVisibility();
            }
        };
    }

    private void setVisibility() {
        if (getItemCount() > 0) {
            listener.setAttachmentArea(true);
        } else {
            listener.setAttachmentArea(false);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
