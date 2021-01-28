package com.github.brunosc.fetcher.domain;

import java.io.InputStream;

public final class YouTubeFetcherParams {

    private final String host;
    private final int port;
    private final InputStream clientSecretsIn;

    private YouTubeFetcherParams(String host, int port, InputStream clientSecretsIn) {
        this.host = host;
        this.port = port;
        this.clientSecretsIn = clientSecretsIn;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public InputStream getClientSecretsIn() {
        return clientSecretsIn;
    }

    public static final class Builder {
        private String host = "localhost";
        private int port = -1;
        private final InputStream clientSecretsIn;

        public Builder(InputStream clientSecretsIn) {
            this.clientSecretsIn = clientSecretsIn;
        }

        public YouTubeFetcherParams build() {
            return new YouTubeFetcherParams(host, port, clientSecretsIn);
        }

        public YouTubeFetcherParams.Builder withHost(String host) {
            this.host = host;
            return this;
        }

        public YouTubeFetcherParams.Builder withPort(int port) {
            this.port = port;
            return this;
        }
    }
}
