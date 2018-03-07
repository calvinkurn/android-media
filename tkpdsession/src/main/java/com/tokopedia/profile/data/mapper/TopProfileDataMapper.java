package com.tokopedia.profile.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.profile.data.pojo.ProfileData;
import com.tokopedia.profile.data.pojo.ProfileGraphql;
import com.tokopedia.profile.data.pojo.ProfileInfo;
import com.tokopedia.profile.data.pojo.ProfileReputation;
import com.tokopedia.profile.data.pojo.ProfileShopInfo;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author alvinatin on 27/02/18.
 */

public class TopProfileDataMapper
        implements Func1<Response<GraphqlResponse<ProfileGraphql>>, TopProfileViewModel> {

    @Inject
    public TopProfileDataMapper() {

    }

    @Override
    public TopProfileViewModel call(Response<GraphqlResponse<ProfileGraphql>> graphqlResponse) {
        ProfileGraphql profileGraphql = getDataOrError(graphqlResponse);
        TopProfileViewModel model = new TopProfileViewModel();

        setUserData(model, profileGraphql.getProfileData().getData());
        setUserInfo(model, profileGraphql.getProfileInfo());
        setReputation(model, profileGraphql.getProfileReputation());
        setShopInfo(model, profileGraphql.getProfileShopInfo().getData());
        return model;
    }

    private void setUserData(TopProfileViewModel model, ProfileData.Data data) {
        model.setUserId(data.getId());
        model.setName(data.getName());
        model.setTitle(data.getInfo());
        model.setBiodata(data.getBio() != null ? data.getBio().replace("\n", "") : "");
        model.setFollowing(data.getFollowingFmt());
        model.setFollowers(data.getFollowersFmt());
        model.setFollowed(data.isFollowed());
        model.setFavoritedShop(data.getFavoriteFmt());
        model.setUserPhoto(data.getPhoto());
        model.setKol(data.isIskol());
        model.setIsUser(data.isMe());
    }

    private void setUserInfo(TopProfileViewModel model, ProfileInfo data) {
        model.setPhoneVerified(data.isPhoneVerified());
        model.setEmailVerified(data.isEmailVerified());
        model.setPhoneNumber(data.getPhone());
        model.setEmail(data.getEmail());
        model.setGender(data.getGenderName());
        model.setBirthDate(data.getAppsBday());
        model.setCompletion(data.getCompletion());
    }

    private void setReputation(TopProfileViewModel model, ProfileReputation data) {
        model.setSummaryScore(stringFromFloat(data.getPercentage()));
        model.setPositiveScore(String.valueOf(data.getPositive()));
        model.setNetralScore(String.valueOf(data.getNeutral()));
        model.setNegativeScore(String.valueOf(data.getNegative()));
    }

    private void setShopInfo(TopProfileViewModel model, ProfileShopInfo.Data data) {
        model.setShopId(data.getShopId());
        model.setShopName(data.getShopName());
        model.setGoldShop(data.getIsGold() == 1);
        model.setGoldBadge(data.isGoldBadge());
        model.setOfficialShop(data.getIsOfficial() == 1);
        model.setShopLocation(data.getLocation());
        model.setShopLogo(data.getLogo());
        model.setShopBadge(data.getReputationBadge());
        model.setShopBadgeLevel(data.getShopReputation().getBadgeLevel());
        model.setShopLastOnline(data.getLastOnline());
        model.setShopAppLink(data.getApplink());
        model.setFavorite(data.getIsFavorite());
    }

    private ProfileGraphql getDataOrError(Response<GraphqlResponse<ProfileGraphql>>
                                                  graphqlResponse) {
        if (graphqlResponse != null
                && graphqlResponse.body() != null
                && graphqlResponse.body().getData() != null) {
            if (graphqlResponse.isSuccessful()) {
                if (TextUtils.isEmpty(graphqlResponse.body().getData().getProfileData().getError
                        ())) {
                    return graphqlResponse.body().getData();
                } else {
                    throw new RuntimeException("Server error");
                }
            } else {
                throw new RuntimeException("Network call failed");
            }
        } else {
            throw new RuntimeException("Response is empty");
        }
    }

    private String stringFromFloat(float x) {
        if (x == (long) x)
            return String.valueOf((long) x);
        else
            return String.valueOf(x);
    }
}
