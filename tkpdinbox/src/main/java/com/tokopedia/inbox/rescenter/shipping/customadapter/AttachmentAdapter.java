package com.tokopedia.inbox.rescenter.shipping.customadapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;

import java.io.File;
import java.util.List;

/**
 * Created on 4/14/16.
 */
public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {

    private final AttachmentAdapterListener listener;
    private List<AttachmentResCenterVersion2DB> dataSet;

    public interface AttachmentAdapterListener {
        void onClickAddAttachment(View view);
        void onClickOpenAttachment(View view, int position);
        void onClickRemoveAttachment(View view, int position);
    }

    public AttachmentAdapter(AttachmentAdapterListener listener, List<AttachmentResCenterVersion2DB> list) {
        this.listener = listener;
        this.dataSet = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView attachment;
        ImageView deleteAttachment;
        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mainView = itemView;
            this.attachment = (ImageView) itemView.findViewById(R.id.attachment);
            this.deleteAttachment = (ImageView) itemView.findViewById(R.id.delete_attachment);
        }
    }

    @Override
    public AttachmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_attachment_rescenter_create, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AttachmentAdapter.ViewHolder holder, int position) {
        setValue(holder, position);
        setListener(holder, position);
    }

    private void setListener(final ViewHolder holder, final int position) {
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.getAdapterPosition() == dataSet.size()) {
                    listener.onClickAddAttachment(view);
                }
            }
        });
        holder.deleteAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.getAdapterPosition() != dataSet.size()) {
                    listener.onClickOpenAttachment(view, holder.getAdapterPosition());
                }
            }
        });
    }

    private void remove(int position) {
        dataSet.get(position).delete();
        dataSet.remove(position);
        notifyItemRemoved(position);
    }

    private void setValue(ViewHolder holder, int position) {
        holder.deleteAttachment.setVisibility(View.GONE);
        if (position < dataSet.size()) {
            loadImage(holder, position);
        } else {
            if (dataSet.size() == 5) {
                loadImage(holder, position);
            } else {
                holder.attachment.setImageResource(android.R.color.transparent);
            }
        }
    }

    private void loadImage(ViewHolder holder, int position) {
        File imgFile = new  File(dataSet.get(position).imagePath);
        ImageHandler.loadImageFromFile(holder.itemView.getContext(), holder.attachment, imgFile);
        holder.deleteAttachment.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        if (dataSet.size() == 3) {
            return dataSet.size();
        } else {
            return dataSet.size() + 1;
        }
    }

}