import { Component, OnInit } from '@angular/core';
import { SpotifyService } from 'src/app/services/spotify.service';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { concatMap, map, tap, filter } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-playlists',
  templateUrl: './playlists.component.html',
  styleUrls: ['./playlists.component.scss']
})
export class PlaylistsComponent implements OnInit {

  playlists = [];
  existingPlaylists = []
  page: number = 0;
  perPage: number = 20;
  loadMoreFlag: boolean = true;

  constructor(private _spotifyService: SpotifyService,
              private _location: Location,
              private _router: Router) { }

  ngOnInit() {
    this._location.replaceState('playlists');
    this._getUserPlaylists(this.page);
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
    this._spotifyService.getExistingUserPlaylists().pipe(
      tap(existingPlaylists => {
        this.existingPlaylists = existingPlaylists;
      }),
      concatMap(existingPlaylists => 
        this._spotifyService.getUserPlaylists(page).pipe(
            tap(playlists => {
              if (playlists.length != this.perPage) {
                this.loadMoreFlag = false;
              }
            }),
            map(playlists =>
              playlists.filter(playlist => 
                !existingPlaylists.map(p => p.id).includes(playlist.id)
              )
            )
          )
      )
    ).subscribe(playlists => {
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
