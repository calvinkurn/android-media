package com.tokopedia.profile.view.customview;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;
import com.tokopedia.session.R;

/**
 * Created by alvinatin on 13/02/18.
 */

public class PartialUserDataView extends BaseCustomView {

    RelativeLayout bannerIncompleteProfile;
    RelativeLayout partialPhoneNumber;
    RelativeLayout partialEmail;
    RelativeLayout partialGender;
    RelativeLayout partialBirthDate;
    TextView verifiedPhoneNumber;
    TextView verifiedEmail;
    TextView dataPhoneNumber;
    TextView dataEmail;
    TextView dataGender;
    TextView dataBirthDate;

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

    public PartialUserDataView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PartialUserDataView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PartialUserDataView(@NonNull Context context) {
        super(context);
        init();
    }

    private void init(){
        View view = inflate(getContext(), R.layout.partial_profile_user_data, this);
        bannerIncompleteProfile = view.findViewById(R.id.rl_incomplete_profile);
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
    }

    public void renderData(TopProfileViewModel model){
        bannerIncompleteProfile.setVisibility(GONE);
        dataPhoneNumber.setText(model.getPhoneNumber());
        dataEmail.setText(model.getEmail());
        dataGender.setText(model.getGender());
        dataBirthDate.setText(model.getBirthDate());
    }
}
