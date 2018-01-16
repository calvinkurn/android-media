package com.tokopedia.session.domain.mapper;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.session.R;
import com.tokopedia.session.data.viewmodel.SecurityDomain;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.data.viewmodel.login.ReputationBadgeDomain;
import com.tokopedia.session.data.viewmodel.login.ShopReputationDomain;
import com.tokopedia.session.data.viewmodel.login.UserReputationDomain;
import com.tokopedia.session.domain.pojo.login.MakeLoginPojo;
import com.tokopedia.session.domain.pojo.login.ReputationBadgePojo;
import com.tokopedia.session.domain.pojo.login.SecurityPojo;
import com.tokopedia.session.domain.pojo.login.ShopReputationPojo;
import com.tokopedia.session.domain.pojo.login.UserReputationPojo;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 5/26/17.
 */

public class MakeLoginMapper implements Func1<Response<TkpdResponse>, MakeLoginDomain> {

    private static final String TRUE = "true";
    private static final String TRUE_1 = "1";

    @Inject
    public MakeLoginMapper() {
    }

    @Override
    public MakeLoginDomain call(Response<TkpdResponse> response) {
        return convertToDomainModel(response);
    }

    private MakeLoginDomain convertToDomainModel(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || (!response.body().isNullData()
                    && response.body().getErrorMessages() == null)) {
                MakeLoginPojo pojo = response.body().convertDataObj(MakeLoginPojo.class);
                return convertToDomainData(pojo);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(MainApplication.getAppContext().getString
                            (R.string.default_request_error_unknown));
                }
            }
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private MakeLoginDomain convertToDomainData(MakeLoginPojo data) {
        return new MakeLoginDomain(
                data.getShopIsGold(),
                data.getMsisdnIsVerified().equals(TRUE_1),
                data.getShopId(),
                data.getShopName(),
                data.getFullName(),
                createShopReputationDomain(data.getShopReputation()),
                data.getIsLogin().equals(TRUE),
                createUserReputationDomain(data.getUserReputation()),
                data.getShopHasTerms(),
                data.getShopIsOfficial(),
                data.getIsRegisterDevice(),
                data.getUserId(),
                data.getMsisdnShowDialog(),
                data.getShopAvatar(),
                data.getUserImage(),
                convertToSecurityDomain(data.getSecurityPojo()));
    }

    private SecurityDomain convertToSecurityDomain(@Nullable SecurityPojo securityPojo) {
        if (securityPojo != null) {
            return new SecurityDomain(securityPojo.getAllowLogin(),
                    securityPojo.getUserCheckSecurity1(),
                    securityPojo.getUserCheckSecurity2());
        } else {
            return null;
        }
    }

    private UserReputationDomain createUserReputationDomain(@Nullable UserReputationPojo
                                                                    userReputation) {
        if (userReputation != null) {
            return new UserReputationDomain(
                    userReputation.getPositivePercentage(),
                    userReputation.getNoReputation(),
                    userReputation.getNegative(),
                    userReputation.getPositive(),
                    userReputation.getNeutral()
            );
        } else {
            return null;
        }
    }

    private ShopReputationDomain createShopReputationDomain(@Nullable ShopReputationPojo
                                                                    shopReputation) {
        if (shopReputation != null) {
            return new ShopReputationDomain(
                    shopReputation.getTooltip(),
                    createReputationBadgeDomain(shopReputation.getReputationBadge()),
                    shopReputation.getReputationScore(),
                    shopReputation.getMinBadgeScore(),
                    shopReputation.getScore());
        } else {
            return null;
        }
    }

    private ReputationBadgeDomain createReputationBadgeDomain(@Nullable ReputationBadgePojo
                                                                      reputationBadge) {
        if (reputationBadge != null) {
            return new ReputationBadgeDomain(
                    reputationBadge.getLevel(),
                    reputationBadge.getSet()
            );
        } else {
            return null;
        }

    }


}
