package com.tokopedia.otp.securityquestion.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.user.InterruptService;
import com.tokopedia.otp.securityquestion.domain.mapper.SecurityQuestionMapper;
import com.tokopedia.otp.securityquestion.data.model.securityquestion.QuestionViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 10/19/17.
 */

public class SecurityQuestionDataSource {
    private final InterruptService interruptService;
    private final SecurityQuestionMapper securityQuestionMapper;

    @Inject
    public SecurityQuestionDataSource(InterruptService interruptService,
                                      SecurityQuestionMapper securityQuestionMapper) {
        this.interruptService = interruptService;
        this.securityQuestionMapper = securityQuestionMapper;
    }

    public Observable<QuestionViewModel> getSecurityQuestion(RequestParams params) {
        return interruptService.getApi()
                .getQuestionForm(params.getParameters())
                .map(securityQuestionMapper);
    }
}
