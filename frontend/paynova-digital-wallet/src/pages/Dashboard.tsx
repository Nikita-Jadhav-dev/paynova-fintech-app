import { motion } from "framer-motion";
import {
  Send,
  ArrowDownLeft,
  QrCode,
  Smartphone,
  Zap,
  CreditCard,
  TrendingUp,
  TrendingDown,
  ArrowUpRight,
  ArrowDownRight,
  Eye,
  EyeOff,
  Plus,
} from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { SpendingChart } from "@/components/dashboard/SpendingChart";
import { RecentTransactions } from "@/components/dashboard/RecentTransactions";

const quickActions = [
  { icon: Send, label: "Send", color: "from-primary to-secondary", route: "/send" },
  { icon: ArrowDownLeft, label: "Receive", color: "from-success to-emerald-400", route: "/receive" },
  { icon: QrCode, label: "Scan & Pay", color: "from-secondary to-blue-400", route: "/scan" },
  { icon: Smartphone, label: "Recharge", color: "from-warning to-orange-400", route: "#" },
  { icon: Zap, label: "Pay Bills", color: "from-purple-500 to-pink-500", route: "#" },
  { icon: CreditCard, label: "Cards", color: "from-cyan-500 to-blue-500", route: "/cards" },
];

const container = {
  hidden: { opacity: 0 },
  show: { opacity: 1, transition: { staggerChildren: 0.06 } },
};

const item = {
  hidden: { opacity: 0, y: 20 },
  show: { opacity: 1, y: 0 },
};

export default function Dashboard() {
  const [showBalance, setShowBalance] = useState(true);
  const navigate = useNavigate();

  return (
    <motion.div
      variants={container}
      initial="hidden"
      animate="show"
      className="space-y-6 max-w-7xl mx-auto"
    >
      {/* Wallet Card */}
      <motion.div variants={item}>
        <div className="gradient-card rounded-3xl p-6 lg:p-8 text-primary-foreground relative overflow-hidden">
          <div className="absolute inset-0 opacity-20">
            <div className="absolute -top-20 -right-20 w-60 h-60 rounded-full bg-gradient-to-br from-white/20 to-transparent" />
            <div className="absolute -bottom-10 -left-10 w-40 h-40 rounded-full bg-gradient-to-tr from-white/10 to-transparent" />
          </div>
          <div className="relative z-10">
            <div className="flex items-center justify-between mb-6">
              <div>
                <p className="text-primary-foreground/70 text-sm font-medium">Total Balance</p>
                <div className="flex items-center gap-3 mt-1">
                  <h1 className="text-3xl lg:text-4xl font-bold">
                    {showBalance ? "$24,563.80" : "••••••••"}
                  </h1>
                  <button
                    onClick={() => setShowBalance(!showBalance)}
                    className="text-primary-foreground/60 hover:text-primary-foreground transition-colors"
                  >
                    {showBalance ? <EyeOff className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
                  </button>
                </div>
              </div>
              <Button
                size="sm"
                className="bg-primary-foreground/20 hover:bg-primary-foreground/30 text-primary-foreground border-0 rounded-xl backdrop-blur-sm"
                onClick={() => navigate("/wallet")}
              >
                <Plus className="h-4 w-4 mr-1" /> Add Funds
              </Button>
            </div>
            <div className="flex gap-6">
              <div className="flex items-center gap-2">
                <div className="w-8 h-8 rounded-lg bg-primary-foreground/20 flex items-center justify-center">
                  <TrendingUp className="h-4 w-4" />
                </div>
                <div>
                  <p className="text-xs text-primary-foreground/60">Income</p>
                  <p className="text-sm font-semibold">+$8,420</p>
                </div>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-8 h-8 rounded-lg bg-primary-foreground/20 flex items-center justify-center">
                  <TrendingDown className="h-4 w-4" />
                </div>
                <div>
                  <p className="text-xs text-primary-foreground/60">Expenses</p>
                  <p className="text-sm font-semibold">-$3,280</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </motion.div>

      {/* Quick Actions */}
      <motion.div variants={item}>
        <h2 className="text-sm font-semibold text-muted-foreground uppercase tracking-wider mb-3">
          Quick Actions
        </h2>
        <div className="grid grid-cols-3 sm:grid-cols-6 gap-3">
          {quickActions.map((action) => (
            <motion.button
              key={action.label}
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              onClick={() => navigate(action.route)}
              className="glass-card p-4 flex flex-col items-center gap-2 hover:shadow-elevated transition-shadow cursor-pointer"
            >
              <div className={`w-12 h-12 rounded-2xl bg-gradient-to-br ${action.color} flex items-center justify-center`}>
                <action.icon className="h-5 w-5 text-primary-foreground" />
              </div>
              <span className="text-xs font-medium text-foreground">{action.label}</span>
            </motion.button>
          ))}
        </div>
      </motion.div>

      {/* Stats Row */}
      <motion.div variants={item} className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="glass-card p-5">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-muted-foreground">Monthly Spending</p>
              <p className="text-2xl font-bold mt-1">$3,280</p>
            </div>
            <div className="flex items-center gap-1 text-destructive text-sm">
              <ArrowUpRight className="h-4 w-4" />
              12%
            </div>
          </div>
        </div>
        <div className="glass-card p-5">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-muted-foreground">Savings</p>
              <p className="text-2xl font-bold mt-1">$12,840</p>
            </div>
            <div className="flex items-center gap-1 text-success text-sm">
              <ArrowDownRight className="h-4 w-4" />
              8%
            </div>
          </div>
        </div>
        <div className="glass-card p-5">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-muted-foreground">Pending</p>
              <p className="text-2xl font-bold mt-1">$580</p>
            </div>
            <span className="text-xs bg-warning/20 text-warning px-2 py-1 rounded-full font-medium">
              3 items
            </span>
          </div>
        </div>
      </motion.div>

      {/* Charts and Transactions */}
      <div className="grid grid-cols-1 lg:grid-cols-5 gap-6">
        <motion.div variants={item} className="lg:col-span-3">
          <SpendingChart />
        </motion.div>
        <motion.div variants={item} className="lg:col-span-2">
          <RecentTransactions />
        </motion.div>
      </div>
    </motion.div>
  );
}
