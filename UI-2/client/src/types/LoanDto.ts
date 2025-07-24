export interface LoanDto {
  id: number;
  amount: number;
  purpose: string;
  status: string;
  applicationDate: string;
  approvalDate?: string;
  disbursementDate?: string;
  repaymentDate?: string;
  memberId: number;
  memberName: string;
  createdDate: string;
  groupId: number;
}