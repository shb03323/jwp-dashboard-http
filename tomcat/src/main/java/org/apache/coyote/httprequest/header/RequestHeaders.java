package org.apache.coyote.httprequest.header;

import org.apache.coyote.httprequest.exception.InvalidHeaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHeaders {

    private static final Logger log = LoggerFactory.getLogger(RequestHeaders.class);

    private static final String DELIMITER = ": ";
    private static final int PARSED_HEADER_SIZE = 2;
    private static final int HEADER_KEY_INDEX = 0;
    private static final int HEADER_VALUE_INDEX = 1;

    private final Map<RequestHeaderType, RequestHeader> headers;
    private final CookieRequestHeader cookieRequestHeader;

    private RequestHeaders(final Map<RequestHeaderType, RequestHeader> headers, final CookieRequestHeader cookieRequestHeader) {
        this.headers = headers;
        this.cookieRequestHeader = cookieRequestHeader;
    }

    public static RequestHeaders from(final BufferedReader bufferedReader) throws IOException {
        final Map<RequestHeaderType, RequestHeader> headers = new HashMap<>();
        CookieRequestHeader cookieHeader = CookieRequestHeader.blank();
        log.debug("Request Header:");
        while (true) {
            final String line = bufferedReader.readLine();
            if (line == null || line.isBlank()) {
                break;
            }
            final List<String> parsedHeader = parseByDelimiter(line);
            if (parsedHeader.get(HEADER_KEY_INDEX).equals("Cookie")) {
                cookieHeader = CookieRequestHeader.from(parsedHeader.get(HEADER_VALUE_INDEX));
            }
            final RequestHeaderType headerType = RequestHeaderType.from(parsedHeader.get(HEADER_KEY_INDEX));
            if (headerType.isUnsupportedHeader()) {
                continue;
            }
            log.debug("\t" + line);
            headers.put(headerType, headerType.saveRequestHeader(parsedHeader.get(HEADER_VALUE_INDEX).trim()));
        }
        return new RequestHeaders(headers, cookieHeader);
    }

    private static List<String> parseByDelimiter(final String line) {
        final List<String> parsedRequestHeader = List.of(line.split(DELIMITER));
        if (parsedRequestHeader.size() != PARSED_HEADER_SIZE) {
            throw new InvalidHeaderException();
        }
        return parsedRequestHeader;
    }

    public boolean hasJSessionId() {
        return cookieRequestHeader.hasJSessionId();
    }

    public int getContentLength() {
        if (headers.containsKey(RequestHeaderType.CONTENT_LENGTH)) {
            return Integer.parseInt(headers.get(RequestHeaderType.CONTENT_LENGTH).getValue());
        }
        return 0;

    }

    public CookieRequestHeader getCookieHeader() {
        return this.cookieRequestHeader;
    }
}
