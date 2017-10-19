package com.tokopedia.otp.securityquestion.view.viewmodel;

import android.os.Parcelable;

import com.tokopedia.otp.securityquestion.domain.model.securityquestion.QuestionDomain;
import com.tokopedia.session.data.viewmodel.SecurityDomain;

/**
 * @author by nisie on 10/19/17.
 */

public class SecurityQuestionViewModel {

    private String name;
    private SecurityDomain securityDomain;
    private QuestionDomain questionDomain;

    public SecurityQuestionViewModel(SecurityDomain securityDomain, String name) {
        this.securityDomain = securityDomain;
        this.name = name;
    }

    public SecurityDomain getSecurityDomain() {
        return securityDomain;
    }

    public void setSecurityDomain(SecurityDomain securityDomain) {
        this.securityDomain = securityDomain;
    }

    public QuestionDomain getQuestionDomain() {
        return questionDomain;
    }

    public void setQuestionDomain(QuestionDomain questionDomain) {
        this.questionDomain = questionDomain;
    }

    public String getName() {
        return name;
    }
}
