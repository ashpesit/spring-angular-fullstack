import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EmployeeComponent } from './company/employee/employee.component';
import { DepartmentComponent } from './company/department/department.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';

const routes: Routes = [
  {
    title: 'Employee',
    path: 'employee',
    component: EmployeeComponent, // Use the component in the route
  },{
    title: 'Department',
    path: 'department',
    component: DepartmentComponent, // Use the component in the route
  },
  { path: '',   redirectTo: '/department', pathMatch: 'full' },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
