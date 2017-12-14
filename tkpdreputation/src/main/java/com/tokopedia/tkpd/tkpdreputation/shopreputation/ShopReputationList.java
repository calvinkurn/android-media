package com.tokopedia.tkpd.tkpdreputation.shopreputation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.app.V2BaseFragment;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.view.adapter.ShopReputationAdapterR;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.domain.ActionReputationLikeRetrofit;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.domain.GetShopReputationRetrofit;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.view.viewmodel.ReputationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tkpd_Eka on 10/8/2015.
 */
public class ShopReputationList extends V2BaseFragment {

    class ViewHolder {
        RecyclerView list;
    }

    private ViewHolder holder;
    private ArrayList<ReputationModel> listReputation = new ArrayList<>();
    private ShopReputationAdapterR adapter;
    private int page = 1;
    private GetShopReputationRetrofit facadeGetRep;
    private ActionReputationLikeRetrofit actionReputationLike;
    private String shopId;
    private String shopDomain;
    private boolean isVisibleToUser;
    private boolean isFromResume;
    private boolean isOpenReview = false;

    public static ShopReputationList create() {
        return new ShopReputationList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getActivity().getIntent().getExtras().getString(ShopInfoActivity.SHOP_ID, "");
        shopDomain = getActivity().getIntent().getExtras().getString(ShopInfoActivity.SHOP_DOMAIN, "");
//        if (savedInstanceState != null)
//            loadInstanceState(savedInstanceState);
        adapter = ShopReputationAdapterR.createAdapter(getActivity(), listReputation);
        initFacade();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.isFromResume = false;
        if(this.isVisibleToUser && this.isOpenReview){
            this.isFromResume = true;
            this.page = 1;
            onLoadReputation();
        }
    }

    private void onLoadReputation(){
        adapter.setLoading();
        facadeGetRep.getShopReputation(Integer.toString(page));
    }

    private void loadInstanceState(Bundle instance) {
        listReputation = instance.getParcelableArrayList("list");
//        shopModel = instance.getParcelable("shop_info");
        page = instance.getInt("page");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putParcelable("shop_info", shopModel);
        outState.putParcelableArrayList("list", listReputation);
        outState.putInt("page", page);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (listReputation.size() == 0 && !adapter.hasNoResult() && page == 1 && !facadeGetRep.isFetching()) {
            adapter.setLoading();
            facadeGetRep.getShopReputation(Integer.toString(page));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            if (getActivity() != null &&
                    getActivity() instanceof ShopInfoActivity) {
                ((ShopInfoActivity) getActivity()).swipeAble(true);
            }
        }
    }

    private void initFacade() {
        facadeGetRep = new GetShopReputationRetrofit(getActivity(), shopId, shopDomain);
        actionReputationLike = new ActionReputationLikeRetrofit(getActivity(), shopId, shopDomain);
        facadeGetRep.setOnGetShopReputationListener(onGetReputationListener());
    }

    private GetShopReputationRetrofit.OnGetShopReputationListener onGetReputationListener() {
        return new GetShopReputationRetrofit.OnGetShopReputationListener() {
            @Override
            public void onSuccess(List<ReputationModel> modelList) {
                removeLoading();
                if(isFromResume && isOpenReview){
                    isOpenReview = false;
                    listReputation.clear();
                }
                listReputation.addAll(modelList);
                adapter.notifyDataSetChanged();
                page++;
                if (listReputation.isEmpty()) {
                    adapter.setNoResult();
                    page = -1;
                }
                if (!modelList.isEmpty()) {
                    facadeGetRep.setOnUpdateLikeDislikeListener(onUpdateLikeDislike(), modelList);
                    facadeGetRep.getLikeDislike(getCompiledRevId(modelList));
                }
            }

            @Override
            public void onFailure() {
                removeLoading();
                adapter.setRetry();
            }
        };
    }

