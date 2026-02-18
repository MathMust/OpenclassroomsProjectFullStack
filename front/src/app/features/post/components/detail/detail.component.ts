import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { PostService } from 'src/app/features/post/services/post.service';
import { PostDto } from '../../interfaces/postDto.interface';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommentRequest } from '../../interfaces/commentRequest.interface';
import { CommentService } from '../../services/comment.service';

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss']
})
export class DetailComponent implements OnInit {

  private destroy$ = new Subject<void>();

  public post!: PostDto;

  public postId: number;

  public form!: FormGroup;

  constructor(private fb: FormBuilder,
    private route: ActivatedRoute,
    private postService: PostService,
    private commentService: CommentService) {
    this.postId = +this.route.snapshot.paramMap.get('id')!;
  }

  ngOnInit(): void {
    this.initForm();
    this.loadDetail();
  }

  loadDetail() {
    this.postService.getById(this.postId).pipe(takeUntil(this.destroy$)).subscribe(post => this.post = post);
  }

  initForm(): void {
    this.form = this.fb.group({
      content: ['', [Validators.required]]
    });
  }

  goBack(): void {
    window.history.back();
  }

  isMobile(): boolean {
    return window.innerWidth < 600;
  }

  public submit(): void {
    const commentRequest = this.form.value as CommentRequest;
    commentRequest.postId = this.postId;
    this.commentService.create(commentRequest).pipe(takeUntil(this.destroy$)).subscribe(
      () => {
        this.initForm();
        this.loadDetail();
      })
  }

}