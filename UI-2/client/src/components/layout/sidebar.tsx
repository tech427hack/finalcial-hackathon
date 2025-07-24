import React from "react";
import { Link, useLocation } from "wouter";
import { useAuth } from "@/hooks/use-auth";
import { useI18n } from "@/hooks/use-i18n";
import {
  LayoutDashboard,
  PiggyBank,
  FileText,
  Building2,
  Shield,
  BarChart3,
  Target,
  CheckCircle,
  UserCheck,
  Settings,
  LogOut,
  PlusCircle,
  DollarSign,
  Users,
} from "lucide-react";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";

interface SidebarLinkProps {
  href: string;
  icon: React.ReactNode;
  label: string;
  roles?: string[];
  permissions?: string[];
}

function SidebarLink({ href, icon, label, roles, permissions }: SidebarLinkProps) {
  const [location] = useLocation();
  const { hasRole, hasPermission } = useAuth();
  const isActive = location === href;

  const hasAccess =
    (!roles || roles.some(hasRole)) &&
    (!permissions || permissions.some(hasPermission));

  if (!hasAccess) return null;

  return (
    <Link
      href={href}
      className={`flex items-center space-x-3 px-3 py-2 rounded-lg text-sm font-medium transition-colors ${
        isActive
          ? "bg-primary text-primary-foreground"
          : "text-muted-foreground hover:bg-accent hover:text-accent-foreground"
      }`}
    >
      {icon}
      <span>{label}</span>
    </Link>
  );
}

export default function Sidebar({
  onClose,
  onLogin,
}: {
  onClose: () => void;
  onLogin: () => void;
}) {
  const { user, logout, hasRole } = useAuth();
  const isAuthenticated = !!user;
  const { t, language } = useI18n();
  const [, navigate] = useLocation();

  const handleLogout = async () => {
    await logout();
    navigate("/", { replace: true });
  };

  const sidebarLinks: SidebarLinkProps[] = [
    // Shared dashboard route (you may adjust if separate dashboards have different routes)
    {
      href: "/dashboard",
      icon: <LayoutDashboard className="h-4 w-4" />,
      label: t("nav.dashboard"),
      roles: ["ADMIN", "TREASURER", "MEMBER", "PRESIDENT"],
    },

    // Member-specific routes
    {
      href: "/savings",
      icon: <PiggyBank className="h-4 w-4" />,
      label: t("nav.savings"),
      roles: ["TREASURER", "MEMBER", "PRESIDENT"],
    },
    {
      href: "/loan-application",
      icon: <FileText className="h-4 w-4" />,
      label: t("nav.applyLoan"),
      roles: ["TREASURER", "MEMBER", "PRESIDENT"],
    },

    // Treasurer routes
    {
      href: "/loan-approvals",
      icon: <CheckCircle className="h-4 w-4" />,
      label: t("nav.loanApprovals"),
      roles: ["TREASURER", "PRESIDENT"],
    },
    // President routes
    {
      href: "/polls/create",
      icon: <PlusCircle className="h-4 w-4" />,
      label: t("nav.createPoll"),
      roles: ["PRESIDENT"],
    },
    {
      href: "/group-management",
      icon: <Building2 className="h-4 w-4" />,
      label: t("nav.groupManagement"),
      roles: ["PRESIDENT"],
    },
    // Admin routes
    {
      href: "/member-management",
      icon: <UserCheck className="h-4 w-4" />,
      label: t("nav.memberManagement"),
      roles: ["ADMIN", "PRESIDENT"],
    },
    {
      href: "/group-management",
      icon: <Building2 className="h-4 w-4" />,
      label: t("nav.groupManagement"),
      roles: ["ADMIN"],
    },
    {
      href: "/role-management",
      icon: <Shield className="h-4 w-4" />,
      label: t("nav.roleManagement"),
      roles: ["ADMIN"],
    },
    // Shared for all roles
    {
      href: "/sdg-impact",
      icon: <Target className="h-4 w-4" />,
      label: t("nav.sdgImpact"),
    },
  ];

  return (
    <aside key={`${language}-${user?.username}`} className="w-full h-full bg-background p-4 flex flex-col">
      {/* Top user info or login */}
      <div className="flex items-center justify-between mb-4">
        {isAuthenticated ? (
          <div className="flex items-center space-x-3">
            <Avatar className="w-9 h-9">
              <AvatarFallback>{user?.username?.[0]?.toUpperCase()}</AvatarFallback>
            </Avatar>
            <div className="truncate max-w-[120px]">
              <p className="font-medium truncate">{user?.username}</p>
              <p className="text-xs text-muted-foreground truncate">{user?.email || "User"}</p>
            </div>
          </div>
        ) : (
          <Button variant="outline" size="sm" onClick={onLogin}>
            {t("nav.login")}
          </Button>
        )}

        {isAuthenticated && (
          <Button variant="ghost" size="icon" onClick={handleLogout}>
            <LogOut className="h-5 w-5" />
          </Button>
        )}
      </div>

      {/* Navigation items */}
      <nav className="flex-1 space-y-1 overflow-y-auto">
        {sidebarLinks.map((link, idx) => (
          <SidebarLink key={idx} {...link} />
        ))}
      </nav>
    </aside>
  );
}