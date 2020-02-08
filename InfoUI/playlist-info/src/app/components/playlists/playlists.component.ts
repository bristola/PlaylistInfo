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

  constructor(private _spotifyService: SpotifyService,
              private _location: Location,
              private _router: Router) { }

  ngOnInit() {
    this._location.replaceState('playlists');
    this._getUserPlaylists(this.page);
    this._spotifyService.getExistingUserPlaylists().subscribe();
  }

  ///////////////////////////////////////////
  //            Public Methods            //
  //////////////////////////////////////////
  
  loadMore(): void {
    if (this.loadMoreFlag) {
      this.page++;
      this._getUserPlaylists(this.page);
    }
  }

  generateInfo(playlistID: string): void {
    this._generateInfo(playlistID);
  }

  ///////////////////////////////////////////
  //           Private Methods            //
  //////////////////////////////////////////

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
      .subscribe(() =>
        this._router.navigate([`playlist/${playlistID}`])
      );
  }

}
