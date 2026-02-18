import { HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({ providedIn: 'root' })
export class JwtInterceptor implements HttpInterceptor {
  constructor() { }

  public intercept(request: HttpRequest<any>, next: HttpHandler) {
    const publicUrls = ['api/auth/register', 'api/auth/login'];
    const token = localStorage.getItem('token');
    if (token && !publicUrls.some(url => request.url.includes(url))) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }
    return next.handle(request);
  }
}