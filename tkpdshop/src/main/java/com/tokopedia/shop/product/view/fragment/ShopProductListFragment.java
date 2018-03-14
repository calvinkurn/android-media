package com.tokopedia.shop.product.view.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModelShimmeringGrid;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.analytic.ShopPageTracking;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.etalase.view.activity.ShopEtalaseActivity;
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter;
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductListViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductSingleViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.listener.ShopProductListView;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.shop.product.view.presenter.ShopProductListPresenter;
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListFragment extends BaseSearchListFragment<ShopProductViewModel, ShopProductAdapterTypeFactory> implements ShopProductListView, ShopProductClickedListener, ShopProductAdapterTypeFactory.TypeFactoryListener {

    public static final int SPAN_COUNT = 2;
    public static final int REQUEST_CODE_ETALASE = 12912;
    public static final int REQUEST_CODE_SORT = 12913;

    public static final int LAYOUT_GRID_TYPE = 65;
    public static final int LAYOUT_LIST_TYPE = 97;
    private static final Pair<Integer, Integer>[] layoutType = new Pair[]{
            new Pair<>(ShopProductViewHolder.LAYOUT, LAYOUT_GRID_TYPE),
            new Pair<>(ShopProductSingleViewHolder.LAYOUT, LAYOUT_LIST_TYPE),
            new Pair<>(ShopProductListViewHolder.LAYOUT, LAYOUT_LIST_TYPE)
    };

    private static final int[] LAYOUT_IMAGE_DRAWABLE_LIST = new int[]{
            R.drawable.ic_see_grid,
            R.drawable.ic_see_big_grid,
            R.drawable.ic_see_list};

    @Inject
    ShopProductListPresenter shopProductListPresenter;
    @Inject
    ShopPageTracking shopPageTracking;
    private LabelView etalaseLabelView;
    private ShopModuleRouter shopModuleRouter;

    private String etalaseId;

    private String shopId;
    private String keyword;
    private String sortName = Integer.toString(Integer.MIN_VALUE);
    private Pair<Integer, Integer> currentLayoutType = new Pair<>(ShopProductViewHolder.LAYOUT, 65);
    private int currentIndex = 0, currentImgBottomNav = 0;
    private String sortId;
    private RecyclerView recyclerViews;
    private BottomActionView bottomActionView;

    private DividerItemDecoration listDividerItemDecoration;

    public static ShopProductListFragment createInstance(String shopId, String keyword, String etalaseId, String sort) {
        ShopProductListFragment shopProductListFragment = new ShopProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ShopParamConstant.EXTRA_SHOP_ID, shopId);
        bundle.putString(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, keyword);
        bundle.putString(ShopParamConstant.EXTRA_ETALASE_ID, etalaseId);
        bundle.putString(ShopParamConstant.EXTRA_SORT_ID, sort);
        shopProductListFragment.setArguments(bundle);
        return shopProductListFragment;
    }

    @Override
    protected ShopProductAdapterTypeFactory getAdapterTypeFactory() {
        return new ShopProductAdapterTypeFactory(this, this);
    }

    @NonNull
    @Override
    protected BaseListAdapter<ShopProductViewModel, ShopProductAdapterTypeFactory> createAdapterInstance() {
        return new ShopProductAdapter(getAdapterTypeFactory());
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        EmptyModel emptyModel = new EmptyModel();
        emptyModel.setIconRes(R.drawable.ic_empty_list_search);
        emptyModel.setTitle(getString(R.string.shop_product_empty_product_title, keyword));
        emptyModel.setContent(getString(R.string.shop_product_empty_product_title_owner));
        return emptyModel;
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
        shopId = getArguments().getString(ShopParamConstant.EXTRA_SHOP_ID);
        keyword = getArguments().getString(ShopParamConstant.EXTRA_PRODUCT_KEYWORD);
        etalaseId = getArguments().getString(ShopParamConstant.EXTRA_ETALASE_ID);
        sortName = getArguments().getString(ShopParamConstant.EXTRA_SORT_ID, Integer.toString(Integer.MIN_VALUE));
        shopProductListPresenter.attachView(this);
    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return view.findViewById(R.id.swipe_refresh_layout);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_product_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchInputView.setSearchHint(getString(R.string.shop_product_search_hint));
        recyclerViews = view.findViewById(R.id.recycler_view);
        etalaseLabelView = view.findViewById(R.id.label_view_etalase);
        bottomActionView = view.findViewById(R.id.bottom_action_view);

        setBottomActionViewImage(currentImgBottomNav);
        RecyclerView.LayoutManager layoutManager = iterate(recyclerViews);
        recyclerViews.setLayoutManager(layoutManager);

        etalaseLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopModuleRouter != null) {
                    shopPageTracking.eventClickEtalaseShop(getString(R.string.shop_info_title_tab_product), false, shopId);
                    Intent etalaseIntent = ShopEtalaseActivity.createIntent(getActivity(), shopId, etalaseId);
                    startActivityForResult(etalaseIntent, REQUEST_CODE_ETALASE);
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
                shopPageTracking.eventClickViewTypeProduct(getString(R.string.shop_info_title_tab_product), currentImgBottomNav, shopId);
            }
        });

        bottomActionView.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopPageTracking.eventClickSortProductList(getString(R.string.shop_info_title_tab_product), shopId);
                ShopProductListFragment.this.startActivityForResult(ShopProductSortActivity.createIntent(getActivity(), sortName), REQUEST_CODE_SORT);
            }
        });
        if (!TextUtils.isEmpty(keyword)) {
            searchInputView.getSearchTextView().setText(keyword);
        }
        listDividerItemDecoration = new DividerItemDecoration(getActivity());
    }

    @Override
    public void renderList(@NonNull List<ShopProductViewModel> list) {
        super.renderList(list);
        shopPageTracking.eventViewProductImpression(getString(R.string.shop_info_title_tab_product),
                list,false);
    }

    @Override
    public LoadingModel getLoadingModel() {
        if (isLoadingInitialData) {
            return new LoadingModelShimmeringGrid();
        } else {
            return new LoadingModel();
        }
    }

    @Override
    public void onSwipeRefresh() {
        shopProductListPresenter.clearProductCache();
        super.onSwipeRefresh();
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
        recyclerView.removeItemDecoration(listDividerItemDecoration);
        switch (currentLayoutType.second) {
            case LAYOUT_GRID_TYPE:
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
                layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.addItemDecoration(listDividerItemDecoration);
                break;
        }
        currentIndex++;

        return layoutManager;
    }

    @Override
    protected EndlessLayoutManagerListener getEndlessLayoutManagerListener() {
        return new EndlessLayoutManagerListener() {
            public RecyclerView.LayoutManager getCurrentLayoutManager() {
                return recyclerViews.getLayoutManager();
            }
        };
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
        shopPageTracking.eventClickWishlistShop(getString(R.string.shop_info_title_tab_product), shopProductViewModel.isWishList(), false, shopId);
        if (shopProductViewModel.isWishList()) {
            shopProductListPresenter.removeFromWishList(shopProductViewModel.getId());
        } else {
            shopProductListPresenter.addToWishList(shopProductViewModel.getId());
        }
    }

    @Override
    public void onProductClicked(ShopProductViewModel shopProductViewModel, int adapterPosition) {
        shopPageTracking.eventClickProductImpression(getString(R.string.shop_info_title_tab_product),
                shopProductViewModel.getName(), shopProductViewModel.getId(), shopProductViewModel.getPrice(), adapterPosition, false);
        shopModuleRouter.goToProductDetail(getActivity(), shopProductViewModel.getProductUrl());
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
    public void onSuccessGetShopName(String shopName) {
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(MethodChecker.fromHtml(shopName).toString());
        }
    }

    @Override
    public void onSuccessGetEtalase(String etalaseId, String etalaseName) {
        if (!etalaseId.equalsIgnoreCase(this.etalaseId)) {
            this.etalaseId = etalaseId;
        }
        if (TextUtils.isEmpty(etalaseName)) {
            if (shopProductListPresenter.getUserSession().getShopId().equals(shopId)) {
                etalaseLabelView.setContent(getString(R.string.shop_info_filter_all_showcase));
            } else {
                etalaseLabelView.setContent(getString(R.string.shop_info_filter_menu_etalase_all));
            }
        } else {
            etalaseLabelView.setContent(MethodChecker.fromHtml(etalaseName));
        }
    }

    @Override
    public void onSearchSubmitted(String s) {
        keyword = s;
        loadInitialData();
    }

    @Override
    public void onSearchTextChanged(String s) {
        if (TextUtils.isEmpty(s)) {
            keyword = s;
            loadInitialData();
        }
    }

    @Override
    public void onLastItemVisible() {
        //Do nothing
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ETALASE:
                if (resultCode == Activity.RESULT_OK) {
                    etalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID);
                    String etalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME);
                    shopPageTracking.eventClickEtalaseShopChoose(getString(R.string.shop_info_title_tab_product), false, etalaseName, shopId);
                    this.isLoadingInitialData = true;
                    loadInitialData();
                }
                break;

            case REQUEST_CODE_SORT:
                if (resultCode == Activity.RESULT_OK) {
                    sortId = data.getStringExtra(ShopProductSortActivity.SORT_ID);
                    sortName = data.getStringExtra(ShopProductSortActivity.SORT_NAME);
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

    @Override
    public int getType(Object type) {
        return currentLayoutType.first;
    }
}
