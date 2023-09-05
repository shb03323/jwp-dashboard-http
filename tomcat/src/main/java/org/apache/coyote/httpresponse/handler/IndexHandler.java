package org.apache.coyote.httpresponse.handler;

import org.apache.coyote.httprequest.HttpRequest;
import org.apache.coyote.httpresponse.HttpResponse;
import org.apache.coyote.httpresponse.HttpStatus;

public class IndexHandler implements Handler {

    @Override
    public HttpResponse handle(final HttpRequest request) {
        return HttpResponse
                .init(request.getHttpVersion())
                .setHttpStatus(HttpStatus.OK)
                .setContent(request.getPath());
    }
}
