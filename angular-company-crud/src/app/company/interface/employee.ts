import { IDepartment } from './department';
export interface IEmployee {
  id: number;
  firstName: string;
  lastName: string;
  emailAddress: string;
  departments: IDepartment[];
  departmentNames: string | null;
}
