import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  username: any
  password: any
  email: any
  repeatPassword: any
  rememberMe: boolean = false
  authLoading: boolean = false
  error: any

  constructor(private http: HttpClient, private router: Router) {
  }

  ngOnInit(): void {
  }

  checkPasswords(): boolean {
    return this.password && this.repeatPassword && this.password === this.repeatPassword
  }

  clickRegister() {
    this.error = undefined
    if (!this.checkPasswords()) return
    this.authLoading = true
    if (this.username && this.email) {
      this.http.post<any>('/api/users/register', {
        username: this.username,
        password: this.password,
        email: this.email
      }, {responseType: 'text' as 'json'}).subscribe(token => {
        this.router.navigate(['/'])
      }, error => {
        this.error = JSON.parse(error.error)
        this.authLoading = false
      })
    } else {
      this.authLoading = false
    }
  }
}
