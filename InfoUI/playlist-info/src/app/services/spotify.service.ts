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

  getUserPlaylists(code: string): Observable<any> {
    const headers = new HttpHeaders({'SPOTIFY-AUTH-CODE': code});
    return this._httpClient.get(`${this.API_URL}/playlists`, {headers: headers});
  }

  setCode(code: string): void {
    sessionStorage.setItem('code', code);
  }

  getCode(): string {
    return sessionStorage.getItem('code');
  }
}
