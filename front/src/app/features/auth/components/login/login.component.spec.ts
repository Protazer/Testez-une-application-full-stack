import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
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

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
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
    fixture.detectChanges();
  });

  it('should create component instance with form and fields', () => {
    expect(component).toBeTruthy();
  });
  it('should submit the form with no error', () => {
    fixture.ngZone?.run(async () => {
      const navigateSpy = jest.spyOn(router, 'navigate');
      const authSpy = jest.spyOn(authService, 'login').mockReturnValue(of({token: 'jwt',
        type: 'session',
        id: 1,
        username: 'user@user.com',
        firstName: 'User',
        lastName: 'User',
        admin: false,}));
      const sessionServiceSpy = jest.spyOn(sessionService, 'logIn');

      const emailInput = fixture.nativeElement.querySelector('input[formControlName="email"]');
      const passwordInput = fixture.nativeElement.querySelector('input[formControlName="password"]');
      emailInput.value = 'yoga@studio.com'
      emailInput.dispatchEvent(new Event('input'));
      passwordInput.value = 'test!1234';
      passwordInput.dispatchEvent(new Event('input'));
      await fixture.whenStable();
      fixture.detectChanges()
      component.submit();


      expect(component.form.value).toStrictEqual({email: 'yoga@studio.com', password: 'test!1234'});
      expect(component.form.valid).toBeTruthy();
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
    const sessionServiceSpy = jest.spyOn(sessionService, 'logIn');
    component.submit();
    expect(component.form.value).toStrictEqual({email: '', password: ''});
    expect(component.form.valid).toBeFalsy();
    expect(sessionServiceSpy).not.toHaveBeenCalled();
    expect(authSpy).toHaveBeenCalled();
    expect(component.onError).toBeTruthy();
  })
});
