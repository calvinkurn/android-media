package com.tokopedia.otp.securityquestion.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.otp.securityquestion.data.mapper.SecurityQuestionMapper;
import com.tokopedia.otp.securityquestion.domain.model.securityquestion.QuestionDomain;

import rx.Observable;

/**
 * @author by nisie on 10/19/17.
 */

public class SecurityQuestionDataSource {
    private final AccountsService accountsService;
    private final SecurityQuestionMapper securityQuestionMapper;

    public SecurityQuestionDataSource(AccountsService accountsService,
                                      SecurityQuestionMapper securityQuestionMapper) {
        this.accountsService = accountsService;
        this.securityQuestionMapper = securityQuestionMapper;
    }

    public Observable<QuestionDomain> getSecurityQuestion(RequestParams params) {
        return accountsService.getApi()
                .getQuestionForm(params.getParameters())
                .map(securityQuestionMapper);
    }
}
