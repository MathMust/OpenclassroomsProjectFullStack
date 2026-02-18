import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/interfaces/user.interface';
import { RegisterRequest } from '../../auth/interfaces/registerRequest.interface';
import { AuthSuccess } from '../../auth/interfaces/authSuccess.interface';

@Injectable({
  providedIn: 'root'
})
export class MeService {

  private pathService = 'api/auth';

  constructor(private httpClient: HttpClient) { }

  public me(): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/me`);
  }

  public update(registerRequest: RegisterRequest): Observable<AuthSuccess> {
    return this.httpClient.post<AuthSuccess>(`${this.pathService}/update`, registerRequest);
  }

}
