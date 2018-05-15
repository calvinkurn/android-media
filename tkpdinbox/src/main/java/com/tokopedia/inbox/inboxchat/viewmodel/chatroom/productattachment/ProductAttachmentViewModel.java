package com.tokopedia.inbox.inboxchat.viewmodel.chatroom.productattachment;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;

/**
 * @author by nisie on 5/14/18.
 */
public class ProductAttachmentViewModel extends BaseChatViewModel implements Visitable<ChatRoomTypeFactory> {

    private boolean isRead;
    private boolean isDummy;
    private Integer productId;
    private String productName;
    private String productPrice;
    private Long dateTimeInMilis;
    private String productUrl;
    private String productImage;
    private boolean isSender;

    /**
     * Constructor for WebSocket. Only for product attachment coming from opposite user (not self
     * send).
     *
     * @param messageId       message Id
     * @param fromUid         user id of sender
     * @param from            username of sender
     * @param fromRole        role of sender
     * @param attachmentId    attachment id
     * @param attachmentType  attachment type. Please refer to
     *                        {@link com.tokopedia.inbox.inboxchat.domain.WebSocketMapper} types
     * @param replyTime       replytime in unixtime
     * @param productId       product id
     * @param productName     product name
     * @param productPrice    product price
     * @param productUrl      product url
     * @param productImage    product image url
     */
    public ProductAttachmentViewModel(String messageId, String fromUid,
                                      String from, String fromRole,
                                      String attachmentId, String attachmentType,
                                      String replyTime, Integer productId,
                                      String productName, String productPrice,
                                      String productUrl, String productImage,
                                      boolean isSender) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime);
        this.isRead = false;
        this.isDummy = false;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.dateTimeInMilis = Long.parseLong(replyTime);
        this.productUrl = productUrl;
        this.productImage = productImage;
        this.isSender = isSender;
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public boolean isReadStatus() {
        return isRead;
    }

    public void setReadStatus(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isDummy() {
        return isDummy;
    }

    public void setDummy(boolean isDummy) {
        this.isDummy = isDummy;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public Long getDateTimeInMilis() {
        return dateTimeInMilis;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getProductImage() {
        return productImage;
    }

    public boolean isSender() {
        return isSender;
    }

}
