package com.tokopedia.seller.product.view.adapter.addurlvideo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.AddUrlVideoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author normansyahputa on 4/12/17.
 */
public class AddUrlVideoAdapter extends BaseLinearRecyclerViewAdapter {
    private List<AddUrlVideoModel> addUrlVideoModels;
    private int maxRows;
    private ImageHandler imageHandler;
    private String videoSameWarn;

    public AddUrlVideoAdapter(ImageHandler imageHandler) {
        this.imageHandler = imageHandler;
        addUrlVideoModels = new ArrayList<>();
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public void setVideoSameWarn(String videoSameWarn) {
        this.videoSameWarn = videoSameWarn;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case AddUrlVideoModel.TYPE:
                View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_add_video_url_ll, viewGroup, false);
                return new AddUrlVideoViewHolder(itemLayoutView, imageHandler);
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case AddUrlVideoModel.TYPE:
                ((AddUrlVideoViewHolder) holder).bindData(
                        addUrlVideoModels.get(position), new AddUrlVideoViewHolder.AddUrlVideoOnClickRemove() {
                            @Override
                            public void onClick(View v, int index) {
                                remove(index);
                            }
                        }
                );
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (addUrlVideoModels.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return addUrlVideoModels.get(position).getType();
        }

    }

    private boolean isLastItemPosition(int position) {
        return position == addUrlVideoModels.size();
    }

    @Override
    public int getItemCount() {
        return addUrlVideoModels.size() + super.getItemCount();
    }

    private void remove(int index) {
        addUrlVideoModels.remove(index);
        notifyItemRemoved(index);
    }

    public void add(AddUrlVideoModel addUrlVideoModel) {
        // prevent addition if size will exceed the limit.
        if (addUrlVideoModels.size() == maxRows)
            return;

        // check if model is already contain, then
        if (addUrlVideoModels.contains(addUrlVideoModel))
            throw new IllegalArgumentException(videoSameWarn);

        addUrlVideoModels.add(addUrlVideoModel);
        notifyItemInserted(addUrlVideoModels.size() - 1);
    }
}
