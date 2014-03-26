package io.kare.suggest.fetch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kare.suggest.Logger;

import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author arshsab
 * @since 03 2014
 */

public class Fetcher {
    private static final ObjectMapper mapper = new ObjectMapper();

    private final int MAX_CONCURRENT_REQUESTS = (System.getProperty("suggest.fetch.Fetcher.max") == null) ?
            Integer.parseInt(System.getProperty("suggest.fetch.Fetcher.max")) :
            8;

    private final AtomicInteger dispatched = new AtomicInteger();
    private final AtomicBoolean error = new AtomicBoolean(false);
    private final String access;
    private final Http http = new Http();
    private final AtomicBoolean canProceed = new AtomicBoolean(true);
    private final AtomicBoolean searchCanProceed = new AtomicBoolean(true);

    public Fetcher(String access) {
        this.access = access;
    }

    // Fetches the URL. Blocks if necessary until API requests are available.
    public String fetch(String url) throws IOException {
        if (error.get()) {
            throw new IOException("500 Error has occurred. Stop the world!");
        }

        claimRequest();

        boolean isSearch = isSearchUrl(url);
        String fixed = prepUrl(url);

        if (isSearch) {
            waitOnSearch();
        } else {
            waitOnNormal();
        }

        Logger.debug("Fetching URL: " + url);

        String ret;
        try {
            ret = http.get(fixed);
        } catch (IOException ioe) {
            if (ioe.getMessage().contains("404")) {
                throw new FileNotFoundException();
            } else if (ioe.getMessage().contains("403")) {
                while (!(isSearch ? isSearchReady() : isNormalReady())) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                ret = fetch(url);
            } else {
                error.set(true);
                throw ioe;
            }
        } finally {
            relinquishClaim();
        }

        return ret;
    }

    private void claimRequest() {
        while (dispatched.getAndIncrement() > MAX_CONCURRENT_REQUESTS) {
            dispatched.getAndDecrement();
            try {
                dispatched.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void relinquishClaim() {
        dispatched.getAndDecrement();
        dispatched.notify();
    }

    private void waitOnNormal() {
        while (!canProceed.get()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void waitOnSearch() {
        while (!searchCanProceed.get()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isSearchReady() throws IOException {
        if (searchCanProceed.get()) {
            return true;
        }

        String rateLimit = http.get("https://api.github.com/rate_limit?" + access);

        JsonNode node = mapper.readTree(rateLimit);

        int value = node.path("resources")
                        .path("search")
                        .path("remaining").intValue();

        return value != 0;
    }

    private boolean isNormalReady() throws IOException {
        if (canProceed.get()) {
            return true;
        }

        String rateLimit = http.get("https://api.github.com/rate_limit?" + access);

        JsonNode node = mapper.readTree(rateLimit);

        int value = node.path("resources")
                .path("core")
                .path("remaining").intValue();

        return value != 0;
    }

    private boolean isSearchUrl(String url) {
        return url.startsWith("search") || url.startsWith("/search");
    }


    private String prepUrl(String url) {
        if (!url.startsWith("/")) {
            url = "/" + url;
        }

        if (!url.startsWith("https://api.github.com")) {
            url = "https://api.github.com" + url;
        }

        if (!url.contains(access)) {
            url += url.contains("?") ? "&" : "?";
            url += access;
        }

        return url;
    }


}
