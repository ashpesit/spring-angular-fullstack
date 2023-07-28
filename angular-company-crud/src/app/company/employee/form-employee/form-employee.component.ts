import { Component, OnInit } from '@angular/core';
import {
  FormGroup,
  FormControl,
  Validators,
  FormBuilder,
  ValidatorFn,
  ValidationErrors,
  AbstractControl,
} from '@angular/forms';
import { BsModalService } from 'ngx-bootstrap/modal';
import { ToastrService } from 'ngx-toastr';
import { IEmployee } from '../../interface/employee';
import { EmployeeService } from '../../service/employee.service';
import { Subject } from 'rxjs';
import { IDepartment } from '../../interface/department';
import { DepartmentService } from '../../service/department.service';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
@Component({
  selector: 'app-form-employee',
  templateUrl: './form-employee.component.html',
  styleUrls: ['./form-employee.component.css'],
})
export class FormEmployeeComponent implements OnInit {
  constructor(
    private bsModalService: BsModalService,
    private readonly employeeService: EmployeeService,
    private toastrService: ToastrService,
    private readonly departmentService: DepartmentService,
    private formBuilder: FormBuilder
  ) {
    this.createForm();
  }
  public onClose: Subject<boolean> = new Subject();
  public data?: IEmployee;
  public title!: string;
  formEmployee!: FormGroup;
  dropdownSettings: IDropdownSettings = {};
  // = new FormGroup({
  //   id: new FormControl(this.data?.id),
  //   firstName: new FormControl(this.data?.firstName, [Validators.required]),
  //   lastName: new FormControl(this.data?.lastName, [Validators.required]),
  //   emailAddress: new FormControl(this.data?.emailAddress, [
  //     Validators.required,
  //     Validators.email,
  //   ]),
  //   departments: new FormControl<IDepartment[]>([]),
  // });
  createForm() {
    this.formEmployee = this.formBuilder.group({
      id: new FormControl(this.data?.id),
      firstName: new FormControl(this.data?.firstName, [Validators.required]),
      lastName: new FormControl(this.data?.lastName, [Validators.required]),
      emailAddress: new FormControl(this.data?.emailAddress, [
        Validators.required,
        Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$'),
      ]),
      departments: new FormControl<IDepartment[]>(
        [],
        [Validators.required, this.requiredDepartmentValidator()]
      ),
    });
  }
  showEmployeeId: boolean = false;

  departmentList: IDepartment[] = [];
  requiredDepartmentList: IDepartment[] = [];
  selectedDepartmentList: IDepartment[] = [];
  ngOnInit(): void {
    this.departmentService.getDepartmentList(0, 100).subscribe({
      next: (res) => {
        if (res.data && Array.isArray(res.data)) {
          this.requiredDepartmentList = res.data.filter((d) => d.required);
          this.departmentList = res.data;
          if (this.data) {
            this.title = 'Update Employee';
            this.showEmployeeId = true;
            this.doInitFormUpdate();
          } else {
            this.showEmployeeId = false;
            this.title = 'Create Employee';
            this.selectedDepartmentList = res.data.filter((d) => d.required);
          }
        }
      },
    });
    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'name',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: true,
    };
  }
  doInitFormUpdate() {
    try {
      this.formEmployee.setValue({
        id: this.data?.id,
        firstName: this.data?.firstName,
        lastName: this.data?.lastName,
        emailAddress: this.data?.emailAddress,
        departments: this.data?.departments,
      });
      if (this.data?.departments) {
        let deptIds: number[] = [];
        this.data?.departments.forEach((item) => {
          deptIds.push(item.id);
        });
        this.selectedDepartmentList = this.departmentList.filter((d) =>
          deptIds.includes(d.id)
        );
      }
    } catch (err) {
      console.log(err);
    }
  }
  closeModal(): void {
    this.bsModalService.hide();
  }
  submit() {
    if (this.data) {
      this.doUpdate(this.formEmployee.value);
    } else {
      this.doCreate(this.formEmployee.value);
    }
  }
  doCreate(body: IEmployee) {
    this.employeeService.createEmployee(body).subscribe({
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
  doUpdate(body: IEmployee) {
    this.employeeService.updateEmployee(body).subscribe({
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
  onItemSelect(item: any) {
    console.log(item);
  }
  onSelectAll(items: any) {
    console.log(items);
  }
  requiredDepartmentValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;

      if (!value || !Array.isArray(value)) {
        return null;
      }
      let deptIds: number[] = [];
      this.requiredDepartmentList.forEach((item) => {
        deptIds.push(item.id);
      });
      const allRequiredDeptAdded: boolean = deptIds.every((d) =>
        value.some((dep) => dep.id === d)
      );

      return !allRequiredDeptAdded ? { reqDeptBoolean: true } : null;
    };
  }
}
