package com.github.brunosc.fetcher;

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
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collection;

import static java.util.Collections.singletonList;

final class YouTubeServiceHolder {

    private static final int DEFAULT_LOCAL_SERVER_PORT = 8095;
    private static final String CREDENTIALS_DIRECTORY = ".oauth-credentials";
    private static final String DATASTORE_NAME = "credentialstore";

    private static final String APPLICATION_NAME = "YouTube Fetcher";
    private static final Collection<String> SCOPES = singletonList("https://www.googleapis.com/auth/youtube " +
            "https://www.googleapis.com/auth/youtube.force-ssl " +
            "https://www.googleapis.com/auth/youtube.readonly " +
            "https://www.googleapis.com/auth/youtubepartner " +
            "https://www.googleapis.com/auth/youtubepartner-channel-audit");

    private static YouTubeServiceHolder INSTANCE;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final YouTube service;
    private final String localServerHost;

    private YouTubeServiceHolder(final InputStream clientSecretsIn, final String localServerHost) throws GeneralSecurityException, IOException {
        this.localServerHost = localServerHost;

        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(clientSecretsIn, httpTransport);
        this.service = new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    YouTube getService() {
        return this.service;
    }

    static YouTubeServiceHolder getInstance(final InputStream clientSecretsIn, final String localServerHost) throws GeneralSecurityException, IOException {
        if (INSTANCE == null) {
            INSTANCE = new YouTubeServiceHolder(clientSecretsIn, localServerHost);
        }
        return INSTANCE;
    }

    private Credential authorize(final InputStream clientSecretsIn, final NetHttpTransport httpTransport) throws IOException {

        // Load client secrets.
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(clientSecretsIn));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow authFlow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setCredentialDataStore(buildDataStore())
                .build();

        // Build the local server and bind it to port 8095
        LocalServerReceiver localReceiver = new LocalServerReceiver.Builder()
                .setPort(DEFAULT_LOCAL_SERVER_PORT)
                .setHost(this.localServerHost)
                .build();

        return new AuthorizationCodeInstalledApp(authFlow, localReceiver).authorize("user");
    }

    private DataStore<StoredCredential> buildDataStore() throws IOException {
        FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new File(System.getProperty("user.home") + "/" + CREDENTIALS_DIRECTORY));
        return fileDataStoreFactory.getDataStore(DATASTORE_NAME);
    }

}
