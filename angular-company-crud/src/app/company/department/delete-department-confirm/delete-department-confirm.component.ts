import { Component } from '@angular/core';
import { IDepartment } from '../../interface/department';
import { BsModalService } from 'ngx-bootstrap/modal';
import { ToastrService } from 'ngx-toastr';
import { DepartmentService } from '../../service/department.service';
import { Subject } from 'rxjs/internal/Subject';

@Component({
  selector: 'app-delete-department-confirm',
  templateUrl: './delete-department-confirm.component.html',
  styleUrls: ['./delete-department-confirm.component.css'],
})
export class DeleteDepartmentConfirmComponent {
  public onClose: Subject<boolean> = new Subject();
  public data!: IDepartment;
  constructor(
    private bsModalService: BsModalService,
    private departmentService: DepartmentService,
    private toastrService: ToastrService
  ) {}

  closeModal(): void {
    this.bsModalService.hide();
  }

  deleteDepartment(id: number) {
    if (id) {
      this.departmentService.deleteDepartment(id).subscribe({
        next: (res) => {
          console.log(res);
          this.toastrService.success('Success', 'Delete Successful!');
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
}
