package com.tokopedia.topads.sdk.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.TopAdsLayoutWrapper;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractAdsViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.TopAdsViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.TopAdsViewModel;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * @author by errysuprayogi on 4/10/17.
 */

public abstract class RecycleViewAdapterTopAds<T> extends RecyclerView.Adapter<AbstractAdsViewHolder> {

    private final Context context;
    private final ArrayList<T> mData;
    private final RecyclerView recyclerView;
    LayoutInflater inflater;
    ArrayList<TopAdsLayoutWrapper> layoutWrappers = new ArrayList<>();
    View emptyView = null;
    @LayoutRes
    int emptyLayoutId = 0;
    boolean autoEmptyLayoutHandling = false;


    public RecycleViewAdapterTopAds(Context context, RecyclerView recyclerView, TopAdsLayoutWrapper layoutWrapper) {
        this.context = context;
        this.mData = new ArrayList<T>();
        this.recyclerView = recyclerView;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutWrappers.add(new TopAdsLayoutWrapper(TopAdsViewModel.class, TopAdsViewHolder.class, R.layout.item_ads, 0));
        layoutWrappers.add(layoutWrapper);

    }

    @Override
    public AbstractAdsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TopAdsLayoutWrapper layoutWrapper = getWrapperFromType(viewType);
        final ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(layoutWrapper.getLayoutId(), parent, false);
        try {
            Constructor c = layoutWrapper.getViewHolder().getConstructor(View.class, Context.class);
            return (AbstractAdsViewHolder) c.newInstance(view, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(AbstractAdsViewHolder holder, int position) {
        T t = mData.get(position);
        this.populateViewHolderItem(holder, t, position);
    }

    @Override
    public int getItemCount() {
        int size = mData.size();
        Log.d("Size", size + "");

        return size;
    }

    @Override
    public int getItemViewType(int position) {
        //TODO: Find a better way to compare classes.
        if (layoutWrappers.size() > 1) {
            for (TopAdsLayoutWrapper wrapper : layoutWrappers) {
                if (wrapper.getModel().getName().equals(mData.get(position).getClass().getName()))
                    return wrapper.getLayoutType();
            }
            throw new RuntimeException("Please Check the SnapLayoutWrapper and the input Dataset Classes");
        }
        return layoutWrappers.get(0).getLayoutType();

    }

    public void populateViewHolderItem(AbstractAdsViewHolder viewHolder, T item, int position) {
        viewHolder.setItemData(item);
        viewHolder.populateViewHolder(item, position);
        viewHolder.attachOnClickListeners(viewHolder, item, position);
    }

    public TopAdsLayoutWrapper getWrapperFromType(int type) {

        for (TopAdsLayoutWrapper wrapper : layoutWrappers)
            if (wrapper.getLayoutType().equals(type))
                return wrapper;
        return null;
    }

    public void add(@Nullable T cardBase) {
        if (cardBase == null) return;
        this.mData.add(cardBase);
        notifyItemInserted(this.mData.size() - 1);
    }

    public void addAll(@Nullable ArrayList<T> list) {
        if (list == null) return;
        final int prevSize = this.mData.size() - 1;
        this.mData.addAll(list);
        this.notifyItemRangeInserted(prevSize, this.mData.size() - 1);
    }
}
