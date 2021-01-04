package com.github.brunosc.fetcher;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collection;

import static java.util.Collections.singletonList;

final class YouTubeServiceHolder {

    private static final String APPLICATION_NAME = "YouTube Fetcher";
    private static final Collection<String> SCOPES = singletonList("https://www.googleapis.com/auth/youtube " +
            "https://www.googleapis.com/auth/youtube.force-ssl " +
            "https://www.googleapis.com/auth/youtube.readonly " +
            "https://www.googleapis.com/auth/youtubepartner " +
            "https://www.googleapis.com/auth/youtubepartner-channel-audit");

    private static YouTubeServiceHolder INSTANCE;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final YouTube service;

    private YouTubeServiceHolder(final InputStream clientSecretsIn) throws GeneralSecurityException, IOException {
        final var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        final var credential = authorize(clientSecretsIn, httpTransport);

        this.service = new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    YouTube getService() {
        return this.service;
    }

    static YouTubeServiceHolder getInstance(final InputStream clientSecretsIn) throws GeneralSecurityException, IOException {
        if (INSTANCE == null) {
            INSTANCE = new YouTubeServiceHolder(clientSecretsIn);
        }
        return INSTANCE;
    }

    private Credential authorize(final InputStream clientSecretsIn, final NetHttpTransport httpTransport) throws IOException {
        // Load client secrets.
        final var clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(clientSecretsIn));

        // Build flow and trigger user authorization request.
        final var authFlow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .build();

        return new AuthorizationCodeInstalledApp(authFlow, new LocalServerReceiver()).authorize("user");
    }

}
