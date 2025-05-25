import {HttpClientModule} from '@angular/common/http';
import {TestBed} from '@angular/core/testing';
import {MatToolbarModule} from '@angular/material/toolbar';
import {RouterTestingModule} from '@angular/router/testing';
import {expect} from '@jest/globals';
import {AppComponent} from './app.component';
import {SessionService} from "./services/session.service";
import {of} from "rxjs";
import {Router} from "@angular/router";


describe('AppComponent', () => {
  let component: AppComponent;
  let sessionService: SessionService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule,
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();
    const fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should $isLogged call session service', () => {
    const sessionServiceSpy = jest.spyOn(sessionService, '$isLogged').mockReturnValue(of(false));
    component.$isLogged();
    expect(sessionServiceSpy).toHaveBeenCalled();
  })

  it('should logout method call sessionService and navigate', () => {
    const sessionServiceSpy = jest.spyOn(sessionService, 'logOut');
    const navigateSpy = jest.spyOn(router, 'navigate');
    component.logout();
    expect(sessionServiceSpy).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['']);
  })
});
