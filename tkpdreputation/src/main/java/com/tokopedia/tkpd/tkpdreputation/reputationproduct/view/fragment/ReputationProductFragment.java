package com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.PreviewProductImage;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.product.ReviewActService;
import com.tokopedia.core.network.apiservices.shop.ReputationActService;
import com.tokopedia.core.network.apiservices.shop.ShopService;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.reputationproduct.util.ReputationLevelUtils;
import com.tokopedia.core.util.LabelUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SelectableSpannedMovementMethod;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.StarGenerator;
import com.tokopedia.core.util.ToolTipUtils;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.factory.ReputationProductDataFactory;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.mapper.ActResultMapper;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.mapper.LikeDislikeDomainMapper;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.pojo.ViewHolderComment;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.pojo.ViewHolderMain;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review.ReviewProductDetailModel;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review.ReviewProductModel;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository.DeleteCommentRepository;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository.DeleteCommentRepositoryImpl;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository.GetLikeDislikeRepository;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository.GetLikeDislikeRepositoryImpl;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository.LikeDislikeRepository;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository.LikeDislikeRepositoryImpl;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository.PostReportRepository;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.repository.PostReportRepositoryImpl;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.ActReviewPass;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ActResultDomain;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.usecase.DeleteCommentUseCase;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.usecase.GetLikeDislikeUseCase;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.usecase.LikeDislikeUseCase;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.usecase.PostReportUseCase;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.adapter.ImageUploadAdapter;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.listener.ReputationProductFragmentView;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.presenter.ReputationProductViewFragmentPresenter;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.presenter.ReputationProductViewFragmentPresenterImpl;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by hangnadi on 8/19/15.
 */
public class ReputationProductFragment extends BasePresenterFragment<ReputationProductViewFragmentPresenter> implements ReputationProductFragmentView {

    public static final int SMILEY_BAD = -1;
    public static final int SMILEY_DEFAULT = 0;
    public static final int SMILEY_NETRAL = 1;
    public static final int SMILEY_SMILE = 2;
    private static final int REQUEST_LOGIN = 101;

    private String productID;
    private String shopID;
    private ReviewProductModel model;
    private TkpdProgressDialog progressDialog;
    private ViewHolderMain holder;
    private ViewHolderComment holderComment;
    private LabelUtils labelHeader;
    private LabelUtils labelResponder;
    private ImageUploadAdapter imageUploadAdapter;

    private GetLikeDislikeRepository getLikeDislikeRepository;
    private LikeDislikeRepository likeDislikeRepository;
    private PostReportRepository postReportRepository;
    private DeleteCommentRepository deleteCommentRepository;

    private ReputationProductDataFactory reputationProductDataFactory;

    private LikeDislikeDomainMapper likeDislikeDomainMapper;
    private ActResultMapper actResultMapper;

    private GetLikeDislikeUseCase getLikeDislikeUseCase;
    private LikeDislikeUseCase likeDislikeUseCase;
    private PostReportUseCase postReportUseCase;
    private DeleteCommentUseCase deleteCommentUseCase;

    public static ReputationProductFragment createInstance(String ProductID, String ShopID, ReviewProductModel Model) {
        ReputationProductFragment fragment = new ReputationProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString("product_id", ProductID);
        bundle.putString("shop_id", ShopID);
        bundle.putParcelable("data", Model);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        likeDislikeDomainMapper = new LikeDislikeDomainMapper();
        actResultMapper = new ActResultMapper();

        reputationProductDataFactory = new ReputationProductDataFactory(getActivity(),
                new ShopService(),
                new ReviewActService(),
                new ReputationActService(),
                likeDislikeDomainMapper,
                actResultMapper);

        getLikeDislikeRepository = new GetLikeDislikeRepositoryImpl(reputationProductDataFactory);
        likeDislikeRepository = new LikeDislikeRepositoryImpl(reputationProductDataFactory);
        postReportRepository = new PostReportRepositoryImpl(reputationProductDataFactory);
        deleteCommentRepository = new DeleteCommentRepositoryImpl(reputationProductDataFactory);

        getLikeDislikeUseCase = new GetLikeDislikeUseCase(new JobExecutor(),
                new UIThread(),
                getLikeDislikeRepository);
        likeDislikeUseCase = new LikeDislikeUseCase(new JobExecutor(),
                new UIThread(),
                likeDislikeRepository);
        postReportUseCase = new PostReportUseCase(new JobExecutor(),
                new UIThread(),
                postReportRepository);
        deleteCommentUseCase = new DeleteCommentUseCase(new JobExecutor(),
                new UIThread(),
                deleteCommentRepository);

        presenter = new ReputationProductViewFragmentPresenterImpl(this,
                getLikeDislikeUseCase,
                likeDislikeUseCase,
                postReportUseCase,
                deleteCommentUseCase);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_reputation_product_view;
    }

