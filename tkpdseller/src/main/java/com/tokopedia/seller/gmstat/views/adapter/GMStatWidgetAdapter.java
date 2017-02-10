package com.tokopedia.seller.gmstat.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.presenters.GMStat;
import com.tokopedia.seller.gmstat.views.models.BaseGMModel;
import com.tokopedia.seller.gmstat.views.models.CommomGMModel;
import com.tokopedia.seller.gmstat.views.models.ConvRate;
import com.tokopedia.seller.gmstat.views.models.GrossIncome;
import com.tokopedia.seller.gmstat.views.models.LoadingGMModel;
import com.tokopedia.seller.gmstat.views.models.LoadingGMTwoModel;
import com.tokopedia.seller.gmstat.views.models.ProdSeen;
import com.tokopedia.seller.gmstat.views.models.ProdSold;
import com.tokopedia.seller.gmstat.views.models.SuccessfulTransaction;
import com.tokopedia.seller.gmstat.views.viewholder.CommonGMVH;
import com.tokopedia.seller.gmstat.views.viewholder.EmptyVH;
import com.tokopedia.seller.gmstat.views.viewholder.GrossEarnVH;
import com.tokopedia.seller.gmstat.views.viewholder.LoadingGM;
import com.tokopedia.seller.gmstat.views.viewholder.LoadingGMGrossIncome;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class GMStatWidgetAdapter extends RecyclerView.Adapter {

    private List<BaseGMModel> baseGMModels;
    private GMStat gmStat;

    public GMStatWidgetAdapter() {
        baseGMModels = new ArrayList<>();
    }

    public GMStatWidgetAdapter(List<BaseGMModel> baseGMModels, GMStat gmStat) {
        this.baseGMModels = baseGMModels;
        this.gmStat = gmStat;
    }

    public void clear() {
        this.baseGMModels.clear();
    }

    public void addAll(List<BaseGMModel> baseGMModels) {
        if (baseGMModels == null)
            return;
        this.baseGMModels.addAll(baseGMModels);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SuccessfulTransaction.TYPE:
            case ProdSeen.TYPE:
            case ProdSold.TYPE:
            case ConvRate.TYPE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_gmstat, parent, false);
                return new CommonGMVH(view);
            case GrossIncome.TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_gross_earn, parent, false);
                return new GrossEarnVH(view);
            case LoadingGMModel.TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_gmstat_loading, parent, false);
                return new LoadingGM(view);
            case LoadingGMTwoModel.TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_gross_earn_loading, parent, false);
                return new LoadingGMGrossIncome(view);
        }
        return new EmptyVH(new ImageView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (baseGMModels.get(position).type) {
            case SuccessfulTransaction.TYPE:
            case ProdSeen.TYPE:
            case ProdSold.TYPE:
            case ConvRate.TYPE:
                CommonGMVH commonGMVH = ((CommonGMVH) holder);
                commonGMVH.gmStat = gmStat;
                commonGMVH.bind((CommomGMModel) baseGMModels.get(position));
                break;
            case GrossIncome.TYPE:
                ((GrossEarnVH) holder).bind((GrossIncome) baseGMModels.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return baseGMModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (baseGMModels.get(position).type) {
            case SuccessfulTransaction.TYPE:
            case ProdSeen.TYPE:
            case ProdSold.TYPE:
            case GrossIncome.TYPE:
            case ConvRate.TYPE:
            case LoadingGMModel.TYPE:
            case LoadingGMTwoModel.TYPE:
                return baseGMModels.get(position).type;
            default:
                return super.getItemViewType(position);
        }
    }
}
