package com.tokopedia.shop.product.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.view.activity.ShopEtalaseActivity;
import com.tokopedia.shop.product.view.activity.ShopProductFilterActivity;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductListViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductSingleViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.listener.ShopProductListView;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.shop.product.view.presenter.ShopProductListPresenter;

import javax.inject.Inject;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListFragment extends BaseSearchListFragment<ShopProductViewModel, ShopProductAdapterTypeFactory> implements ShopProductListView, ShopProductClickedListener {

    public static final int SPAN_COUNT = 2;
    public static final int REQUEST_CODE_ETALASE = 12912;
    public static final int REQUEST_CODE_SORT = 12913;
    public static final String ETALASE_ID = "ETALASE_ID";
    public static final String ETALASE_NAME = "ETALASE_NAME";
    private static final Pair<Integer, Integer>[] layoutType = new Pair[]{
            new Pair<>(ShopProductViewHolder.LAYOUT, 65),
            new Pair<>(ShopProductSingleViewHolder.LAYOUT, 97),
            new Pair<>(ShopProductListViewHolder.LAYOUT, 97)
    };

    private static final int[] LAYOUT_IMAGE_DRAWABLE_LIST = new int[]{
            R.drawable.ic_see_grid,
            R.drawable.ic_see_big_grid,
            R.drawable.ic_see_list};

    @Inject
    ShopProductListPresenter shopProductListPresenter;
    private LabelView chooseEtalaseLabelView;
    private ShopModuleRouter shopModuleRouter;
    private String etalaseName;
    private String etalaseId;

    private String shopId;
    private String keyword;
    private String sortName = Integer.toString(Integer.MIN_VALUE);
    private Pair<Integer, Integer> currentLayoutType = new Pair<>(ShopProductViewHolder.LAYOUT, 65);
    private int currentIndex = 0, currentImgBottomNav = 0;
    private String sortId;
    private RecyclerView recyclerViews;
    private BottomActionView bottomActionView;

    public static ShopProductListFragment createInstance(String shopId) {
        ShopProductListFragment shopProductListFragment = new ShopProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ShopParamConstant.SHOP_ID, shopId);
        shopProductListFragment.setArguments(bundle);
        return shopProductListFragment;
    }

    @Override
    protected ShopProductAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopProductAdapterTypeFactory(this);
    }

    @NonNull
    @Override
    protected BaseListAdapter<ShopProductViewModel, ShopProductAdapterTypeFactory> createAdapterInstance() {
        return new ShopProductAdapter(getAdapterTypeFactory());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null && context.getApplicationContext() instanceof ShopModuleRouter) {
            shopModuleRouter = ((ShopModuleRouter) context.getApplicationContext());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getArguments().getString(ShopParamConstant.SHOP_ID);
        shopProductListPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_product_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViews = view.findViewById(R.id.recycler_view);
        chooseEtalaseLabelView = view.findViewById(R.id.label_view_choose_etalase);
        bottomActionView = view.findViewById(R.id.bottom_action_view);

        setBottomActionViewImage(currentImgBottomNav);
        RecyclerView.LayoutManager layoutManager = iterate(recyclerViews);
        recyclerViews.setLayoutManager(layoutManager);

        chooseEtalaseLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopModuleRouter != null) {
                    Intent etalaseIntent = ShopEtalaseActivity.createIntent(getActivity(), shopId, etalaseId);
                    ShopProductListFragment.this.startActivityForResult(etalaseIntent, REQUEST_CODE_ETALASE);
                }
            }
        });

        bottomActionView.setButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.LayoutManager layoutManager = iterate(recyclerViews);
                recyclerViews.setLayoutManager(layoutManager);
                getAdapter().notifyDataSetChanged();
                setBottomActionViewImage(++currentImgBottomNav);
            }
        });

        bottomActionView.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopProductListFragment.this.startActivityForResult(ShopProductFilterActivity.createIntent(getActivity(), sortName), REQUEST_CODE_SORT);
            }
        });

        if (shopModuleRouter != null && !shopModuleRouter.isMyOwnShop(shopId)) {
            chooseEtalaseLabelView.setContent(getString(R.string.shop_info_filter_all_showcase));
        } else {
            chooseEtalaseLabelView.setContent(getString(R.string.shop_info_filter_menu_etalase_all));
        }
    }

    private void setBottomActionViewImage(int index) {
        if (bottomActionView != null && (index >= 0 && index < LAYOUT_IMAGE_DRAWABLE_LIST.length))
            bottomActionView.setSecondImageDrawable(LAYOUT_IMAGE_DRAWABLE_LIST[index]);
        else {
            currentImgBottomNav = 0;
            bottomActionView.setSecondImageDrawable(LAYOUT_IMAGE_DRAWABLE_LIST[currentImgBottomNav]);
        }
    }

    private RecyclerView.LayoutManager iterate(final RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = null;
        if (getNextIndex(currentIndex, layoutType.length) < 0) {
            currentLayoutType = layoutType[currentIndex = 0];
        } else {
            currentLayoutType = layoutType[currentIndex];
        }
        switch (currentLayoutType.second) {
            case 65:
                layoutManager = new GridLayoutManager(recyclerView.getContext(), SPAN_COUNT, LinearLayoutManager.VERTICAL, false);
                ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (recyclerView.getAdapter().getItemViewType(position) == ShopProductViewHolder.LAYOUT) {
                            return ShopProductViewHolder.SPAN_LOOK_UP;
                        }
                        return SPAN_COUNT;
                    }
                });
                break;
            default:
                layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false
                );
                break;
        }
        currentIndex++;

        return layoutManager;
    }

    private int getNextIndex(int currentIndex, int max) {
        if (currentIndex >= 0 && currentIndex < max) {
            return currentIndex;
        } else {
            return -1;
        }
    }

    @Override
    public void loadData(int page) {
        shopProductListPresenter.getShopPageList(shopId, keyword, etalaseId, 0, page, Integer.valueOf(sortName));
    }

    @Override
    public void onItemClicked(ShopProductViewModel shopProductViewModel) {

    }

    @Override
    public void onWishListClicked(ShopProductViewModel shopProductViewModel) {
        if (shopProductViewModel.isWishList()) {
            shopProductListPresenter.removeFromWishList(shopProductViewModel.getId());
        } else {
            shopProductListPresenter.addToWishList(shopProductViewModel.getId());
        }
    }

    @Override
    public void onSuccessAddToWishList(String productId, Boolean value) {
        ((ShopProductAdapter) getAdapter()).updateWishListStatus(productId, true);
    }

    @Override
    public void onErrorAddToWishList(Throwable e) {

    }

    @Override
    public void onSuccessRemoveFromWishList(String productId, Boolean value) {
        ((ShopProductAdapter) getAdapter()).updateWishListStatus(productId, false);
    }

    @Override
    public void onErrorRemoveFromWishList(Throwable e) {

    }

    @Override
    public void onSearchSubmitted(String s) {
        this.isLoadingInitialData = true;
        keyword = s;
    }

    @Override
    public void onSearchTextChanged(String s) {
        this.isLoadingInitialData = true;
        keyword = s;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ETALASE:
                if (resultCode == Activity.RESULT_OK) {
                    etalaseId = data.getStringExtra(ETALASE_ID);
                    etalaseName = data.getStringExtra(ETALASE_NAME);
                    chooseEtalaseLabelView.setContent(MethodChecker.fromHtml(etalaseName));
                    this.isLoadingInitialData = true;
                    loadInitialData();
                }
                break;

            case REQUEST_CODE_SORT:
                if (resultCode == Activity.RESULT_OK) {
                    sortId = data.getStringExtra(ShopProductFilterActivity.SORT_ID);
                    sortName = data.getStringExtra(ShopProductFilterActivity.SORT_NAME);
                    this.isLoadingInitialData = true;
                    loadInitialData();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initInjector() {
        DaggerShopProductComponent
                .builder()
                .shopProductModule(new ShopProductModule())
                .shopComponent(getComponent(ShopComponent.class))
                .build()
                .inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopProductListPresenter != null) {
            shopProductListPresenter.detachView();
        }
    }
}
