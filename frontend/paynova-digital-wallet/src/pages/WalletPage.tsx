import { motion } from "framer-motion";
import { Wallet as WalletIcon, Plus, ArrowDownToLine, CreditCard, Building2, Shield } from "lucide-react";
import { Button } from "@/components/ui/button";

const linkedAccounts = [
  { name: "Chase Checking", last4: "4829", type: "bank", balance: "$12,450.00" },
  { name: "Visa Platinum", last4: "7291", type: "card", balance: "$8,200.00" },
  { name: "Wells Fargo Savings", last4: "1053", type: "bank", balance: "$25,800.00" },
];

export default function WalletPage() {
  return (
    <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="max-w-3xl mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold">Wallet</h1>
        <p className="text-muted-foreground text-sm mt-1">Manage your wallet and linked accounts</p>
      </div>

      <div className="gradient-card rounded-3xl p-6 text-primary-foreground relative overflow-hidden">
        <div className="absolute -top-16 -right-16 w-48 h-48 rounded-full bg-gradient-to-br from-white/10 to-transparent" />
        <div className="relative z-10">
          <div className="flex items-center gap-3 mb-4">
            <WalletIcon className="h-6 w-6" />
            <span className="font-medium">PayNova Wallet</span>
          </div>
          <p className="text-3xl font-bold">$24,563.80</p>
          <p className="text-primary-foreground/60 text-sm mt-1">Available Balance</p>
          <div className="flex gap-3 mt-6">
            <Button size="sm" className="bg-primary-foreground/20 hover:bg-primary-foreground/30 text-primary-foreground border-0 rounded-xl gap-2">
              <Plus className="h-4 w-4" /> Add Funds
            </Button>
            <Button size="sm" className="bg-primary-foreground/20 hover:bg-primary-foreground/30 text-primary-foreground border-0 rounded-xl gap-2">
              <ArrowDownToLine className="h-4 w-4" /> Withdraw
            </Button>
          </div>
        </div>
      </div>

      <div>
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-semibold">Linked Accounts</h2>
          <Button variant="outline" size="sm" className="rounded-xl gap-2">
            <Plus className="h-4 w-4" /> Link Account
          </Button>
        </div>
        <div className="space-y-3">
          {linkedAccounts.map((account) => (
            <div key={account.last4} className="glass-card p-4 flex items-center gap-4">
              <div className="w-12 h-12 rounded-xl bg-accent flex items-center justify-center">
                {account.type === "bank" ? (
                  <Building2 className="h-5 w-5 text-accent-foreground" />
                ) : (
                  <CreditCard className="h-5 w-5 text-accent-foreground" />
                )}
              </div>
              <div className="flex-1">
                <p className="font-medium text-sm">{account.name}</p>
                <p className="text-xs text-muted-foreground">•••• {account.last4}</p>
              </div>
              <p className="font-semibold text-sm">{account.balance}</p>
            </div>
          ))}
        </div>
      </div>

      <div className="glass-card p-5 flex items-center gap-4">
        <div className="w-12 h-12 rounded-xl bg-success/10 flex items-center justify-center">
          <Shield className="h-5 w-5 text-success" />
        </div>
        <div className="flex-1">
          <p className="font-medium text-sm">Daily Transaction Limit</p>
          <p className="text-xs text-muted-foreground">$10,000.00 per day</p>
        </div>
        <Button variant="outline" size="sm" className="rounded-xl">Manage</Button>
      </div>
    </motion.div>
  );
}
