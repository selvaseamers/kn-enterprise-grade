package kn.service.citylist.be.config.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static kn.service.citylist.be.constant.CommonConstant.*;

public class ApiLoggerInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LogManager.getLogger(ApiLoggerInterceptor.class.getName());


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {

        setThreadContextAndAttributes(httpServletRequest);

        LOGGER.info("ApiEntryLog uri: {}, method: {}, start-time: {}",
                httpServletRequest::getRequestURI, httpServletRequest::getMethod, () -> Instant.now().toString());
        return true;
    }

    private void setThreadContextAndAttributes(HttpServletRequest httpServletRequest) throws IOException {

        String traceId = getTraceId(httpServletRequest);
        ThreadContext.put(TRACEID, traceId);
        ThreadContext.put(XB3TRACEID, traceId);
        ThreadContext.put(REQUEST_METHOD, httpServletRequest.getMethod());
        ThreadContext.put(API, httpServletRequest.getRequestURI());
        httpServletRequest.setAttribute(REQUEST_ENTRY_TIME, System.currentTimeMillis());
        httpServletRequest.setAttribute(REQUEST_ENTRY_TIMESTAMP, Instant.now().toString());
        httpServletRequest.setAttribute(TRACEID, traceId);
    }


    private String getTraceId(HttpServletRequest httpServletRequest) {
        AtomicReference<String> traceId = new AtomicReference<>(httpServletRequest.getHeader(TRACEID));
        if (!StringUtils.hasText(traceId.get())) {
            traceId.set(UUID.randomUUID().toString());
            LOGGER.info("Application generated traceId: {}", traceId::get);
        }
        return traceId.get();
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        LOGGER.info("Invoked Post Handle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object handler, Exception ex) {
        String requestExitTime = Instant.now().toString();
        String duration = (System.currentTimeMillis() - (long) httpServletRequest.getAttribute(REQUEST_ENTRY_TIME))
                + "ms";
        String requestEntryTime = (String) httpServletRequest.getAttribute(REQUEST_ENTRY_TIMESTAMP);
        String requestURI = httpServletRequest.getRequestURI();
        String httpMethod = httpServletRequest.getMethod();
        Integer httpStatusCode = httpServletResponse.getStatus();
        Map<String, String> responseHeaders = getResponseHeadersInfo(httpServletResponse);
        LOGGER.info("ApiExitLog uri: {} method: {}, status code: {}, start-time: {}, end-time: {}, duration: {}, http-headers: {}",
                () -> requestURI, () -> httpMethod, () -> httpStatusCode, () -> requestEntryTime, () -> requestExitTime, () -> duration, () -> responseHeaders);
        ThreadContext.clearAll();
    }

    private static Map<String, String> getResponseHeadersInfo(HttpServletResponse httpServletResponse) {
        Map<String, String> responseMap = new HashMap<>();
        httpServletResponse.getHeaderNames().forEach(key -> responseMap.put(key, httpServletResponse.getHeader(key)));
        return responseMap;
    }
}