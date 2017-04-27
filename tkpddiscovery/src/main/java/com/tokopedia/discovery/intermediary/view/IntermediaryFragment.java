package com.tokopedia.discovery.intermediary.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.NonScrollGridLayoutManager;
import com.tokopedia.core.util.NonScrollLinearLayoutManager;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.widgets.DividerItemDecoration;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.discovery.intermediary.di.IntermediaryDependencyInjector;
import com.tokopedia.discovery.intermediary.domain.model.ChildCategoryModel;
import com.tokopedia.discovery.intermediary.domain.model.CuratedSectionModel;
import com.tokopedia.discovery.intermediary.domain.model.HeaderModel;
import com.tokopedia.discovery.intermediary.domain.model.HotListModel;
import com.tokopedia.discovery.intermediary.domain.model.ProductModel;
import com.tokopedia.discovery.intermediary.view.adapter.CuratedProductAdapter;
import com.tokopedia.discovery.intermediary.view.adapter.CurationAdapter;
import com.tokopedia.discovery.intermediary.view.adapter.HotListItemAdapter;
import com.tokopedia.discovery.intermediary.view.adapter.IntermediaryCategoryAdapter;
import com.tokopedia.discovery.view.CategoryHeaderTransformation;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.view.TopAdsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tokopedia.topads.sdk.domain.TopAdsParams.DEFAULT_KEY_EP;
import static com.tokopedia.topads.sdk.domain.TopAdsParams.SRC_INTERMEDIARY_VALUE;

/**
 * Created by alifa on 3/24/17.
 */

