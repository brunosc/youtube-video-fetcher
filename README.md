# youtube-video-fetcher

[![](https://jitpack.io/v/brunosc/youtube-video-fetcher.svg)](https://jitpack.io/#brunosc/youtube-video-fetcher)

A simple way to fetch the latest videos from a YouTube channel using Java.

## How to Use It

``` bash

InputStream clientSecretsIn = MyApp.class.getResourceAsStream("your_json_path_in_resources_folder");
YouTubeFetcher fetcher = new YouTubeFetcher(clientSecretsIn);

// By playlist id
List<VideoDetails> list = fetcher.fetchByPlaylistId("your_playlist_id");

// Or by channel id
List<VideoDetails> list = fetcher.fetchByChannelId("your_channel_id");

// By default, it returns the 5 latest. It is possible to return more than that using a second parameter
List<VideoDetails> list = fetcher.fetchByChannelId("your_channel_id", 20);

```

## Dependency

### Maven

``` bash
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.brunosc.fetcher</groupId>
        <artifactId>youtube-video-fetcher</artifactId>
        <version>0.0.1</version>
    </dependency>
</dependencies>
```

### Gradle

``` bash
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

<dependencies>
    <dependency>
        <groupId>com.github.brunosc.fetcher</groupId>
        <artifactId>youtube-video-fetcher</artifactId>
        <version>0.0.1</version>
    </dependency>
</dependencies>

dependencies {
  implementation 'com.github.brunosc.fetcher:youtube-video-fetcher:0.0.1'
}
```

## Getting the Client Secret JSON

Go to [Credentials](https://console.developers.google.com/apis/credentials) webpage.
<br>There will be a table called **OAuth 2.0 Client IDs**
<br>Click on the Download icon at the last column
<br>There it is!

## Getting the Playlist ID

You have to call the channels API:

https://www.googleapis.com/youtube/v3/channels?id={CHANNEL_ID}&key={YOUR_API_KEY}&part=contentDetails

OR 

https://developers.google.com/youtube/v3/docs/channels/list

The playlist id is: `response.items.get(0).contentDetails.uploads`

``` bash
{
  "kind": "youtube#channelListResponse",
  "etag": "e_tag_value",
  "pageInfo": {
    "totalResults": 1,
    "resultsPerPage": 5
  },
  "items": [
    {
      "kind": "youtube#channel",
      "etag": "e_tag_value",
      "id": "CHANNEL_ID",
      "contentDetails": {
        "relatedPlaylists": {
          "likes": "",
          "favorites": "",
          "uploads": "UPLOAD_ID"
        }
      }
    }
  ]
}
```

## Domain

``` bash
public class VideoDetails {
    private final String id;
    private final String url;
    private final String title;
    private final String description;
    private final VideoThumbnails thumbnails;
    private final LocalDateTime publishedAt; (GMT TimeZone)    
}

public class VideoThumbnails {
    private final ThumbnailItem defaultThumbnail;
    private final ThumbnailItem high;
    private final ThumbnailItem maxres;
    private final ThumbnailItem medium;
    private final ThumbnailItem standard;
}

public static class VideoThumbnails.ThumbnailItem {
    private final int height;
    private final int width;
    private final String url;
}
```