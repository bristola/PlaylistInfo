import { Component, OnInit } from '@angular/core';
import { SpotifyService } from 'src/app/services/spotify.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-playlists',
  templateUrl: './playlists.component.html',
  styleUrls: ['./playlists.component.scss']
})
export class PlaylistsComponent implements OnInit {

  code: string;
  $playlists: Observable<any>;

  constructor(private _spotifyService: SpotifyService) { }

  ngOnInit() {
    const accessToken = sessionStorage.getItem('access');
    const refreshToken = sessionStorage.getItem('refresh');
    this.$playlists = this._spotifyService.getUserPlaylists(accessToken, refreshToken);
  }

}
