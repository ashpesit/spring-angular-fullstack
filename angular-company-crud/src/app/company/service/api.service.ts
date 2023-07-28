import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { Observable } from 'rxjs';
import { IBaseResponse } from '../interface/baseResponse';

@Injectable({
    providedIn: 'root'
})
export class ApiService {
    private readonly API_URL = 'http://localhost:8080/api';
    constructor(private http: HttpClient) { }

    public get<T>(path: string, routerParams?: Params): Observable<IBaseResponse<T>> {
        let queryParams: Params = {};
        if (routerParams) {
            queryParams = routerParams;
        }
        console.log(queryParams);
        return this.http.get<IBaseResponse<T>>(this.path(path), { params: queryParams });
    }

    public put<T>(path: string, body: T): Observable<IBaseResponse<T>> {
        return this.http.put<IBaseResponse<T>>(this.path(path), body);
    }

    public post<T>(path: string, body: T): Observable<IBaseResponse<T>> {
        return this.http.post<IBaseResponse<T>>(this.path(path), body);
    }

    public delete<T>(path: string): Observable<IBaseResponse<T>> {
        return this.http.delete<IBaseResponse<T>>(this.path(path));
    }


    private path(path: string): string {
        return `${this.API_URL}${path}`;
    }
}