import { motion } from "framer-motion";
import {
  AreaChart, Area, BarChart, Bar, PieChart, Pie, Cell,
  XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend,
} from "recharts";

const monthlyData = [
  { month: "Sep", income: 4200, expenses: 2800 },
  { month: "Oct", income: 5100, expenses: 3200 },
  { month: "Nov", income: 4800, expenses: 2900 },
  { month: "Dec", income: 6200, expenses: 4100 },
  { month: "Jan", income: 5500, expenses: 3400 },
  { month: "Feb", income: 5800, expenses: 3100 },
  { month: "Mar", income: 6390, expenses: 3280 },
];

const categoryData = [
  { name: "Food", value: 850, color: "hsl(262, 80%, 50%)" },
  { name: "Transport", value: 420, color: "hsl(220, 70%, 55%)" },
  { name: "Entertainment", value: 320, color: "hsl(142, 72%, 42%)" },
  { name: "Shopping", value: 680, color: "hsl(38, 92%, 50%)" },
  { name: "Bills", value: 1010, color: "hsl(0, 72%, 51%)" },
];

const weeklySpending = [
  { day: "Mon", amount: 120 },
  { day: "Tue", amount: 85 },
  { day: "Wed", amount: 200 },
  { day: "Thu", amount: 150 },
  { day: "Fri", amount: 310 },
  { day: "Sat", amount: 280 },
  { day: "Sun", amount: 95 },
];

const container = { hidden: { opacity: 0 }, show: { opacity: 1, transition: { staggerChildren: 0.08 } } };
const item = { hidden: { opacity: 0, y: 20 }, show: { opacity: 1, y: 0 } };

export default function Analytics() {
  return (
    <motion.div variants={container} initial="hidden" animate="show" className="max-w-7xl mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold">Financial Analytics</h1>
        <p className="text-muted-foreground text-sm mt-1">Track your spending patterns and financial health</p>
      </div>

      {/* Insights */}
      <motion.div variants={item} className="grid grid-cols-1 sm:grid-cols-4 gap-4">
        {[
          { label: "Total Income", value: "$38,040", change: "+12.5%", positive: true },
          { label: "Total Expenses", value: "$22,780", change: "+8.2%", positive: false },
          { label: "Net Savings", value: "$15,260", change: "+18.3%", positive: true },
          { label: "Avg Daily Spend", value: "$106", change: "-5.1%", positive: true },
        ].map((s) => (
          <div key={s.label} className="glass-card p-5">
            <p className="text-sm text-muted-foreground">{s.label}</p>
            <p className="text-2xl font-bold mt-1">{s.value}</p>
            <span className={`text-xs font-medium ${s.positive ? "text-success" : "text-destructive"}`}>
              {s.change}
            </span>
          </div>
        ))}
      </motion.div>

      {/* Income vs Expenses */}
      <motion.div variants={item} className="glass-card p-6">
        <h3 className="font-semibold mb-4">Income vs Expenses</h3>
        <ResponsiveContainer width="100%" height={300}>
          <AreaChart data={monthlyData}>
            <defs>
              <linearGradient id="ig" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="hsl(262,80%,50%)" stopOpacity={0.3} />
                <stop offset="95%" stopColor="hsl(262,80%,50%)" stopOpacity={0} />
              </linearGradient>
              <linearGradient id="eg" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="hsl(220,70%,55%)" stopOpacity={0.3} />
                <stop offset="95%" stopColor="hsl(220,70%,55%)" stopOpacity={0} />
              </linearGradient>
            </defs>
            <CartesianGrid strokeDasharray="3 3" stroke="hsl(230,20%,90%)" vertical={false} />
            <XAxis dataKey="month" tick={{ fontSize: 12, fill: "hsl(230,15%,45%)" }} axisLine={false} tickLine={false} />
            <YAxis tick={{ fontSize: 12, fill: "hsl(230,15%,45%)" }} axisLine={false} tickLine={false} />
            <Tooltip contentStyle={{ borderRadius: "12px", border: "1px solid hsl(230,20%,90%)" }} />
            <Legend />
            <Area type="monotone" dataKey="income" stroke="hsl(262,80%,50%)" fill="url(#ig)" strokeWidth={2} />
            <Area type="monotone" dataKey="expenses" stroke="hsl(220,70%,55%)" fill="url(#eg)" strokeWidth={2} />
          </AreaChart>
        </ResponsiveContainer>
      </motion.div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Category Breakdown */}
        <motion.div variants={item} className="glass-card p-6">
          <h3 className="font-semibold mb-4">Spending by Category</h3>
          <ResponsiveContainer width="100%" height={250}>
            <PieChart>
              <Pie data={categoryData} cx="50%" cy="50%" innerRadius={60} outerRadius={100} paddingAngle={4} dataKey="value">
                {categoryData.map((entry) => (
                  <Cell key={entry.name} fill={entry.color} />
                ))}
              </Pie>
              <Tooltip contentStyle={{ borderRadius: "12px", border: "1px solid hsl(230,20%,90%)" }} />
              <Legend />
            </PieChart>
          </ResponsiveContainer>
        </motion.div>

        {/* Weekly Spending */}
        <motion.div variants={item} className="glass-card p-6">
          <h3 className="font-semibold mb-4">Weekly Spending</h3>
          <ResponsiveContainer width="100%" height={250}>
            <BarChart data={weeklySpending}>
              <CartesianGrid strokeDasharray="3 3" stroke="hsl(230,20%,90%)" vertical={false} />
              <XAxis dataKey="day" tick={{ fontSize: 12, fill: "hsl(230,15%,45%)" }} axisLine={false} tickLine={false} />
              <YAxis tick={{ fontSize: 12, fill: "hsl(230,15%,45%)" }} axisLine={false} tickLine={false} />
              <Tooltip contentStyle={{ borderRadius: "12px", border: "1px solid hsl(230,20%,90%)" }} />
              <Bar dataKey="amount" fill="hsl(262,80%,50%)" radius={[8, 8, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </motion.div>
      </div>
    </motion.div>
  );
}
