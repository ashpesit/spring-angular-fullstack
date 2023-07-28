export interface IBaseResponse<T> {
    errorStatus: number;
    message: string;
    data: T | T[];
}