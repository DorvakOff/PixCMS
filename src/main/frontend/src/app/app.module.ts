import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FormsModule} from "@angular/forms";
import {HomeComponent} from './home/home.component';
import {LoginComponent} from './login/login.component';
import {Error404Component} from './error404/error404.component';
import {ThemeSwitcherComponent} from "./theme-switcher/theme-switcher.component";
import {HttpClientModule} from "@angular/common/http";
import {RegisterComponent} from './register/register.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    Error404Component,
    ThemeSwitcherComponent,
    RegisterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
