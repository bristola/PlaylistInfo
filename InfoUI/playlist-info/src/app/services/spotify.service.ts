import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { HEADERS } from '../constants/constants';
import { IAuthorizeResponse, ISimplePlaylist } from '../interfaces/interfaces';

@Injectable({
  providedIn: 'root'
})
export class SpotifyService {

  API_URL: string = environment.API_URL;
  authData: IAuthorizeResponse;

  constructor(private _httpClient: HttpClient) { }

  setAuthorizationData(authData: IAuthorizeResponse): void {
    sessionStorage.setItem('accessToken', authData.accessToken);
    sessionStorage.setItem('refreshToken', authData.refreshToken);
  }

  getAuthorizationData(): IAuthorizeResponse {
    const accessToken = sessionStorage.getItem('accessToken');
    const refreshToken = sessionStorage.getItem('refreshToken');
    
    return !accessToken && !refreshToken ?
      null :
      { accessToken: accessToken, refreshToken: refreshToken };
  }

  getSigninURI(): Observable<string> {
    return this._httpClient.get(`${this.API_URL}/authorization/signinuri`, {responseType: 'text'});
  }

  authorize(code: string): Observable<IAuthorizeResponse> {
    const headers = new HttpHeaders().append(HEADERS.CODE, code);
    return this._httpClient.get<IAuthorizeResponse>(`${this.API_URL}/authorization/access`, {headers: headers});
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
