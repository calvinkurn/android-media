package com.tokopedia.seller.topads.view.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.models.TopAdsProductViewModel;
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
        implements AdapterSelectionListener<TopAdsProductViewModel> {
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
    public void onChecked(int position, TopAdsProductViewModel data) {
        selections.put(position, true);

        fragmentItemSelection.onChecked(position, data);
    }

    @Override
    public void onUnChecked(int position, TopAdsProductViewModel data) {
        selections.put(position, false);

        fragmentItemSelection.onUnChecked(position, data);
    }

    public void notifyUnCheck(int position){
        Log.d("MNORMANSYAH", "#3 before reset selections : "+selections.get(position)
                +" position "+ position);
        selections.put(position, false);
        Log.d("MNORMANSYAH", "#3 before after selections : "+selections.get(position)
                +" position "+ position);

        TypeBasedModel typeBasedModel = datas.get(position);
        if(typeBasedModel != null && typeBasedModel instanceof TopAdsAddProductModel){
            TopAdsAddProductModel topAdsAddProductModel = (TopAdsAddProductModel) typeBasedModel;
            Log.d("MNORMANSYAH", "#4 before after selections : "+topAdsAddProductModel.isSelected()
                    +" position "+ position);
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
