package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.factory.HotlistTypeFactory;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistHeaderViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.SearchEmptyViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionGeneralAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 10/8/17.
 */

@SuppressWarnings("ALL")
public class HotlistAdapter extends SearchSectionGeneralAdapter {

    private static final String INSTANCE_TOTAL_DATA = "INSTANCE_TOTAL_DATA";
    private static final String INSTANCE_LIST_DATA = "INSTANCE_LIST_DATA";
    private static final String INSTANCE_START_FROM = "INSTANCE_START_FROM";

    private List<Visitable> mVisitables;
    private final HotlistTypeFactory typeFactory;
    private int totalData;
    private int startFrom;


    public HotlistAdapter(OnItemChangeView itemChangeView, HotlistTypeFactory typeFactory) {
        super(itemChangeView);
        this.typeFactory = typeFactory;
        mVisitables = new ArrayList<>();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(mVisitables.get(position));
    }

    @Override
    public int getItemCount() {
        return mVisitables.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(int position) {
        return mVisitables.get(position).type(typeFactory);
    }

    public void setElement(List<Visitable> data) {
        mVisitables = data;
        notifyDataSetChanged();
    }

    public void addElement(Visitable visitable) {
        getItemList().add(visitable);
        notifyDataSetChanged();
    }

    public void addElements(List<Visitable> data) {
        int start = getItemCount();
        getItemList().addAll(data);
        notifyItemRangeInserted(start, data.size());
    }

    @Override
    public List<Visitable> getItemList() {
        return mVisitables;
    }

    @Override
    protected SearchSectionTypeFactory getTypeFactory() {
        return typeFactory;
    }

    public void setChangedItem(int position, Visitable visitable) {
        if (position < mVisitables.size()) {
            mVisitables.remove(position);
            mVisitables.add(position, visitable);
            notifyDataSetChanged();
        }
    }

    public void removeItem(int position) {
        mVisitables.remove(position);
        notifyDataSetChanged();
    }

    public boolean isHotListBanner(int position) {
        if (checkDataSize(position))
            return getItemList().get(position) instanceof HotlistHeaderViewModel;
        return false;
    }

    public boolean isEmptyHotlist(int position) {
        if (checkDataSize(position))
            return getItemList().get(position) instanceof SearchEmptyViewModel;
        return false;
    }

    public boolean isEmpty() {
        return getItemList().isEmpty();
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public int getTotalData() {
        return totalData;
    }

    public void resetStartFrom() {
        setStartFrom(Integer.parseInt(BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS));
    }

    public boolean hasNextPage() {
        return getStartFrom() <= getTotalData();
    }

    public void incrementStart() {
        setStartFrom(getStartFrom() + Integer.parseInt(BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS));
    }

    public int getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(int start) {
        this.startFrom = start;
    }

    public void updateWishlistStatus(String productID, boolean isWishlist) {
        for (int i = 0; i < getItemList().size(); i++) {
            Visitable visitable = getItemList().get(i);
            if (visitable instanceof HotlistProductViewModel) {
                HotlistProductViewModel model = ((HotlistProductViewModel) visitable);
                if (model.getProductID().equals(productID)) {
                    model.setWishlist(isWishlist);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public void enableWishlistButton(String productID) {
        for (int i = 0; i < getItemList().size(); i++) {
            Visitable visitable = getItemList().get(i);
            if (visitable instanceof HotlistProductViewModel) {
                HotlistProductViewModel model = ((HotlistProductViewModel) visitable);
                if (model.getProductID().equals(productID)) {
                    model.setWishlistButtonEnabled(true);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public void disableWishlistButton(String productID) {
        for (int i = 0; i < getItemList().size(); i++) {
            Visitable visitable = getItemList().get(i);
            if (visitable instanceof HotlistProductViewModel) {
                HotlistProductViewModel model = ((HotlistProductViewModel) visitable);
                if (model.getProductID().equals(productID)) {
                    model.setWishlistButtonEnabled(false);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public void updateWishlistStatus(String productID) {
        for (int i = 0; i < getItemList().size(); i++) {
            Visitable visitable = getItemList().get(i);
            if (visitable instanceof HotlistProductViewModel) {
                HotlistProductViewModel model = ((HotlistProductViewModel) visitable);
                if (model.getProductID().equals(productID)) {
                    model.setWishlist(!model.isWishlist());
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TOTAL_DATA, getTotalData());
        outState.putInt(INSTANCE_START_FROM, 12);
        outState.putParcelableArrayList(INSTANCE_LIST_DATA, mappingIntoParcelableArrayList(getItemList()));
    }

    private ArrayList<Parcelable> mappingIntoParcelableArrayList(List<Visitable> elements) {
        ArrayList<Parcelable> list = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {

            if (i > 12) {
                break;
            }

            Visitable visitable = elements.get(i);
            if (visitable instanceof HotlistHeaderViewModel) {
                list.add((HotlistHeaderViewModel) visitable);
            }

            if (visitable instanceof HotlistProductViewModel) {
                list.add((HotlistProductViewModel) visitable);
            }

            if (visitable instanceof SearchEmptyViewModel) {
                list.add((SearchEmptyViewModel) visitable);
            }
        }
        return list;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        setTotalData(savedInstanceState.getInt(INSTANCE_TOTAL_DATA));
        setStartFrom(savedInstanceState.getInt(INSTANCE_START_FROM));
        setElement(mappingIntoVisitable(savedInstanceState.getParcelableArrayList(INSTANCE_LIST_DATA)));
        notifyDataSetChanged();
    }

    private List<Visitable> mappingIntoVisitable(ArrayList<Parcelable> parcelableArrayList) {
        List<Visitable> list = new ArrayList<>();
        for (Parcelable parcelable : parcelableArrayList) {
            if (parcelable instanceof HotlistHeaderViewModel) {
                list.add((HotlistHeaderViewModel) parcelable);
            }

            if (parcelable instanceof HotlistProductViewModel) {
                list.add((HotlistProductViewModel) parcelable);
            }

            if (parcelable instanceof SearchEmptyViewModel) {
                list.add((SearchEmptyViewModel) parcelable);
            }
        }
        return list;
    }
}
