import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TopicsResponse } from '../interfaces/topicsResponse.interface';

@Injectable({
  providedIn: 'root'
})
export class TopicService {

  private pathService = 'api/topic';

  constructor(private httpClient: HttpClient) { }

  public getAll(): Observable<TopicsResponse> {
    return this.httpClient.get<TopicsResponse>(`${this.pathService}`);
  }

  public unsubscribe(topicId: number): Observable<string> {
    return this.httpClient.delete(`${this.pathService}/${topicId}/subscribe`, { responseType: 'text' });
  }

  public subscribe(topicId: number): Observable<string> {
    return this.httpClient.get(`${this.pathService}/${topicId}/subscribe`, { responseType: 'text' });
  }


}
