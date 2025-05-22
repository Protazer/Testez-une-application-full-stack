import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import {SessionApiService} from "../../services/session-api.service";
import {of} from "rxjs";
import {Router} from "@angular/router";
import {Session} from "../../interfaces/session.interface";
import {Teacher} from "../../../../interfaces/teacher.interface";
import {TeacherService} from "../../../../services/teacher.service";


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionApiService: SessionApiService;
  let matSnackBar:MatSnackBar;
  let router: Router;
  let teacherService: TeacherService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  const mockedMatSnackBar = {
    open: (message: string, action?: string | undefined, opts?: any) => {},
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        MatSnackBarModule,
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent],
      providers: [{ provide: SessionService, useValue: mockSessionService }, {provide: MatSnackBar, useValue: mockedMatSnackBar}],
    })
      .compileComponents();
      sessionApiService = TestBed.inject(SessionApiService);
      matSnackBar = TestBed.inject(MatSnackBar);
      router = TestBed.inject(Router);
      teacherService = TestBed.inject(TeacherService);
      fixture = TestBed.createComponent(DetailComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should test the back method', () => {
    const historySpy = jest.spyOn(window.history, 'back');
    component.back();
    expect(historySpy).toHaveBeenCalled();
  })

  it('should test the delete method',  () => {
    fixture.ngZone?.run(() => {
      const sessionApiserviceSpy = jest.spyOn(sessionApiService, "delete").mockReturnValue(of(null));
      const navigateSpy = jest.spyOn(router, 'navigate');
      const matSnackBarSpy = jest.spyOn(matSnackBar, 'open');
      component.delete();
      expect(sessionApiserviceSpy).toHaveBeenCalled();
      expect(matSnackBarSpy).toHaveBeenCalledWith("Session deleted !", "Close", {"duration": 3000});
      expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
    })
  });
  it('should test the participate method',  () => {
    const sessionApiserviceSpy = jest.spyOn(sessionApiService, "participate").mockReturnValue(of(undefined));
    component.sessionId = "sessionId";
    component.userId = "userId";

    component.participate();
    expect(sessionApiserviceSpy).toHaveBeenCalledWith("sessionId", "userId");
  })
  it('should test the unParticipate method',  () => {
    const sessionApiserviceSpy = jest.spyOn(sessionApiService, "unParticipate").mockReturnValue(of(undefined));
    component.sessionId = "sessionId";
    component.userId = "userId";

    component.unParticipate();
    expect(sessionApiserviceSpy).toHaveBeenCalledWith("sessionId", "userId");
  });

  it('should test the fetchSession method',  () => {
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
    const mockedTeacher:Teacher = {
      id:1,
      lastName:"Teacher",
      firstName:"Teacher",
      createdAt:new Date("05/22/2025"),
      updatedAt:new Date("05/22/2025")
    }
    const sessionApiserviceSpy = jest.spyOn(sessionApiService, "detail").mockReturnValue(of(mockedSession));
    const teacherServiceSpy = jest.spyOn(teacherService, "detail").mockReturnValue(of(mockedTeacher));
    component["fetchSession"]();
    expect(sessionApiserviceSpy).toHaveBeenCalled();
    expect(teacherServiceSpy).toHaveBeenCalled();
    expect(component.teacher).toStrictEqual(mockedTeacher);
  })
});

