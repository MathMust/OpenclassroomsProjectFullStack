import { Component } from '@angular/core';
import { Validators, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable, Subject, takeUntil } from 'rxjs';
import { PostRequest } from '../../interfaces/postRequest.interface';
import { TopicsResponse } from 'src/app/features/topic/interfaces/topicsResponse.interface';
import { TopicService } from 'src/app/features/topic/services/topic.service';
import { TopicDto } from 'src/app/features/topic/interfaces/topicDto.interface';
import { PostService } from '../../services/post.service';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss']
})
export class FormComponent {

  private destroy$ = new Subject<void>();

  public topic$!: Observable<TopicsResponse>;

  options: TopicDto[] = [];

  public form = this.fb.group({
    topicId: [null, [Validators.required]],
    title: ['', [Validators.required]],
    content: ['', [Validators.required]]
  });

  constructor(private fb: FormBuilder,
    private topicService: TopicService,
    private postService: PostService,
    private router: Router) { }

  ngOnInit(): void {
    this.loadTopics();
  }

  loadTopics() {
    this.topicService.getAll().pipe(takeUntil(this.destroy$)).subscribe(response => {
      this.options = response.topics;
    })
  }

  public submit(): void {
    const postRequest = this.form.value as PostRequest;
    this.postService.create(postRequest).pipe(takeUntil(this.destroy$)).subscribe(
      () => {
        this.router.navigate(['/posts']);
      })
  }
}
