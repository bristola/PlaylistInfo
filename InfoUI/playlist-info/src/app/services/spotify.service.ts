import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SpotifyService {

  API_URL: string = environment.API_URL;

  constructor(private _httpClient: HttpClient) { }

  getSigninURI(): Observable<string> {
    return this._httpClient.get(`${this.API_URL}/signinuri`, {responseType: 'text'});
  }

  getUserPlaylists(accessToken: string, refreshToken: string, page: number): Observable<any> {
    const headers = new HttpHeaders({
      'SPOTIFY-ACCESS-TOKEN': accessToken,
      'SPOTIFY-REFRESH-TOKEN': refreshToken
    });
    return this._httpClient.get(`${this.API_URL}/playlists/${page}`, {headers: headers});
  }

  authorize(code: string): Observable<any> {
    const headers = new HttpHeaders({'SPOTIFY-AUTH-CODE': code});
    return this._httpClient.get(`${this.API_URL}/authorize`, {headers: headers});
  }

  generateInfo(accessToken: string, refreshToken: string, playlistID: string): Observable<any> {
    const headers = new HttpHeaders({
      'SPOTIFY-ACCESS-TOKEN': accessToken,
      'SPOTIFY-REFRESH-TOKEN': refreshToken
    });
    return this._httpClient.get(`${this.API_URL}/generateinfo/${playlistID}`, {headers: headers});
  }
}
