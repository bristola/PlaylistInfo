import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { PlaylistsComponent } from './components/playlists/playlists.component';
import { SpotifyAuthGuard } from './route-guards/spotify-auth.guard';
import { PlaylistInfoComponent } from './components/playlist-info/playlist-info.component';


const routes: Routes = [
  { 
    path: '', 
    component: HomeComponent 
  },
  { 
    path: 'playlists', 
    component: PlaylistsComponent,
    canActivate: [SpotifyAuthGuard]
  },
  {
    path: 'playlist/:id',
    component: PlaylistInfoComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
