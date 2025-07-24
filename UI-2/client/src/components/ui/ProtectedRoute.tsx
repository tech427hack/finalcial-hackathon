import { useEffect } from "react";
import { useLocation } from "wouter";
import { useAuth } from "@/hooks/use-auth";
import { useToast } from "@/hooks/use-toast"; // ✅ This is your custom toast

export const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
  const { user } = useAuth();
  const [, navigate] = useLocation();
  const { toast } = useToast(); // ✅ get toast function from hook

  useEffect(() => {
    if (!user) {
      toast({
        title: "Access denied",
        description: "You need to login first.",
      });
      navigate("/", { replace: true });
    }
  }, [user]);

  if (!user) return null;

  return <>{children}</>;
};