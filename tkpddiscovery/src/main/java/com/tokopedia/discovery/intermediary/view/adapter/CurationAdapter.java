package com.tokopedia.discovery.intermediary.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.core.util.NonScrollGridLayoutManager;
import com.tokopedia.core.widgets.DividerItemDecoration;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.domain.model.CuratedSectionModel;

import java.util.Collections;
import java.util.List;

/**
 * Created by alifa on 3/29/17.
 */

public class CurationAdapter extends
        RecyclerView.Adapter<CurationAdapter.ItemRowHolder> {

    private final Context context;
    private List<CuratedSectionModel> dataList;
    private int homeMenuWidth;
    private final CuratedProductAdapter.OnItemClickListener onProductItemClickListener;

    public CurationAdapter(Context context,
                           CuratedProductAdapter.OnItemClickListener onProductItemClickListener) {

        this.context = context;
        this.dataList = Collections.emptyList();
        this.onProductItemClickListener = onProductItemClickListener;
    }


    @Override
    public CurationAdapter.ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.item_curation, null
        );
        return new CurationAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(CurationAdapter.ItemRowHolder itemRowHolder, int i) {
        final CuratedSectionModel curatedSectionModel = dataList.get(i);
        CuratedProductAdapter itemAdapter = new CuratedProductAdapter(context,
                curatedSectionModel.getProducts(),homeMenuWidth,onProductItemClickListener
                ,curatedSectionModel.getTitle());

        itemRowHolder.itemTitle.setText(curatedSectionModel.getTitle());
        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setNestedScrollingEnabled(false);
        itemRowHolder.recycler_view_list.setLayoutManager(
                new NonScrollGridLayoutManager(context, 2,
                        GridLayoutManager.VERTICAL, false));
        itemRowHolder.recycler_view_list.addItemDecoration(new DividerItemDecoration(context,R.drawable.divider300));
        itemRowHolder.recycler_view_list.setAdapter(itemAdapter);

    }

    @Override
    public int getItemCount() {
        return  dataList.size();
    }

    public void setDataList(List<CuratedSectionModel> dataList) {
        this.dataList = dataList;
    }

    public void setHomeMenuWidth(int homeMenuWidth) {
        this.homeMenuWidth = homeMenuWidth;
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {

        TextView itemTitle;
        RecyclerView recycler_view_list;

        ItemRowHolder(View view) {
            super(view);
            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
        }

    }

}