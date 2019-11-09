import { Component } from '@angular/core';
import { SpotifyService } from 'src/app/services/spotify.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  constructor(private _spotifyService: SpotifyService) { }

  signin(): void {
    this._spotifyService.getSigninURI().subscribe(uri => {
      window.location.href = uri;
    });
  }

}
