export interface LoanAuditLogDto {
  id: number;
  performedBy: string;
  description: string;
  status: string;
  timestamp: string;
}