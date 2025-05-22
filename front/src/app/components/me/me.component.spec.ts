import { HttpClientModule } from '@angular/common/http';
import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import {UserService} from "../../services/user.service";
import {of} from "rxjs";
import {User} from "../../interfaces/user.interface";
import {Router} from "@angular/router";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let router: Router;
  let userService: UserService;
  let sessionService: SessionService;
  let matSnackBar: MatSnackBar;

  const mockedUser: User = {id: 1,
    email: 'user@user.com',
    lastName: 'user',
    firstName: 'user',
    admin: true,
    password: 'password',
    createdAt: new Date('05/22/2025'),
    updatedAt: new Date('05/22/2025')
  };
  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn(() => of({})),
  }

  const mockedSnackBar = {
    open: (message: string, action?: string | undefined, opts?: any) => {},
  } as MatSnackBar;


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: MatSnackBar, useValue: mockedSnackBar }, { provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    userService = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService);
    matSnackBar = TestBed.inject(MatSnackBar);
    fixture.detectChanges();
  });



  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it("should set the user on component init", () => {
    const userServicesSpy = jest.spyOn(userService, "getById").mockReturnValue(of(mockedUser));
    component.ngOnInit();
    expect(userServicesSpy).toHaveBeenCalled();
    expect(component.user).toStrictEqual(mockedUser);
});

  it('should test the back method', () => {
    const historySpy = jest.spyOn(window.history, 'back');
    component.back();
    expect(historySpy).toHaveBeenCalled();
  })

  it('should test the delete method',  () => {
    fixture.ngZone?.run(() => {
      const userServiceSpy = jest.spyOn(userService, "delete").mockReturnValue(of(null));
      const sessionServiceSpy = jest.spyOn(mockSessionService, "logOut");
      const navigateSpy = jest.spyOn(router, 'navigate');
      const matSnackBarSpy = jest.spyOn(matSnackBar, 'open');
      component.delete();
      expect(userServiceSpy).toHaveBeenCalled();
      expect(matSnackBarSpy).toHaveBeenCalledWith("Your account has been deleted !", "Close", {"duration": 3000});
      expect(sessionServiceSpy).toHaveBeenCalled();
      expect(navigateSpy).toHaveBeenCalledWith(['/']);
    })
  });
});
