package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.util.Base64;
import android.util.Log;

import com.aliyuncs.RoaAcsRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.MethodType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sachinbansal on 3/8/18.
 */

public class SearchItemRequestLocal extends RoaAcsRequest<NewImageSearchResponse> {
    private static final int MAX_POST_CONTENT_LENGTH = 8388608;
    private int start;
    private int num;
    private String catId;
    private byte[] searchPicture;
    private String instanceName;

    public SearchItemRequestLocal() {
        super("ImageSearch", "2018-01-20", "SearchItem");
        this.setUriPattern("/item/search");
        this.setMethod(MethodType.POST);
        this.start = 0;
        this.num = 100;
        this.catId = "";
    }

    public String getInstanceName() {
        return this.instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
        if (instanceName != null) {
            this.putQueryParameter("instanceName", instanceName);
        }

    }

    public int getStart() {
        return this.start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getCatId() {
        return this.catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public byte[] getSearchPicture() {
        return this.searchPicture;
    }

    public void setSearchPicture(byte[] searchPicture) {
        this.searchPicture = searchPicture;
    }

    public boolean buildPostContent() {
        if (this.searchPicture != null && this.searchPicture.length != 0) {
            Map<String, String> kv = new HashMap();
            kv.put("s", String.valueOf(this.start));
            kv.put("n", String.valueOf(this.num));
            if (this.catId != null && this.catId.length() > 0) {
                kv.put("cat_id", this.catId);
            }

            String encodePicName = Base64.encodeToString("searchPic".getBytes(),
                    Base64.NO_WRAP | Base64.NO_CLOSE);
            String encodePicContent = Base64.encodeToString(this.searchPicture,
                    Base64.NO_WRAP | Base64.NO_CLOSE);
            kv.put("pic_list", encodePicName);
            kv.put(encodePicName, encodePicContent);
            String content = buildContent(kv);

            Log.e("Request String:", content);

            if (content.length() > 8388608) {
                return false;
            } else {
                this.putHeaderParameter("Accept-Encoding", "");
                this.setHttpContent(content.getBytes(), "UTF-8", FormatType.RAW);
                this.setAcceptFormat(FormatType.JSON);
                return true;
            }
        } else {
            return false;
        }
    }

    private static String buildContent(Map<String, String> kv) {
        String meta = "";
        String body = "";
        int start = 0;

        String value;
        for (Iterator i$ = kv.entrySet().iterator(); i$.hasNext(); start += value.length()) {
            Map.Entry<String, String> entry = (Map.Entry) i$.next();
            value = (String) entry.getValue();
            if (meta.length() > 0) {
                meta = meta + "#";
            }

            meta = meta + (String) entry.getKey() + "," + start + "," + (start + value.length());
            body = body + value;
        }

        return meta + "^" + body;
    }

    public Class<NewImageSearchResponse> getResponseClass() {
        return NewImageSearchResponse.class;
    }
}

