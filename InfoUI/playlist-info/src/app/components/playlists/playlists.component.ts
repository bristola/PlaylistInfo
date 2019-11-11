import { Component, OnInit } from '@angular/core';
import { SpotifyService } from 'src/app/services/spotify.service';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-playlists',
  templateUrl: './playlists.component.html',
  styleUrls: ['./playlists.component.scss']
})
export class PlaylistsComponent implements OnInit {

  code: string;
  $playlists: Observable<any>;

  constructor(private _router: Router, private _spotifyService: SpotifyService) { }

  ngOnInit() {
    const accessToken = sessionStorage.getItem('access');
    const refreshToken = sessionStorage.getItem('refresh');
    if (accessToken && refreshToken) {
      this.$playlists = this._spotifyService.getUserPlaylists(accessToken, refreshToken);
    } else {
      this._router.navigate(['home']);
    }
  }

}
