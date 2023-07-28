import { Component, OnInit } from '@angular/core';
import { IDepartment } from '../interface/department';
import { faTrash, faPencil } from '@fortawesome/free-solid-svg-icons';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { IBaseResponse } from '../interface/baseResponse';
import { FormDepartmentComponent } from './form-department/form-department.component';
import { DepartmentService } from '../service/department.service';
import { DeleteDepartmentConfirmComponent } from './delete-department-confirm/delete-department-confirm.component';
import { PageEvent } from '@angular/material/paginator';
import { NumberInput } from '@angular/cdk/coercion';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-department',
  templateUrl: './department.component.html',
  styleUrls: ['./department.component.css'],
})
export class DepartmentComponent implements OnInit {
  modalRef?: BsModalRef;
  faTrash = faTrash;
  faPencil = faPencil;
  private readonly PATH = '/v1/department';
  pageEvent: PageEvent = new PageEvent();
  totalRows: NumberInput = 0;
  currentPage: number = 0;
  pageSize: number = 5;
  pageSizeOptions: readonly number[] = [5, 10, 20];
  count: number = 100;
  constructor(
    private readonly departmentService: DepartmentService,
    private modalService: BsModalService
  ) {}
  displayedColumns: string[] = [
    'id',
    'name',
    'readOnly',
    'required',
    'actions',
  ];
  dataSource: MatTableDataSource<IDepartment> = new MatTableDataSource();
  ngOnInit(): void {
    this.refreshTable();
  }
  refreshTable() {
    this.setTotalRowCount();
    this.getDepartments(this.currentPage, this.pageSize);
  }
  setTotalRowCount() {
    this.departmentService
      .countDepartment()
      .subscribe((response: IBaseResponse<number>) => {
        if (response.errorStatus === 0) {
          this.totalRows = Array.isArray(response.data) ? 0 : response.data;
        }
      });
  }
  handlePageEvent(e: PageEvent) {
    this.pageEvent = e;
    this.pageSize = e.pageSize;
    this.currentPage = e.pageIndex;
    this.getDepartments(this.currentPage, this.pageSize);
  }

  createDepartment(): void {
    this.modalRef = this.modalService.show(FormDepartmentComponent, {
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
  }

  getDepartments(pageNo: number, pageSize: number) {
    this.departmentService
      .getDepartmentList(pageNo, pageSize)
      .subscribe((response: IBaseResponse<IDepartment>) => {
        if (response.errorStatus === 0) {
          if (!Array.isArray(response.data)) {
            response.data = [response.data];
          }
          this.dataSource = new MatTableDataSource(response.data);
        }
      });
  }

  updateDepartment($event: IDepartment): void {
    this.modalRef = this.modalService.show(FormDepartmentComponent, {
      initialState: {
        data: $event,
      },
      animated: true,
    });
    this.modalRef.content.onClose.subscribe((response: boolean) => {
      if (response) {
        this.getDepartments(this.currentPage, this.pageSize);
      }
    });
    console.log('update');
  }

  deleteDepartment($event: IDepartment): void {
    this.modalRef = this.modalService.show(DeleteDepartmentConfirmComponent, {
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
}
