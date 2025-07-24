import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useI18n } from '@/hooks/use-i18n';
import { useAuth } from '@/hooks/use-auth';
import { useToast } from '@/hooks/use-toast';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { PiggyBank, FileText, Clock, TrendingUp, Plus, Eye, EyeOff, XCircle } from 'lucide-react';
import { Link } from 'wouter';

import { getMyLoans, getLoanLogs } from '@/services/loanService';
import { fetchMyDeposits } from '@/services/savingService'; 
import { cancelLoan } from '@/services/loanService'; 
import { LoanDto, LoanAuditLogDto } from '@/types';
import MainFrame from "@/components/layout/MainFrame";

export default function MemberDashboard() {
  const { t } = useI18n();
  const { user } = useAuth();
  const { toast } = useToast();
  const queryClient = useQueryClient();
  const [expandedLoanId, setExpandedLoanId] = useState<number | null>(null);

  const { data: savingDeposits, isLoading: savingsLoading } = useQuery({
	queryKey: ['/api/saving-deposits'],
	queryFn: fetchMyDeposits,
  });

  const { data: loanApplications, isLoading: loansLoading } = useQuery<LoanDto[]>({
    queryKey: ['/api/loans/my'],
    queryFn: () => getMyLoans().then(res => res.data),
  });

  const { data: auditLogs = [] } = useQuery<LoanAuditLogDto[]>({
    queryKey: ['loan-logs', expandedLoanId],
    queryFn: () => expandedLoanId ? getLoanLogs(expandedLoanId).then(res => res.data) : Promise.resolve([]),
    enabled: !!expandedLoanId,
  });

  const cancelLoanMutation = useMutation({
    mutationFn: (loanId: number) => cancelLoan(loanId),
    onSuccess: () => {
      toast({ title: 'Loan Cancelled', description: 'Your loan was cancelled successfully.' });
      queryClient.invalidateQueries({ queryKey: ['/loans/my'] });
    },
    onError: () => {
      toast({ title: 'Failed to cancel', description: 'Please try again later.', variant: 'destructive' });
    }
  });

  const toggleLogs = (loanId: number) => {
    setExpandedLoanId(prev => (prev === loanId ? null : loanId));
  };

  const totalSavings = savingDeposits?.reduce((sum: number, deposit: any) => sum + parseFloat(deposit.amount), 0) || 0;
  const activeLoans = loanApplications?.filter((loan) => loan.status !== 'CANCELLED') || [];

  const stats = [
    {
      title: t('dashboard.totalSavings'),
      value: `₹${totalSavings.toLocaleString()}`,
      icon: PiggyBank,
      color: 'from-emerald-400 to-emerald-600',
      change: '+₹500 this month'
    },
    {
      title: t('dashboard.activeLoan'),
      value: activeLoans.length ? `₹${parseFloat(activeLoans[0].amount).toLocaleString()}` : 'No active loan',
      icon: FileText,
      color: 'from-blue-400 to-blue-600',
      change: activeLoans.length ? t('dashboard.dueDate') : 'Apply for a loan'
    },
    {
      title: t('dashboard.remainingInstallments'),
      value: activeLoans.length,
      icon: Clock,
      color: 'from-amber-400 to-amber-600',
      change: `Approx. ₹${activeLoans.reduce((sum, loan) => sum + loan.amount * 0.1, 0).toLocaleString()} pending`
    },
    {
      title: t('dashboard.repaymentScore'),
      value: '95%',
      icon: TrendingUp,
      color: 'from-purple-400 to-purple-600',
      change: t('dashboard.excellent')
    }
  ];

  return (
   <MainFrame>
    <div className="p-6 space-y-8">
      <div className="space-y-2">
        <h1 className="text-3xl font-bold">{t('dashboard.member.title')}</h1>
        <p className="text-muted-foreground">{t('dashboard.member.subtitle')}</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {stats.map((stat, index) => (
          <Card key={index}>
            <CardContent className="p-6">
              <div className="flex justify-between items-center">
                <div>
                  <p className="text-sm font-medium text-muted-foreground">{stat.title}</p>
                  <p className="text-2xl font-bold">{stat.value}</p>
                  <p className="text-xs text-green-600">{stat.change}</p>
                </div>
                <div className={`w-12 h-12 bg-gradient-to-br ${stat.color} rounded-lg flex items-center justify-center`}>
                  <stat.icon className="w-6 h-6 text-white" />
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

     <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
		  {activeLoans.map((loan) => (
			<Card key={loan.id}>
			  <CardHeader>
				<CardTitle>Loan #{loan.id}</CardTitle>
			  </CardHeader>
			  <CardContent className="space-y-3">
				<div>
				  <p className="text-sm text-muted-foreground">Amount</p>
				  <p className="text-lg font-bold">₹{parseFloat(loan.amount).toLocaleString()}</p>
				</div>
				<div>
				  <p className="text-sm text-muted-foreground">Status</p>
				  <p className="text-sm">{loan.status}</p>
				</div>
				<div className="flex flex-wrap gap-2">
				  <Button variant="ghost" size="sm" onClick={() => toggleLogs(loan.id)}>
					{expandedLoanId === loan.id ? (
					  <><EyeOff className="w-4 h-4 mr-1" /> Hide Logs</>
					) : (
					  <><Eye className="w-4 h-4 mr-1" /> View Logs</>
					)}
				  </Button>
				  <Button variant="destructive" size="sm" onClick={() => cancelLoanMutation.mutate(loan.id)}>
					<XCircle className="w-4 h-4 mr-1" /> Cancel
				  </Button>
				</div>

				{expandedLoanId === loan.id && (
				  <div className="ml-2 border-l pl-4 space-y-2 text-sm text-muted-foreground">
					{auditLogs.length === 0 ? (
					  <p>No audit logs available.</p>
					) : (
					  auditLogs.map(log => (
						<div key={log.id}>
						  <p>{log.description}</p>
						  <p className="text-xs">
							{log.performedBy} – {new Date(log.timestamp).toLocaleString()}
						  </p>
						</div>
					  ))
					)}
				  </div>
				)}
			  </CardContent>
			</Card>
		  ))}
		</div>

      <div className="flex flex-wrap gap-4">
        <Link href="/savings">
          <Button className="gradient-bg">
            <Plus className="w-4 h-4 mr-2" />
            New Deposit
          </Button>
        </Link>
        <Link href="/loans/apply">
          <Button variant="outline">
            <FileText className="w-4 h-4 mr-2" />
            Apply for Loan
          </Button>
        </Link>
      </div>
    </div>
   </MainFrame>	
  );
}