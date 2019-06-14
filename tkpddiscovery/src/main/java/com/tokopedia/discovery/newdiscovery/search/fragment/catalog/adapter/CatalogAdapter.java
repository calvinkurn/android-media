package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.common.utils.toolargetool.TooLargeTool;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.BrowseSectionGeneralAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.BrowseSectionTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.factory.CatalogTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model.CatalogHeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model.CatalogViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 10/12/17.
 */

@SuppressWarnings({"FieldCanBeLocal", "unused", "WeakerAccess"})
public class CatalogAdapter extends BrowseSectionGeneralAdapter {

    private static final String INSTANCE_NEXT_PAGE = "INSTANCE_NEXT_PAGE";
    private static final String INSTANCE_LIST_DATA = "INSTANCE_LIST_DATA";
    private static final String INSTANCE_START_FROM = "INSTANCE_START_FROM";
    public static final int THRESHOLD_CRASH_LIST_COUNT = 12;

    private List<Visitable> mVisitables;
    private final CatalogTypeFactory typeFactory;

    private boolean nextPage;
    private int startFrom;

    public CatalogAdapter(OnItemChangeView itemChangeView, CatalogTypeFactory typeFactory) {
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
        int start = getItemCount();
        mVisitables.add(visitable);
        notifyItemRangeInserted(start, 1);
    }

    public void addElements(List<Visitable> data) {
        int start = getItemCount();
        mVisitables.addAll(data);
        notifyItemRangeInserted(start, data.size());
    }

    public List<Visitable> getElements() {
        return mVisitables;
    }

    public void setChangedItem(int position, Visitable visitable) {
        if (position < mVisitables.size()) {
            mVisitables.remove(position);
            mVisitables.add(position, visitable);
            notifyItemChanged(position);
        }
    }

    public void removeItem(int position) {
        mVisitables.remove(position);
        notifyItemRemoved(position);
    }

    public boolean isEmpty() {
        return mVisitables.isEmpty();
    }

    public boolean hasNextPage() {
        return nextPage;
    }

    public void setNextPage(boolean nextPage) {
        this.nextPage = nextPage;
    }

    public void onSaveInstanceState(Bundle outState) {
        //assume that 12 object is potential crash
        if ((getElements() != null && getElements().size() > THRESHOLD_CRASH_LIST_COUNT)) {
            return;
        }
        ArrayList<Parcelable> parcelables = mappingIntoParcelableArrayList(getElements());
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(INSTANCE_LIST_DATA, parcelables);
        if (!TooLargeTool.isPotentialCrash(bundle)) {
            outState.putBoolean(INSTANCE_NEXT_PAGE, hasNextPage());
            outState.putInt(INSTANCE_START_FROM, getStartFrom());
            outState.putParcelableArrayList(INSTANCE_LIST_DATA, parcelables);
        }
        bundle.clear();
    }

    private ArrayList<Parcelable> mappingIntoParcelableArrayList(List<Visitable> elements) {
        ArrayList<Parcelable> list = new ArrayList<>();
        for (Visitable visitable : elements) {
            if (visitable instanceof CatalogViewModel) {
                list.add((CatalogViewModel) visitable);
            }
        }
        return list;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(INSTANCE_LIST_DATA)) {
            setNextPage(savedInstanceState.getBoolean(INSTANCE_NEXT_PAGE));
            setStartFrom(savedInstanceState.getInt(INSTANCE_START_FROM));
            setElement(mappingIntoVisitable(savedInstanceState.getParcelableArrayList(INSTANCE_LIST_DATA)));
            notifyDataSetChanged();
        }
    }

    private List<Visitable> mappingIntoVisitable(ArrayList<Parcelable> parcelableArrayList) {
        List<Visitable> list = new ArrayList<>();
        for (Parcelable parcelable : parcelableArrayList) {
            if (parcelable instanceof CatalogViewModel) {
                list.add((CatalogViewModel) parcelable);
            }
        }
        return list;
    }

    public void incrementStart() {
        setStartFrom(getStartFrom() + Integer.parseInt(BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS));
    }

    public void resetStartFrom() {
        setStartFrom(Integer.parseInt(BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS));
    }

    public int getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(int startFrom) {
        this.startFrom = startFrom;
    }

    @Override
    protected BrowseSectionTypeFactory getTypeFactory() {
        return typeFactory;
    }

    @Override
    public List<Visitable> getItemList() {
        return mVisitables;
    }

    public boolean isCatalogHeader(int position) {
        if (checkDataSize(position))
            return getItemList().get(position) instanceof CatalogHeaderViewModel;
        return false;
    }

    @Override
    public boolean isEmptyItem(int position) {
        return checkDataSize(position) && getItemList().get(position) instanceof EmptySearchModel;
    }

    @Override
    public int getIconTypeRecyclerView() {
        switch (getTypeFactory().getRecyclerViewItem()) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
                return R.drawable.ic_list_green;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
                return R.drawable.ic_grid_default_green;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
                return R.drawable.ic_grid_box_green;
            default:
                return R.drawable.ic_grid_default_green;
        }
    }
}
