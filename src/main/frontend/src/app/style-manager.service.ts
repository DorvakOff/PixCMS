import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StyleManagerService {

  constructor() { }

  setStyle(key: string, href: string): void {
    getLinkElementForKey(key).setAttribute("href", href)
  }

  removeStyle(key: string): void {
    const existingLinkElement = getExistingLinkElementByKey(key)
    if (existingLinkElement) {
      document.head.removeChild(existingLinkElement)
    }
  }
}

function getLinkElementForKey(key: string): HTMLLinkElement {
  return getExistingLinkElementByKey(key) || createLinkElementWithKey(key)
}

function getExistingLinkElementByKey(key: string): HTMLLinkElement | null {
  return document.head.querySelector(
    `link[rel="stylesheet"].${getClassNameForKey(key)}`
  )
}

function createLinkElementWithKey(key: string): HTMLLinkElement {
  const linkEl = document.createElement("link")
  linkEl.setAttribute("rel", "stylesheet")
  linkEl.classList.add(getClassNameForKey(key))
  document.head.appendChild(linkEl)
  return linkEl
}

function getClassNameForKey(key: string): string {
  return `app-${key}`
}
