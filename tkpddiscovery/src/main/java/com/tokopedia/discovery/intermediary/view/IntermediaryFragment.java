package com.tokopedia.discovery.intermediary.view;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.entity.categoriesHades.Child;
import com.tokopedia.core.util.NonScrollGridLayoutManager;
import com.tokopedia.core.util.NonScrollLinearLayoutManager;
import com.tokopedia.core.widgets.DividerItemDecoration;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.adapter.RevampCategoryAdapter;
import com.tokopedia.discovery.intermediary.di.IntermediaryDependencyInjector;
import com.tokopedia.discovery.intermediary.domain.model.ChildCategoryModel;
import com.tokopedia.discovery.intermediary.domain.model.CuratedSectionModel;
import com.tokopedia.discovery.intermediary.domain.model.HeaderModel;
import com.tokopedia.discovery.intermediary.domain.model.HotListModel;
import com.tokopedia.discovery.intermediary.view.adapter.CuratedProductAdapter;
import com.tokopedia.discovery.intermediary.view.adapter.CurationAdapter;
import com.tokopedia.discovery.intermediary.view.adapter.HotListItemAdapter;
import com.tokopedia.discovery.intermediary.view.adapter.IntermediaryCategoryAdapter;
import com.tokopedia.discovery.view.CategoryHeaderTransformation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alifa on 3/24/17.
 */

public class IntermediaryFragment extends BaseDaggerFragment implements IntermediaryContract.View {

    private TkpdProgressDialog progressDialog;

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

    private IntermediaryCategoryAdapter categoryAdapter;
    private IntermediaryCategoryAdapter.CategoryListener categoryListener;
    ArrayList<ChildCategoryModel> activeChildren = new ArrayList<>();
    boolean isUsedUnactiveChildren = false;

    private CurationAdapter curationAdapter;



    private IntermediaryContract.Presenter presenter;
    public static final String TAG = "INTERMEDIARY_FRAGMENT";


    public static IntermediaryFragment createInstance(IntermediaryCategoryAdapter.CategoryListener listener) {

        IntermediaryFragment intermediaryFragment = new IntermediaryFragment();
        intermediaryFragment.categoryListener = listener;
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

        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.MAIN_PROGRESS);
        ButterKnife.bind(this, parentView);

        presenter.attachView(this);
        presenter.getIntermediaryCategory(((IntermediaryActivity) getActivity()).getDepartmentId());

        return parentView;
    }

    @Override
    public void renderHeader(HeaderModel headerModel) {
        ImageHandler.loadImageFitTransformation(imageHeader.getContext(),imageHeader,
                headerModel.getHeaderImageUrl(), new CategoryHeaderTransformation(imageHeader.getContext()));
        titleHeader.setText(headerModel.getCategoryName().toUpperCase());
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
        categoryAdapter = new IntermediaryCategoryAdapter(  getCategoryWidth(),activeChildren,categoryListener);
        revampCategoriesRecyclerView.setAdapter(categoryAdapter);
        if (isUsedUnactiveChildren) {
            expandLayout.setVisibility(View.VISIBLE);
            expandLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UnifyTracking.eventShowMoreCategory();
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
        curationAdapter = new CurationAdapter(getActivity());
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

        HotListItemAdapter hotListItemAdapter = new HotListItemAdapter(hotListModelList,getCategoryWidth(),getActivity());

        hotListRecyclerView.setHasFixedSize(true);
        hotListRecyclerView.setNestedScrollingEnabled(false);
        hotListRecyclerView.setLayoutManager(
                new NonScrollGridLayoutManager(getActivity(), 2,
                        GridLayoutManager.VERTICAL, false));
        hotListRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        hotListRecyclerView.setAdapter(hotListItemAdapter);
    }

    @Override
    public void showLoading() {
        progressDialog.showDialog();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void emptyState() {
        showErrorEmptyState();
    }

    private void showErrorEmptyState() {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
               //TODO
            }
        }).showRetrySnackbar();
    }

    private int getCategoryWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return (int) (width / 2);
    }
}
