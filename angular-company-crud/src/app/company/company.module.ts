import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmployeeComponent } from './employee/employee.component';
import { DepartmentComponent } from './department/department.component';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { FormDepartmentComponent } from './department/form-department/form-department.component';
import { DeleteDepartmentConfirmComponent } from './department/delete-department-confirm/delete-department-confirm.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FormEmployeeComponent } from './employee/form-employee/form-employee.component';
import { DeleteConfirmationDialogComponent } from './employee/delete-confirmation-dialog/delete-confirmation-dialog.component';
import { MatPaginatorModule } from '@angular/material/paginator';
// import { NgSelectModule } from '@ng-select/ng-select';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
@NgModule({
  declarations: [
    EmployeeComponent,
    DepartmentComponent,
    FormDepartmentComponent,
    DeleteDepartmentConfirmComponent,
    FormEmployeeComponent,
    DeleteConfirmationDialogComponent,
  ],
  imports: [
    CommonModule,
    MatTableModule,
    MatIconModule,
    FormsModule,
    ReactiveFormsModule,
    MatPaginatorModule,
    MatSelectModule,
    // NgSelectModule,
    NgMultiSelectDropDownModule.forRoot(),
  ],
})
export class CompanyModule {}
