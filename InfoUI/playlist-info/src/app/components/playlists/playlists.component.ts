import { Component, OnInit } from '@angular/core';
import { SpotifyService } from 'src/app/services/spotify.service';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-playlists',
  templateUrl: './playlists.component.html',
  styleUrls: ['./playlists.component.scss']
})
export class PlaylistsComponent implements OnInit {

  code: string;
  playlists = [];
  accessToken: string;
  refreshToken: string;
  page: number = 0;
  perPage: number = 20;
  loadMoreFlag: boolean = true;

  constructor(private _router: Router,
              private _spotifyService: SpotifyService,
              private _location: Location) { }

  ngOnInit() {
    this._location.replaceState(this._router.url);
    this._getUserPlaylists(this.page);
  }

  loadMore(): void {
    if (this.loadMoreFlag) {
      this.page++;
      this._getUserPlaylists(this.page);
    }
  }

  generateInfo(playlistID: string): void {
    this._generateInfo(playlistID);
  }

  private _getUserPlaylists(page: number) {
    this._spotifyService.getUserPlaylists(page)
      .subscribe(playlists => {
        if (playlists.length != this.perPage) {
          this.loadMoreFlag = false;
        }
        this.playlists = this.playlists.concat(playlists);
      });
  }

  private _generateInfo(playlistID: string) {
    this._spotifyService.generateInfo(playlistID)
      .subscribe();
  }

}
