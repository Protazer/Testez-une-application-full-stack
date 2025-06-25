import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {MatToolbarModule} from '@angular/material/toolbar';
import {RouterTestingModule} from '@angular/router/testing';
import {expect} from '@jest/globals';
import {AppComponent} from './app.component';
import {SessionService} from "./services/session.service";
import {Router} from "@angular/router";
import {of} from "rxjs";

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
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
      providers: [SessionService]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should $isLogged call session service', () => {
    const sessionServiceSpy = jest.spyOn(sessionService, '$isLogged');
    component.$isLogged().subscribe(response => of(true));
    expect(sessionServiceSpy).toHaveBeenCalled();
  })

  it('should logout method call sessionService and navigate', () => {
    fixture.ngZone?.run(() => {
      const sessionServiceSpy = jest.spyOn(sessionService, 'logOut');
      const navigateSpy = jest.spyOn(router, 'navigate');
      component.logout();
      expect(sessionServiceSpy).toHaveBeenCalled();
      expect(navigateSpy).toHaveBeenCalledWith(['']);
    })

  })
});
