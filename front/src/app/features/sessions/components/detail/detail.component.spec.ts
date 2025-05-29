import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {RouterTestingModule,} from '@angular/router/testing';
import {expect} from '@jest/globals';
import {SessionService} from '../../../../services/session.service';
import {DetailComponent} from './detail.component';
import {SessionApiService} from "../../services/session-api.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Session} from "../../interfaces/session.interface";
import {Teacher} from "../../../../interfaces/teacher.interface";
import {TeacherService} from "../../../../services/teacher.service";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionApiService: SessionApiService;
  let matSnackBar: MatSnackBar;
  let router: Router;
  let teacherService: TeacherService;
  let httpMock: HttpTestingController;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };
  const mockedMatSnackBar = {
    open: jest.fn()
  }
  const mockedSession: Session = {
    id: 1,
    name: "user",
    description: "description",
    date: new Date("05/22/2025"),
    teacher_id: 1,
    users: [1],
    createdAt: new Date("05/22/2025"),
    updatedAt: new Date("05/22/2025"),
  }
  const mockedTeacher: Teacher = {
    id: 1,
    lastName: "Teacher",
    firstName: "Teacher",
    createdAt: new Date("05/22/2025"),
    updatedAt: new Date("05/22/2025")
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        MatSnackBarModule,
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent],
      providers: [TeacherService, {provide: SessionService, useValue: mockSessionService}, {
        provide: ActivatedRoute, useValue: {snapshot: {paramMap: {get: () => 1,},},},
      }, {
        provide: MatSnackBar,
        useValue: mockedMatSnackBar
      }],
    })
      .compileComponents();
    sessionApiService = TestBed.inject(SessionApiService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    teacherService = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

  });


  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should go back', () => {
    const historySpy = jest.spyOn(window.history, 'back');
    component.back();
    expect(historySpy).toHaveBeenCalled();
  })

  it('should delete', () => {
    fixture.ngZone?.run(() => {
      const sessionApiServiceSpy = jest.spyOn(sessionApiService, "delete");
      const navigateSpy = jest.spyOn(router, 'navigate');
      const matSnackBarSpy = jest.spyOn(matSnackBar, 'open');
      component.delete();
      const deleteRequest = httpMock.expectOne({url: "api/session/1", method: "DELETE"});
      deleteRequest.flush(null);

      expect(sessionApiServiceSpy).toHaveBeenCalled();
      expect(matSnackBarSpy).toHaveBeenCalledWith("Session deleted !", "Close", {"duration": 3000});
      expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
    })
  });
  it('should participate', () => {

    const sessionApiServiceSpy = jest.spyOn(sessionApiService, "participate");
    component.sessionId = "1";
    component.userId = "1";

    component.participate();
    const participateRequest = httpMock.match({url: "api/session/1/participate/1", method: "POST"});
    participateRequest.forEach((req) => req.flush(null));

    expect(sessionApiServiceSpy).toHaveBeenCalledWith("1", "1");
  })
  it('should unParticipate', () => {
    const sessionApiServiceSpy = jest.spyOn(sessionApiService, "unParticipate");
    component.sessionId = "1";
    component.userId = "1";
    component.unParticipate();
    const unParticipateRequest = httpMock.match({url: "api/session/1/participate/1", method: "DELETE"});
    unParticipateRequest.forEach((req) => req.flush(null));
    expect(sessionApiServiceSpy).toHaveBeenCalledWith("1", "1");
  });

  it('should test the fetchSession method', () => {
    const sessionApiServiceSpy = jest.spyOn(sessionApiService, "detail");
    const teacherServiceSpy = jest.spyOn(teacherService, "detail");
    component["fetchSession"]();

    const sessionDetailsRequest = httpMock.match({url: 'api/session/1', method: "GET"});
    sessionDetailsRequest[0].flush(mockedSession);
    const teacherDetailsRequest = httpMock.match({url: 'api/teacher/1', method: "GET"});
    teacherDetailsRequest[0].flush(mockedTeacher);

    expect(sessionApiServiceSpy).toHaveBeenCalled();
    expect(teacherServiceSpy).toHaveBeenCalled();
    expect(component.teacher).toStrictEqual(mockedTeacher);
  })
});

