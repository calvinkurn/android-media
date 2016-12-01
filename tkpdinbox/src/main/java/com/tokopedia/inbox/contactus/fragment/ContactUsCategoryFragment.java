package com.tokopedia.inbox.contactus.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.inbox.contactus.ContactUsConstant;
import com.tokopedia.inbox.contactus.activity.ContactUsActivity;
import com.tokopedia.inbox.contactus.adapter.ContactUsAdapter;
import com.tokopedia.inbox.contactus.listener.ContactUsCategoryFragmentView;
import com.tokopedia.inbox.contactus.model.contactuscategory.ContactUsCategory;
import com.tokopedia.inbox.contactus.model.contactuscategory.TicketCategory;
import com.tokopedia.inbox.contactus.presenter.ContactUsCategoryFragmentPresenter;
import com.tokopedia.inbox.contactus.presenter.ContactUsCategoryFragmentPresenterImpl;
import com.tokopedia.core.customView.ObservableWebView;
import com.tokopedia.core.network.NetworkErrorHelper;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by nisie on 8/12/16.
 */
public class ContactUsCategoryFragment extends
        BasePresenterFragment<ContactUsCategoryFragmentPresenter>
        implements ContactUsCategoryFragmentView, ContactUsConstant {

    public interface ContactUsCategoryFragmentListener {
        void onOpenWebView(String url);

        void onOpenContactUsTicketForm(int lastCatId, ArrayList<String> path);
    }

    @BindView(R2.id.new_user)
    ImageView typeNewUser;

    @BindView(R2.id.seller)
    ImageView typeSeller;

    @BindView(R2.id.buyer)
    ImageView typeBuyer;

    @BindView(R2.id.other)
    ImageView typeOther;

    @BindView(R2.id.new_user_text)
    TextView textNewUser;

    @BindView(R2.id.seller_text)
    TextView textSeller;

    @BindView(R2.id.buyer_text)
    TextView textBuyer;

    @BindView(R2.id.other_text)
    TextView textOther;

    @BindView(R2.id.step_1)
    View step1;

    @BindView(R2.id.step_2)
    View step2;

    @BindView(R2.id.step_3)
    View step3;

    @BindView(R2.id.problem_list)
    LinearLayout problemList;

    @BindView(R2.id.please_contact_us)
    View pleaseContactUs;

    @BindView(R2.id.scroll)
    ScrollView mainView;

    @BindView(R2.id.finish_message)
    View finishMessage;

    @BindView(R2.id.solution)
    ObservableWebView solution;

    TkpdProgressDialog progressDialog;
    ContactUsAdapter adapter;
    ContactUsCategoryFragmentListener listener;


    public static ContactUsCategoryFragment createInstance() {
        ContactUsCategoryFragment fragment = new ContactUsCategoryFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.initCategory();
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ContactUsCategoryFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        if (activity instanceof ContactUsCategoryFragmentListener) {
            listener = (ContactUsCategoryFragmentListener) activity;
        } else {
            throw new RuntimeException("Needs to implement ContactUsCategoryFragmentListener");
        }
    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_contact_us;
    }

    @Override
    protected void initView(View view) {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        progressDialog.setCancelable(false);
        mainView.setSmoothScrollingEnabled(true);

        adapter = new ContactUsAdapter(problemList, onActionCategoryContactUs());

    }

    private ContactUsAdapter.AdapterListener onActionCategoryContactUs() {
        return new ContactUsAdapter.AdapterListener() {
            @Override
            public void onShowDescription(TicketCategory model) {
                step3.setVisibility(View.VISIBLE);
                if (model.getTicketCategoryDescription().length() > 0) {
                    solution.loadDataWithBaseURL("http://www.tokopedia.com", model.getTicketCategoryDescription(), "text/html", "UTF-8", null);
                } else {
                    solution.loadDataWithBaseURL("http://www.tokopedia.com", getString(R.string.message_default_suggestion), "text/html", "UTF-8", null);
                }
                scrollTo((int) Math.floor(step2.getY()));
            }

            @Override
            public void addSpinner() {
                if (step3.getVisibility() == View.VISIBLE) {
                    hideDescription();
                } else {
                    scrollTo((int) Math.floor(step2.getY()));
                }
            }

            @Override
            public void onRemoveDescription() {
                if (step3.getVisibility() == View.VISIBLE) {
                    hideDescription();
                }
            }
        };
    }

    private void hideDescription() {
        int y = mainView.getHeight() - ((int) Math.floor(step3.getY()));
        ObjectAnimator yScroll = ObjectAnimator.ofInt(mainView, "scrollY", y);
        ObjectAnimator alpha = ObjectAnimator.ofInt(step3, "alpha", 0);
        alpha.setDuration(250L);
        AnimatorSet animators = new AnimatorSet();
        animators.setDuration(500L);
        animators.playTogether(yScroll, alpha);
        animators.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {

            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                step3.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator arg0) {
            }
        });
        animators.start();
    }

    private void scrollTo(int y) {
        ObjectAnimator yTranslate = ObjectAnimator.ofInt(mainView, "scrollY", y);

        AnimatorSet animators = new AnimatorSet();
        animators.setDuration(500L);
        animators.play(yTranslate);
        animators.start();
    }

    @Override
    protected void setViewListener() {
        solution.setWebViewClient(new BrowseSolutionWebView());
        pleaseContactUs.setOnClickListener(onCreateTicket());

    }

    private View.OnClickListener onCreateTicket() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onOpenContactUsTicketForm(adapter.getLastCatId(), adapter.getPath());
            }
        };
    }

    private View.OnClickListener onCategoryTypeSelectListener(final ImageView typeImage, final TextView typeText, final TicketCategory data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllTypeTransparent();
                setGreenCircle(typeImage, typeText);
                step2.setVisibility(View.VISIBLE);
                adapter.updateData(data.getTicketCategoryChild());
                adapter.setSelectedMainCategory(data.getTicketCategoryName());
                if (step3.getVisibility() == View.VISIBLE)
                    hideDescription();
            }
        };
    }

    private void setAllTypeTransparent() {
        setTransparent(typeBuyer, textBuyer);
        setTransparent(typeSeller, textSeller);
        setTransparent(typeNewUser, textNewUser);
        setTransparent(typeOther, textOther);
    }

    private void setGreenCircle(ImageView type, TextView typeText) {
        type.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.contact_us_green_circle));
        typeText.setTextColor(Color.BLACK);
    }

    private void setTransparent(ImageView image, TextView text) {
        image.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.contact_us_transparent));
        text.setTextColor(Color.LTGRAY);
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog != null)
            progressDialog.showDialog();
    }

    @Override
    public void finishLoading() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void setCategory(ContactUsCategory result) {
        adapter.appendSpinner(getActivity(), result.getList());
        step1.setVisibility(View.VISIBLE);
        setTypeCategoryData();
    }

    @Override
    public ContactUsActivity.BackButtonListener getBackButtonListener() {
        return new ContactUsActivity.BackButtonListener() {
            @Override
            public void onBackPressed() {
                if (solution.canGoBack()) {
                    solution.goBack();
                }

            }

            @Override
            public boolean canGoBack() {
                return solution.canGoBack();
            }
        };
    }

    @Override
    public void showError(String error) {
        if (error.equals(""))
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.initCategory();
                }
            });
        else
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.initCategory();
                }
            });
    }

    private void setTypeCategoryData() {
        typeSeller.setOnClickListener(onCategoryTypeSelectListener(typeSeller, textSeller, adapter.getData(ID_SELLER)));
        typeNewUser.setOnClickListener(onCategoryTypeSelectListener(typeNewUser, textNewUser, adapter.getData(ID_NEW_USER)));
        typeBuyer.setOnClickListener(onCategoryTypeSelectListener(typeBuyer, textBuyer, adapter.getData(ID_BUYER)));
        typeOther.setOnClickListener(onCategoryTypeSelectListener(typeOther, textOther, adapter.getData(ID_OTHER)));
    }

    private class BrowseSolutionWebView extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains(".pdf")) {
                openInBrowser(url);
            } else {
                listener.onOpenWebView(url);
            }
            return true;
        }

        public void openInBrowser(String url) {
            String googleDocsUrl = "http://docs.google.com/viewer?url=";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(googleDocsUrl + url), "text/html");
            startActivity(intent);
        }
    }
}
