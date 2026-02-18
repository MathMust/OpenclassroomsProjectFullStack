import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PostsResponse } from '../interfaces/postsResponse.interface';
import { PostRequest } from '../interfaces/postRequest.interface';
import { PostDto } from '../interfaces/postDto.interface';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private pathService = 'api/post';

  constructor(private httpClient: HttpClient) { }

  public getAll(): Observable<PostsResponse> {
    return this.httpClient.get<PostsResponse>(`${this.pathService}`);
  }

  public create(postRequest: PostRequest): Observable<string> {
    return this.httpClient.post(`${this.pathService}`, postRequest, { responseType: 'text' });
  }

  public getById(postId: number): Observable<PostDto> {
    return this.httpClient.get<PostDto>(`${this.pathService}/${postId}`);
  }

}
