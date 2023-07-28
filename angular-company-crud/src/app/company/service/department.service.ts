import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { IDepartment } from '../interface/department';
import { IBaseResponse } from '../interface/baseResponse';
import { HttpParams } from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class DepartmentService {
  private readonly PATH = "/v1/department"
  constructor(private readonly apiService: ApiService) { }

  getDepartmentList(pageNo: number, limit: number): Observable<IBaseResponse<IDepartment>> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("pageNo", pageNo);
    queryParams = queryParams.append("limit", limit);
    return this.apiService.get<IDepartment>(this.PATH, queryParams);
  }

  createDepartment(department: IDepartment): Observable<IBaseResponse<IDepartment>> {
    return this.apiService.post<IDepartment>(this.PATH, department);
  }
  updateDepartment(department: IDepartment): Observable<IBaseResponse<IDepartment>> {
    return this.apiService.put<IDepartment>(this.PATH, department);
  }
  deleteDepartment(departmentId: number): Observable<IBaseResponse<Object>> {
    return this.apiService.delete<Object>(this.PATH + '/' + departmentId);
  }
  countDepartment(): Observable<IBaseResponse<number>> {
    return this.apiService.get<number>(this.PATH + '/count');
  }
}

