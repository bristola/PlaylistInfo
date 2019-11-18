import { Component, OnInit } from '@angular/core';
import { SpotifyService } from 'src/app/services/spotify.service';
import { Router } from '@angular/router';

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

  constructor(private _router: Router, private _spotifyService: SpotifyService) { }

  ngOnInit() {
    this.accessToken = sessionStorage.getItem('access');
    this.refreshToken = sessionStorage.getItem('refresh');
    if (this.accessToken && this.refreshToken) {
      this._getUserPlaylists(this.accessToken, this.refreshToken, this.page);
    } else {
      this._router.navigate(['home']);
    }
  }

  loadMore(): void {
    if (this.loadMoreFlag) {
      this.page++;
      this._getUserPlaylists(this.accessToken, this.refreshToken, this.page);
    }
  }

  generateInfo(playlistID: string): void {
    this._generateInfo(this.accessToken, this.refreshToken, playlistID);
  }

  private _getUserPlaylists(accessToken: string, refreshToken: string, page: number) {
    this._spotifyService.getUserPlaylists(accessToken, refreshToken, page)
      .subscribe(playlists => {
        if (playlists.length != this.perPage) {
          this.loadMoreFlag = false;
        }
        this.playlists = this.playlists.concat(playlists);
      });
  }

  private _generateInfo(accessToken: string, refreshToken: string, playlistID: string) {
    this._spotifyService.generateInfo(accessToken, refreshToken, playlistID)
      .subscribe();
  }

}
