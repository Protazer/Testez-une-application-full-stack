import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {SessionService} from 'src/app/services/session.service';
import {MeComponent} from './me.component';
import {UserService} from "../../services/user.service";
import {of} from "rxjs";
import {User} from "../../interfaces/user.interface";
import {Router} from "@angular/router";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let router: Router;
  let userService: UserService;
  let matSnackBar: MatSnackBar;
  let httpMock: HttpTestingController;

  const mockedUser: User = {
    id: 1,
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
    open: jest.fn(),
  };


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        HttpClientTestingModule,
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [UserService,
        {provide: MatSnackBar, useValue: mockedSnackBar}, {provide: SessionService, useValue: mockSessionService}],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    userService = TestBed.inject(UserService);
    matSnackBar = TestBed.inject(MatSnackBar);
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });


  it('should create the MeComponent', () => {
    expect(component).toBeTruthy();
  });

  it("should set the user on component init", () => {
    const userServicesSpy = jest.spyOn(userService, "getById").mockReturnValue(of(mockedUser));
    component.ngOnInit();
    expect(userServicesSpy).toHaveBeenCalled();
    expect(component.user).toStrictEqual(mockedUser);
  });

  it('should call back method', () => {
    const historySpy = jest.spyOn(window.history, 'back');
    component.back();
    expect(historySpy).toHaveBeenCalled();
  })

  it('should call the delete method', () => {
    fixture.ngZone?.run(() => {
      const userServiceSpy = jest.spyOn(userService, "delete");
      const sessionServiceSpy = jest.spyOn(mockSessionService, "logOut");
      const navigateSpy = jest.spyOn(router, 'navigate');
      const matSnackBarSpy = jest.spyOn(matSnackBar, 'open');
      component.delete();

      const deleteRequest = httpMock.expectOne({url: "api/user/1", method: "DELETE"});
      deleteRequest.flush(null);

      expect(userServiceSpy).toHaveBeenCalled();
      expect(matSnackBarSpy).toHaveBeenCalledWith("Your account has been deleted !", "Close", {"duration": 3000});
      expect(sessionServiceSpy).toHaveBeenCalled();
      expect(navigateSpy).toHaveBeenCalledWith(['/']);
    })
  });
});
