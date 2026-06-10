import { motion } from "framer-motion";
import { Bell, CreditCard, Shield, ArrowDownLeft, AlertTriangle, Settings, Check } from "lucide-react";
import { Button } from "@/components/ui/button";

const notifications = [
  { id: 1, icon: ArrowDownLeft, title: "Payment Received", desc: "Sarah Miller sent you $250.00", time: "2 min ago", type: "success", read: false },
  { id: 2, icon: CreditCard, title: "Card Transaction", desc: "Netflix subscription - $15.99", time: "1 hour ago", type: "info", read: false },
  { id: 3, icon: Shield, title: "Security Alert", desc: "New login from Chrome on MacOS", time: "3 hours ago", type: "warning", read: false },
  { id: 4, icon: AlertTriangle, title: "Payment Failed", desc: "Electricity bill payment failed", time: "Yesterday", type: "error", read: true },
  { id: 5, icon: ArrowDownLeft, title: "Salary Credited", desc: "$5,420.00 deposited to your account", time: "Yesterday", type: "success", read: true },
  { id: 6, icon: Bell, title: "Promotion", desc: "Get 5% cashback on bill payments", time: "2 days ago", type: "info", read: true },
];

const typeStyles: Record<string, string> = {
  success: "bg-success/10 text-success",
  info: "bg-secondary/10 text-secondary",
  warning: "bg-warning/10 text-warning",
  error: "bg-destructive/10 text-destructive",
};

export default function Notifications() {
  return (
    <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="max-w-3xl mx-auto space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold">Notifications</h1>
          <p className="text-muted-foreground text-sm mt-1">Stay updated on your account activity</p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" size="sm" className="rounded-xl gap-2">
            <Check className="h-4 w-4" /> Mark all read
          </Button>
          <Button variant="outline" size="icon" className="rounded-xl">
            <Settings className="h-4 w-4" />
          </Button>
        </div>
      </div>

      <div className="glass-card overflow-hidden divide-y divide-border">
        {notifications.map((n, i) => (
          <motion.div
            key={n.id}
            initial={{ opacity: 0, x: -10 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: i * 0.04 }}
            className={`flex items-start gap-4 p-4 hover:bg-muted/30 transition-colors ${!n.read ? "bg-accent/30" : ""}`}
          >
            <div className={`w-10 h-10 rounded-xl flex items-center justify-center shrink-0 ${typeStyles[n.type]}`}>
              <n.icon className="h-4 w-4" />
            </div>
            <div className="flex-1 min-w-0">
              <div className="flex items-center gap-2">
                <p className="text-sm font-medium">{n.title}</p>
                {!n.read && <span className="w-2 h-2 rounded-full bg-primary" />}
              </div>
              <p className="text-xs text-muted-foreground mt-0.5">{n.desc}</p>
            </div>
            <span className="text-xs text-muted-foreground shrink-0">{n.time}</span>
          </motion.div>
        ))}
      </div>
    </motion.div>
  );
}
