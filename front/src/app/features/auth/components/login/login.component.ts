import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/interfaces/user.interface';
import { SessionService } from 'src/app/services/session.service';
import { AuthSuccess } from '../../interfaces/authSuccess.interface';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { AuthService } from '../../services/auth.service';
import { Subject, takeUntil } from 'rxjs';
import { MeService } from 'src/app/features/me/services/me.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  private destroy$ = new Subject<void>();

  public onError: boolean = false;
  public errorMessage: string = "";

  public form = this.fb.group({
    identifier: ['', [Validators.required]],
    password: ['', [Validators.required]]
  });

  constructor(private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private sessionService: SessionService,
    private meService: MeService
  ) { }

  ngOnInit(): void {
    this.form.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.onError = false;
      this.errorMessage = '';
    });
  }

  public submit(): void {
    const loginRequest = this.form.value as LoginRequest;
    this.authService.login(loginRequest).pipe(takeUntil(this.destroy$)).subscribe({
      next: (response: AuthSuccess) => {
        localStorage.setItem('token', response.token);
        this.meService.me().pipe(takeUntil(this.destroy$)).subscribe((user: User) => {
          this.sessionService.logIn(user);
          this.router.navigate(['/posts']);
        });
      },
      error: (err) => {
        if (err.status === 400) {
          this.errorMessage = err.error?.message;
          if (this.errorMessage) {
            this.onError = true;
          }
        }
      }
    });
  }

}