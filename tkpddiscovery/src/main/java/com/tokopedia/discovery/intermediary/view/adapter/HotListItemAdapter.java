package com.tokopedia.discovery.intermediary.view.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.network.entity.topPicks.Item;
import com.tokopedia.core.network.entity.topPicks.Toppick;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.domain.model.HotListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alifa on 3/30/17.
 */

public class HotListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TOPPICKS_TITLE = 0;

    private List<HotListModel> hotListModelList = new ArrayList<>();
    private final int homeMenuWidth;

    public HotListItemAdapter(List<HotListModel> hotListModelList, int homeMenuWidth) {
        this.hotListModelList = hotListModelList;
        this.homeMenuWidth = homeMenuWidth;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.item_hotlist, null
        );
        v.setMinimumWidth(homeMenuWidth);
        return new HotListItemRowHolder(v);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int i) {


    }

    @Override
    public int getItemCount() {
        return hotListModelList.size();
    }


    class HotListItemRowHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;

        protected View view;
        HotListItemRowHolder(View view) {
            super(view);
            this.view = view;
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);

        }
    }

    public interface OnHotListListener {
        void onTitleClicked(HotListModel toppick);
    }

}