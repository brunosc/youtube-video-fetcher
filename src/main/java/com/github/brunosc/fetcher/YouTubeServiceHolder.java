package com.github.brunosc.fetcher;

import com.github.brunosc.fetcher.domain.YouTubeFetcherParams;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;

import static java.util.Collections.singletonList;

final class YouTubeServiceHolder {

    private static final String CREDENTIALS_DIRECTORY = ".oauth-credentials";
    private static final String DATASTORE_NAME = "credentialstore";

    private static final String APPLICATION_NAME = "YouTube Fetcher";

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final YouTube service;
    private final YouTubeFetcherParams params;

    private YouTubeServiceHolder(final YouTubeFetcherParams params) throws GeneralSecurityException, IOException {
        this.params = params;

        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(params.getClientSecretsIn(), httpTransport);
        this.service = new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    YouTube getService() {
        return this.service;
    }

    static YouTubeServiceHolder getInstance(final YouTubeFetcherParams localServerParams) throws GeneralSecurityException, IOException {
        return new YouTubeServiceHolder(localServerParams);
    }

    private Credential authorize(final InputStream clientSecretsIn, final NetHttpTransport httpTransport) throws IOException {
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow authFlow = buildAuthFlow(clientSecretsIn, httpTransport);

        return new AuthorizationCodeInstalledApp(authFlow, buildLocalReceiver()).authorize("user");
    }

    private GoogleAuthorizationCodeFlow buildAuthFlow(final InputStream clientSecretsIn, final NetHttpTransport httpTransport) throws IOException {
        // Load client secrets.
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(clientSecretsIn));

        return new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, singletonList(YouTubeScopes.YOUTUBE_READONLY))
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .setCredentialDataStore(buildDataStore())
                .build();
    }

    private LocalServerReceiver buildLocalReceiver() {
        return new LocalServerReceiver.Builder()
                .setPort(params.getPort())
                .setHost(params.getHost())
                .build();
    }

    private DataStore<StoredCredential> buildDataStore() throws IOException {
        FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new File(System.getProperty("user.home") + "/" + CREDENTIALS_DIRECTORY));
        return fileDataStoreFactory.getDataStore(DATASTORE_NAME);
    }

}
