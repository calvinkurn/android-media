package com.tokopedia.inbox.inboxticket.model;


import com.tokopedia.core.customadapter.ImageUpload;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nisie on 4/19/16.
 */
public class InboxTicketParam {

    private static final String PARAM_FILTER = "filter";
    private static final String PARAM_KEYWORD = "keyword";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_STATUS = "status";
    private static final String PARAM_RATING = "rating";

    private static final String PARAM_TICKET_INBOX_ID = "ticket_inbox_id";

    private static final String PARAM_RATE = "rate";
    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_TICKET_ID = "ticket_id";
    private static final String PARAM_COMMENT_ID = "comment_id";
    private static final String PARAM_FILE_UPLOADED = "file_uploaded";
    private static final String PARAM_REPLY_MESSAGE = "ticket_reply_message";
    private static final String PARAM_HAS_PHOTO = "p_photo";
    private static final String PARAM_PHOTO_ALL = "p_photo_all";
    private static final String PARAM_PHOTO_OBJ = "prd_pic_obj";
    private static final String PARAM_ID = "image_id";
    private static final String PARAM_POST_KEY = "post_key";
    private static final String PARAM_IS_TEMP = "is_temp";

    public final static String YES_HELPFUL = "YES";
    public final static String NO_HELPFUL = "NO";

    private String rating;
    private String commentId;
    private String userId;
    private String filter;
    private String keyword;
    private String page;
    private String status;
    private String inboxId;

    private String rate;
    private String ticketId;
    private String fileUploaded;

    private String replyMessage;
    List<ImageUpload> imageUploads;

    private String postKey;
    private GeneratedHost generatedHost;

    public InboxTicketParam() {

    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInboxId() {
        return inboxId;
    }

    public void setInboxId(String inboxId) {
        this.inboxId = inboxId;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public List<ImageUpload> getImageUploads() {
        return imageUploads;
    }

    public void setImageUploads(List<ImageUpload> imageUploads) {
        this.imageUploads = imageUploads;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setGeneratedHost(GeneratedHost generatedHost) {
        this.generatedHost = generatedHost;
    }

    public GeneratedHost getGeneratedHost() {
        return generatedHost;
    }

    public Map<String, String> getInboxTicketParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_FILTER, getFilter());
        param.put(PARAM_KEYWORD, getKeyword());
        param.put(PARAM_PAGE, getPage());
        param.put(PARAM_STATUS, getStatus());
        return param;
    }

    public Map<String, String> getInboxTicketDetailParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_TICKET_INBOX_ID, getInboxId());
        return param;
    }

    public Map<String, String> getParamSetRating() {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_RATE, getRate());
        param.put(PARAM_TICKET_ID, getTicketId());
        param.put(PARAM_FILE_UPLOADED, getFileUploaded());
        return param;
    }

    public Map<String, String> getViewMoreParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_TICKET_ID, getTicketId());
        param.put(PARAM_PAGE, getPage());
        return param;
    }

    public Map<String, String> getCommentRatingParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_COMMENT_ID, getCommentId());
        param.put(PARAM_RATING, getRating());
        param.put(PARAM_USER_ID, getUserId());
        return param;
    }

    public Map<String, String> getReplyTicketParamValidation() {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_TICKET_ID, getTicketId());
        param.put(PARAM_REPLY_MESSAGE, getReplyMessage());
        if (getImageUploads() != null && getImageUploads().size() > 0) {
            param.put(PARAM_HAS_PHOTO, "1");
            param.put(PARAM_PHOTO_ALL, getReviewPhotos());
            param.put(PARAM_PHOTO_OBJ, getReviewPhotosObj());
        }
        return param;
    }

    private String getReviewPhotos() {
        String allPhoto = "";
        for (int i = 0; i < imageUploads.size(); i++) {
            if (i == imageUploads.size() - 1) {
                allPhoto += imageUploads.get(i).getImageId();
            } else {
                allPhoto += imageUploads.get(i).getImageId() + "~";
            }
        }
        return allPhoto;
    }

    private String getReviewPhotosObj() {

        JSONObject reviewPhotos = new JSONObject();
        try {
            for (ImageUpload image : imageUploads) {
                JSONObject photoObj = new JSONObject();
                photoObj.put(PARAM_ID, image.getImageId());
                photoObj.put(PARAM_IS_TEMP, "1");
                reviewPhotos.put(image.getImageId(), photoObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviewPhotos.toString();
    }

    private String getFileUploaded() {
        JSONObject reviewPhotos = new JSONObject();
        try {
            for (ImageUpload image : imageUploads) {
                reviewPhotos.put(image.getImageId(), image.getPicObj());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviewPhotos.toString();
    }

    public Map<String, String> getReplyTicketParamSubmit() {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_TICKET_ID, getTicketId());
        param.put(PARAM_FILE_UPLOADED, getFileUploaded());
        param.put(PARAM_POST_KEY, getPostKey());
        return param;
    }
}
