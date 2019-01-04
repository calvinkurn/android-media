package com.tokopedia.inbox.rescenter.discussion.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R2;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nisie on 3/31/17.
 */

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.image_upload)
        ImageView image;

        @BindView(R2.id.delete_but)
        ImageView deleteButton;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ProductImageListener {
        View.OnClickListener onImageClicked(int position, AttachmentViewModel imageUpload);

        View.OnClickListener onDeleteImage(int position, AttachmentViewModel imageUpload);

    }


    private ProductImageListener listener;
    private ArrayList<AttachmentViewModel> data;
    private boolean canDelete;
    public boolean isClickable = true;

    public AttachmentAdapter(Context context, boolean canDelete) {
        this.context = context;
        this.data = new ArrayList<>();
        this.canDelete = canDelete;
    }

    public Context context;

    public static AttachmentAdapter createAdapter(Context context, boolean canDelete) {
        return new AttachmentAdapter(context, canDelete);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listview_image_upload_delete, viewGroup, false));

        if (canDelete)
            holder.deleteButton.setVisibility(View.VISIBLE);
        else
            holder.deleteButton.setVisibility(View.GONE);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            default:
                bindImage(holder, position);
                break;
        }
    }

    private void bindImage(ViewHolder holder, final int position) {

        try {
            if (data.get(position).getImgThumb() != null
                    && !data.get(position).getImgThumb().equals("")
                    ) {
                Log.d("hangnadi", "bindImage: wrong1");
                ImageHandler.LoadImage(holder.image, data.get(position).getImgThumb());
            } else if (data.get(position).getFileLoc() != null
                    && !data.get(position).getFileLoc().equals("")) {
                Log.d("hangnadi", "bindImage: " + data.get(position).getFileLoc());
                ImageHandler.loadImageFromFile(context, holder.image, new File(data.get(position).getFileLoc()));
            } else if (data.get(position).getUrl() != null &&
                    !data.get(position).getUrl().equals("")) {
                Log.d("hangnadi", "bindImage: wrong2");
                ImageHandler.loadImageWithId(holder.image, R.drawable.ic_video_thumb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.image.setOnClickListener(listener.onImageClicked(position, data.get(position)));
        holder.deleteButton.setOnClickListener(listener.onDeleteImage(position, data.get(position)));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);

    }

    public void addList(List<AttachmentViewModel> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public ArrayList<AttachmentViewModel> getList() {
        return this.data;
    }

    public void addImage(AttachmentViewModel image) {
        data.add(image);
        notifyDataSetChanged();
    }

    public void setListener(ProductImageListener listener) {
        this.listener = listener;
    }

}
