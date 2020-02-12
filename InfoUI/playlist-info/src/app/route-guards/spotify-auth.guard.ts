import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router, ActivatedRoute } from '@angular/router';
import { Observable, of } from 'rxjs';
import { SpotifyService } from '../services/spotify.service';
import { concatMap, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SpotifyAuthGuard implements CanActivate {

  constructor(private _spotifyService: SpotifyService) { }

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean | UrlTree> {
    if (this._spotifyService.getAccessToken()) {
      return of(true);
    }
    if (route.queryParams.code) {
      return this._spotifyService.authorize(route.queryParams.code).pipe(
        map(response => {
          if (response) {
            this._spotifyService.setAccessToken(response);
            return true;
          } else {
            return false;
          }
        })
      );
    } else {
      return of(false);
    }
  
  }
}
