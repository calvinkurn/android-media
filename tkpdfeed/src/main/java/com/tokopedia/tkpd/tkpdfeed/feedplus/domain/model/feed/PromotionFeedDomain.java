package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed;

import javax.annotation.Nonnull;

/**
 * @author ricoharisin .
 */

public class PromotionFeedDomain {

    private final @Nonnull
    String id;

    private final @Nonnull String name;

    private final @Nonnull String type;

    private final @Nonnull String thumbnail;

    private final @Nonnull String featureImage;

    private final @Nonnull String description;

    private final @Nonnull String periode;

    private final @Nonnull String code;

    private final @Nonnull String url;

    private final @Nonnull String minTranscation;

    public PromotionFeedDomain(@Nonnull String id, @Nonnull String name, @Nonnull String type,
                               @Nonnull String thumbnail, @Nonnull String feature_image, @Nonnull String description,
                               @Nonnull String periode, @Nonnull String code, @Nonnull String url,
                               @Nonnull String min_transcation) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.thumbnail = thumbnail;
        this.featureImage = feature_image;
        this.description = description;
        this.periode = periode;
        this.code = code;
        this.url = url;
        this.minTranscation = min_transcation;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public String getType() {
        return type;
    }

    @Nonnull
    public String getThumbnail() {
        return thumbnail;
    }

    @Nonnull
    public String getFeatureImage() {
        return featureImage;
    }

    @Nonnull
    public String getDescription() {
        return description;
    }

    @Nonnull
    public String getPeriode() {
        return periode;
    }

    @Nonnull
    public String getCode() {
        return code;
    }

    @Nonnull
    public String getUrl() {
        return url;
    }

    @Nonnull
    public String getMinTranscation() {
        return minTranscation;
    }

    @Nonnull
    public String getId() {
        return id;
    }
}
