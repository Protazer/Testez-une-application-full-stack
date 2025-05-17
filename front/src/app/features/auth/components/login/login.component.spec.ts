import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {of, throwError} from "rxjs";


describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;
  let formBuilder: FormBuilder;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService, AuthService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
    formBuilder = TestBed.inject(FormBuilder);
    component.form = formBuilder.group(({email: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],
      password: [
        '',
        [
          Validators.required,
          Validators.min(3)
        ]
      ]}))
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should submit the form with no error', () => {
    fixture.ngZone?.run(() => {
      const navigateSpy = jest.spyOn(router, 'navigate');
      const authSpy = jest.spyOn(authService, 'login').mockReturnValue(of({token: 'jwt',
        type: 'session',
        id: 1,
        username: 'user@user.com',
        firstName: 'User',
        lastName: 'User',
        admin: false,}));
      const sessionServiceSpy = jest.spyOn(sessionService, 'logIn');
      component.form.setValue({email: 'yoga@studio.com', password: 'test!1234'});
      component.submit();
      expect(authSpy).toHaveBeenCalledWith({email: 'yoga@studio.com', password: 'test!1234'});
      expect(sessionServiceSpy).toHaveBeenCalled();
      expect(sessionService.sessionInformation).toStrictEqual({
        token: 'jwt',
        type: 'session',
        id: 1,
        username: 'user@user.com',
        firstName: 'User',
        lastName: 'User',
        admin: false,
      })
      expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
      expect(component.onError).toBeFalsy();
    })
  })
  it('should submit the form with error', () => {
    const authSpy = jest.spyOn(authService, 'login').mockReturnValueOnce(throwError(() => {}));
    component.submit();
    expect(component.onError).toBeTruthy();
  })
});
