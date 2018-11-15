package com.tokopedia.tkpdpdp.helper;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.domain.gql.ImageReviewGqlResponse;
import com.tokopedia.tkpdpdp.viewmodel.ImageReviewItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

public class GqlHelper {

    private static final String KEY_PRODUCT_ID = "productID";
    private static final String KEY_PAGE = "page";
    private static final String KEY_TOTAL = "total";

    public static void requestImageReview(Context context,
                                               int page,
                                               int total,
                                               int productId,
                                               GraphqlUseCase graphqlUseCase,
                                               Subscriber<GraphqlResponse> subscriber) {

        Map<String, Object> variables = new HashMap<>();
        variables.put(KEY_PRODUCT_ID, productId);
        variables.put(KEY_PAGE, page);
        variables.put(KEY_TOTAL, total);

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.gql_image_review), ImageReviewGqlResponse.class, variables);

        graphqlUseCase.clearRequest();
        graphqlUseCase.setRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }

    public static List<ImageReviewItem> convertToImageReviewItemList(ImageReviewGqlResponse gqlResponse) {
        Map<Integer, ImageReviewGqlResponse.Review> reviewMap = new HashMap<>();
        Map<Integer, ImageReviewGqlResponse.Image> imageMap = new HashMap<>();

        for (ImageReviewGqlResponse.Image image : gqlResponse.getDetail().getImages()) {
            imageMap.put(image.getImageAttachmentID(), image);
        }

        for (ImageReviewGqlResponse.Review review : gqlResponse.getDetail().getReviews()) {
            reviewMap.put(review.getReviewId(), review);
        }

        List<ImageReviewItem> imageReviewItems = new ArrayList<>();
        for (ImageReviewGqlResponse.Item item : gqlResponse.getList()) {
            ImageReviewGqlResponse.Image image = imageMap.get(item.getImageID());
            ImageReviewGqlResponse.Review review = reviewMap.get(item.getReviewID());

            ImageReviewItem imageReviewItem = new ImageReviewItem();
            imageReviewItem.setImageUrlLarge(image.getUriLarge());
            imageReviewItem.setImageUrlThumbnail(image.getUriThumbnail());
            imageReviewItem.setFormattedDate(review.getTimeFormat().getDateTimeFmt1());
            imageReviewItem.setRating(review.getRating());
            imageReviewItem.setReviewerName(review.getReviewer().getFullName());
            imageReviewItems.add(imageReviewItem);
        }

        return imageReviewItems;
    }
}
