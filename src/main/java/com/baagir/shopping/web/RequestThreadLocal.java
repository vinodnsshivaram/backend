package com.baagir.shopping.web;

import java.util.HashMap;
import java.util.Map;

public class RequestThreadLocal {

    private static final ThreadLocal<Map<String, String>> threadLocalStore = ThreadLocal.withInitial(() -> new HashMap<>());

    public static void setCorrelationId(String correlationId) {
        threadLocalStore.get().put("correlation-id", correlationId);
    }

    public static String getCorrelationId() {
        return threadLocalStore.get().get("correlation-id");
    }

    public static void setRequestUrl(String url) {
        threadLocalStore.get().put("REQUEST_URL", url);
    }

    public static String getRequestUrl() {
        return threadLocalStore.get().get("REQUEST_URL");
    }

    public static void setRequestQueryString(String queryString) {
        threadLocalStore.get().put("REQUEST_QUERY_STRING", (queryString == null) ? "" : queryString);
    }

    public static String getRequestQueryString() {
        return threadLocalStore.get().get("REQUEST_QUERY_STRING");
    }
}
