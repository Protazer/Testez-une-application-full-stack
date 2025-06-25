import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {expect} from '@jest/globals';
import {SessionService} from 'src/app/services/session.service';
import {LoginComponent} from './login.component';
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {throwError} from "rxjs";
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";


describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;
  let httpMock: HttpTestingController

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        HttpClientTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule],
      providers: [AuthService, SessionService]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  it('should create component instance with form and fields', () => {
    expect(component).toBeTruthy();
  });
  it('should submit the form with no error', () => {
    fixture.ngZone?.run(async () => {
      const mockedSession: SessionInformation = {
        token: 'jwt',
        type: 'session',
        id: 1,
        username: 'user@user.com',
        firstName: 'User',
        lastName: 'User',
        admin: false,
      }
      const navigateSpy = jest.spyOn(router, 'navigate');
      const authSpy = jest.spyOn(authService, 'login');
      const sessionServiceSpy = jest.spyOn(sessionService, 'logIn');

      const emailInput = fixture.nativeElement.querySelector('input[formControlName="email"]');
      emailInput.value = 'yoga@studio.com'
      emailInput.dispatchEvent(new Event('input'));

      const passwordInput = fixture.nativeElement.querySelector('input[formControlName="password"]');
      passwordInput.value = 'test!1234';
      passwordInput.dispatchEvent(new Event('input'));

      await fixture.whenStable();
      fixture.detectChanges()
      component.submit();

      const loginRequest = httpMock.expectOne({url: 'api/auth/login', method: "POST"});
      loginRequest.flush(mockedSession);

      expect(component.form.value).toStrictEqual({email: 'yoga@studio.com', password: 'test!1234'});
      expect(component.form.valid).toBeTruthy();
      expect(authSpy).toHaveBeenCalledWith({email: 'yoga@studio.com', password: 'test!1234'});
      expect(sessionServiceSpy).toHaveBeenCalled();
      expect(sessionService.sessionInformation).toStrictEqual(mockedSession)
      expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
      expect(component.onError).toBeFalsy();
    })
  })

  it('should submit the form with error', () => {
    const authSpy = jest.spyOn(authService, 'login').mockReturnValue(throwError(() => {
    }));
    const sessionServiceSpy = jest.spyOn(sessionService, 'logIn');
    component.submit();
    expect(component.form.value).toStrictEqual({email: '', password: ''});
    expect(component.form.valid).toBeFalsy();
    expect(sessionServiceSpy).not.toHaveBeenCalled();
    expect(authSpy).toHaveBeenCalled();
    expect(component.onError).toBeTruthy();
  })
});
