import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/interfaces/user.interface';
import { MeService } from '../services/me.service';
import { Observable, Subject, takeUntil } from 'rxjs';
import { FormBuilder, Validators } from '@angular/forms';
import { RegisterRequest } from '../../auth/interfaces/registerRequest.interface';
import { AuthSuccess } from '../../auth/interfaces/authSuccess.interface';
import { SessionService } from 'src/app/services/session.service';
import { TopicDto } from '../../topic/interfaces/topicDto.interface';
import { TopicService } from '../../topic/services/topic.service';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit {

  public user$: Observable<User> = this.meService.me();

  private destroy$ = new Subject<void>();

  public form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    name: ['', [Validators.required, Validators.min(3)]],
    password: [
      '',
      [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/)
      ]
    ]
  });

  public topics: TopicDto[] = [];

  constructor(
    private meService: MeService,
    private fb: FormBuilder,
    private sessionService: SessionService,
    private topicService: TopicService
  ) { }

  ngOnInit(): void {
    this.meService.me().pipe(takeUntil(this.destroy$)).subscribe(user => {
      this.form.patchValue({
        name: user.name,
        email: user.email
      });
      this.topics = user.topics;
    });
  }

  unSubscription(topic: TopicDto) {
    const index = this.topics.findIndex(top => top.id === topic.id);
    this.topicService.unsubscribe(topic.id).subscribe({
      next: () => {
        if (index !== -1) {
          this.topics.splice(index, 1);
        }
      }
    });
  }

  public submit(): void {
    const registerRequest = this.form.value as RegisterRequest;
    this.meService.update(registerRequest).pipe(takeUntil(this.destroy$)).subscribe({
      next: (response: AuthSuccess) => {
        localStorage.setItem('token', response.token);
        this.meService.me().pipe(takeUntil(this.destroy$)).subscribe((user: User) => {
          this.sessionService.logIn(user);
        });
      },
      error: (err) => {
        if (err.status === 400) {
          const errorMessage = err.error?.message;
          if (errorMessage) {
            const [field, message] = errorMessage.split(':');
            const control = this.form.get(field);
            if (control) {
              control.setErrors({ error: message });
              control.markAsTouched();
            }
          }
        }
      }

    });
  }

}