    @Override
    protected void initView(View rootView) {
        holder = new ViewHolderMain();
        holder.avatar = (ImageView) rootView.findViewById(R.id.user_avatar);
        holder.username = (TextView) rootView.findViewById(R.id.username);
        holder.date = (TextView) rootView.findViewById(R.id.date);
        holder.counterSmiley = (LinearLayout) rootView.findViewById(R.id.counter_smiley);
        holder.starQuality = (LinearLayout) rootView.findViewById(R.id.star_quality);
        holder.starAccuracy = (LinearLayout) rootView.findViewById(R.id.star_accuracy);
        holder.comment = (TextView) rootView.findViewById(R.id.comment);
        holder.counterComment = (TextView) rootView.findViewById(R.id.counter_comment);
        holder.counterLike = (TextView) rootView.findViewById(R.id.counter_like);
        holder.counterDislike = (TextView) rootView.findViewById(R.id.counter_dislike);
        holder.overFlow = (ImageView) rootView.findViewById(R.id.btn_overflow);
        labelHeader = LabelUtils.getInstance(getActivity(), holder.username);
        holder.viewLikeDislike = rootView.findViewById(R.id.view_like_dislike);
        holder.loading = (ProgressBar) rootView.findViewById(R.id.loading);
        holder.iconDislike = (ImageView) rootView.findViewById(R.id.icon_dislike);
        holder.iconLike = (ImageView) rootView.findViewById(R.id.icon_like);
        holder.textPercentage = (TextView) rootView.findViewById(R.id.rep_rating);
        holder.iconPercentage = (ImageView) rootView.findViewById(R.id.rep_icon);
//        holder.viewReputation = rootView.findViewById(R.id.counter_smiley);
        holder.imageHolder = (RecyclerView) rootView.findViewById(R.id.image_holder);
        holder.imageHolder.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        // Comment Area;
        holderComment = new ViewHolderComment();
        holderComment.commentView = rootView.findViewById(R.id.comment_area);
        holderComment.commentAvatar = (ImageView) rootView.findViewById(R.id.user_ava_comment);
        holderComment.commentBadges = (LinearLayout) rootView.findViewById(R.id.badges);
        holderComment.commentMessage = (TextView) rootView.findViewById(R.id.message_comment);
        holderComment.date = (TextView) rootView.findViewById(R.id.create_time_comment);
        holderComment.buttonOverflow = (ImageView) rootView.findViewById(R.id.but_overflow_comment);
        holderComment.commentUsername = (TextView) rootView.findViewById(R.id.comment_username);
        labelResponder = LabelUtils.getInstance(getActivity(), holderComment.commentUsername);
    }

    @Override
    protected void setViewListener() {
        holder.username.setOnClickListener(OnUserNameClickListener());
        holder.avatar.setOnClickListener(OnUserNameClickListener());
        holder.overFlow.setOnClickListener(onOverFlowClickListener(R.menu.report_menu));
        holderComment.buttonOverflow.setOnClickListener(onOverFlowClickListener(R.menu.delete_menu));

        holder.counterLike.setOnClickListener(OnLikeClickListener());
        holder.iconLike.setOnClickListener(OnLikeClickListener());
        holder.counterDislike.setOnClickListener(OnDislikeClickListener());
        holder.iconDislike.setOnClickListener(OnDislikeClickListener());
        holder.counterSmiley.setOnClickListener(OnViewReputationClickListener());

        holderComment.commentUsername.setOnClickListener(OnShopNameClickListener());
        holderComment.commentAvatar.setOnClickListener(OnShopNameClickListener());
    }

