import {Injectable} from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {environment} from "../../../environments/environment";
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";

@Injectable()
export class ApiInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private router: Router) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    // Update the request URL here
    const updatedRequest = request.clone({
      url: `${environment.baseUrl}${request.url}`
    });

    // Pass the updated request to the next handler
    return next.handle(updatedRequest).pipe(
      catchError((error: HttpErrorResponse) => {
        if(error.status === 401){
          this.authService.removeRoleFromLocalStorage();
          this.router.navigate(['/login']);
        }
        return throwError(()=> new Error(error.error.message));
      }
    ));
  }
}
