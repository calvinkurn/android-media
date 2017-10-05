package com.tokopedia.topads.sdk.network;


/**
 * @author by errysuprayogi on 3/29/17.
 */

public interface HttpResponseConverter <T>{
  public T convertResponse(String httpResponse);
}
