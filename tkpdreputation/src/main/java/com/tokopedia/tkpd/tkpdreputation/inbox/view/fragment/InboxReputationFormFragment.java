package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customadapter.ImageUpload;
import com.tokopedia.core.customadapter.ImageUploadAdapter;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationForm;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter.InboxReputationFormPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 8/20/17.
 */

public class InboxReputationFormFragment extends BaseDaggerFragment
        implements InboxReputationForm.View {

    RatingBar rating;
    TextView ratingText;
    EditText review;
    TextInputLayout reviewLayout;
    ImageView uploadInfo;
    RecyclerView listImageUpload;
    Switch shareFbSwitch;
    Switch anomymousSwitch;
    Button sendButton;
    ImageUploadAdapter adapter;

    @Inject
    InboxReputationFormPresenter presenter;

    public static InboxReputationFormFragment createInstance(String id) {
        InboxReputationFormFragment fragment = new InboxReputationFormFragment();
        Bundle bundle = new Bundle();
//        bundle.putString(InboxReputationDetailActivity.ARGS_REPUTATION_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_REVIEW_FORM;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerReputationComponent reputationComponent =
                (DaggerReputationComponent) DaggerReputationComponent
                        .builder()
                        .appComponent(appComponent)
                        .build();
        reputationComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View parentView = inflater.inflate(R.layout.fragment_inbox_reputation_form, container,
                false);
        rating = (RatingBar) parentView.findViewById(R.id.rating);
        ratingText = (TextView) parentView.findViewById(R.id.rating_text);
        review = (EditText) parentView.findViewById(R.id.review);
        reviewLayout = (TextInputLayout) parentView.findViewById(R.id.review_layout);
        uploadInfo = (ImageView) parentView.findViewById(R.id.upload_info);
        listImageUpload = (RecyclerView) parentView.findViewById(R.id.list_image_upload);
        shareFbSwitch = (Switch) parentView.findViewById(R.id.switch_facebook);
        anomymousSwitch = (Switch) parentView.findViewById(R.id.switch_anonym);
        sendButton = (Button) parentView.findViewById(R.id.send_button);
        prepareView();
        presenter.attachView(this);
        return parentView;
    }

    private void prepareView() {

        adapter = ImageUploadAdapter.createAdapter(getContext());
        adapter.setCanUpload(true);
        adapter.setListener(new ImageUploadAdapter.ProductImageListener() {
            @Override
            public View.OnClickListener onUploadClicked(int position) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                };
            }

            @Override
            public View.OnClickListener onImageClicked(int position, ArrayList<ImageUpload> imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                };
            }
        });
        listImageUpload.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        listImageUpload.setAdapter(adapter);

        uploadInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog uploadInfoDialog = new BottomSheetDialog(getActivity());
                uploadInfoDialog.setContentView(R.layout.upload_info_dialog);
                Button closeDialog = (Button) uploadInfoDialog.findViewById(R.id.close_button);
                closeDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadInfoDialog.dismiss();
                    }
                });
                uploadInfoDialog.show();
            }
        });

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating == 1.0f){
                    ratingText.setText(MainApplication.getAppContext().getString(R.string
                            .rating_title_1));
                }else if(rating == 2.0f){
                    ratingText.setText(MainApplication.getAppContext().getString(R.string
                            .rating_title_2));
                }else if(rating == 3.0f){
                    ratingText.setText(MainApplication.getAppContext().getString(R.string
                            .rating_title_3));
                }else if(rating == 4.0f){
                    ratingText.setText(MainApplication.getAppContext().getString(R.string
                            .rating_title_4));
                }else if(rating == 5.0f){
                    ratingText.setText(MainApplication.getAppContext().getString(R.string
                            .rating_title_5));
                }else if(rating == 0.0f){
                    ratingText.setText(MainApplication.getAppContext().getString(R.string
                            .rating_title_0));
                }
            }
        });
    }
}
