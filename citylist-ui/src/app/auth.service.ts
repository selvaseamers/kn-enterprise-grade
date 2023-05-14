import { Injectable } from '@angular/core';
import { User } from './user';
import { HttpClient, HttpHeaders } from '@angular/common/http'
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import {ClientService} from './client.service'

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient, private router: Router, private clientService: ClientService) { }

  public  signIn(userData: User) {
    return this.clientService.login(userData).pipe();
  }

  public isLoggedIn() {
    return localStorage.getItem('ACCESS_TOKEN') !== null;
  }
  public logout() {
    localStorage.removeItem('ACCESS_TOKEN');
    localStorage.removeItem('ROLE')
  }
  public getRole() {
    return localStorage.getItem('ROLE')
  }
}