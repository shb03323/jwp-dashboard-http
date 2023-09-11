package org.apache.coyote.controller;

import org.apache.coyote.httprequest.HttpRequest;
import org.apache.coyote.httpresponse.HttpResponse;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class IndexControllerTest extends ControllerTestSupport {

    @Test
    void index_페이지를_보여준다() {
        // given
        final String input = String.join("\r\n",
                "GET /index.html HTTP/1.1",
                "Host: localhost:8080",
                "Connection: keep-alive",
                "Accept: */*");
        final HttpRequest httpRequest = super.makeHttpRequest(input);
        final HttpResponse httpResponse = HttpResponse.init(httpRequest.getHttpVersion());
        final IndexController indexController = new IndexController();

        // when
        indexController.service(httpRequest, httpResponse);
        final String actual = super.bytesToText(httpResponse.getBytes());
        final Set<String> expectedHeaders = Set.of(
                "HTTP/1.1 200 OK",
                "Content-Type: text/html;charset=utf-8"
        );

        // then
        assertThat(actual).contains(expectedHeaders);
    }
}
