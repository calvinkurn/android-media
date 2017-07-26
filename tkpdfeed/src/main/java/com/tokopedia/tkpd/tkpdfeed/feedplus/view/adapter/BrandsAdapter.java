package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 7/26/17.
 */

public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.ViewHolder> {

    private static final int IS_NEW = 1;
    private final FeedPlus.View viewListener;
    ArrayList<OfficialStoreViewModel> listStore;

    public BrandsAdapter(FeedPlus.View viewListener) {
        this.listStore = new ArrayList<>();
        this.viewListener = viewListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView labelNew;

        public ViewHolder(View itemView) {
            super(itemView);
            this.logo = (ImageView) itemView.findViewById(R.id.iv_brands);
            this.labelNew = (TextView) itemView.findViewById(R.id.text_new);
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onBrandClicked(listStore.get(getAdapterPosition()));
                }
            });
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_brands, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageHandler.LoadImage(holder.logo, listStore.get(position).getLogoUrl());

        if (listStore.get(position).getIsNew() == IS_NEW)
            holder.labelNew.setVisibility(View.VISIBLE);
        else
            holder.labelNew.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        if (listStore != null)
            return listStore.size();
        else
            return 0;
    }

    public void setList(ArrayList<OfficialStoreViewModel> listStore) {
        this.listStore.addAll(listStore);
        notifyDataSetChanged();
    }


}
