package com.tokopedia.inbox.rescenter.discussion.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R2;
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

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.image_upload)
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ProductImageListener {
        View.OnClickListener onImageClicked(int position, AttachmentViewModel imageUpload);
    }


    private ProductImageListener listener;
    private ArrayList<AttachmentViewModel> data;

    public ImageAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    public Context context;

    public static ImageAdapter createAdapter(Context context) {
        return new ImageAdapter(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listview_image_upload, viewGroup, false));
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
            if (data.get(position).getFileLoc() == null
                    && data.get(position).getImgThumb() != null
                    && !data.get(position).getImgThumb().equals("")
                    ) {
                ImageHandler.LoadImage(holder.image, data.get(position).getImgThumb());
            } else {
                ImageHandler.loadImageFromFile(context, holder.image, new File(data.get(position).getFileLoc()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.image.setOnClickListener(listener.onImageClicked(position, data.get(position)));

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
