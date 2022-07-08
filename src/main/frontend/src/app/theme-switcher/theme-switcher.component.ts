import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ThemeService} from "../theme.service";
import {environment} from "../../environments/environment";

interface Theme {
  name: string
  fileName: string
}

@Component({
  selector: 'app-theme-switcher',
  templateUrl: './theme-switcher.component.html',
  styleUrls: ['./theme-switcher.component.css']
})
export class ThemeSwitcherComponent implements OnInit {

  themes: Theme[] = []

  constructor(http: HttpClient, public themeService: ThemeService) {
    http.get<Theme[]>(`./${environment.production ? '' : '../../assets/themes/'}themes.json`).subscribe(value => {
        this.themes = value
      }
    )
  }

  ngOnInit(): void {
  }

  switchTheme($event: Event) {
    let nomTheme: string = ($event.target as HTMLInputElement).value
    this.themeService.setTheme(nomTheme)
  }
}
