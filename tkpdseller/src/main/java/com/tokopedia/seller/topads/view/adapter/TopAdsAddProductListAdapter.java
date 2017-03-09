package com.tokopedia.seller.topads.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsAddProductListViewHolder;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsEmptyRowViewHolder;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsNonPromotedViewHolder;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsPromotedViewHolder;
import com.tokopedia.seller.topads.view.listener.AdapterSelectionListener;
import com.tokopedia.seller.topads.view.listener.FragmentItemSelection;
import com.tokopedia.seller.topads.view.model.BaseTopAdsProductModel;
import com.tokopedia.seller.topads.view.model.EmptyTypeBasedModel;
import com.tokopedia.seller.topads.view.model.NonPromotedTopAdsAddProductModel;
import com.tokopedia.seller.topads.view.model.PromotedTopAdsAddProductModel;
import com.tokopedia.seller.topads.view.model.TopAdsAddProductModel;
import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;
import com.tokopedia.seller.topads.view.model.TypeBasedModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2/13/17.
 *
 * @author normansyahputa
 *         <p>
 *         13-02-2017 - create first time and layout
 *         06-03-2017, separate promoted and non promoted.
 *         <p>
 */
public class TopAdsAddProductListAdapter extends BaseLinearRecyclerViewAdapter
        implements AdapterSelectionListener<TopAdsProductViewModel> {
    List<TypeBasedModel> datas;
    FragmentItemSelection fragmentItemSelection;
    private ImageHandler imageHandler;
    private boolean isEmptyShown;

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
        LayoutInflater from = LayoutInflater.
                from(parent.getContext());
        switch (viewType) {
            case PromotedTopAdsAddProductModel.TYPE:
                View view = from.inflate(R.layout.row_top_ads_add_product_list_promoted,
                        parent, false);
                return new TopAdsPromotedViewHolder(view);
            case NonPromotedTopAdsAddProductModel.TYPE:
                view = from.inflate(R.layout.row_top_ads_add_product_list_non_promoted,
                        parent, false);
                return new TopAdsNonPromotedViewHolder(view, this);
            case TopAdsAddProductModel.TYPE:
                view = from.inflate(R.layout.row_top_ads_add_product_list, parent, false);
                return new TopAdsAddProductListViewHolder(view, imageHandler, this);
            case EmptyTypeBasedModel.TYPE:
                view = from.inflate(R.layout.row_top_ads_empty_list, parent, false);
                return new TopAdsEmptyRowViewHolder(view);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case PromotedTopAdsAddProductModel.TYPE:
                ((TopAdsPromotedViewHolder) holder).bind(
                        ((PromotedTopAdsAddProductModel) datas.get(position))
                );
                break;
            case NonPromotedTopAdsAddProductModel.TYPE:
                ((TopAdsNonPromotedViewHolder) holder).bind(
                        ((NonPromotedTopAdsAddProductModel) datas.get(position))
                );
                break;
            case TopAdsAddProductModel.TYPE:
                TopAdsAddProductModel topAdsAddProductModel
                        = (TopAdsAddProductModel) datas.get(position);
                ((TopAdsAddProductListViewHolder) holder).
                        bind(topAdsAddProductModel);
                break;
            case EmptyTypeBasedModel.TYPE:
                break; // prevent from entering super.
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
            return datas.get(position).getType();
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
            if (data != null && data instanceof BaseTopAdsProductModel) {
                BaseTopAdsProductModel baseTopAdsProductModel = ((BaseTopAdsProductModel) data);
                if (baseTopAdsProductModel.getTopAdsProductViewModel() != null) {
                    topAdsProductViewModels.add(
                            baseTopAdsProductModel.getTopAdsProductViewModel());
                }
            } else {
                throw new RuntimeException("all model in this adapter must implement " +
                        "BaseTopAdsProductModel");
            }
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
        } else {
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

    public void insertEmptyFooter() {
        if (!isEmptyShown) {
            isEmptyShown = datas.add(new EmptyTypeBasedModel());
            notifyItemInserted(datas.size() - 1);
        }
    }

    public void removeEmptyFooter() {
        if (isEmptyShown) {
            int index = datas.size() - 1;
            datas.remove(index);
            notifyItemRemoved(index);

            isEmptyShown = false;
        }
    }
}
