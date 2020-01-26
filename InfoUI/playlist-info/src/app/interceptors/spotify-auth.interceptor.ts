import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SpotifyService } from '../services/spotify.service';
import { HEADERS } from '../constants/constants';

@Injectable({
  providedIn: 'root'
})
export class SpotifyAuthInterceptor implements HttpInterceptor {

    constructor(private _spotifyService: SpotifyService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const authData = this._spotifyService.getAuthorizationData();
        let headers = req.headers;

        if (authData) {
            headers = headers.append(HEADERS.ACCESS, authData.accessToken)
                             .append(HEADERS.REFRESH, authData.refreshToken);
        }

        const outReq = req.clone({headers: headers});

        return next.handle(outReq);
   }
}
