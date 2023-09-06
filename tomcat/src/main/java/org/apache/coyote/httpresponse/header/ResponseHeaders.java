package org.apache.coyote.httpresponse.header;

import org.apache.coyote.httpresponse.ContentBody;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseHeaders {

    private final Map<ResponseHeaderType, ResponseHeader> headers;

    private ResponseHeaders(Map<ResponseHeaderType, ResponseHeader> headers) {
        this.headers = headers;
    }

    public static ResponseHeaders blank() {
        return new ResponseHeaders(new EnumMap<>(ResponseHeaderType.class));
    }

    public static ResponseHeaders of(final String path, final ContentBody contentBody) {
        final Map<ResponseHeaderType, ResponseHeader> headers = new LinkedHashMap<>();
        headers.put(ResponseHeaderType.CONTENT_TYPE, ContentTypeHeader.from(path));
        headers.put(ResponseHeaderType.CONTENT_LENGTH, new ContentLengthHeader(contentBody));
        return new ResponseHeaders(headers);
    }

    public void setLocationHeader(final String path) {
        headers.put(ResponseHeaderType.LOCATION, new LocationHeader(path));
    }

    public String getFormattedHeaders() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<ResponseHeaderType, ResponseHeader> header : headers.entrySet()) {
            final ResponseHeaderType responseHeaderType = header.getKey();
            final ResponseHeader responseHeader = header.getValue();
            stringBuilder.append(responseHeader.getKeyAndValue(responseHeaderType)).append(" \r\n");
        }
        return stringBuilder.toString();
    }
}
