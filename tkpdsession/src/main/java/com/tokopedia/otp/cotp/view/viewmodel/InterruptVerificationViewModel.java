package com.tokopedia.otp.cotp.view.viewmodel;

import com.tokopedia.core.analytics.AppScreen;
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


    public InterruptVerificationViewModel(int type, String appScreenName, int iconId,
                                          String userName, String promptText, String buttonText) {
        this.type = type;
        this.appScreenName = appScreenName;
        this.iconId = iconId;
        this.userName = userName;
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

    public static InterruptVerificationViewModel createDefaultSmsInterruptPage(String fullName, String phone) {
        return new InterruptVerificationViewModel(
                VerificationActivity.TYPE_SMS,
                AppScreen.SCREEN_INTERRUPT_VERIFICATION_SMS,
                R.drawable.ic_verification_sms,
                fullName,
                "Kami akan mengirimkan SMS kode verifikasi ke nomor <b>" + phone + "</b>.",
                "Kirim SMS Verifikasi"
        );
    }
}