public class IntermediaryFragment extends BaseDaggerFragment implements IntermediaryContract.View,
    CuratedProductAdapter.OnItemClickListener, TopAdsItemClickListener, TopAdsListener,
        IntermediaryCategoryAdapter.CategoryListener {

    @BindView(R2.id.nested_intermediary)
    NestedScrollView nestedScrollView;

    @BindView(R2.id.image_header)
    ImageView imageHeader;

    @BindView(R2.id.title_header)
    TextView titleHeader;

    @BindView(R2.id.expand_layout)
    LinearLayout expandLayout;

    @BindView(R2.id.hide_layout)
    LinearLayout hideLayout;

    @BindView(R2.id.recycler_view_revamp_categories)
    RecyclerView revampCategoriesRecyclerView;

    @BindView(R2.id.recycler_view_curation)
    RecyclerView curationRecyclerView;

    @BindView(R2.id.card_hoth_intermediary)
    CardView cardViewHotList;

    @BindView(R2.id.recycler_view_hot_list)
    RecyclerView hotListRecyclerView;

    @BindView(R2.id.category_view_all)
    TextView viewAllCategory;

    public static final String TAG = "INTERMEDIARY_FRAGMENT";

    private String departmentId = "";
    private IntermediaryCategoryAdapter categoryAdapter;
    private IntermediaryCategoryAdapter.CategoryListener categoryListener;
    private ArrayList<ChildCategoryModel> activeChildren = new ArrayList<>();
    private boolean isUsedUnactiveChildren = false;
    private CurationAdapter curationAdapter;
    private IntermediaryContract.Presenter presenter;
    com.tokopedia.topads.sdk.view.TopAdsView topAdsView;

    public static IntermediaryFragment createInstance(String departmentId) {
        IntermediaryFragment intermediaryFragment = new IntermediaryFragment();
        intermediaryFragment.departmentId = departmentId;
        return intermediaryFragment;
    }


    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        presenter = IntermediaryDependencyInjector.getPresenter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_intermediary, container, false);

        ButterKnife.bind(this, parentView);

        presenter.attachView(this);
        presenter.getIntermediaryCategory(departmentId);
        topAdsView = (TopAdsView) parentView.findViewById(R.id.top_ads_view);

        return parentView;
    }

    @Override
    public void renderHeader(HeaderModel headerModel) {
        ImageHandler.loadImageFitTransformation(imageHeader.getContext(),imageHeader,
                headerModel.getHeaderImageUrl(), new CategoryHeaderTransformation(imageHeader.getContext()));
        titleHeader.setText(headerModel.getCategoryName().toUpperCase());
        titleHeader.setShadowLayer(24, 0, 0, com.tokopedia.core.R.color.checkbox_text);
        viewAllCategory.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderTopAds() {
        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(getActivity()))
                .build();

        topAdsView.setAdsItemClickListener(this);
        topAdsView.setAdsListener(this);
        topAdsView.setConfig(config);

        TopAdsParams params = new TopAdsParams();
        params.getParam().put(TopAdsParams.KEY_USER_ID, SessionHandler.getLoginID(MainApplication.getAppContext()));
        params.getParam().put(TopAdsParams.KEY_SRC,SRC_INTERMEDIARY_VALUE);
        params.getParam().put(TopAdsParams.KEY_EP,DEFAULT_KEY_EP);
        params.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID,departmentId);
        topAdsView.setTopAdsParams(params);
        topAdsView.loadTopAds();
    }

    @Override
    public void renderCategoryChildren(final List<ChildCategoryModel> childCategoryModelList) {

        if (childCategoryModelList!=null && childCategoryModelList.size()>9) {
            activeChildren.addAll(childCategoryModelList
                    .subList(0,9));
            isUsedUnactiveChildren = true;
        } else if (childCategoryModelList!=null){
            activeChildren.addAll(childCategoryModelList);
        }

        revampCategoriesRecyclerView.setVisibility(View.VISIBLE);
        revampCategoriesRecyclerView.setHasFixedSize(true);
        revampCategoriesRecyclerView.setLayoutManager(
                new NonScrollGridLayoutManager(getActivity(), 3,
                        GridLayoutManager.VERTICAL, false));
        categoryAdapter = new IntermediaryCategoryAdapter(  getCategoryWidth(),activeChildren,this);
        revampCategoriesRecyclerView.setAdapter(categoryAdapter);
        if (isUsedUnactiveChildren) {
            expandLayout.setVisibility(View.VISIBLE);
            expandLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UnifyTracking.eventShowMoreCategory(departmentId);
                    categoryAdapter.addDataChild(childCategoryModelList
                            .subList(9,childCategoryModelList.size()));
                    expandLayout.setVisibility(View.GONE);
                    isUsedUnactiveChildren = false;
                    hideLayout.setVisibility(View.VISIBLE);

                }
            });
            hideLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryAdapter.hideExpandable();
                    expandLayout.setVisibility(View.VISIBLE);
                    isUsedUnactiveChildren = true;
                    hideLayout.setVisibility(View.GONE);
                    revampCategoriesRecyclerView.scrollToPosition(0);
                }
            });
        }
    }

    @Override
    public void renderCuratedProducts(List<CuratedSectionModel> curatedSectionModelList) {
        curationRecyclerView.setHasFixedSize(true);
        curationRecyclerView.setNestedScrollingEnabled(false);
        curationAdapter = new CurationAdapter(getActivity(),this);
        curationAdapter.setHomeMenuWidth(getCategoryWidth());
        curationRecyclerView.setLayoutManager(
                new NonScrollLinearLayoutManager(getActivity(),
                        LinearLayoutManager.VERTICAL,
                        false)
        );
        curationRecyclerView.setAdapter(curationAdapter);
        curationAdapter.setDataList(curatedSectionModelList);
        curationAdapter.notifyDataSetChanged();
    }

    @Override
    public void renderHotList(List<HotListModel> hotListModelList) {
        cardViewHotList.setVisibility(View.VISIBLE);

        HotListItemAdapter hotListItemAdapter = new HotListItemAdapter(hotListModelList,
                getCategoryWidth(),getActivity(),departmentId);

        hotListRecyclerView.setHasFixedSize(true);
        hotListRecyclerView.setNestedScrollingEnabled(false);
        hotListRecyclerView.setLayoutManager(
                new NonScrollGridLayoutManager(getActivity(), 2,
                        GridLayoutManager.VERTICAL, false));
        hotListRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider300));
        hotListRecyclerView.setAdapter(hotListItemAdapter);
    }

    @Override
    public void showLoading() {
        ((IntermediaryActivity) getActivity()).getProgressBar().setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        ((IntermediaryActivity) getActivity()).getProgressBar().setVisibility(View.GONE);
    }

    @Override
    public void emptyState() {
        showErrorEmptyState();
    }

    @Override
    public void skipIntermediaryPage() {
        BrowseProductActivity.moveToWithoutAnimation(
            getActivity(),
                departmentId,
            TopAdsApi.SRC_DIRECTORY,
            BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY,
                ((IntermediaryActivity) getActivity()).getCategoryName()
        );
        getActivity().overridePendingTransition(0,0);
        getActivity().finish();

    }

    @Override
    public void backToTop() {
        nestedScrollView.smoothScrollTo(0, 0);
    }

    private void showErrorEmptyState() {
        NetworkErrorHelper.showEmptyState(getActivity(),  ((IntermediaryActivity) getActivity())
                        .getFrameLayout(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                       presenter.getIntermediaryCategory(departmentId);
                    }
                });
    }

    private int getCategoryWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return (int) (width / 2);
    }

    @OnClick(R2.id.category_view_all)
    public void viewAllCategory() {
        BrowseProductActivity.moveTo(
                getActivity(),
                departmentId,
                TopAdsApi.SRC_DIRECTORY,
                BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY,
                ((IntermediaryActivity) getActivity()).getCategoryName()
        );
    }

    @Override
    public void onItemClicked(ProductModel productModel, String curatedName) {
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity(),
                Integer.toString(productModel.getId()));
        getActivity().startActivity(intent);
        UnifyTracking.eventCuratedIntermediary(departmentId,
                curatedName,productModel.getName());
    }


    @Override
    public void onTopAdsLoaded() {
        topAdsView.setVisibility(View.VISIBLE);
        backToTop();
    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {

    }

    @Override
    public void onProductItemClicked(Product product) {
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity(),
               product.getId());
        getActivity().startActivity(intent);
    }

    @Override
    public void onShopItemClicked(Shop shop) {
        Bundle bundle = ShopInfoActivity.createBundle(shop.getId(), "");
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onAddFavorite(Data shopData) {
        //TODO: this listener not used in this sprint
    }

    @Override
    public void onCategoryRevampClick(ChildCategoryModel child) {
        BrowseProductActivity.moveTo(
                getActivity(),
                child.getCategoryId(),
                TopAdsApi.SRC_DIRECTORY,
                BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY,
                child.getCategoryName()
        );
        UnifyTracking.eventLevelCategoryIntermediary(departmentId,child.getCategoryId());
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}
