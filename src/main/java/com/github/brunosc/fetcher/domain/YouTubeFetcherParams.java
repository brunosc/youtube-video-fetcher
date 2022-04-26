package com.github.brunosc.fetcher.domain;

import java.io.InputStream;

public final class YouTubeFetcherParams {

    private final InputStream clientSecretsIn;

    private YouTubeFetcherParams(InputStream clientSecretsIn) {
        this.clientSecretsIn = clientSecretsIn;
    }

    public InputStream getClientSecretsIn() {
        return clientSecretsIn;
    }

    public static final class Builder {
        private final InputStream clientSecretsIn;

        public Builder(InputStream clientSecretsIn) {
            this.clientSecretsIn = clientSecretsIn;
        }

        public YouTubeFetcherParams build() {
            return new YouTubeFetcherParams(clientSecretsIn);
        }
    }
}
