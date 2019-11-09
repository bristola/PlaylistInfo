import { Component, OnInit } from '@angular/core';
import { SpotifyService } from 'src/app/services/spotify.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  uri: string;

  constructor(private _spotifyService: SpotifyService) { }

  ngOnInit() {
    this._spotifyService.getURI().subscribe(uri => {
      this.uri = uri;
    })
  }

}