    private ActionReputationLikeRetrofit.OnLikeDislikeReviewListener onActionLike() {
        return new ActionReputationLikeRetrofit.OnLikeDislikeReviewListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(String message) {
                Snackbar.make(getRootView(), message, Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    private GetShopReputationRetrofit.OnUpdateLikeDislikeListener onUpdateLikeDislike() {
        return new GetShopReputationRetrofit.OnUpdateLikeDislikeListener() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
                adapter.notifyDataSetChanged();
            }
        };
    }

    public String getCompiledRevId(List<ReputationModel> list) {
        int total = list.size();
        String compile = list.get(0).reviewId;
        for (int i = 1; i < total; i++) {
            compile = compile + "~" + list.get(i).reviewId;
        }
        return compile;
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    protected void onCreateView() {
        if (listReputation.isEmpty() && page == 1)
            if (adapter.hasNoResult())
                adapter.setNoResult();
            else if (adapter.hasRetry())
                adapter.setRetry();
            else
                adapter.setLoading();
    }

    @Override
    protected Object getHolder() {
        return holder;
    }

    @Override
    protected void setHolder(Object holder) {
        this.holder = (ViewHolder) holder;
    }

    @Override
    protected void initView() {
        holder = new ViewHolder();
        holder.list = (RecyclerView) findViewById(R.id.list);
        holder.list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        holder.list.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        holder.list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isAtBottom() && page > 0 && !adapter.hasLoading()) {
                    loadNextPage();
                }
            }
        });
        adapter.setReputationAdapterListener(onAdapterItemClick());
        adapter.setOnRetryClickListener(onRetryClickListener());
    }

    private View.OnClickListener onRetryClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeRetry();
                loadNextPage();
            }
        };
    }

    private ShopReputationAdapterR.ReputationAdapterListener onAdapterItemClick() {
        return new ShopReputationAdapterR.ReputationAdapterListener() {
            @Override
            public void onOpenReview(int pos) {
                isOpenReview = true;
                openReview(pos);
            }

            @Override
            public void onLikeReview(int pos) {
                doLikeReview(pos);
            }

            @Override
            public void onDislikeReview(int pos) {
                doDislikeReview(pos);
            }

            @Override
            public void onProdClick(int pos) {

            }

            @Override
            public void onUserClick(int pos) {
                startActivity(
                        PeopleInfoNoDrawerActivity.createInstance(getActivity(), listReputation.get(pos).userId)
                );
            }

            @Override
            public void onReport(int pos) {
                createReportDialog(pos);
            }
        };
    }

    private void createReportDialog(final int pos) { // TODO
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.prompt_dialog_report, null, false);
        final TextView message = (TextView) dialogView.findViewById(R.id.reason);
        builder.setView(dialogView);
        builder.setPositiveButton("Kirim", null);
        builder.setNegativeButton("Batal", null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (message.length() > 0) {
                            actionReputationLike.actionReportReview(listReputation.get(pos).reviewId, message.getText().toString());
                            alertDialog.dismiss();
                        } else {
                            message.setError(getString(R.string.error_empty_report));
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void openReview(int pos) {
        Intent intent = new Intent(getActivity(), ReputationViewShop.class);
        intent.putExtra(ReputationViewShop.EXTRA_PRODUCT_ID, listReputation.get(pos).productId);
        intent.putExtra("data_model", convertShopReviewModelToReviewModel(listReputation.get(pos)));
        intent.putParcelableArrayListExtra("data_images", listReputation.get(pos).getImages());
        intent.putExtra("shop_id", shopId);
        startActivityForResult(intent, 0);
    }

    private ReputationViewShop.Model convertShopReviewModelToReviewModel(ReputationModel model) {
        ReputationViewShop.Model repModel = new ReputationViewShop.Model();
        repModel.avatarUrl = model.avatarUrl;
        repModel.avatarUrlResponder = model.avatarUrlResponder;
        repModel.comment = model.comment;
        repModel.counterDislike = model.counterDislike;
        repModel.counterLike = model.counterLike;
        repModel.counterResponse = model.counterResponse;
        repModel.counterSmiley = model.counterSmiley;
        repModel.date = model.date;
        repModel.Editable = model.Editable;
        repModel.reviewId = model.reviewId;
        repModel.username = model.username;
        repModel.userId = model.userId;
        repModel.avatarUrl = model.avatarUrl;
        repModel.starAccuracy = model.starAccuracy;
        repModel.starQuality = model.starQuality;
        repModel.smiley = model.smiley;
        repModel.shopName = model.shopName;
        repModel.shopId = model.shopId;
        repModel.shopAvatarUrl = getActivity().getIntent().getStringExtra(ShopInfoActivity.SHOP_AVATAR);
        repModel.responseMessage = model.responseMessage;
        repModel.responseDate = model.responseDate;
        repModel.userNameResponder = model.userNameResponder;
        repModel.avatarUrlResponder = model.avatarUrlResponder;
        repModel.labelIdResponder = model.labelIdResponder;
        repModel.userLabelResponder = model.userLabelResponder;
        repModel.userIdResponder = model.userIdResponder;
        repModel.productId = model.productId;
        repModel.shopReputation = model.shopReputation;
        repModel.productName = model.productName;
        repModel.typeMedal = model.typeMedal;
        repModel.levelMedal = model.levelMedal;
        repModel.isGetLikeDislike = model.isGetLikeDislike;
        repModel.statusLikeDislike = model.statusLikeDislike;
        repModel.positive = model.positive;
        repModel.negative = model.negative;
        repModel.netral = model.netral;
        repModel.noReputationUserScore = model.noReputationUserScore;
        repModel.productAvatar = model.productAvatar;
        repModel.reputationId = model.reputationId;
        repModel.userLabel = model.userLabel;
        return repModel;
    }

    private void doLikeReview(int pos) {
        if (listReputation.get(pos).statusLikeDislike == 1) {
            removeLikeDislike(pos);
            return;
        }
        actionReputationLike.setOnLikeDislikeReviewListener(onActionLike());
        actionReputationLike.actionReputationLikeDislike(listReputation.get(pos).productId, listReputation.get(pos).reviewId, Integer.toString(listReputation.get(pos).statusLikeDislike));
        resetLikeDislike(pos);
        setTemporaryLike(pos);
    }

    private void doDislikeReview(int pos) {
        if (listReputation.get(pos).statusLikeDislike == 2) {
            removeLikeDislike(pos);
            return;
        }
        actionReputationLike.setOnLikeDislikeReviewListener(onActionLike());
        actionReputationLike.actionReputationLikeDislike(listReputation.get(pos).productId, listReputation.get(pos).reviewId, Integer.toString(listReputation.get(pos).statusLikeDislike));
        resetLikeDislike(pos);
        setTemporaryDislike(pos);
    }

    private void removeLikeDislike(int pos) {
        resetLikeDislike(pos);
        adapter.notifyDataSetChanged();
        actionReputationLike.setOnLikeDislikeReviewListener(onActionLike());
        actionReputationLike.actionReputationLikeDislike(listReputation.get(pos).productId, listReputation.get(pos).reviewId, Integer.toString(listReputation.get(pos).statusLikeDislike));
    }

    private void resetLikeDislike(int pos) {
        switch (listReputation.get(pos).statusLikeDislike) {
            case 1:
                listReputation.get(pos).counterLike -= 1;
                break;
            case 2:
                listReputation.get(pos).counterDislike -= 1;
                break;
        }
        listReputation.get(pos).statusLikeDislike = 0;
    }

    private void setTemporaryLike(int pos) {
        listReputation.get(pos).statusLikeDislike = 1;
        listReputation.get(pos).counterLike += 1;
        adapter.notifyDataSetChanged();
    }

    private void setTemporaryDislike(int pos) {
        listReputation.get(pos).statusLikeDislike = 2;
        listReputation.get(pos).counterDislike += 1;
        adapter.notifyDataSetChanged();
    }

    private boolean isAtBottom() {
        int visibleItem = holder.list.getLayoutManager().getChildCount();
        int totalItem = holder.list.getLayoutManager().getItemCount();
        int pastVisibleItem = ((LinearLayoutManager) holder.list.getLayoutManager()).findFirstVisibleItemPosition();
        return ((visibleItem + pastVisibleItem) >= totalItem);
    }

    private void loadNextPage() {
        setLoading();
        facadeGetRep.getShopReputation(Integer.toString(page));
    }

    private void setLoading() {
        adapter.setLoading();
    }

    private void removeLoading() {
        adapter.removeLoading();
    }
}
