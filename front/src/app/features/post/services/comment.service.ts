import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentRequest } from '../interfaces/commentRequest.interface';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private pathService = 'api/comment';

  constructor(private httpClient: HttpClient) { }

  public create(commentRequest: CommentRequest): Observable<string> {
    return this.httpClient.post(`${this.pathService}`, commentRequest, { responseType: 'text' });
  }

}
