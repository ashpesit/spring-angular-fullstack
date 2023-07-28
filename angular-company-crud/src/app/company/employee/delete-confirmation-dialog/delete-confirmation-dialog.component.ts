import { Component } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { ToastrService } from 'ngx-toastr';
import { IEmployee } from '../../interface/employee';
import { EmployeeService } from '../../service/employee.service';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-delete-confirmation-dialog',
  templateUrl: './delete-confirmation-dialog.component.html',
  styleUrls: ['./delete-confirmation-dialog.component.css'],
})
export class DeleteConfirmationDialogComponent {
  public onClose: Subject<boolean> = new Subject();
  public data!: IEmployee;
  constructor(
    private bsModalService: BsModalService,
    private employeeService: EmployeeService,
    private toastrService: ToastrService
  ) {}

  closeModal(): void {
    this.bsModalService.hide();
  }

  deleteEmployee(id: number) {
    if (id) {
      this.employeeService.deleteEmployee(id).subscribe({
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
}
