package com.tokopedia.otp.cotp.view.viewmodel;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.session.R;

/**
 * @author by nisie on 1/4/18.
 */

public class InterruptVerificationViewModel {

    int type;
    String appScreenName;
    int iconId;
    String userName;
    String promptText;
    String buttonText;
    private boolean hasOtherMethod;


    public InterruptVerificationViewModel(int type, String appScreenName, int iconId,
                                          String promptText, String buttonText) {
        this.type = type;
        this.appScreenName = appScreenName;
        this.iconId = iconId;
        this.promptText = promptText;
        this.buttonText = buttonText;
    }

    public int getType() {
        return type;
    }

    public String getAppScreenName() {
        return appScreenName;
    }

    public int getIconId() {
        return iconId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPromptText() {
        return promptText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public static InterruptVerificationViewModel createDefaultSmsInterruptPage(String phone) {
        return new InterruptVerificationViewModel(
                VerificationActivity.TYPE_SMS,
                AppScreen.SCREEN_INTERRUPT_VERIFICATION_SMS,
                R.drawable.ic_verification_sms,
                MainApplication.getAppContext().getString(R.string.to_verify_sms)
                        + "<br><font color='#b3000000'>" + MethodItem.getMaskedPhoneNumber(phone) +
                        "</font>.",
                MainApplication.getAppContext().getString(R.string.send_sms_verification)
        );
    }

    public static InterruptVerificationViewModel createDefaultEmailInterruptPage(String email) {
        return new InterruptVerificationViewModel(
                VerificationActivity.TYPE_EMAIL,
                AppScreen.SCREEN_INTERRUPT_VERIFICATION_EMAIL,
                R.drawable.ic_verification_email,
                MainApplication.getAppContext().getString(R.string.to_verify_email)
                        + "<br><font color='#b3000000'>" + email + "</font>.",
                MainApplication.getAppContext().getString(R.string.send_email_verification)
        );
    }

    public void setHasOtherMethod(boolean hasOtherMethod) {
        this.hasOtherMethod = hasOtherMethod;
    }

    public boolean isHasOtherMethod() {
        return hasOtherMethod;
    }
}
