import { motion } from "framer-motion";
import { Search, Filter, Download, ArrowUpRight, ArrowDownLeft } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { useState } from "react";

const allTransactions = [
  { id: "TXN-001", name: "Netflix Subscription", amount: -15.99, status: "completed", date: "2026-03-08", category: "Entertainment", type: "debit" },
  { id: "TXN-002", name: "Sarah Miller", amount: 250.0, status: "completed", date: "2026-03-08", category: "Transfer", type: "credit" },
  { id: "TXN-003", name: "Uber Ride", amount: -24.5, status: "completed", date: "2026-03-07", category: "Transport", type: "debit" },
  { id: "TXN-004", name: "Salary Deposit", amount: 5420.0, status: "completed", date: "2026-03-07", category: "Income", type: "credit" },
  { id: "TXN-005", name: "Amazon Purchase", amount: -89.99, status: "pending", date: "2026-03-06", category: "Shopping", type: "debit" },
  { id: "TXN-006", name: "John Doe", amount: 120.0, status: "completed", date: "2026-03-05", category: "Transfer", type: "credit" },
  { id: "TXN-007", name: "Spotify", amount: -9.99, status: "completed", date: "2026-03-05", category: "Entertainment", type: "debit" },
  { id: "TXN-008", name: "Electricity Bill", amount: -145.0, status: "failed", date: "2026-03-04", category: "Bills", type: "debit" },
  { id: "TXN-009", name: "Freelance Payment", amount: 850.0, status: "completed", date: "2026-03-03", category: "Income", type: "credit" },
  { id: "TXN-010", name: "Grocery Store", amount: -67.3, status: "completed", date: "2026-03-02", category: "Food", type: "debit" },
];

const statusColors: Record<string, string> = {
  completed: "bg-success/10 text-success border-success/20",
  pending: "bg-warning/10 text-warning border-warning/20",
  failed: "bg-destructive/10 text-destructive border-destructive/20",
};

export default function Transactions() {
  const [search, setSearch] = useState("");
  const [filter, setFilter] = useState("all");

  const filtered = allTransactions.filter((tx) => {
    const matchesSearch = tx.name.toLowerCase().includes(search.toLowerCase()) || tx.id.toLowerCase().includes(search.toLowerCase());
    const matchesFilter = filter === "all" || tx.status === filter;
    return matchesSearch && matchesFilter;
  });

  return (
    <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="max-w-5xl mx-auto space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold">Transactions</h1>
          <p className="text-muted-foreground text-sm mt-1">View and manage your transaction history</p>
        </div>
        <Button variant="outline" className="rounded-xl gap-2">
          <Download className="h-4 w-4" /> Export
        </Button>
      </div>

      <div className="flex flex-col sm:flex-row gap-3">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="Search by name or transaction ID..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="pl-10 rounded-xl bg-muted/50 border-transparent"
          />
        </div>
        <div className="flex gap-2">
          {["all", "completed", "pending", "failed"].map((f) => (
            <Button
              key={f}
              variant={filter === f ? "default" : "outline"}
              size="sm"
              className={`rounded-xl capitalize ${filter === f ? "gradient-primary text-primary-foreground border-0" : ""}`}
              onClick={() => setFilter(f)}
            >
              {f}
            </Button>
          ))}
        </div>
      </div>

      <div className="glass-card overflow-hidden">
        <div className="divide-y divide-border">
          {filtered.map((tx, i) => (
            <motion.div
              key={tx.id}
              initial={{ opacity: 0, x: -10 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: i * 0.03 }}
              className="flex items-center gap-4 p-4 hover:bg-muted/30 transition-colors cursor-pointer"
            >
              <div className={`w-10 h-10 rounded-xl flex items-center justify-center shrink-0 ${tx.type === "credit" ? "bg-success/10 text-success" : "bg-destructive/10 text-destructive"}`}>
                {tx.type === "credit" ? <ArrowDownLeft className="h-4 w-4" /> : <ArrowUpRight className="h-4 w-4" />}
              </div>
              <div className="flex-1 min-w-0">
                <p className="text-sm font-medium truncate">{tx.name}</p>
                <p className="text-xs text-muted-foreground">{tx.id} · {tx.category}</p>
              </div>
              <div className="text-right">
                <p className={`text-sm font-semibold ${tx.type === "credit" ? "text-success" : "text-foreground"}`}>
                  {tx.type === "credit" ? "+" : "-"}${Math.abs(tx.amount).toFixed(2)}
                </p>
                <p className="text-xs text-muted-foreground">{tx.date}</p>
              </div>
              <Badge variant="outline" className={`rounded-full text-xs ${statusColors[tx.status]}`}>
                {tx.status}
              </Badge>
            </motion.div>
          ))}
        </div>
      </div>
    </motion.div>
  );
}
