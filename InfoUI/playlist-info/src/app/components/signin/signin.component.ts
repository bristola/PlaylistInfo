import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { SpotifyService } from 'src/app/services/spotify.service';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {

  constructor(private _router: Router,
              private _route: ActivatedRoute,
              private _spotifyService: SpotifyService) { }

  ngOnInit() {
    this._route.queryParams.subscribe(params => {
      const code = params['code'];
      this._spotifyService.authorize(code).subscribe(data => {
        sessionStorage.setItem("access", data.accessToken);
        sessionStorage.setItem("refresh", data.refreshToken);
      });
      this._router.navigate(['/playlists']);
    });
  }

}
