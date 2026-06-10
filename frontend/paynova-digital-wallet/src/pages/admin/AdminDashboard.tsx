import { motion } from "framer-motion";
import {
  Users, DollarSign, AlertTriangle, TrendingUp,
  Search, MoreVertical, Shield, Eye,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, LineChart, Line } from "recharts";

const stats = [
  { label: "Total Users", value: "12,847", change: "+5.2%", icon: Users, color: "bg-primary/10 text-primary" },
  { label: "Revenue", value: "$1.2M", change: "+12.8%", icon: DollarSign, color: "bg-success/10 text-success" },
  { label: "Fraud Alerts", value: "23", change: "-8%", icon: AlertTriangle, color: "bg-warning/10 text-warning" },
  { label: "Growth Rate", value: "18.5%", change: "+3.1%", icon: TrendingUp, color: "bg-secondary/10 text-secondary" },
];

const revenueData = [
  { month: "Oct", revenue: 82000 }, { month: "Nov", revenue: 95000 }, { month: "Dec", revenue: 110000 },
  { month: "Jan", revenue: 98000 }, { month: "Feb", revenue: 125000 }, { month: "Mar", revenue: 140000 },
];

const users = [
  { name: "John Smith", email: "john@email.com", status: "active", balance: "$4,250", risk: "low" },
  { name: "Sarah Johnson", email: "sarah@email.com", status: "active", balance: "$12,800", risk: "low" },
  { name: "Mike Wilson", email: "mike@email.com", status: "suspended", balance: "$890", risk: "high" },
  { name: "Emma Davis", email: "emma@email.com", status: "pending", balance: "$0", risk: "medium" },
  { name: "Alex Brown", email: "alex@email.com", status: "active", balance: "$6,300", risk: "low" },
];

const statusColors: Record<string, string> = {
  active: "bg-success/10 text-success border-success/20",
  suspended: "bg-destructive/10 text-destructive border-destructive/20",
  pending: "bg-warning/10 text-warning border-warning/20",
};

const riskColors: Record<string, string> = {
  low: "bg-success/10 text-success border-success/20",
  medium: "bg-warning/10 text-warning border-warning/20",
  high: "bg-destructive/10 text-destructive border-destructive/20",
};

const container = { hidden: { opacity: 0 }, show: { opacity: 1, transition: { staggerChildren: 0.06 } } };
const item = { hidden: { opacity: 0, y: 20 }, show: { opacity: 1, y: 0 } };

export default function AdminDashboard() {
  return (
    <motion.div variants={container} initial="hidden" animate="show" className="max-w-7xl mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold">Admin Dashboard</h1>
        <p className="text-muted-foreground text-sm mt-1">Monitor platform activity and manage users</p>
      </div>

      <motion.div variants={item} className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {stats.map((s) => (
          <div key={s.label} className="glass-card p-5 flex items-center gap-4">
            <div className={`w-12 h-12 rounded-xl flex items-center justify-center ${s.color}`}>
              <s.icon className="h-5 w-5" />
            </div>
            <div>
              <p className="text-sm text-muted-foreground">{s.label}</p>
              <p className="text-xl font-bold">{s.value}</p>
              <span className="text-xs text-success font-medium">{s.change}</span>
            </div>
          </div>
        ))}
      </motion.div>

      <motion.div variants={item} className="glass-card p-6">
        <h3 className="font-semibold mb-4">Revenue Overview</h3>
        <ResponsiveContainer width="100%" height={250}>
          <LineChart data={revenueData}>
            <CartesianGrid strokeDasharray="3 3" stroke="hsl(230,20%,90%)" vertical={false} />
            <XAxis dataKey="month" tick={{ fontSize: 12, fill: "hsl(230,15%,45%)" }} axisLine={false} tickLine={false} />
            <YAxis tick={{ fontSize: 12, fill: "hsl(230,15%,45%)" }} axisLine={false} tickLine={false} />
            <Tooltip contentStyle={{ borderRadius: "12px", border: "1px solid hsl(230,20%,90%)" }} />
            <Line type="monotone" dataKey="revenue" stroke="hsl(262,80%,50%)" strokeWidth={3} dot={{ r: 4 }} />
          </LineChart>
        </ResponsiveContainer>
      </motion.div>

      <motion.div variants={item}>
        <div className="flex items-center justify-between mb-4">
          <h3 className="font-semibold">User Management</h3>
          <div className="relative w-64">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
            <Input placeholder="Search users..." className="pl-10 rounded-xl bg-muted/50 border-transparent h-9 text-sm" />
          </div>
        </div>
        <div className="glass-card overflow-hidden">
          <div className="divide-y divide-border">
            {users.map((user) => (
              <div key={user.email} className="flex items-center gap-4 p-4 hover:bg-muted/30 transition-colors">
                <div className="w-10 h-10 rounded-xl gradient-primary flex items-center justify-center shrink-0">
                  <span className="text-primary-foreground text-xs font-bold">{user.name.split(" ").map(n => n[0]).join("")}</span>
                </div>
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-medium">{user.name}</p>
                  <p className="text-xs text-muted-foreground">{user.email}</p>
                </div>
                <Badge variant="outline" className={`rounded-full text-xs capitalize ${statusColors[user.status]}`}>{user.status}</Badge>
                <Badge variant="outline" className={`rounded-full text-xs capitalize ${riskColors[user.risk]}`}>{user.risk} risk</Badge>
                <span className="text-sm font-semibold w-20 text-right">{user.balance}</span>
                <Button variant="ghost" size="icon" className="rounded-xl shrink-0">
                  <MoreVertical className="h-4 w-4" />
                </Button>
              </div>
            ))}
          </div>
        </div>
      </motion.div>
    </motion.div>
  );
}
