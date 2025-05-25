import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RouterTestingModule} from '@angular/router/testing';
import {expect} from '@jest/globals';
import {SessionService} from 'src/app/services/session.service';
import {SessionApiService} from '../../services/session-api.service';

import {FormComponent} from './form.component';
import {Router} from "@angular/router";
import {of} from "rxjs";
import {Session} from "../../interfaces/session.interface";

describe('FormComponent', () => {

  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let sessionApiService: SessionApiService;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }
  const mockedMatSnackBar = {
    open: jest.fn(),
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          {path: 'sessions', component: FormComponent},
          {path: 'update', component: FormComponent}
        ]),
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        {provide: SessionService, useValue: mockSessionService},
        {provide: MatSnackBar, useValue: mockedMatSnackBar},
      ],
      declarations: [FormComponent]
    })
      .compileComponents();
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create the form component', () => {
    expect(component).toBeTruthy();
  });
  describe("should init the component", () => {
    it('should redirect to sessions if the user is not admin', () => {
      fixture.ngZone?.run(async () => {
        const navigateSpy = jest.spyOn(router, 'navigate');
        mockSessionService.sessionInformation.admin = false;
        component.ngOnInit();
        expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
      });
    });
    it('should not redirect to sessions if the user is admin', () => {
      fixture.ngZone?.run(async () => {
        const navigateSpy = jest.spyOn(router, 'navigate');
        mockSessionService.sessionInformation.admin = true;
        component.ngOnInit();
        expect(navigateSpy).not.toHaveBeenCalledWith(['sessions']);
      })
    });
    it('should init form if url include update ', () => {
      fixture.ngZone?.run(async () => {
        const mockedSession: Session = {
          users: [],
          name: '',
          description: '',
          date: new Date(),
          createdAt: new Date(),
          updatedAt: new Date(),
          teacher_id: 0,
        };
        const sessionApiServiceSpy = jest.spyOn(sessionApiService, "detail").mockReturnValue(of(mockedSession));
        await router.navigate(['/update']);
        mockSessionService.sessionInformation.admin = true;
        component.ngOnInit();
        expect(component.onUpdate).toBeTruthy();
        expect(sessionApiServiceSpy).toHaveBeenCalled();
        expect(component["initForm"]).toHaveBeenCalledWith(mockedSession)
      })
    })
  });
  describe("should submit the form", () => {
    const mockedSession: Session = {
      name: 'Test',
      description: 'Test description',
      date: new Date(),
      teacher_id: 1,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date(),
    };
    it('should create a session', () => {
      fixture.ngZone?.run(async () => {

        const sessionApiServiceSpy = jest.spyOn(sessionApiService, "create").mockReturnValue(of(mockedSession));
        const navigateSpy = jest.spyOn(router, 'navigate');
        const matSnackBarSpy = jest.spyOn(mockedMatSnackBar, 'open');
        component.sessionForm?.setValue({
          name: mockedSession.name,
          description: mockedSession.description,
          date: mockedSession.date,
          teacher_id: mockedSession.teacher_id
        });
        component.onUpdate = false;
        component.submit();
        expect(component.sessionForm).toBeDefined();
        expect(sessionApiServiceSpy).toHaveBeenCalledWith(mockedSession);
        expect(matSnackBarSpy).toHaveBeenCalledWith("Session created !", "Close", {"duration": 3000});
        expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
      })
    })
    it('should update a session', () => {
      fixture.ngZone?.run(async () => {
        const sessionApiServiceSpy = jest.spyOn(sessionApiService, "update").mockReturnValue(of(mockedSession));
        const navigateSpy = jest.spyOn(router, 'navigate');
        const matSnackBarSpy = jest.spyOn(mockedMatSnackBar, 'open');
        component.sessionForm?.setValue({
          name: mockedSession.name,
          description: mockedSession.description,
          date: mockedSession.date,
          teacher_id: mockedSession.teacher_id
        });
        component.onUpdate = true;
        component.submit();
        expect(component.sessionForm).toBeDefined();
        expect(sessionApiServiceSpy).toHaveBeenCalledWith(mockedSession);
        expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
        expect(matSnackBarSpy).toHaveBeenCalledWith("Session updated !", "Close", {"duration": 3000});
      })
    });
  })
  describe("should not submit the form", () => {
    it('should not submit request with invalid form', () => {

      const sessionApiServiceSpy = jest.spyOn(sessionApiService, "create");
      const navigateSpy = jest.spyOn(router, 'navigate');
      const matSnackBarSpy = jest.spyOn(mockedMatSnackBar, 'open');
      component.sessionForm = undefined;
      component.submit();
      expect(sessionApiServiceSpy).not.toHaveBeenCalledWith();
      expect(navigateSpy).not.toHaveBeenCalledWith();
      expect(matSnackBarSpy).not.toHaveBeenCalledWith();
    })
  });
});

