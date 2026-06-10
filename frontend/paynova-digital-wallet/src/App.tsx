import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AppLayout } from "@/components/layout/AppLayout";
import Dashboard from "./pages/Dashboard";
import SendMoney from "./pages/SendMoney";
import Receive from "./pages/Receive";
import ScanPay from "./pages/ScanPay";
import Transactions from "./pages/Transactions";
import Analytics from "./pages/Analytics";
import WalletPage from "./pages/WalletPage";
import CardsPage from "./pages/CardsPage";
import Notifications from "./pages/Notifications";
import SettingsPage from "./pages/SettingsPage";
import AdminDashboard from "./pages/admin/AdminDashboard";
import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import VerifyOTP from "./pages/auth/VerifyOTP";
import NotFound from "./pages/NotFound";

const queryClient = new QueryClient();

const DashboardLayout = ({ children }: { children: React.ReactNode }) => (
  <AppLayout>{children}</AppLayout>
);

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      <Toaster />
      <Sonner />
      <BrowserRouter>
        <Routes>
          {/* Auth routes - no layout */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/verify-otp" element={<VerifyOTP />} />

          {/* App routes - with layout */}
          <Route path="/" element={<DashboardLayout><Dashboard /></DashboardLayout>} />
          <Route path="/send" element={<DashboardLayout><SendMoney /></DashboardLayout>} />
          <Route path="/receive" element={<DashboardLayout><Receive /></DashboardLayout>} />
          <Route path="/scan" element={<DashboardLayout><ScanPay /></DashboardLayout>} />
          <Route path="/transactions" element={<DashboardLayout><Transactions /></DashboardLayout>} />
          <Route path="/analytics" element={<DashboardLayout><Analytics /></DashboardLayout>} />
          <Route path="/wallet" element={<DashboardLayout><WalletPage /></DashboardLayout>} />
          <Route path="/cards" element={<DashboardLayout><CardsPage /></DashboardLayout>} />
          <Route path="/notifications" element={<DashboardLayout><Notifications /></DashboardLayout>} />
          <Route path="/settings" element={<DashboardLayout><SettingsPage /></DashboardLayout>} />
          <Route path="/admin" element={<DashboardLayout><AdminDashboard /></DashboardLayout>} />
          <Route path="/admin/users" element={<DashboardLayout><AdminDashboard /></DashboardLayout>} />

          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;
