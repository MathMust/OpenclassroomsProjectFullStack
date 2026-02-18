import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { AuthSuccess } from '../../interfaces/authSuccess.interface';
import { User } from 'src/app/interfaces/user.interface';
import { Subject, takeUntil } from 'rxjs';
import { MeService } from 'src/app/features/me/services/me.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

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

  constructor(private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private sessionService: SessionService,
    private meService: MeService
  ) { }

  public submit(): void {
    const registerRequest = this.form.value as RegisterRequest;
    this.authService.register(registerRequest).pipe(takeUntil(this.destroy$)).subscribe({
      next: (response: AuthSuccess) => {
        localStorage.setItem('token', response.token);
        this.meService.me().pipe(takeUntil(this.destroy$)).subscribe((user: User) => {
          this.sessionService.logIn(user);
          this.router.navigate(['/posts'])
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
