package com.tokopedia.seller.topads.view.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.listener.AdapterSelectionListener;
import com.tokopedia.seller.topads.listener.FragmentItemSelection;
import com.tokopedia.seller.topads.view.models.TopAdsAddProductModel;
import com.tokopedia.seller.topads.view.models.TypeBasedModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2/13/17.
 *
 * @author normansyahputa
 *         <p>
 *         13-02-2017 - create first time and layout
 *         <p>
 */
public class TopAdsAddProductListAdapter extends BaseLinearRecyclerViewAdapter
        implements AdapterSelectionListener {
    List<TypeBasedModel> datas;
    SparseArrayCompat<Boolean> selections;
    FragmentItemSelection fragmentItemSelection;
    private ImageHandler imageHandler;

    public TopAdsAddProductListAdapter(ImageHandler imageHandler,
                                       FragmentItemSelection fragmentItemSelection) {
        this.imageHandler = imageHandler;
        this.fragmentItemSelection = fragmentItemSelection;
        datas = new ArrayList<>();
        selections = new SparseArrayCompat<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TopAdsAddProductModel.TYPE:
                View view = LayoutInflater.
                        from(parent.getContext()).inflate(R.layout.row_top_ads_add_product_list, parent, false);
                return new TopAdsAddProductListViewHolder(view, imageHandler, this);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TopAdsAddProductModel.TYPE:
                TopAdsAddProductModel topAdsAddProductModel
                        = (TopAdsAddProductModel) datas.get(position);
                ((TopAdsAddProductListViewHolder) holder).
                        bind(topAdsAddProductModel);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (datas.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return TopAdsAddProductModel.TYPE;
        }
    }

    private boolean isLastItemPosition(int position) {
        return position == datas.size();
    }

    public void clear() {
        datas.clear();
    }

    public void addAll(List<TypeBasedModel> typeBasedModels) {
        datas.addAll(typeBasedModels);
        notifyDataSetChanged();
    }

    public void addAllWithoutNotify(List<TypeBasedModel> typeBasedModels) {
        datas.addAll(typeBasedModels);
    }

    @Override
    public int getItemCount() {
        return datas.size() + super.getItemCount();
    }

    @Override
    public void onChecked(int position) {
        selections.put(position, true);

        fragmentItemSelection.onChecked(position);
    }

    @Override
    public void onUnChecked(int position) {
        selections.put(position, true);

        fragmentItemSelection.onUnChecked(position);
    }

    public int getDataSize() {
        return datas.size();
    }
}
