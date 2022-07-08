import {Injectable} from '@angular/core';
import {StyleManagerService} from "./style-manager.service";
import {environment} from "../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  currentTheme: string

  constructor(private styleManager: StyleManagerService) {
    this.currentTheme = localStorage.getItem('theme') || 'default'
  }

  setTheme(themeToSet: string = this.currentTheme) {
    localStorage.setItem('theme', themeToSet)
    if (themeToSet == 'default') {
      this.styleManager.setStyle('theme', '')
    } else {
      this.styleManager.setStyle('theme', `./${environment.production ? '' : '../assets/themes/css/'}${themeToSet}.css`)
    }
  }
}
