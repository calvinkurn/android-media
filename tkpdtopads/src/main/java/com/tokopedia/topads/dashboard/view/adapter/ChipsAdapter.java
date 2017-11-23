package com.tokopedia.topads.dashboard.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.topads.R;
import com.tokopedia.seller.common.utils.CircleTransform;
import com.tokopedia.topads.dashboard.view.model.ChipsEntity;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

import java.util.List;

public class ChipsAdapter extends RecyclerView.Adapter<ChipsAdapter.ViewHolder> {

    private List<ChipsEntity<TopAdsProductViewModel>> chipsEntities;
    private OnRemoveListener onRemoveListener;
    private boolean isShowingPosition;

    public ChipsAdapter(List<ChipsEntity<TopAdsProductViewModel>> chipsEntities, OnRemoveListener onRemoveListener) {
        this.chipsEntities = chipsEntities;
        this.onRemoveListener = onRemoveListener;
    }

    public ChipsAdapter(List<ChipsEntity<TopAdsProductViewModel>> chipsEntities, OnRemoveListener onRemoveListener, boolean isShowingPosition) {
        this.chipsEntities = chipsEntities;
        this.onRemoveListener = onRemoveListener;
        this.isShowingPosition = isShowingPosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(chipsEntities.get(position));
    }

    @Override
    public int getItemCount() {
        return chipsEntities.size();
    }

    public void remove(int position) {
        chipsEntities.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDescription;
        private ImageView ivPhoto;
        private TextView tvName;
        private ImageButton ibClose;
        private TextView tvPosition;
        private TopAdsProductViewModel productDomain;

        ViewHolder(View itemView) {
            super(itemView);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            ibClose = (ImageButton) itemView.findViewById(R.id.ibClose);
            tvPosition = (TextView) itemView.findViewById(R.id.tvPosition);
        }

        void bindItem(final ChipsEntity entity) {
            itemView.setTag(entity.getName());
            tvDescription.setVisibility(View.GONE);

            if (entity.getDrawableResId() != 0) {
                Glide.with(ivPhoto.getContext()).load(entity.getDrawableResId())
                        .transform(new CircleTransform(ivPhoto.getContext())).into(ivPhoto);
            }

            if (entity.getRawData() != null
                    && entity.getRawData() instanceof TopAdsProductViewModel) {
                productDomain = ((TopAdsProductViewModel) entity.getRawData());
                Glide.with(ivPhoto.getContext()).load(productDomain.getImageUrl())
                        .transform(new CircleTransform(ivPhoto.getContext())).into(ivPhoto);
            }

            tvName.setText(entity.getName());

            if (isShowingPosition) {
                tvPosition.setText(String.valueOf(getAdapterPosition()));
            }

            ibClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRemoveListener != null && getAdapterPosition() != -1) {
                        onRemoveListener.onItemRemoved(getAdapterPosition());
                    }
                }
            });
        }
    }


}
