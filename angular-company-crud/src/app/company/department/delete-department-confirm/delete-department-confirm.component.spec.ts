import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteDepartmentConfirmComponent } from './delete-department-confirm.component';

describe('DeleteDepartmentConfirmComponent', () => {
  let component: DeleteDepartmentConfirmComponent;
  let fixture: ComponentFixture<DeleteDepartmentConfirmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeleteDepartmentConfirmComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteDepartmentConfirmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
