package com.land.registry.auth.util;

import org.apache.commons.codec.binary.Base64;

public class ApiKey {

  public String createApiKey(final String uName, final String passWd) {
    final byte[] encodedBytes = (uName + "*" + passWd).getBytes();
    return Base64.encodeBase64String(encodedBytes);
  }

  public String decode(final String encodedApiKey) {
    final byte[] decoderBytes = Base64.decodeBase64(encodedApiKey);
    return new String(decoderBytes);
  }

}