    @Override
    protected void initialVar() {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        model = ((ReviewProductModel) getArguments().getParcelable("data"));
        shopID = getArguments().getString("shop_id");
        productID = getArguments().getString("product_id");
    }

    @Override
    protected void setActionVar() {

    }


    @Override
    protected boolean isRetainInstance() {
        return true;
    }


    @Override
    protected void onFirstTimeLaunched() {
        initData();
        setModelToView();
        presenter.getLikeDislike(getActivity(), model.getReviewShopId(), String.valueOf(model.getReviewId()));

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    private void initData() {
        model.detail = new ReviewProductDetailModel();
    }

    @Override
    public void setResultToModel(LikeDislikeDomain result) {
        model.detail.isGetLikeDislike = true;
        model.detail.statusLikeDislike = result.getLikeDislikeReviewDomain().get(0).getLikeStatus();
        model.detail.counterDislike = result.getLikeDislikeReviewDomain().get(0).getTotalLikeDislikeDomain().getTotalDislike();
        model.detail.counterLike = result.getLikeDislikeReviewDomain().get(0).getTotalLikeDislikeDomain().getTotalLike();
        setModelToView();
    }

    private void setModelToView() {
        imageUploadAdapter = ImageUploadAdapter.createAdapter(getActivity());
        imageUploadAdapter.setListener(onProductImageActionListener());
        holder.imageHolder.setAdapter(imageUploadAdapter);
        setVisibility();
        ImageHandler.loadImageCircle2(getActivity(), holder.avatar, model.getReviewUserImage());
        holder.username.setText(MethodChecker.fromHtml(model.getReviewUserName()).toString());
        holder.date.setText(model.getReviewCreateTime());
        holder.comment.setText(model.getReviewMessage());
        holder.comment.setMovementMethod(new SelectableSpannedMovementMethod());

        try {
            labelHeader.giveSquareLabel(model.getReviewUserLabel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCounter();
//        setSmiley();
        setStar();
        if (isResponded()) {
            model.detail.counterResponse = 1;
            holderComment.date.setText(model.getReviewResponse().getResponseCreateTime());
            holderComment.commentUsername.setText(model.getReviewProductOwner().getUserShopName());
            holderComment.commentMessage.setText(model.getReviewResponse().getResponseMessage());
            holderComment.commentMessage.setMovementMethod(LinkMovementMethod.getInstance());
            holder.comment.setMovementMethod(new SelectableSpannedMovementMethod());
            ImageHandler.loadImageCircle2(getActivity(), holderComment.commentAvatar, model.getReviewProductOwner().getUserShopImage());
            labelResponder.giveSquareLabel(model.getReviewProductOwner().getUserLabel());
            ReputationLevelUtils.setReputationMedals(getActivity(), holderComment.commentBadges,
                    model.getReviewProductOwner().getUserShopReputation().getReputationBadge().getSet(),
                    model.getReviewProductOwner().getUserShopReputation().getReputationBadge().getLevel(),
                    model.getReviewProductOwner().getUserShopReputation().getScore());
        }
        imageUploadAdapter.addList(model.getImages());
    }

    private ImageUploadAdapter.ProductImageListener onProductImageActionListener() {
        return new ImageUploadAdapter.ProductImageListener() {
            @Override
            public View.OnClickListener onUploadClicked(int position) {
                return null;
            }

            @Override
            public View.OnClickListener onImageClicked(final int position, ImageUpload imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> listImage = new ArrayList<>();
                        ArrayList<String> listDesc = new ArrayList<>();
                        for (ImageUpload imageUpload : model.getImages()) {
                            listImage.add(imageUpload.getPicSrcLarge());
                            listDesc.add(imageUpload.getDescription());
                        }

                        Intent intent = new Intent(getActivity(), PreviewProductImage.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("fileloc", listImage);
                        bundle.putStringArrayList("image_desc", listDesc);
                        bundle.putInt("img_pos", position);
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);
                    }
                };
            }
        };
    }

    private void setVisibility() {
        if (isResponded()) {
            holderComment.commentView.setVisibility(View.VISIBLE);
        } else {
            holderComment.commentView.setVisibility(View.GONE);
        }

        if (SessionHandler.isV4Login(getActivity()) && isProductOwner()) {
            holder.overFlow.setVisibility(View.VISIBLE);
            holderComment.buttonOverflow.setVisibility(View.VISIBLE);
        } else {
            holder.overFlow.setVisibility(View.GONE);
            holderComment.buttonOverflow.setVisibility(View.GONE);
        }

        if (!model.detail.isGetLikeDislike) {
            holder.viewLikeDislike.setVisibility(View.GONE);
            holder.loading.setVisibility(View.VISIBLE);
        } else {
            holder.viewLikeDislike.setVisibility(View.VISIBLE);
            holder.loading.setVisibility(View.GONE);
        }

        if (model.getImages().size() > 0) {
            holder.imageHolder.setVisibility(View.VISIBLE);
        } else {
            holder.imageHolder.setVisibility(View.GONE);
        }
    }

    private boolean isProductOwner() {
        SessionHandler session = new SessionHandler(getActivity());
        try {
            return String.valueOf(model.getReviewProductOwner().getUserId()).equals(session.getLoginID());
        } catch (Exception e) {
            return false;
        }
    }

    private void setCounter() {
        holder.textPercentage.setText(model.getReviewUserReputation().getPositivePercentage() + "%");
        setIconPercentage();
        holder.counterComment.setText(model.detail.counterResponse + " komentar");

        setIconLike();
        setIconDislike();

        holder.counterLike.setText("" + model.detail.counterLike);
        holder.counterDislike.setText("" + model.detail.counterDislike);
        holder.counterComment.setText("" + model.detail.counterResponse);
    }

    private void setIconPercentage() {
        if (allowActiveSmiley()) {
            holder.iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile_active);
            holder.textPercentage.setVisibility(View.VISIBLE);
        } else {
            holder.iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile);
            holder.textPercentage.setVisibility(View.GONE);
        }
    }

    private boolean allowActiveSmiley() {
        return model.getReviewUserReputation().getNoReputation().equals("0");
    }

    private void setIconLike() {
        if (model.detail.statusLikeDislike == 1) {
            holder.iconLike.setImageResource(R.drawable.ic_icon_repsis_like_active);
        } else {
            holder.iconLike.setImageResource(R.drawable.ic_icon_repsis_like);
        }
    }

    private void setIconDislike() {
        if (model.detail.statusLikeDislike == 2) {
            holder.iconDislike.setImageResource(R.drawable.ic_icon_repsis_dislike_active);
        } else {
            holder.iconDislike.setImageResource(R.drawable.ic_icon_repsis_dislike);
        }
    }

    private int generateSmiley(int args) {
        int smiley;
        switch (args) {
            case SMILEY_BAD:
                smiley = R.drawable.ic_icon_repsis_sad_active;
                break;
            case SMILEY_NETRAL:
                smiley = R.drawable.ic_icon_repsis_neutral_active;
                break;
            case SMILEY_SMILE:
                smiley = R.drawable.ic_icon_repsis_smile_active;
                break;
            default:
                smiley = R.drawable.ic_icon_repsis_smile;
                break;
        }
        return smiley;
    }

    private void setStar() {
        StarGenerator.setReputationStars(getActivity(), holder.starAccuracy, model.getReviewRateAccuracy());
        StarGenerator.setReputationStars(getActivity(), holder.starQuality, model.getReviewRateProduct());
    }

    private int generateStar(int args) {
        int star = R.drawable.ic_star_none;
        switch (args) {
            case 1:
                star = R.drawable.ic_star_one;
                break;
            case 2:
                star = R.drawable.ic_star_two;
                break;
            case 3:
                star = R.drawable.ic_star_three;
                break;
            case 4:
                star = R.drawable.ic_star_four;
                break;
            case 5:
                star = R.drawable.ic_star_five;
                break;
        }
        return star;
    }

    private int getBadges(int args) {
        int drawable = 0;
        switch (args) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
        return drawable;
    }

    private boolean isResponded() {
        return !model.getReviewResponse().getResponseMessage().toString().equals("0");
    }


    private View.OnClickListener OnViewReputationClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolTipUtils.showToolTip(setViewToolTip(), v);
            }
        };
    }

    private View setViewToolTip() {
        return ToolTipUtils.setToolTip(getActivity(), R.layout.view_tooltip_user, new ToolTipUtils.ToolTipListener() {
            @Override
            public void setView(View view) {
                TextView smile = (TextView) view.findViewById(R.id.text_smile);
                TextView netral = (TextView) view.findViewById(R.id.text_netral);
                TextView bad = (TextView) view.findViewById(R.id.text_bad);
                smile.setText("" + model.getReviewUserReputation().getPositive());
                netral.setText("" + model.getReviewUserReputation().getNeutral());
                bad.setText("" + model.getReviewUserReputation().getNegative());
            }

            @Override
            public void setListener() {

            }
        });
    }

    private View.OnClickListener OnLikeClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SessionHandler.isV4Login(getActivity())) {
                    OnLikeConnection();
                } else {
                    showLoginOption();
                }
            }
        };
    }

    private void OnLikeConnection() {
        ReviewProductModel UnUpdatedModel = model;
        switch (model.detail.statusLikeDislike) {
            case 0:
                setTemporaryActivatedLike();
                break;
            case 1:
                setTemporaryDeactivatedLike();
                break;
            case 2:
                setTemporaryDeactivatedDislike();
                setTemporaryActivatedLike();
                break;
            case 3:
                setTemporaryActivatedLike();
                break;
        }
        UpdateFacade(UnUpdatedModel);
    }

    private void setTemporaryDeactivatedLike() {
        model.detail.statusLikeDislike = 3;
        model.detail.counterLike = model.detail.counterLike - 1;
        setCounter();
    }

    private void setTemporaryDeactivatedDislike() {
        model.detail.statusLikeDislike = 3;
        model.detail.counterDislike = model.detail.counterDislike - 1;
        setCounter();
    }

    private void setTemporaryActivatedLike() {
        model.detail.statusLikeDislike = 1;
        model.detail.counterLike = model.detail.counterLike + 1;
        setCounter();
    }

    private void setTemporaryActivatedDislike() {
        model.detail.statusLikeDislike = 2;
        model.detail.counterDislike = model.detail.counterDislike + 1;
        setCounter();
    }

    private View.OnClickListener OnDislikeClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SessionHandler.isV4Login(getActivity())) {
                    OnDislikeConnection();
                } else {
                    showLoginOption();
                }
            }
        };
    }

    private void showLoginOption() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.error_not_logged));
        builder.setPositiveButton(getString(R.string.title_activity_login), OnLoginClickListener());
        builder.setNegativeButton(getString(R.string.title_cancel), OnCancelClickListener());
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    private DialogInterface.OnClickListener OnLoginClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = ((ReputationRouter) MainApplication.getAppContext()).getLoginIntent
                        (getActivity());
                getActivity().startActivityForResult(intent, REQUEST_LOGIN);
            }
        };
    }

    private DialogInterface.OnClickListener OnCancelClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
    }

    private void OnDislikeConnection() {
        ReviewProductModel UnUpdatedModel = model;
        switch (model.detail.statusLikeDislike) {
            case 0:
                setTemporaryActivatedDislike();
                break;
            case 3:
                setTemporaryActivatedDislike();
                break;
            case 1:
                setTemporaryDeactivatedLike();
                setTemporaryActivatedDislike();
                break;
            case 2:
                setTemporaryDeactivatedDislike();
                break;
        }
        UpdateFacade(UnUpdatedModel);

    }

    private void UpdateFacade(final ReviewProductModel model) {
        progressDialog.showDialog();
        presenter.updateFacade(getActivity(), model.getReviewId(), productID, shopID, model.detail.statusLikeDislike, model);
    }

    private Map<String, String> getActionLikeDislikeParam() {
        ActReviewPass pass = new ActReviewPass();
        pass.setReviewId(String.valueOf(model.getReviewId()));
        pass.setProductId(productID);
        pass.setShopId(shopID);
        pass.setLikeStatus(String.valueOf(model.detail.statusLikeDislike));
        return pass.getLikeDislikeParam();
    }

    private void revertBack(ReviewProductModel model) {
        this.model = model;
        setCounter();
    }

    private View.OnClickListener onOverFlowClickListener(final int menu) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(menu, v);
            }
        };
    }

    private void showPopUp(int menu, View view) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(menu, popup.getMenu());
        popup.setOnMenuItemClickListener(onPopUpMenuClick());
        popup.show();
    }

    private PopupMenu.OnMenuItemClickListener onPopUpMenuClick() {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_report) {
                    showDialogReport();
                    return true;
                } else if (item.getItemId() == R.id.action_delete) {
                    progressDialog.showDialog();
                    deleteComment();
                    return true;
                } else {
                    return false;
                }
            }
        };
    }

    private void showDialogReport() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.prompt_dialog_report, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.reason);

        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton(getActivity().getString(R.string.action_report),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                .setNegativeButton(getActivity().getString(R.string.title_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (userInput.length() > 0) {
                            String message = userInput.getText().toString();
                            ActReviewPass pass = new ActReviewPass();
                            pass.setReviewId(String.valueOf(model.getReviewId()));
                            pass.setShopId(shopID);
                            pass.setReportMessage(message);
                            reportReview(pass);
                            alertDialog.dismiss();
                        } else
                            userInput.setError(getActivity().getString(R.string.error_field_required));
                    }
                });
            }
        });
        // show it
        alertDialog.show();
    }

    private void reportReview(ActReviewPass pass) {
        progressDialog.showDialog();
        presenter.postReport(getActivity(), pass.getReviewId(), pass.getShopId(), pass.getReportMessage());
    }

    private void showNetworkErrorSnackbar() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    private void showSnackbar(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    private void deleteComment() {
        presenter.deleteComment(getActivity(), model.getReviewReputationId(), model.getReviewId(), model.getReviewShopId());
    }


//    private Map<String, String> getDeleteCommentParam() {
//        ActReviewPass pass = new ActReviewPass();
//        pass.setReputationId(model.getReviewReputationId());
//        pass.setReviewId(String.valueOf(model.getReviewId()));
//        pass.setShopId(model.getReviewShopId());
//        return pass.getDeleteParam();
//    }

    private View.OnClickListener OnUserNameClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        PeopleInfoNoDrawerActivity.createInstance(getActivity(), model.getReviewUserId())
                );
            }
        };
    }

    private View.OnClickListener OnShopNameClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ((ReputationRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), model.getReviewShopId());
                startActivity(intent);
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        actNetworkInteractor.unSubscribeObservable();
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }



    @Override
    public void onSuccessDeleteComment(ActResultDomain result) {
        progressDialog.dismiss();
        CommonUtils.UniversalToast(getActivity(), getString(R.string.msg_delete_comment));
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("last_action", "delete_comment_review");
        intent.putExtras(bundle);
        getActivity().setResult(getActivity().RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onSuccessLikeDislikeReview(ActResultDomain result) {
        progressDialog.dismiss();
    }

    @Override
    public void onSuccessGetLikeDislikeReview() {
        progressDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("is_success", 1);
        intent.putExtra("action", "update_product");
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void onSuccessReportReview(ActResultDomain resultDomain) {
        progressDialog.dismiss();
        showSnackbar(getResources().getString(R.string.toast_success_report));
    }

    @Override
    public void onErrorGetLikeDislikeReview(ReviewProductModel model, String err) {
        progressDialog.dismiss();
        revertBack(model);
        showSnackbar(err);
    }

    @Override
    public void onErrorConnectionGetLikeDislikeReview(ReviewProductModel model) {
        progressDialog.dismiss();
        revertBack(model);
        showNetworkErrorSnackbar();
    }

    @Override
    public void onError(String error) {
        progressDialog.dismiss();
        showSnackbar(error);
    }

    @Override
    public void onErrorConnection() {
        progressDialog.dismiss();
        showNetworkErrorSnackbar();
    }

}
