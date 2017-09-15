package com.tokopedia.seller.goldmerchant.featured.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.NoResultDataBinder;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseEmptyDataBinder;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.seller.base.view.emptydatabinder.EmptyDataBinder;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.featured.constant.FeaturedProductType;
import com.tokopedia.seller.goldmerchant.featured.di.component.DaggerFeaturedProductComponent;
import com.tokopedia.seller.goldmerchant.featured.domain.interactor.FeaturedProductPOSTUseCase;
import com.tokopedia.seller.goldmerchant.featured.helper.ItemTouchHelperAdapter;
import com.tokopedia.seller.goldmerchant.featured.helper.OnStartDragListener;
import com.tokopedia.seller.goldmerchant.featured.helper.SimpleItemTouchHelperCallback;
import com.tokopedia.seller.goldmerchant.featured.view.adapter.FeaturedProductAdapter;
import com.tokopedia.seller.goldmerchant.featured.view.adapter.model.FeaturedProductModel;
import com.tokopedia.seller.goldmerchant.featured.view.listener.FeaturedProductView;
import com.tokopedia.seller.goldmerchant.featured.view.presenter.FeaturedProductPresenterImpl;
import com.tokopedia.seller.product.picker.common.ProductListPickerConstant;
import com.tokopedia.seller.product.picker.view.ProductListPickerActivity;
import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel;
import com.tokopedia.seller.topads.dashboard.view.adapter.viewholder.TopAdsEmptyAdDataBinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class FeaturedProductFragment extends BaseListFragment<BlankPresenter, FeaturedProductModel>
        implements FeaturedProductView, OnStartDragListener,
        FeaturedProductAdapter.UseCaseListener, SimpleItemTouchHelperCallback.isEnabled, BaseMultipleCheckListAdapter.CheckedCallback<FeaturedProductModel> {

    private static final int REQUEST_CODE = 12314;
    FloatingActionButton fab;
    @Inject
    FeaturedProductPresenterImpl featuredProductPresenter;
    @FeaturedProductType
    int featuredProductType = FeaturedProductType.DEFAULT_DISPLAY;
    List<FeaturedProductModel> productModelsTemp = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    private int MAX_ITEM = 5;
    private int delete_selection_count = 0;
    private CoordinatorLayout coordinatorLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initInjector() {
        DaggerFeaturedProductComponent
                .builder()
                .goldMerchantComponent(getComponent(GoldMerchantComponent.class))
                .build().inject(this);
        featuredProductPresenter.attachView(this);
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        updateSubtitleCounterProduct();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_featured_product;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        featuredProductPresenter.detachView();
    }

    @Override
    public void onItemClicked(FeaturedProductModel featuredProductModel) {

    }

    @Override
    protected void showViewList(@NonNull List<FeaturedProductModel> list) {
        super.showViewList(list);
        if(list.size() < MAX_ITEM) {
            showFab();
        }else{
            hideFab();
        }
        updateSubtitleCounterProduct();
    }

    private void updateSubtitleCounterProduct() {
        if(featuredProductType == FeaturedProductType.DELETE_DISPLAY){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("");
        }else if(getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(getString(R.string.featured_product_subtitle_counter, adapter.getDataSize(), MAX_ITEM));
        }
    }

    @Override
    protected void showViewEmptyList() {
        super.showViewEmptyList();
        hideFab();
        updateSubtitleCounterProduct();
    }

    @Override
    protected void showViewSearchNoResult() {
        super.showViewSearchNoResult();
        hideFab();
        updateSubtitleCounterProduct();
    }

    @Override
    protected void setAndSearchForPage(int page) {
        featuredProductPresenter.loadData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout_parent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToProductPicker();
            }
        });
        return view;
    }

    protected void moveToProductPicker() {
        List<ProductListPickerViewModel> productListPickerViewModels =
                new ArrayList<>();
        for (FeaturedProductModel featuredProductModel : adapter.getData()) {
            ProductListPickerViewModel productListPickerViewModel =
                    new ProductListPickerViewModel();
            productListPickerViewModel.setId(featuredProductModel.getId());
            productListPickerViewModel.setProductPrice(featuredProductModel.getProductPrice());
            productListPickerViewModel.setTitle(featuredProductModel.getProductName());
            productListPickerViewModel.setImageUrl(featuredProductModel.getImageUrl());

            productListPickerViewModels.add(productListPickerViewModel);
        }


        Intent intent = ProductListPickerActivity.createIntent(
                FeaturedProductFragment.this.getActivity(), productListPickerViewModels

        );
        FeaturedProductFragment.this.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback((ItemTouchHelperAdapter) adapter, this);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected BaseListAdapter<FeaturedProductModel> getNewAdapter() {
        FeaturedProductAdapter featuredProductAdapter = new FeaturedProductAdapter(this);
        featuredProductAdapter.setUseCaseListener(this);
        featuredProductAdapter.setCheckedCallback(this);
        return featuredProductAdapter;
    }

    @Override
    public void onResume() {
        super.onResume();
        switch (featuredProductType) {
            case FeaturedProductType.DEFAULT_DISPLAY:
                if (productModelsTemp != null && productModelsTemp.size() > 0) {
                    featuredProductPresenter.postData(
                            FeaturedProductPOSTUseCase.createParam(
                                    Collections.unmodifiableList(productModelsTemp)
                            )
                    );
                } else {
                    featuredProductPresenter.loadData();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void searchForPage(int page) {

    }

    public void updateTitleView(int itemCount, int maxItemCount){
        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setTitle(String.format("%d / %d Item", itemCount, maxItemCount));
            }
        }
    }

    public void updateTitleView(@StringRes int text) {
        if(getActivity() != null && getActivity() instanceof AppCompatActivity){
            ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if( supportActionBar != null){
                supportActionBar.setTitle(text);
            }
        }
    }

    @Override
    public void onPostSuccess() {
        switch (featuredProductType) {
            case FeaturedProductType.DEFAULT_DISPLAY:
                adapter.clearData();
                onSearchLoaded(productModelsTemp, productModelsTemp.size());
                productModelsTemp.clear();
                break;
            default:
                showViewEmptyList();
                break;
        }
        setFeaturedProductType(FeaturedProductType.DEFAULT_DISPLAY);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


    @Override
    public void postData(List<FeaturedProductModel> data) {
        // do nothing
    }

    @Override
    public int getFeaturedProductType() {
        return featuredProductType;
    }

    public void setFeaturedProductType(@FeaturedProductType int featuredProductType) {
        this.featuredProductType = featuredProductType;
        getActivity().invalidateOptionsMenu();
        if(featuredProductType == FeaturedProductType.DELETE_DISPLAY || featuredProductType == FeaturedProductType.ARRANGE_DISPLAY){
            swipeToRefresh.setEnabled(false);
        }else{
            swipeToRefresh.setEnabled(true);
        }
        adapter.notifyDataSetChanged();
        updateSubtitleCounterProduct();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_gm_featured_product, menu);
        if(featuredProductType == FeaturedProductType.DELETE_DISPLAY){
            setVisibleMenuModeDelete(menu, true);
        }else{
            setVisibleMenuModeDelete(menu, false);
        }
    }

    void setVisibleMenuModeDelete(Menu menu, boolean isMenuModeDeleteVisible) {
        MenuItem menuItemDeleteMode = menu.findItem(R.id.menu_delete_mode);
        MenuItem menuItemArrange = menu.findItem(R.id.menu_arrange);
        MenuItem menuItemDelete = menu.findItem(R.id.menu_delete);
        menuItemDeleteMode.setVisible(isMenuModeDeleteVisible);
        menuItemArrange.setVisible(!isMenuModeDeleteVisible);
        menuItemDelete.setVisible(!isMenuModeDeleteVisible);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_arrange) {
            setFeaturedProductType(FeaturedProductType.ARRANGE_DISPLAY);
            hideFab();
            return true;
        }else if(item.getItemId() == R.id.menu_delete){
            ((FeaturedProductAdapter) adapter).clearSelections();
            setFeaturedProductType(FeaturedProductType.DELETE_DISPLAY);
            hideFab();
            return true;
        }else if(item.getItemId() == R.id.menu_delete_mode){
            showOtherActionDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showOtherActionDialog() {
        String title;
        String message;
        String labelAction;

        switch(featuredProductType){
            case FeaturedProductType.ARRANGE_DISPLAY:
                title = getString(R.string.featured_product_sort_title);
                message = getString(R.string.featured_product_sort_desc);
                labelAction = getString(R.string.action_sort);
                break;
            case FeaturedProductType.DELETE_DISPLAY:
                // TODO change this according to type.
                title = getString(R.string.featured_product_delete_title);
                message = getString(R.string.featured_product_delete_desc, ((FeaturedProductAdapter)adapter).getSelectedSize());
                labelAction = getString(R.string.label_delete);
                break;
            default:
                title = getString(R.string.featured_product_sort_title);
                message = getString(R.string.featured_product_sort_desc);
                labelAction = getString(R.string.action_sort);
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.label_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                switch (featuredProductType) {
                    case FeaturedProductType.DELETE_DISPLAY:
                        ((FeaturedProductAdapter) adapter).removeSelections();

                        break;
                    default:
                        break;
                }

                updateTitleView(R.string.featured_product_title);
                showFab();

                featuredProductPresenter.postData(
                        FeaturedProductPOSTUseCase.createParam(
                                adapter.getData()
                        )
                );

                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                setFeaturedProductType(FeaturedProductType.DEFAULT_DISPLAY);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean isLongPressDragEnabled() {
        switch (featuredProductType){
            case FeaturedProductType.ARRANGE_DISPLAY:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void onItemChecked(FeaturedProductModel featuredProductModel, boolean checked) {
        switch (featuredProductType){
            case FeaturedProductType.DELETE_DISPLAY:
                if(checked){
                    delete_selection_count++;
                }else{
                    delete_selection_count--;
                }
                updateTitleView(delete_selection_count, MAX_ITEM);
                break;
            default:
                break;
        }
    }


    public void showFab() {
        fab.show();
    }


    public void hideFab() {
        fab.hide();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE && intent != null) {
            List<ProductListPickerViewModel> productListPickerViewModels = intent.getParcelableArrayListExtra(ProductListPickerConstant.EXTRA_PRODUCT_LIST_SUBMIT);

            if (productListPickerViewModels != null) {

                productModelsTemp = new ArrayList<>();
                for (ProductListPickerViewModel productListPickerViewModel : productListPickerViewModels) {
                    FeaturedProductModel featuredProductModel = new FeaturedProductModel();
                    featuredProductModel.setProductId(Long.valueOf(productListPickerViewModel.getId()));
                    featuredProductModel.setImageUrl(productListPickerViewModel.getIcon());
                    featuredProductModel.setProductPrice(productListPickerViewModel.getProductPrice());
                    featuredProductModel.setProductName(productListPickerViewModel.getTitle());

                    productModelsTemp.add(featuredProductModel);
                }
                updateSubtitleCounterProduct();
            }
        }
    }

    @Override
    protected NoResultDataBinder getEmptyViewDefaultBinder() {
        EmptyDataBinder emptyGroupAdsDataBinder = new EmptyDataBinder(adapter, R.drawable.ic_empty_featured_product);
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.featured_product_title_empty));
        emptyGroupAdsDataBinder.setEmptyContentText(getString(R.string.featured_product_description_empty));
        emptyGroupAdsDataBinder.setEmptyButtonItemText(getString(R.string.featured_product_add_title_empty));
        emptyGroupAdsDataBinder.setCallback(new BaseEmptyDataBinder.Callback() {
            @Override
            public void onEmptyContentItemTextClicked() {
                moveToProductPicker();
            }

            @Override
            public void onEmptyButtonClicked() {
                moveToProductPicker();
            }
        });
        return emptyGroupAdsDataBinder;
    }

    @Override
    protected void initSnackbarRetry(NetworkErrorHelper.RetryClickedListener listener) {
        snackBarRetry = NetworkErrorHelper.createSnackbarWithAction(coordinatorLayout, listener);
    }
}
