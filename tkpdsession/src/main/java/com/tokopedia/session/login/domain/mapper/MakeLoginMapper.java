package com.tokopedia.session.login.domain.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.session.login.domain.model.MakeLoginDomainData;
import com.tokopedia.session.login.domain.model.MakeLoginDomainModel;
import com.tokopedia.session.login.domain.model.ReputationBadgeDomain;
import com.tokopedia.session.login.domain.model.ShopReputationDomain;
import com.tokopedia.session.login.domain.model.UserReputationDomain;
import com.tokopedia.session.login.domain.pojo.MakeLoginData;
import com.tokopedia.session.login.domain.pojo.ReputationBadge;
import com.tokopedia.session.login.domain.pojo.ShopReputation;
import com.tokopedia.session.login.domain.pojo.UserReputation;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 5/26/17.
 */

public class MakeLoginMapper implements Func1<Response<TkpdResponse>, MakeLoginDomainModel> {

    @Override
    public MakeLoginDomainModel call(Response<TkpdResponse> response) {
        return convertToDomainModel(response);
    }

    private MakeLoginDomainModel convertToDomainModel(Response<TkpdResponse> response) {
        MakeLoginDomainModel model = new MakeLoginDomainModel();

        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                MakeLoginData data = response.body().convertDataObj(MakeLoginData.class);
                model.setSuccess(true);
                model.setMakeLoginDomainData(convertToDomainData(data));
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    model.setSuccess(false);
                } else {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                }
            }
            model.setStatusMessage(response.body().getStatusMessageJoined());
            model.setResponseCode(response.code());
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }

    private MakeLoginDomainData convertToDomainData(MakeLoginData data) {
        return new MakeLoginDomainData(
                data.getShopIsGold(),
                data.getMsisdnIsVerified(),
                data.getShopId(),
                data.getShopName(),
                data.getFullName(),
                createShopReputationDomain(data.getShopReputation()),
                data.getIsLogin(),
                createUserReputationDomain(data.getUserReputation()),
                data.getShopHasTerms(),
                data.getShopIsOfficial(),
                data.getIsRegisterDevice(),
                data.getUserId(),
                data.getMsisdnShowDialog(),
                data.getShopAvatar(),
                data.getUserImage());
    }

    private UserReputationDomain createUserReputationDomain(UserReputation userReputation) {
        return new UserReputationDomain(
                userReputation.getPositivePercentage(),
                userReputation.getNoReputation(),
                userReputation.getNegative(),
                userReputation.getPositive(),
                userReputation.getNeutral()
        );
    }

    private ShopReputationDomain createShopReputationDomain(ShopReputation shopReputation) {
        return new ShopReputationDomain(
                shopReputation.getTooltip(),
                createReputationBadgeDomain(shopReputation.getReputationBadge()),
                shopReputation.getReputationScore(),
                shopReputation.getMinBadgeScore(),
                shopReputation.getScore());
    }

    private ReputationBadgeDomain createReputationBadgeDomain(ReputationBadge reputationBadge) {
        return new ReputationBadgeDomain(
                reputationBadge.getLevel(),
                reputationBadge.getSet()
        );
    }


}
