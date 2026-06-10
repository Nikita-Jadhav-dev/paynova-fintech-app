import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from "recharts";

const data = [
  { name: "Jan", income: 4000, expenses: 2400 },
  { name: "Feb", income: 3000, expenses: 1398 },
  { name: "Mar", income: 5000, expenses: 3200 },
  { name: "Apr", income: 4780, expenses: 2908 },
  { name: "May", income: 5890, expenses: 3800 },
  { name: "Jun", income: 6390, expenses: 3280 },
  { name: "Jul", income: 7490, expenses: 4300 },
];

export function SpendingChart() {
  return (
    <div className="glass-card p-6 h-full">
      <div className="flex items-center justify-between mb-6">
        <h3 className="font-semibold text-foreground">Spending Overview</h3>
        <span className="text-xs text-muted-foreground bg-muted px-3 py-1 rounded-full">Last 7 months</span>
      </div>
      <ResponsiveContainer width="100%" height={280}>
        <AreaChart data={data}>
          <defs>
            <linearGradient id="incomeGrad" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor="hsl(262, 80%, 50%)" stopOpacity={0.3} />
              <stop offset="95%" stopColor="hsl(262, 80%, 50%)" stopOpacity={0} />
            </linearGradient>
            <linearGradient id="expenseGrad" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor="hsl(220, 70%, 55%)" stopOpacity={0.3} />
              <stop offset="95%" stopColor="hsl(220, 70%, 55%)" stopOpacity={0} />
            </linearGradient>
          </defs>
          <CartesianGrid strokeDasharray="3 3" stroke="hsl(230, 20%, 90%)" vertical={false} />
          <XAxis dataKey="name" tick={{ fontSize: 12, fill: "hsl(230, 15%, 45%)" }} axisLine={false} tickLine={false} />
          <YAxis tick={{ fontSize: 12, fill: "hsl(230, 15%, 45%)" }} axisLine={false} tickLine={false} />
          <Tooltip
            contentStyle={{
              background: "hsl(0, 0%, 100%)",
              border: "1px solid hsl(230, 20%, 90%)",
              borderRadius: "12px",
              boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
            }}
          />
          <Area type="monotone" dataKey="income" stroke="hsl(262, 80%, 50%)" fill="url(#incomeGrad)" strokeWidth={2} />
          <Area type="monotone" dataKey="expenses" stroke="hsl(220, 70%, 55%)" fill="url(#expenseGrad)" strokeWidth={2} />
        </AreaChart>
      </ResponsiveContainer>
    </div>
  );
}
