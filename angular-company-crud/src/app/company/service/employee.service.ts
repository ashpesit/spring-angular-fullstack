import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IBaseResponse } from '../interface/baseResponse';
import { IEmployee } from '../interface/employee';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private readonly PATH = "/v1/employee"
  constructor(private readonly apiService: ApiService) { }

  getEmployeeList(pageNo: number, limit: number): Observable<IBaseResponse<IEmployee>> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("pageNo", pageNo);
    queryParams = queryParams.append("limit", limit);
    return this.apiService.get<IEmployee>(this.PATH, queryParams);
  }

  createEmployee(Employee: IEmployee): Observable<IBaseResponse<IEmployee>> {
    return this.apiService.post<IEmployee>(this.PATH, Employee);
  }
  updateEmployee(Employee: IEmployee): Observable<IBaseResponse<IEmployee>> {
    return this.apiService.put<IEmployee>(this.PATH, Employee);
  }
  deleteEmployee(employeeId: number): Observable<IBaseResponse<Object>> {
    return this.apiService.delete<Object>(this.PATH + '/' + employeeId);
  }
  countEmployee(): Observable<IBaseResponse<number>> {
    return this.apiService.get<number>(this.PATH + '/count');
  }
}
