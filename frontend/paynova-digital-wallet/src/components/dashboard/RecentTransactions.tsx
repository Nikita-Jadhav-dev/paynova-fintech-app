import { ArrowUpRight, ArrowDownLeft } from "lucide-react";

const transactions = [
  { id: 1, name: "Netflix Subscription", amount: -15.99, type: "debit", date: "Today", category: "Entertainment" },
  { id: 2, name: "Sarah Miller", amount: 250.0, type: "credit", date: "Today", category: "Transfer" },
  { id: 3, name: "Uber Ride", amount: -24.5, type: "debit", date: "Yesterday", category: "Transport" },
  { id: 4, name: "Salary Deposit", amount: 5420.0, type: "credit", date: "Yesterday", category: "Income" },
  { id: 5, name: "Amazon Purchase", amount: -89.99, type: "debit", date: "Mar 5", category: "Shopping" },
  { id: 6, name: "John Doe", amount: 120.0, type: "credit", date: "Mar 4", category: "Transfer" },
];

export function RecentTransactions() {
  return (
    <div className="glass-card p-6 h-full">
      <div className="flex items-center justify-between mb-4">
        <h3 className="font-semibold text-foreground">Recent Transactions</h3>
        <a href="/transactions" className="text-xs text-primary font-medium hover:underline">
          View All
        </a>
      </div>
      <div className="space-y-3">
        {transactions.map((tx) => (
          <div key={tx.id} className="flex items-center gap-3 p-2 rounded-xl hover:bg-muted/50 transition-colors">
            <div
              className={`w-10 h-10 rounded-xl flex items-center justify-center shrink-0 ${
                tx.type === "credit"
                  ? "bg-success/10 text-success"
                  : "bg-destructive/10 text-destructive"
              }`}
            >
              {tx.type === "credit" ? (
                <ArrowDownLeft className="h-4 w-4" />
              ) : (
                <ArrowUpRight className="h-4 w-4" />
              )}
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium truncate">{tx.name}</p>
              <p className="text-xs text-muted-foreground">{tx.date}</p>
            </div>
            <span
              className={`text-sm font-semibold ${
                tx.type === "credit" ? "text-success" : "text-foreground"
              }`}
            >
              {tx.type === "credit" ? "+" : ""}${Math.abs(tx.amount).toFixed(2)}
            </span>
          </div>
        ))}
      </div>
    </div>
  );
}
