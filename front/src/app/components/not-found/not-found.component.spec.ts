import {ComponentFixture, TestBed} from '@angular/core/testing';
import {expect} from '@jest/globals';
import {NotFoundComponent} from './not-found.component';

describe('NotFoundComponent', () => {
  let component: NotFoundComponent;
  let fixture: ComponentFixture<NotFoundComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NotFoundComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(NotFoundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the NotFoundComponent', () => {
    expect(component).toBeTruthy();
  });
});
