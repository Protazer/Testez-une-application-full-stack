import { HttpClientModule } from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import { ReactiveFormsModule} from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {RouterTestingModule} from "@angular/router/testing";
import {of, throwError} from "rxjs";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create component instance with form and fields', () => {
    expect(component).toBeTruthy();
  });

  it("should register without error", () => {
    fixture.ngZone?.run( () => {
      const navigateSpy = jest.spyOn(router, 'navigate');
      const authSpy = jest.spyOn(authService, 'register').mockReturnValue(of(undefined));

      const emailInput = fixture.nativeElement.querySelector('input[formControlName="email"]');
      emailInput.value = 'test@test.com'
      emailInput.dispatchEvent(new Event('input'));

      const firstNameInput = fixture.nativeElement.querySelector('input[formControlName="firstName"]');
      firstNameInput.value = 'User';
      firstNameInput.dispatchEvent(new Event('input'));

      const lastNameInput = fixture.nativeElement.querySelector('input[formControlName="lastName"]');
      lastNameInput.value = 'User';
      lastNameInput.dispatchEvent(new Event('input'));

      const passwordInput = fixture.nativeElement.querySelector('input[formControlName="password"]');
      passwordInput.value = 'test!1234';
      passwordInput.dispatchEvent(new Event('input'));
      fixture.detectChanges();
      component.submit();

      expect(authSpy).toHaveBeenCalledWith({
        email: 'test@test.com',
        firstName: 'User',
        lastName: 'User',
        password: 'test!1234'
      });
      expect(component.form.valid).toBeTruthy();
      expect(component.form.value).toStrictEqual({
        email: 'test@test.com',
        firstName: 'User',
        lastName: 'User',
        password: 'test!1234'
      });
      expect(navigateSpy).toHaveBeenCalledWith(["/login"]);
      expect(component.onError).toBeFalsy();
    })
  })
  it("should not register", () => {
    fixture.ngZone?.run(() => {
      const navigateSpy = jest.spyOn(router, 'navigate');
      const authSpy = jest.spyOn(authService, 'register').mockReturnValueOnce(throwError(() => {}));
      component.submit();
      expect(component.form.value).toStrictEqual({
        email: '',
        firstName: '',
        lastName: '',
        password: ''
      });
      expect(component.form.valid).toBeFalsy();
      expect(authSpy).toHaveBeenCalledWith({
        email: '',
        firstName: '',
        lastName: '',
        password: ''
      });
      expect(navigateSpy).not.toHaveBeenCalled();
      expect(component.onError).toBeTruthy();
    })
  })
});
