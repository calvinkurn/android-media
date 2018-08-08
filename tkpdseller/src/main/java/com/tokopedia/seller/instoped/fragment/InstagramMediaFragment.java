package com.tokopedia.seller.instoped.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.seller.R;
import com.tokopedia.seller.instoped.InstagramActivityListener;
import com.tokopedia.seller.instoped.adapter.MediaAdapter;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.seller.instoped.model.InstagramUserModel;
import com.tokopedia.seller.instoped.presenter.InstagramMedia;
import com.tokopedia.seller.instoped.presenter.InstagramMediaFragmentView;
import com.tokopedia.seller.instoped.presenter.InstagramMediaPresenterImpl;
import com.tokopedia.core.var.RecyclerViewItem;

import org.parceler.Parcels;

import java.util.List;

import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * Created by Tkpd_Eka on 4/6/2016.
 */
public class InstagramMediaFragment extends Fragment implements InstagramMediaFragmentView {

    public static final String INSTAGRAM_DATA = "INSTAGRAM_DATA";
    public static final String MAX_SELECT_ITEM = "MAX_SELECT";
    public static final int DEFAULT_MAX_ITEM_COUNT = 20;
    InstagramMedia instagramMedia;
    int LANDSCAPE = Configuration.ORIENTATION_LANDSCAPE;

    public RecyclerView mediaRV;

    public LinearLayout noResult;

    public ImageView emptyImage;

    public Toolbar toolbar;

    private InstagramUserModel model;

    private MediaAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private TkpdProgressDialog progressDialog;

    SwipeToRefresh swipeToRefresh;

    private int maxItemCount;

    @Override
    public int getFragmentId() {
        return 0;
    }

    @Override
    public void ariseRetry(int type, Object... data) {

    }

    @Override
    public void setData(int type, Bundle data) {

    }

    @Override
    public void onNetworkError(int type, Object... data) {

    }

    @Override
    public void onMessageError(int type, Object... data) {
        if (!isAdded()) {
            return;
        }
        switch (type){
            case InstagramMediaPresenterImpl.FAILED_LOAD_IMAGE:
            case InstagramMediaPresenterImpl.MAXIMUM_IMAGE_SELECTED:
                if(data[0] != null && data[0] instanceof String) {
                    Toast.makeText(getActivity(), (String) data[0], Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public interface OnGetInstagramMediaListener{
        void onSuccess(SparseArray<InstagramMediaModel> selectedModel);
    }

    public static InstagramMediaFragment createDialog(InstagramUserModel model, int maxItemCount) {
        InstagramMediaFragment dialog = new InstagramMediaFragment();
        Bundle data = new Bundle();
        data.putParcelable(INSTAGRAM_DATA, Parcels.wrap(model));
        data.putInt(MAX_SELECT_ITEM, maxItemCount);
        dialog.setArguments(data);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        handleArguments(getArguments());
        instagramMedia = new InstagramMediaPresenterImpl(this);
        instagramMedia.setModel(model);
        instagramMedia.initIntagramMediaInstances(getActivity());
    }

    private void handleArguments(Bundle argument){
        if(checkNotNull(argument)){
            model = Parcels.unwrap(argument.getParcelable(INSTAGRAM_DATA));
            maxItemCount = argument.getInt(MAX_SELECT_ITEM, -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.instagram_media_dialog, container, false);
        mediaRV = (RecyclerView) view.findViewById(R.id.list);
        noResult = (LinearLayout) view.findViewById(R.id.no_result);
        emptyImage = (ImageView) view.findViewById(R.id.no_result_image);
        prepareView();
        setListener();

        instagramMedia.initData();
        return view;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_instoped_media, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.confirm){
            if (instagramMedia.getDataSize() == 0) {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_picture_instagram), Toast.LENGTH_SHORT).show();
                return true;
            }
            if(checkNotNull(getActivity()) && getActivity() instanceof InstagramActivityListener){
                ((InstagramActivityListener)getActivity()).onGetInstagramMediaListener().onSuccess(
                        instagramMedia.getSelectedModel()
                );
            }
        }
        return true;
    }

    @Override
    public void updateTitleView(int itemCount, int maxItemCount){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(String.format("%d / %d Gambar", itemCount, maxItemCount));
    }

    @Override
    public void loadingShow(boolean isLoading) {
        if(isLoading){
            progressDialog = new TkpdProgressDialog(getContext(),TkpdProgressDialog.NORMAL_PROGRESS);
            progressDialog.showDialog();
        } else {
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void showEmptyData() {
        mediaRV.setVisibility(View.GONE);
        noResult.setVisibility(View.VISIBLE);
        ImageHandler.loadImageWithId(emptyImage, R.drawable.status_no_result);
    }

    @Override
    public int getMaxItem() {
        return maxItemCount < 0 ? DEFAULT_MAX_ITEM_COUNT : maxItemCount;
    }

    private MediaAdapter.OnItemToggledListener onItemToggledListener(){
        return new MediaAdapter.OnItemToggledListener() {
            @Override
            public void onSelected(int pos) {
                instagramMedia.updateItemSelection(instagramMedia.ITEM_INCREMENT, pos);
            }

            @Override
            public void onUnselected(int pos) {
                instagramMedia.updateItemSelection(instagramMedia.ITEM_DECREMENT, pos);
            }
        };
    }

    protected void setListener(){
        mediaRV.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int itemPosition = 0;
                if(isLandscape() && layoutManager instanceof GridLayoutManager){
                    itemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                }else{
                    itemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }
                if (!isSwipeShow() && isLoadMoreShow()  && itemPosition == layoutManager.getItemCount() - 1) {//
                    instagramMedia.loadMore();
                }
            }

        });
    }

    @Override
    public void initHolder() {

    }

    @Override
    public void initAdapter(List<RecyclerViewItem> data) {
        if(adapter == null) {
            adapter = new MediaAdapter(getActivity(), data);
            adapter.setDataSelection(new MediaAdapter.DataSelection() {
                @Override
                public boolean isSelected(int position) {
                    return instagramMedia.isSelected(position);
                }
            });
            adapter.setOnItemToggledListener(onItemToggledListener());
            layoutManager = new GridLayoutManager(getActivity(), 2);
            mediaRV.setLayoutManager(layoutManager);
            mediaRV.setAdapter(adapter);
        }
    }

    @Override
    public void prepareView() {
        ((InstagramActivityListener)getActivity()).triggerAppBarAnimation(false);
    }

    @Override
    public void setPullEnabled(boolean isPullEnabled) {

    }

    @Override
    public void displayPull(boolean isShow) {

    }

    @Override
    public boolean isSwipeShow() {
        if(swipeToRefresh != null)
            return swipeToRefresh.isRefreshing();
        else
            return false;
    }

    @Override
    public void loadDataChange() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void displayLoadMore(boolean isLoadMore) {

    }

    @Override
    public boolean isLoadMoreShow() {
        return instagramMedia.getHasLoadMore();
    }

    @Override
    public void moveToOtherActivity(Bundle bundle, Class<?> clazz) {

    }

    @Override
    public BaseRecyclerViewAdapter getAdapter() {
        return null;
    }

    @Override
    public void displayRetry(boolean isRetry) {

    }

    @Override
    public void displayTimeout() {

    }

    @Override
    public void setRetry(boolean isRetry) {

    }

    @Override
    public void setOnRetryListenerRV() {

    }

    @Override
    public boolean isLandscape() {
        return getScreenRotation()==LANDSCAPE;
    }

    @Override
    public int getScreenRotation() {
        return getActivity().getResources().getConfiguration().orientation;
    }
}