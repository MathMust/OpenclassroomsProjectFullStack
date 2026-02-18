import { Component, OnInit } from '@angular/core';
import { map, Observable, Subject, takeUntil } from 'rxjs';
import { PostsResponse } from 'src/app/features/post/interfaces/postsResponse.interface';
import { PostService } from 'src/app/features/post/services/post.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

  private destroy$ = new Subject<void>();

  public posts$!: Observable<PostsResponse>;

  sortAsc = false;

  constructor(private postService: PostService) { }

  ngOnInit(): void {
    this.loadPosts();
  }

  loadPosts() {
    this.posts$ = this.postService.getAll().pipe(
      takeUntil(this.destroy$),
      map(data => {
        return {
          ...data,
          posts: data.posts.sort((a, b) => {
            const dateA = new Date(a.date).getTime();
            const dateB = new Date(b.date).getTime();
            return this.sortAsc ? dateA - dateB : dateB - dateA;
          })
        };
      })
    );
  }

  toggleSort() {
    this.sortAsc = !this.sortAsc;
    this.loadPosts();
  }

}