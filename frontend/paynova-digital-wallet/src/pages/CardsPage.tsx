import { motion } from "framer-motion";
import { CreditCard, Plus, Wifi, Lock } from "lucide-react";
import { Button } from "@/components/ui/button";

const cards = [
  { name: "Visa Platinum", last4: "7291", expiry: "12/28", color: "from-primary to-secondary", balance: "$8,200.00" },
  { name: "Mastercard Gold", last4: "3456", expiry: "06/27", color: "from-warning to-orange-500", balance: "$3,450.00" },
];

export default function CardsPage() {
  return (
    <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="max-w-3xl mx-auto space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold">Cards</h1>
          <p className="text-muted-foreground text-sm mt-1">Manage your linked cards</p>
        </div>
        <Button className="rounded-xl gradient-primary text-primary-foreground border-0 gap-2">
          <Plus className="h-4 w-4" /> Add Card
        </Button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {cards.map((card) => (
          <motion.div
            key={card.last4}
            whileHover={{ scale: 1.02 }}
            className={`bg-gradient-to-br ${card.color} rounded-3xl p-6 text-primary-foreground relative overflow-hidden aspect-[1.6/1] flex flex-col justify-between`}
          >
            <div className="absolute -top-10 -right-10 w-40 h-40 rounded-full bg-white/10" />
            <div className="flex justify-between items-start relative z-10">
              <CreditCard className="h-8 w-8" />
              <Wifi className="h-5 w-5 rotate-90 opacity-60" />
            </div>
            <div className="relative z-10">
              <p className="text-lg tracking-[0.25em] font-mono mb-3">•••• •••• •••• {card.last4}</p>
              <div className="flex justify-between items-end">
                <div>
                  <p className="text-xs opacity-60">Card Holder</p>
                  <p className="text-sm font-medium">Alex Johnson</p>
                </div>
                <div>
                  <p className="text-xs opacity-60">Expires</p>
                  <p className="text-sm font-medium">{card.expiry}</p>
                </div>
              </div>
            </div>
          </motion.div>
        ))}
      </div>

      <div className="glass-card p-5 flex items-center gap-4">
        <div className="w-12 h-12 rounded-xl bg-accent flex items-center justify-center">
          <Lock className="h-5 w-5 text-accent-foreground" />
        </div>
        <div className="flex-1">
          <p className="font-medium text-sm">Card Security</p>
          <p className="text-xs text-muted-foreground">All cards are protected with 3D Secure authentication</p>
        </div>
      </div>
    </motion.div>
  );
}
