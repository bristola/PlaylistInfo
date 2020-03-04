import { Component, OnInit } from '@angular/core';
import { SpotifyService } from 'src/app/services/spotify.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { BROAD_GENRES } from 'src/app/constants/constants';

@Component({
  selector: 'app-playlist-info',
  templateUrl: './playlist-info.component.html',
  styleUrls: ['./playlist-info.component.scss']
})
export class PlaylistInfoComponent implements OnInit {

  playlist: any;
  genres: Map<string, number>;
  dates: any;
  songLength: any;
  songPopularity: any;
  songYear: any;
  maxGenres: number = 15;
  pieOptions = {
    pieHole: 0.4,
    legend: 'none',
    pieSliceText: 'label'
  }
  dateOptions = {
    timeline: { showRowLabels: false }
  }

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
      this.getPopularity(info);
      this.getReleaseDates(info);
    });
  }

  private getGenres(info): void {
    this.genres = new Map<string, number>();
    BROAD_GENRES.forEach(genre => this.genres.set(genre, 0));
    info.songs.forEach(song => {
      song.genres.forEach(genre => {
        this.genres.forEach((value, key) => {
          if (genre.includes(key)) {
            this.genres.set(key, value + 1);
          }
        });
      });
    });
  }

  private getSongLength(info): void {
    const lengths: number[] = info.songs.map(song => song.duration);
    const total = lengths.reduce((sum, current) => sum + current, 0);
    this.songLength = (total / lengths.length) / 1000;
  }

  private getPopularity(info): void {
    const lengths: number[] = info.songs.map(song => song.popularity);
    const total = lengths.reduce((sum, current) => sum + current, 0);
    this.songPopularity = (total / lengths.length);
  }

  private getReleaseDates(info): void {
    this.dates = [];
    let yearSum = 0;
    info.songs.forEach(song => {
      const songDate = new Date(song.releaseDate);
      this.dates.push(['Release Date', song.name, songDate, songDate]);
      yearSum += songDate.getFullYear();
    });
    this.songYear = Math.round(yearSum / info.songs.length);
  }
}
