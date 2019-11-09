import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SpotifyService {

  API_URL: string = environment.API_URL;

  constructor(private _httpClient: HttpClient) { }

  getURI(): Observable<string> {
    return this._httpClient.get(`${this.API_URL}/signinuri`, {responseType: 'text'});
  }
}
