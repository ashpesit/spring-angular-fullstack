import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { BsModalService } from 'ngx-bootstrap/modal';
import { ToastrService } from 'ngx-toastr';
import { IDepartment } from '../../interface/department';
import { DepartmentService } from '../../service/department.service';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-form-department',
  templateUrl: './form-department.component.html',
  styleUrls: ['./form-department.component.css'],
})
export class FormDepartmentComponent implements OnInit {
  public data?: IDepartment;
  public onClose: Subject<boolean> = new Subject();
  public title!: string;
  formDepartment: FormGroup = new FormGroup({
    id: new FormControl(''),
    name: new FormControl('', [Validators.required]),
    readOnly: new FormControl(false, [Validators.required]),
    required: new FormControl(false, [Validators.required]),
  });
  showIdInputField: boolean = true;
  constructor(
    private bsModalService: BsModalService,
    private departmentService: DepartmentService,
    private toastrService: ToastrService
  ) {}
  ngOnInit(): void {
    if (this.data) {
      this.title = 'Update Department';
      this.showIdInputField = true;
      this.doInitFormUpdate();
    } else {
      this.title = 'Create Department';
      this.showIdInputField = false;
    }
  }
  doInitFormUpdate() {
    this.formDepartment.setValue({
      id: this.data?.id,
      name: this.data?.name,
      readOnly: this.data?.readOnly,
      required: this.data?.required,
    });
  }
  closeModal(): void {
    this.bsModalService.hide();
  }
  submit() {
    if (this.data) {
      this.doUpdate(this.formDepartment.value);
    } else {
      this.doCreate(this.formDepartment.value);
    }
  }
  doCreate(body: IDepartment) {
    console.log(body);
    this.departmentService.createDepartment(body).subscribe({
      next: (res) => {
        console.log(res);
        this.toastrService.success('Success', 'Success Create!');
        this.onClose.next(true);
        this.closeModal();
      },
      error: (err) => {
        this.toastrService.error(err.error.message, 'Failed');
      },
      complete: () => console.info('complete'),
    });
  }
  doUpdate(body: IDepartment) {
    this.departmentService.updateDepartment(body).subscribe({
      next: (res) => {
        console.log(res);
        this.toastrService.success('Success', 'Success Update!');
        this.onClose.next(true);
        this.closeModal();
      },
      error: (err) => {
        this.toastrService.error(err.error.message, 'Failed');
      },
      complete: () => console.info('complete'),
    });
  }
}
