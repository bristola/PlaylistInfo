import { Component, OnInit } from '@angular/core';
import { SpotifyService } from 'src/app/services/spotify.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-playlist-info',
  templateUrl: './playlist-info.component.html',
  styleUrls: ['./playlist-info.component.scss']
})
export class PlaylistInfoComponent implements OnInit {

  playlist: any;
  genres: Map<string, number>;
  songLength: any;

  constructor(private _spotifyService: SpotifyService,
              private _route: ActivatedRoute) { }

  ngOnInit() {
    this.getPlaylistInfo();
  }

  ///////////////////////////////////////////
  //           Private Methods            //
  //////////////////////////////////////////

  private getPlaylistInfo(): void {
    this._route.paramMap.pipe(
      switchMap((params: ParamMap) =>
        this._spotifyService.getPlaylistInfo(params.get('id'))
      )
    ).subscribe(info => {
      this.playlist = info;
      this.getGenres(info);
      this.getSongLength(info);
    });
  }

  private getGenres(info): void {
    this.genres = new Map<string, number>();
    const songs = info.songs;
    songs.forEach(song => {
      song.genres.forEach(genre => {
        if (this.genres.has(genre)) {
          this.genres.set(genre, this.genres.get(genre) + 1);
        } else {
          this.genres.set(genre, 1);
        }
      });
    });
  }

  private getSongLength(info): void {
    const lengths: number[] = info.songs.map(song => song.duration);
    const total = lengths.reduce((sum, current) => sum + current, 0);
    this.songLength = (total / lengths.length) / 1000;
  }
}
