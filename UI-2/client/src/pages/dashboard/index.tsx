import MemberDashboard from "./member-dashboard";
import PresidentDashboard from "./president-dashboard";
import TreasurerDashboard from "./treasurer-dashboard";
import AdminDashboard from "./admin-dashboard";
import { useAuth } from '@/hooks/use-auth';

export default function DashboardPage() {
  const { hasRole } = useAuth();

  if (hasRole('ADMIN')) return <AdminDashboard />;
  if (hasRole('PRESIDENT')) return <PresidentDashboard />;
  if (hasRole('TREASURER')) return <TreasurerDashboard />;
  if (hasRole('MEMBER')) return <MemberDashboard />;

  return <div>Unauthorized</div>;
}