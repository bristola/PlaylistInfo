import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { HEADERS } from '../constants/constants';
import { ISimplePlaylist } from '../interfaces/interfaces';

@Injectable({
  providedIn: 'root'
})
export class SpotifyService {

  API_URL: string = environment.API_URL;

  constructor(private _httpClient: HttpClient) { }

  setAccessToken(accessToken: string): void {
    sessionStorage.setItem('accessToken', accessToken);
  }

  getAccessToken(): string {
    const accessToken = sessionStorage.getItem('accessToken');

    return accessToken;
  }

  getSigninURI(): Observable<string> {
    return this._httpClient.get(`${this.API_URL}/authorization/signinuri`, {responseType: 'text'});
  }

  authorize(code: string): Observable<string> {
    const headers = new HttpHeaders().append(HEADERS.CODE, code);
    return this._httpClient.get(`${this.API_URL}/authorization/access`, {headers: headers, responseType: 'text'});
  }

  getUserPlaylists(page: number): Observable<ISimplePlaylist[]> {
    return this._httpClient.get<ISimplePlaylist[]>(`${this.API_URL}/playlists/spotify/${page}`);
  }

  generateInfo(playlistId: string): Observable<any> {
    return this._httpClient.post(`${this.API_URL}/playlists/info/${playlistId}`, null);
  }

  getPlaylistInfo(playlistId: string): Observable<any> {
    return this._httpClient.get(`${this.API_URL}/playlists/info/${playlistId}`);
  }

  getExistingUserPlaylists(): Observable<ISimplePlaylist[]> {
    return this._httpClient.get<ISimplePlaylist[]>(`${this.API_URL}/playlists/existing`);
  }
}
