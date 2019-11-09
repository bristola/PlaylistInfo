[![Build Status](https://travis-ci.com/bristola/spotify_playlist_creator.svg?branch=master)](https://travis-ci.com/bristola/spotify_playlist_creator)

# Spotify Advanced Queue

## By Austin Bristol

## About

Continuation of previous project

## Technology

For this project, a few different technologies were used:

- [Spring Boot](https://github.com/spring-projects/spring-boot):

    Used to create a website in java.

- [Spotify Web API](https://github.com/thelinmichael/spotify-web-api-java):

    Used to connect to Spotify and make requests.

## Requirements

1. Install [Java](https://gradle.org/releases/)

1. Install [Gradle](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

1. Create your own Spotify Client ID and Client Secret:

    1. First go to <https://developer.spotify.com/dashboard/>

    1. Click log in and use your Spotify account details.

    1. Create a new App.

    1. Once you are in the App's details, save both the ID and the Secret.

## How to run

1. ```git clone git@github.com:bristola/spotify_advanced_q.git```

1. ```cd spotify_advanced_q```

1. Create file called *spotify_settings.txt* in the following format:

    ```
    Client ID: <Your Client ID>
    Client Secret: <Your Client Secret>
    ```

1. ```gradle build```

1. ```java -jar build/libs/gs-spring-boot-0.1.0.jar```

1. Open <http://localhost:8080/> in a web browser
