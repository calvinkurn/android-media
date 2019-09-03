package com.tokopedia.inbox.rescenter.utils;

import android.content.Context;

import com.tokopedia.core.database.model.ResCenterAttachment;
import com.tokopedia.core.database.repository.ResCenterAttachmentRepository;

import java.util.List;

public class LocalCacheManager {

    private static String TAG = LocalCacheManager.class.getSimpleName();

    public static class AttachmentEditResCenter {

        private String resolutionId;
        private String imageLocalPath;
        private String imageUrl;
        private ResCenterAttachmentRepository resCenterRepository;

        public AttachmentEditResCenter(Context ctx) {
            resCenterRepository = new ResCenterAttachmentRepository(ctx);
        }

        public static AttachmentEditResCenter Builder(Context ctx, String resolutionID) {
            AttachmentEditResCenter foo = new AttachmentEditResCenter(ctx);
            foo.resolutionId = resolutionID;
            return foo;
        }

        public String getImageLocalPath() {
            return imageLocalPath;
        }

        public AttachmentEditResCenter setImageLocalPath(String imageLocalPath) {
            this.imageLocalPath = imageLocalPath;
            return this;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public AttachmentEditResCenter setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public void save() {
            ResCenterAttachment db = new ResCenterAttachment();
            db.setResolutionId(resolutionId);
            db.setImagePath(getImageLocalPath());
            db.setImageUrl(getImageUrl());
            db.setModuleName(ResCenterAttachment.MODULE_EDIT_RESCENTER);

            resCenterRepository.insertAttachment(db);
        }

        public List<ResCenterAttachment> getCache() {
            return resCenterRepository.getAttachmentListByResIdModuleName(resolutionId, ResCenterAttachment.MODULE_EDIT_RESCENTER);
        }

        public void clearAll() {
            resCenterRepository.deleteAttachments(getCache());
        }

        public void remove(ResCenterAttachment attachmentReplyResCenterDB) {
            resCenterRepository.deleteAttachmentById(attachmentReplyResCenterDB.getId());
        }
    }

    public static class AttachmentShippingResCenter {

        private String resolutionId;
        private String imageLocalPath;
        private String imageUrl;
        private String imageUUID;
        private ResCenterAttachmentRepository resCenterRepository;

        public AttachmentShippingResCenter(Context ctx) {
            resCenterRepository = new ResCenterAttachmentRepository(ctx);
        }

        public static AttachmentShippingResCenter Builder(Context ctx, String resolutionID) {
            AttachmentShippingResCenter foo = new AttachmentShippingResCenter(ctx);
            foo.resolutionId = resolutionID;
            return foo;
        }

        public String getImageLocalPath() {
            return imageLocalPath;
        }

        public AttachmentShippingResCenter setImageLocalPath(String imageLocalPath) {
            this.imageLocalPath = imageLocalPath;
            return this;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public AttachmentShippingResCenter setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public String getImageUUID() {
            return imageUUID;
        }

        public AttachmentShippingResCenter setImageUUID(String imageUUID) {
            this.imageUUID = imageUUID;
            return this;
        }

        public void save() {
            ResCenterAttachment db = new ResCenterAttachment();
            db.setResolutionId(resolutionId);
            db.setImagePath(getImageLocalPath());
            db.setImageUrl(getImageUrl());
            db.setImageUuid(getImageUUID());
            db.setModuleName(ResCenterAttachment.MODULE_SHIPPING_RESCENTER);

            resCenterRepository.insertAttachment(db);
        }

        public List<ResCenterAttachment> getCache() {
            return resCenterRepository.getAttachmentListByResIdModuleName(resolutionId, ResCenterAttachment.MODULE_SHIPPING_RESCENTER);
        }

        public void clearAll() {
            resCenterRepository.deleteAttachments(getCache());
        }

        public void remove(ResCenterAttachment attachmentReplyResCenterDB) {
            resCenterRepository.deleteAttachmentById(attachmentReplyResCenterDB.getId());
        }
    }
}