import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { HEADERS } from '../constants/constants';

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
    return this._httpClient.get(`${this.API_URL}/signinuri`, {responseType: 'text'});
  }

  authorize(code: string): Observable<IAuthorizeResponse> {
    const headers = new HttpHeaders().append(HEADERS.CODE, code);
    return this._httpClient.get<IAuthorizeResponse>(`${this.API_URL}/authorize`, {headers: headers});
  }

  getUserPlaylists(page: number): Observable<any> {
    return this._httpClient.get(`${this.API_URL}/playlists/${page}`);
  }

  generateInfo(playlistID: string): Observable<any> {
    return this._httpClient.get(`${this.API_URL}/generateinfo/${playlistID}`);
  }
}
