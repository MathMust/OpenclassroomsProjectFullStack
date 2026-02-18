import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { MeComponent } from './me.component';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { HttpClientModule } from '@angular/common/http';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { SessionService } from '../../services/session.service';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { expect } from '@jest/globals';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userServiceMock: any;
  let sessionServiceMock: any;
  let routerMock: any;
  let snackBar: MatSnackBar;

  const mockUser = {
    firstName: 'John',
    lastName: 'Doe',
    email: 'john@example.com',
    admin: true,
    createdAt: new Date('2023-01-01'),
    updatedAt: new Date('2023-06-01'),
  };

  beforeEach(async () => {
    userServiceMock = {
      getById: jest.fn().mockReturnValue(of(mockUser)),
      delete: jest.fn().mockReturnValue(of({}))
    };

    sessionServiceMock = {
      sessionInformation: { id: 1, admin: true },
      logOut: jest.fn()
    };

    routerMock = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        NoopAnimationsModule,
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: UserService, useValue: userServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    snackBar = TestBed.inject(MatSnackBar);

    fixture.detectChanges(); // ngOnInit est appelé
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load user on init', () => {
    expect(userServiceMock.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(mockUser);
  });

  it('should display user info in template', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('John DOE');
    expect(compiled.textContent).toContain('john@example.com');
    expect(compiled.textContent).toContain('You are admin');
    expect(compiled.textContent).toContain('January 1, 2023');
    expect(compiled.textContent).toContain('June 1, 2023');
  });

  it('should call back() when back button clicked', () => {
    jest.spyOn(component, 'back');
    const backButton = fixture.debugElement.query(By.css('button[mat-icon-button]'));
    backButton.triggerEventHandler('click', null);
    expect(component.back).toHaveBeenCalled();
  });

  it('back() should call window.history.back', () => {
    const spy = jest.spyOn(window.history, 'back').mockImplementation(() => { });
    component.back();
    expect(spy).toHaveBeenCalled();
    spy.mockRestore();
  });

  it('should call delete() and trigger logout and navigation', fakeAsync(() => {
    component.user!.admin = false;
    fixture.detectChanges();

    jest.spyOn(snackBar, 'open');

    component.delete();

    tick(0);    // exécute le subscribe de l'Observable
    tick(3000); // avance le timer du MatSnackBar

    expect(userServiceMock.delete).toHaveBeenCalledWith('1');
    expect(snackBar.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  }));
});
