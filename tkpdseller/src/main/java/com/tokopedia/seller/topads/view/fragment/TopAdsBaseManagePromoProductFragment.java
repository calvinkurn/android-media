package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.di.TopAdsAddPromoPoductDI;
import com.tokopedia.seller.topads.view.presenter.TopAdsManagePromoProductPresenter;
import com.tokopedia.seller.topads.view.listener.TopAdsManagePromoProductView;

/**
 * Created by zulfikarrahman on 2/16/17.
 */

public abstract class TopAdsBaseManagePromoProductFragment extends BasePresenterFragment<TopAdsManagePromoProductPresenter>
        implements TopAdsManagePromoProductView {

    protected Button buttonNext;

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

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
        presenter = TopAdsAddPromoPoductDI.createPresenter(getActivity());
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected void initView(View view) {
        buttonNext = (Button) view.findViewById(R.id.button_next);
    }

    @Override
    protected void setViewListener() {
        buttonNext.setOnClickListener(onClickNext());
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    private View.OnClickListener onClickNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()) {
                    String valueOption = getValueOption();
                }else{

                }
            }
        };
    }

    protected abstract boolean validateForm();

    protected abstract String getValueOption();
}
