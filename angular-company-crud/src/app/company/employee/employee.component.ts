import { Component, OnInit } from '@angular/core';
import { IEmployee } from '../interface/employee';
import { faTrash, faPencil } from '@fortawesome/free-solid-svg-icons';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { IBaseResponse } from '../interface/baseResponse';
import { EmployeeService } from '../service/employee.service';
import { DeleteConfirmationDialogComponent } from './delete-confirmation-dialog/delete-confirmation-dialog.component';
import { FormEmployeeComponent } from './form-employee/form-employee.component';
import { IDepartment } from '../interface/department';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css'],
})
export class EmployeeComponent implements OnInit {
  modalRef?: BsModalRef;
  faTrash = faTrash;
  faPencil = faPencil;

  private readonly PATH = '/v1/Employee';
  pageEvent: PageEvent = new PageEvent();
  totalRows: number = 0;
  currentPage: number = 0;
  pageSize: number = 5;
  pageSizeOptions: readonly number[] = [5, 10, 20];
  constructor(
    private readonly employeeService: EmployeeService,
    private modalService: BsModalService
  ) {}
  displayedColumns: string[] = [
    'id',
    'firstName',
    'lastName',
    'emailAddress',
    'departments',
    'actions',
  ];
  dataSource: IEmployee[] = [];
  ngOnInit(): void {
    this.refreshTable();
  }
  refreshTable() {
    this.getEmployee(this.currentPage, this.pageSize);
    this.setTotalRowCount();
  }
  handlePageEvent(e: PageEvent) {
    this.pageEvent = e;
    this.pageSize = e.pageSize;
    this.currentPage = e.pageIndex;
    this.getEmployee(this.currentPage, this.pageSize);
  }
  setTotalRowCount() {
    this.employeeService
      .countEmployee()
      .subscribe((response: IBaseResponse<number>) => {
        if (response.errorStatus === 0) {
          this.totalRows = Array.isArray(response.data) ? 0 : response.data;
        }
      });
  }
  createEmployee(): void {
    try {
      this.modalRef = this.modalService.show(FormEmployeeComponent, {
        initialState: {
          data: undefined,
        },
        animated: true,
      });
      this.modalRef.content.onClose.subscribe((response: boolean) => {
        if (response) {
          this.refreshTable();
        }
      });
      console.log('create');
    } catch (err) {
      console.log(err);
    }
  }

  getEmployee(pageNo: number, pageSize: number) {
    this.employeeService
      .getEmployeeList(pageNo, pageSize)
      .subscribe((response: IBaseResponse<IEmployee>) => {
        if (response.errorStatus === 0) {
          if (!Array.isArray(response.data)) {
            response.data = [response.data];
          }
          // this.dataSource = new MatTableDataSource(response.data);
          response.data.forEach(
            (d) =>
              (d.departmentNames = this.departmentsToStringNames(d.departments))
          );
          this.dataSource = response.data;
        }
      });
  }

  updateEmployee($event: IEmployee): void {
    this.modalRef = this.modalService.show(FormEmployeeComponent, {
      initialState: {
        data: $event,
      },
      animated: true,
    });
    this.modalRef.content.onClose.subscribe((response: boolean) => {
      if (response) {
        this.getEmployee(this.currentPage, this.pageSize);
      }
    });
    console.log('update');
  }

  deleteEmployee($event: IEmployee): void {
    this.modalRef = this.modalService.show(DeleteConfirmationDialogComponent, {
      initialState: {
        data: $event,
      },
      animated: true,
    });
    this.modalRef.content.onClose.subscribe((response: boolean) => {
      if (response) {
        this.refreshTable();
      }
    });
    console.log('delete');
  }

  departmentsToString(departments: IDepartment[] | string): string {
    if (Array.isArray(departments)) {
      return departments
        .map((dept: IDepartment) => dept.id.toString())
        .join(',');
    } else {
      return departments;
    }
  }
  departmentsToStringNames(departments: IDepartment[]): string {
    return departments.map((dept: IDepartment) => dept.name).join(', ');
  }
}
