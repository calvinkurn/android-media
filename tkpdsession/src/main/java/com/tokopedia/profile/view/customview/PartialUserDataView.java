package com.tokopedia.profile.view.customview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.session.R;

/**
 * @author by alvinatin on 13/02/18.
 */

public class PartialUserDataView extends BaseCustomView {

    RelativeLayout bannerIncompleteProfile;
    RelativeLayout partialPhoneNumber;
    RelativeLayout partialEmail;
    RelativeLayout partialGender;
    RelativeLayout partialBirthDate;
    TextView tvIncompleteProfile;
    TextView verifiedPhoneNumber;
    TextView verifiedEmail;
    TextView dataPhoneNumber;
    TextView dataEmail;
    TextView dataGender;
    TextView dataBirthDate;
    TextView progressText;
    ProgressBar progressBar;
    View separatorPhone;
    View separatorEmail;
    View separatorGender;

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchSaveInstanceState(container);
    }

    public PartialUserDataView(@NonNull Context context, @Nullable AttributeSet attrs, int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PartialUserDataView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PartialUserDataView(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.partial_profile_user_data, this);
        bannerIncompleteProfile = view.findViewById(R.id.rl_incomplete_profile);
        tvIncompleteProfile = view.findViewById(R.id.tv_incomplete_profile);
        partialPhoneNumber = view.findViewById(R.id.rl_phone_number);
        partialEmail = view.findViewById(R.id.rl_email);
        partialGender = view.findViewById(R.id.rl_gender);
        partialBirthDate = view.findViewById(R.id.rl_birth_date);
        verifiedPhoneNumber = view.findViewById(R.id.tv_verified_phone_number);
        verifiedEmail = view.findViewById(R.id.tv_verified_email);
        dataPhoneNumber = view.findViewById(R.id.tv_phone_number);
        dataEmail = view.findViewById(R.id.tv_email);
        dataGender = view.findViewById(R.id.tv_gender);
        dataBirthDate = view.findViewById(R.id.tv_birth_date);
        progressText = view.findViewById(R.id.tv_progress);
        progressBar = view.findViewById(R.id.circular_progress_bar);
        separatorPhone = view.findViewById(R.id.separator_phone_number);
        separatorEmail = view.findViewById(R.id.separator_email);
        separatorGender = view.findViewById(R.id.separator_gender);

        bannerIncompleteProfile.setVisibility(GONE);
        partialPhoneNumber.setVisibility(GONE);
        partialEmail.setVisibility(GONE);
        partialGender.setVisibility(GONE);
        partialBirthDate.setVisibility(GONE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void renderData(TopProfileViewModel model) {
        if (model.isUser()) {
            renderIncompleteBanner(model);
            renderPhoneNumber(model);
            renderEmail(model);
            renderBirthDate(model);
            renderGender(model);
            this.setVisibility(VISIBLE);
        } else {
            this.setVisibility(GONE);
        }
    }

    private void renderIncompleteBanner(TopProfileViewModel model) {
        if (model.getCompletion() < 100) {
            bannerIncompleteProfile.setVisibility(VISIBLE);

            tvIncompleteProfile.setOnClickListener(new ClickProfileCompletion());

            ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, model
                    .getCompletion());
            animation.setDuration(2000);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
            String completion = String.valueOf(model.getCompletion()) + "%";
            progressText.setText(completion);
        } else {
            bannerIncompleteProfile.setVisibility(GONE);
        }
    }

    private void renderPhoneNumber(TopProfileViewModel model) {
        if (!model.getPhoneNumber().equals("")) {
            partialPhoneNumber.setVisibility(VISIBLE);
            dataPhoneNumber.setText(model.getPhoneNumber());
            if (model.isPhoneVerified()) {
                verifiedPhoneNumber.setVisibility(VISIBLE);
            } else {
                verifiedPhoneNumber.setVisibility(GONE);
            }

            if (model.getEmail().equals("")
                    && model.getGender().equals("")
                    && model.getBirthDate().equals("")) {
                separatorPhone.setVisibility(GONE);
            } else {
                separatorPhone.setVisibility(VISIBLE);
            }
        } else {
            partialPhoneNumber.setVisibility(GONE);
        }
    }

    private void renderEmail(TopProfileViewModel model) {
        if (!model.getEmail().equals("")) {
            partialEmail.setVisibility(VISIBLE);
            dataEmail.setText(model.getEmail());
            if (model.isEmailVerified()) {
                verifiedEmail.setVisibility(VISIBLE);
            } else {
                verifiedEmail.setVisibility(GONE);
            }

            if (model.getGender().equals("") && model.getBirthDate().equals("")) {
                separatorEmail.setVisibility(GONE);
            } else {
                separatorEmail.setVisibility(VISIBLE);
            }
        } else {
            partialEmail.setVisibility(GONE);
        }
    }

    private void renderBirthDate(TopProfileViewModel model) {
        if (!model.getBirthDate().equals("")) {
            partialBirthDate.setVisibility(VISIBLE);
            dataBirthDate.setText(model.getBirthDate());
        } else {
            partialBirthDate.setVisibility(GONE);
        }
    }

    private void renderGender(TopProfileViewModel model) {
        if (!model.getGender().equals("")) {
            partialGender.setVisibility(VISIBLE);
            dataGender.setText(model.getGender());

            if (model.getBirthDate().equals("")) {
                separatorGender.setVisibility(GONE);
            } else {
                separatorGender.setVisibility(VISIBLE);
            }
        } else {
            partialGender.setVisibility(GONE);
        }
    }

    private class ClickProfileCompletion implements OnClickListener {

        ClickProfileCompletion() {
        }

        @Override
        public void onClick(View v) {
            getContext().startActivity(
                    new Intent(getContext(), ProfileCompletionActivity.class));
        }
    }
}
