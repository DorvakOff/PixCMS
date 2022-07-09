import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username: any
  password: any
  rememberMe: boolean = false;
  authLoading: boolean = false;
  redirect: string = '/'
  error: any;

  constructor(private http: HttpClient, private router: Router, route: ActivatedRoute) {
    route.queryParams.subscribe(params => {
      if (params['redirect']) {
        this.redirect = params["redirect"]
      }
    })
  }

  ngOnInit(): void {
  }

  onClickLogin() {
    this.authLoading = true
    this.error = undefined
    if (this.username && this.password) {
      this.http.post<string>('/api/users/login', {
        username: this.username,
        password: this.password
      }).subscribe(token => {
        this.router.navigate([this.redirect])
      }, error => {
        this.error = error.error
        this.authLoading = false
      })
    } else {
      this.authLoading = false
    }
  }
}
