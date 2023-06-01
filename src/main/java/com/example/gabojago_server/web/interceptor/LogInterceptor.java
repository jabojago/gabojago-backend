package com.example.gabojago_server.web.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;
        MDC.put("ID", UUID.randomUUID().toString());
        writeLogAboutBody(
                new String(cachingRequest.getContentAsByteArray()),
                new String(cachingResponse.getContentAsByteArray()));
        if (hasException(ex)) writeLogAboutHeader(request, response);
        MDC.clear();
    }

    private void writeLogAboutBody(String requestBody, String responseBody) throws Exception {
        log.info(
                "RequestBody : [{}] / ResponseBody : [{}]",
                objectMapper.readTree(requestBody),
                objectMapper.readTree(responseBody)
        );
    }

    private boolean hasException(Exception ex) {
        return ex != null;
    }

    private void writeLogAboutHeader(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.error(
                "Request Header : [{}]",
                objectMapper.writeValueAsString(LogHeader.from(request))
        );
    }

    @Getter
    static class LogHeader {
        private String contentType = "default";
        private Map<String, String> headers = new HashMap<>();
        private String uri = "default";

        public static LogHeader from(HttpServletRequest request) {
            LogHeader logHeader = new LogHeader();
            logHeader.contentType = request.getContentType();
            logHeader.uri = request.getRequestURI();
            logHeader.headers = new HashMap<>();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                logHeader.headers.put(headerName, request.getHeader(headerName));
            }
            return logHeader;
        }

    }


}
