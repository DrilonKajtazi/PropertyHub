import {Injectable} from '@angular/core';
import { HttpClient } from "@angular/common/http";
import jwt_decode from "jwt-decode";
import {Router} from "@angular/router";
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient, private router: Router) {
  }

  login(username: string, password: string): Observable<any> {
    const credentials = { username, password };
    return this.http.post<any>('/api/auth/login', credentials, { withCredentials: true }).pipe(
      tap((response) => {
        // Handle successful login
        this.addRoleToLocalStorage(response.token); // Store the token
        this.router.navigate(['']); // Navigate to the home page or another route
      })
    );
  }

  register(username: string, email: string, password: string) {
    const credentials = {username, email, password};
    this.http.post<any>('/api/auth/register', credentials, {withCredentials: true}).subscribe({
      next: (response) => {
        this.addRoleToLocalStorage(response.token)
        this.router.navigate(['']);
      },
      error: (error) => console.error("error while registering:", error)
    });
  }

  
  logout(): Observable<any> {
    this.removeRoleFromLocalStorage()
    return this.http.post<void>('/api/auth/logout', null, { withCredentials: true });
  }

  addRoleToLocalStorage(token: string): void {
    try {
      const decodedToken = jwt_decode(token);
      // @ts-ignore
      const role = decodedToken.role.authority;
      localStorage.setItem('userRole', role);
    } catch (error) {
      console.log("Something went wrong!", error);
    }
  }

  removeRoleFromLocalStorage(){
    localStorage.removeItem('userRole');
  }

  isAdmin() {
    return localStorage.getItem('userRole') === "ROLE_ADMIN";
  }
}
