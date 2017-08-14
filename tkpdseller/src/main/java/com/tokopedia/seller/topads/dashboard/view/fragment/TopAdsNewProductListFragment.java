package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.core.customadapter.NoResultDataBinder;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsAddProductListActivity;
import com.tokopedia.seller.topads.dashboard.view.adapter.TopAdsNewProductListAdapter;
import com.tokopedia.seller.topads.dashboard.view.adapter.viewholder.TopAdsEmptyProductListDataBinder;
import com.tokopedia.seller.topads.dashboard.view.adapter.viewholder.TopAdsNewProductListViewHolder;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 8/7/17.
 */

public abstract class TopAdsNewProductListFragment extends BaseListFragment<TopAdsDetailEditPresenter, TopAdsProductViewModel> {

    public static final int ADD_PRODUCT_REQUEST_CODE = 1;

    private TextView counterProduct;
    private TextView addProduct;
    private Button buttonNext;
    protected ProgressDialog progressDialog;

    protected void onNextClicked(){
        showLoading();
    };

    @Override
    protected void initView(View view) {
        super.initView(view);
        counterProduct = (TextView) view.findViewById(R.id.counter_product);
        addProduct = (TextView) view.findViewById(R.id.add_product);
        buttonNext = (Button) view.findViewById(R.id.button_submit);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        updateSelectedProductCount();
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TopAdsAddProductListActivity.class);
                intent.putExtra(TopAdsExtraConstant.EXTRA_HIDE_EXISTING_GROUP, true);
                intent.putExtra(TopAdsExtraConstant.EXTRA_HIDE_ETALASE, true);
                intent.putExtra(TopAdsExtraConstant.EXTRA_MAX_NUMBER_SELECTION, 50);
                intent.putParcelableArrayListExtra(TopAdsExtraConstant.EXTRA_SELECTIONS, new ArrayList<Parcelable>(adapter.getData()));
                startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextClicked();
            }
        });
    }

    protected void showLoading() {
        progressDialog.show();
    }

    protected void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    protected void searchData() {
        populateView(adapter.getData());
    }

    @Override
    protected BaseListAdapter getNewAdapter() {
        return new TopAdsNewProductListAdapter(getDeleteListener());
    }

    @Override
    protected NoResultDataBinder getEmptyViewDefaultBinder() {
        TopAdsEmptyProductListDataBinder topAdsEmptyProductListDataBinder = new TopAdsEmptyProductListDataBinder(adapter);
        topAdsEmptyProductListDataBinder.setDrawableAsset(R.drawable.ic_empty_product_list);
        topAdsEmptyProductListDataBinder.setEmptyTitleText(getString(R.string.top_ads_label_choose_product));
        topAdsEmptyProductListDataBinder.setEmptyContentText(getString(R.string.top_ads_label_choose_product_desc_empty));
        return topAdsEmptyProductListDataBinder;
    }

    @NonNull
    protected TopAdsNewProductListViewHolder.DeleteListener getDeleteListener() {
        return new TopAdsNewProductListViewHolder.DeleteListener() {
            @Override
            public void onDelete(int position) {
                removeProduct(position);
            }
        };
    }

    protected void removeProduct(int position) {
        if(adapter.getData().size() > position){
            adapter.getData().remove(position);
            adapter.notifyItemRemoved(position);
        }
        updateSelectedProductCount();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_new_product_list;
    }


    @Override
    public void onItemClicked(TopAdsProductViewModel topAdsProductViewModel) {
        //do nothing
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == ADD_PRODUCT_REQUEST_CODE && intent != null && intent.hasExtra(TopAdsExtraConstant.EXTRA_SELECTIONS)) {
            populateView(intent.getParcelableArrayListExtra(TopAdsExtraConstant.EXTRA_SELECTIONS));
            updateSelectedProductCount();
        }
    }

    private void updateSelectedProductCount() {
        if (adapter.getDataSize() > 0) {
            counterProduct.setText(getString(R.string.top_ads_label_total_selected_product, adapter.getDataSize()));
            addProduct.setText(R.string.label_edit);
            buttonNext.setEnabled(true);
        } else {
            counterProduct.setText(getString(R.string.top_ads_label_total_selected_product_zero, adapter.getDataSize()));
            addProduct.setText(R.string.label_top_ads_add_product);
            buttonNext.setEnabled(false);
        }
    }

    protected void populateView(List topAdsProductViewModels){
        if(topAdsProductViewModels != null) {
            onSearchLoaded(topAdsProductViewModels, topAdsProductViewModels.size());
        }
    }
}
