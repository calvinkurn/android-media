package com.tokopedia.seller.goldmerchant.featured.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckListAdapter;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class FeaturedProductFragment extends BaseListFragment<BlankPresenter, FeaturedProductModel>
        implements FeaturedProductView, OnStartDragListener,
        FeaturedProductAdapter.UseCaseListener, SimpleItemTouchHelperCallback.isEnabled, BaseMultipleCheckListAdapter.CheckedCallback<FeaturedProductModel> {

    FloatingActionButton fab;

    @Inject
    FeaturedProductPresenterImpl featuredProductPresenter;
    private ItemTouchHelper mItemTouchHelper;

    @FeaturedProductType
    int featuredProductType = FeaturedProductType.DEFAULT_DISPLAY;

    private int MAX_ITEM = 5;
    private int delete_selection_count = 0;

    Random random = new Random();

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
    }

    @Override
    protected void showViewEmptyList() {
        super.showViewEmptyList();
        hideFab();
    }

    @Override
    protected void showViewSearchNoResult() {
        super.showViewSearchNoResult();
        hideFab();
    }

    @Override
    public void onSearchLoaded(@NonNull List<FeaturedProductModel> list, int totalItem) {
        list = new ArrayList<FeaturedProductModel>(){{
            add(new FeaturedProductModel(1+random.nextInt(1000), "", "makan", ""));
            add(new FeaturedProductModel(1+random.nextInt(1000), "", "nasi", ""));
            add(new FeaturedProductModel(1+random.nextInt(1000), "", "enak", ""));
            add(new FeaturedProductModel(1+random.nextInt(1000), "", "banget", ""));
        }};

        super.onSearchLoaded(list, list.size());
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
    protected void searchForPage(int page) {
        featuredProductPresenter.loadData();
    }

    public void updateTitleView(int itemCount, int maxItemCount){
        if(getActivity() != null && getActivity() instanceof AppCompatActivity){
            ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if( supportActionBar != null){
                supportActionBar.setTitle(String.format("%d / %d Item", itemCount, maxItemCount));
            }
        }
    }

    @Override
    public void onPostSuccess() {

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


    @Override
    public void postData(List<FeaturedProductModel> data) {
        featuredProductPresenter.postData(FeaturedProductPOSTUseCase.createParam(data));
    }

    @Override
    public int getFeaturedProductType() {
        return featuredProductType;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_gm_featured_product, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_arrange) {
            setFeaturedProductType(FeaturedProductType.ARRANGE_DISPLAY);
            return true;
        }else if(item.getItemId() == R.id.menu_delete){
            setFeaturedProductType(FeaturedProductType.DELETE_DISPLAY);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setFeaturedProductType(@FeaturedProductType int featuredProductType){
        this.featuredProductType = featuredProductType;
        adapter.notifyDataSetChanged();
    }

    public void showOtherActionDialog() {
        String title = getString(R.string.product_title_confirmation_delete_video);
        String message = getString(R.string.product_confirmation_delete_video);

        switch(featuredProductType){
            case FeaturedProductType.ARRANGE_DISPLAY:
            case FeaturedProductType.DELETE_DISPLAY:
                // TODO change this according to type.
                title = getString(R.string.product_title_confirmation_delete_video);
                message = getString(R.string.product_confirmation_delete_video);
                break;
            default:
                title = getString(R.string.product_title_confirmation_delete_video);
                message = getString(R.string.product_confirmation_delete_video);
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                featuredProductPresenter.postData(
                        FeaturedProductPOSTUseCase.createParam(
                                adapter.getData()
                        )
                );

                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
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
}
