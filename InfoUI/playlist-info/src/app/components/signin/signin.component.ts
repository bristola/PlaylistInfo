import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {

  constructor(private _router: Router, private _route: ActivatedRoute) { }

  ngOnInit() {
    this._route.queryParams.subscribe(params => {
      const code = params['code'];
      sessionStorage.setItem('code', code);
      this._router.navigate(['/playlists']);
    });
  }

}
