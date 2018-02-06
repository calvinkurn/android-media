package com.tokopedia.events.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.events.view.viewmodel.PackageViewModel;
import com.tokopedia.events.view.viewmodel.SelectedSeatViewModel;

/**
 * Created by pranaymohapatra on 28/11/17.
 */

public class EventReviewTicketsContractor {

    public interface EventReviewTicketsView extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderFromPackageVM(PackageViewModel packageViewModel, SelectedSeatViewModel selectedSeats);

        void setEmailID(String emailID);

        void setPhoneNumber(String number);

        void showProgressBar();

        void hideProgressBar();

        void initForms(String[] hintText, String[] regex);


        RequestParams getParams();

        android.view.View getRootView();

        void showPromoSuccessMessage(String text, int color);

        void showCashbackMessage(String text);

        void hideSuccessMessage();

        void showEmailTooltip();

        void showMoreinfoTooltip();

        void hideTooltip();
    }

    public interface Presenter extends CustomerPresenter<EventReviewTicketsContractor.EventReviewTicketsView> {

        void initialize();

        void onDestroy();

        void proceedToPayment();

        void updatePromoCode(String code);

        void validateEditText(EditText view);

        void updateEmail(String email);

        void updateNumber(String number);

        void getProfile();

        void clickEmailIcon();

        void clickMoreinfoIcon();

        void clickDismissTooltip();
    }
}
