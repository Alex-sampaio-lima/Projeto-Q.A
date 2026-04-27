// interceptors/auth.interceptor.ts
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Pega o token do localStorage
    const token = localStorage.getItem('authToken');

    if (token) {
      // Clona a requisição e adiciona o header de autorização
      const authReq = req.clone({
        headers: req.headers.set('Authorization', `Basic ${token}`)
      });
      return next.handle(authReq);
    }

    return next.handle(req);
  }
}
