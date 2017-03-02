package com.tokopedia.seller.topads.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.listener.AdapterSelectionListener;
import com.tokopedia.seller.topads.listener.FragmentItemSelection;
import com.tokopedia.seller.topads.view.models.TopAdsAddProductModel;
import com.tokopedia.seller.topads.view.models.TopAdsProductViewModel;
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
        implements AdapterSelectionListener<TopAdsProductViewModel> {
    List<TypeBasedModel> datas;
    FragmentItemSelection fragmentItemSelection;
    private ImageHandler imageHandler;

    public TopAdsAddProductListAdapter(ImageHandler imageHandler,
                                       FragmentItemSelection fragmentItemSelection) {
        this.imageHandler = imageHandler;
        this.fragmentItemSelection = fragmentItemSelection;
        datas = new ArrayList<>();
    }

    public TopAdsAddProductListAdapter() {
        datas = new ArrayList<>();
    }

    public void setImageHandler(ImageHandler imageHandler) {
        this.imageHandler = imageHandler;
    }

    public void setFragmentItemSelection(FragmentItemSelection fragmentItemSelection) {
        this.fragmentItemSelection = fragmentItemSelection;
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
        showEmptyFull(false);
        showEmpty(false);
        showLoadingFull(false);
        showLoading(false);
        showRetry(false);
        showRetryFull(false);
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
    public void onChecked(int position, TopAdsProductViewModel data) {
        fragmentItemSelection.onChecked(position, data);
    }

    @Override
    public void onUnChecked(int position, TopAdsProductViewModel data) {
        fragmentItemSelection.onUnChecked(position, data);
    }

    @Override
    public boolean isSelected(TopAdsProductViewModel data) {
        return fragmentItemSelection.isSelected(data);
    }

    public void notifyUnCheck(TopAdsProductViewModel topAdsProductViewModel) {
        // search for position;
        List<TopAdsProductViewModel> topAdsProductViewModels = convertTo();

        int position = topAdsProductViewModels.indexOf(topAdsProductViewModel);
        Log.d("MNORMANSYAH", " search this " + topAdsProductViewModel + " position " + position);

        if (position >= 0) {
            removedItem(position);
        }
    }

    private List<TopAdsProductViewModel> convertTo() {
        List<TopAdsProductViewModel> topAdsProductViewModels =
                new ArrayList<>();
        for (TypeBasedModel data : datas) {
            TopAdsAddProductModel data1 = (TopAdsAddProductModel) data;
            topAdsProductViewModels.add(data1.productDomain);
        }
        return topAdsProductViewModels;
    }

    public void removedItem(int position) {
        TypeBasedModel typeBasedModel = datas.get(position);
        if (typeBasedModel != null && typeBasedModel instanceof TopAdsAddProductModel) {
            TopAdsAddProductModel topAdsAddProductModel = (TopAdsAddProductModel) typeBasedModel;
            Log.d("MNORMANSYAH", "#4 before after selections : " + topAdsAddProductModel.isSelected()
                    + " position " + position);
            topAdsAddProductModel.setSelected(false);

            datas.set(position, topAdsAddProductModel);


            notifyItemChanged(position);
        }
    }

    public int getDataSize() {
        return datas.size();
    }

    public TopAdsProductViewModel getItem(int position) {
        if (position >= 0 && position < datas.size()) {
            TypeBasedModel typeBasedModel = datas.get(position);
            if (typeBasedModel instanceof TopAdsAddProductModel) {
                return ((TopAdsAddProductModel) typeBasedModel).productDomain;
            }
        }

        return null;
    }
}
