USE [SpotifyInfo];
GRANT INSERT TO SpotifyRestUser;
GRANT SELECT TO SpotifyRestUser;
GRANT DELETE TO SpotifyRestUser;
GRANT UPDATE TO SpotifyRestUser;
GO

CREATE TABLE [SpotifyInfo].[dbo].[aggregate_playlist] (
	spotify_id VARCHAR(100) PRIMARY KEY,
	current_username VARCHAR(100),
	name VARCHAR(100),
	created_by VARCHAR(100),
	description VARCHAR(100),
	spotify_url VARCHAR(60),
	image_url VARCHAR(200),
	followers INT,
	is_collaborative BIT,
	is_public BIT
);

CREATE TABLE [SpotifyInfo].[dbo].[song] (
	spotify_id VARCHAR(100) PRIMARY KEY,
	name VARCHAR(100),
	album VARCHAR(100),
	album_url VARCHAR(60),
	album_cover_url VARCHAR(200),
	release_date VARCHAR(60),
	explicit BIT,
	popularity INT,
	duration INT,
	artists VARCHAR(200),
	genres VARCHAR(500)
);

CREATE TABLE [SpotifyInfo].[dbo].[aggregate_playlist_songs] (
	id INT IDENTITY(1,1) PRIMARY KEY,
	aggregate_playlist_spotify_id VARCHAR(100) FOREIGN KEY REFERENCES [SpotifyInfo].[dbo].[aggregate_playlist](spotify_id),
	songs_spotify_id VARCHAR(100) FOREIGN KEY REFERENCES [SpotifyInfo].[dbo].[song] (spotify_id)
);