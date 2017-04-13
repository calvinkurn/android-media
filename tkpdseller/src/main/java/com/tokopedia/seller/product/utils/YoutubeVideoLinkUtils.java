package com.tokopedia.seller.product.utils;

import android.content.Context;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.util.Pair;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.data.source.cloud.model.youtube.ContentRating;
import com.tokopedia.seller.product.data.source.cloud.model.youtube.YoutubeResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author normansyahputa on 4/11/17.
 *
 * no one should override this, this is just util class.
 */
public final class YoutubeVideoLinkUtils {
    public static final int VIDEO_ID_INDEX = 1;
    public static final String YOUTUBE_API_KEY = "AIzaSyADrnEdJGwsVM1Z6uWWnWAgZZf1sSfnIVQ";
    private static final String TAG = "YoutubeVideoLinkUtils";
    private static final String YOUTUBE_REGEX = "^(?:https?:\\/\\/)?(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
    private static final String YT_AGE_RESTRICTED = "ytAgeRestricted";
    private final Pattern compiledPattern = Pattern.compile(YOUTUBE_REGEX, Pattern.CASE_INSENSITIVE);
    private String youtubeUrl;
    private String videoId;
    private String videoNotFound, invalidVideoUrl;
    private String videoAdultWarn;

    public YoutubeVideoLinkUtils(Context context, String youtubeUrl) {
        this(youtubeUrl);
        fillExceptionString(context);
    }

    public YoutubeVideoLinkUtils(String youtubeUrl) {
        setYoutubeUrl(youtubeUrl);
    }

    public YoutubeVideoLinkUtils() {
    }

    public void setYoutubeUrl(String youtubeUrl) {
        if (youtubeUrl == null
                || youtubeUrl.length() == 0
                ) {
            throw new IllegalArgumentException();
        }

        this.youtubeUrl = youtubeUrl.trim();
    }

    public synchronized void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    private Pair<Boolean, String> isValidYoutubeUrl() {
        Matcher matcher = compiledPattern.matcher(youtubeUrl);
        if (matcher.find()) {
            return new Pair<>(true, matcher.group(VIDEO_ID_INDEX));
        } else {
            return new Pair<>(false, invalidVideoUrl);
        }
    }

    public String saveVideoID() {
        Pair<Boolean, String> validYoutubeUrl = isValidYoutubeUrl();
        if (validYoutubeUrl.getModel1()) {
            return videoId = validYoutubeUrl.getModel2();
        } else {
            throw new IllegalArgumentException(validYoutubeUrl.getModel2());
        }
    }

    public void fillExceptionString(Context context) {
        videoNotFound = context.getString(R.string.video_not_found);
        invalidVideoUrl = context.getString(R.string.invalid_video_url);
        videoNotFound = context.getString(R.string.video_not_found);
        videoAdultWarn = context.getString(R.string.video_adult_warn);
    }

    public synchronized void checkIfVideoExists(YoutubeResponse youtubeResponse) {
        if (!CommonUtils.checkNotNull(youtubeResponse))
            return;

        if (youtubeResponse.getItems().isEmpty())
            throw new RuntimeException(videoNotFound);
    }

    public synchronized void checkIfVideoNotAgeRestricted(YoutubeResponse youtubeResponse) {
        if (!CommonUtils.checkNotNull(youtubeResponse))
            return;

        ContentRating contentRating = youtubeResponse.getItems().get(0).getContentDetails().getContentRating();
        if (contentRating != null && contentRating.getYtRating().equals(YT_AGE_RESTRICTED)) {
            throw new RuntimeException(videoAdultWarn);
        }
    }


}
