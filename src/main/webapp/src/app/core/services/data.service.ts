import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private http: HttpClient) {
  }

  public get(url: string): Observable<any> {
    return this.http.get<any>(url);
  }

  public postWithToken(url: string, body: object): Observable<any> {
    return this.http.post<any>(url, body, {withCredentials: true});
  }

  public putWithToken(url: string, body: object): Observable<any> {
    return this.http.put<any>(url, body, {withCredentials: true});
  }

  public deleteWithToken(url: string): Observable<any> {
    return this.http.delete<any>(url, {withCredentials: true});
  }
}
