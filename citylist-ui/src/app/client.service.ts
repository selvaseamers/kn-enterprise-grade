import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from './user';
import { catchError, map } from 'rxjs';
import { City } from './city';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(private http: HttpClient) { }

  login(userData: User) {
    console.log("***" + userData.email + ':' + userData.password)
    let url = "http://localhost:8080/api/enterprise-grade/v1/login";
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',

        'Authorization': 'Basic ' + btoa(userData.email + ':' + userData.password)
      })
    };
    return this.http.get(url, httpOptions).pipe(
      map(data => data),
      catchError((err) => {
        console.error(err);
        throw err;
      }));
  }

  getCityList() {
    let url = "http://localhost:8080/api/enterprise-grade/v1/city-list?page=0&pageSize=1000";
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',

        'Authorization': 'Basic ' + localStorage.getItem('ACCESS_TOKEN')
      })
    };
    return this.http.get(url, httpOptions).pipe(
      map(data => data),
      catchError((err) => {
        console.error(err);
        throw err;
      }));
  }

  updateCityDetails(cityList: City, file: File) {
    let url = "http://localhost:8080/api/enterprise-grade/v1/admin/city-list";
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': 'Basic ' + localStorage.getItem('ACCESS_TOKEN')
      })
    };
    //console.log("*********"+file.name)
    const formData = new FormData();
    formData.append("id", "" + cityList.id);
    formData.append("cityName", "" + cityList.cityName);
    formData.append("image", file);

    return this.http.put(url, formData, httpOptions).pipe(
      map(data => data),
      catchError((err) => {
        console.error(err);
        throw err;
      }));

  }
}
