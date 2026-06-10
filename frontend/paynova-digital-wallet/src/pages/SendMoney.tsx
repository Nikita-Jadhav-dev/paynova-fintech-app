import { motion } from "framer-motion";
import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { ArrowRight, User, DollarSign, FileText, CheckCircle } from "lucide-react";

const recentContacts = [
  { name: "Sarah M.", initials: "SM" },
  { name: "John D.", initials: "JD" },
  { name: "Lisa K.", initials: "LK" },
  { name: "Mike R.", initials: "MR" },
  { name: "Emma W.", initials: "EW" },
];

export default function SendMoney() {
  const [step, setStep] = useState(1);
  const [recipient, setRecipient] = useState("");
  const [amount, setAmount] = useState("");
  const [note, setNote] = useState("");

  const quickAmounts = [50, 100, 250, 500, 1000];

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="max-w-lg mx-auto space-y-6"
    >
      <div>
        <h1 className="text-2xl font-bold">Send Money</h1>
        <p className="text-muted-foreground text-sm mt-1">Transfer funds instantly to anyone</p>
      </div>

      {/* Progress */}
      <div className="flex gap-2">
        {[1, 2, 3].map((s) => (
          <div
            key={s}
            className={`h-1 flex-1 rounded-full transition-colors ${
              s <= step ? "gradient-primary" : "bg-muted"
            }`}
          />
        ))}
      </div>

      {step === 1 && (
        <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-5">
          <div className="glass-card p-5 space-y-4">
            <label className="text-sm font-medium flex items-center gap-2">
              <User className="h-4 w-4 text-primary" />
              Recipient
            </label>
            <Input
              placeholder="Email, phone, or username"
              value={recipient}
              onChange={(e) => setRecipient(e.target.value)}
              className="rounded-xl h-12 bg-muted/50 border-transparent focus:border-primary/30"
            />
          </div>

          <div>
            <p className="text-sm font-medium text-muted-foreground mb-3">Recent Contacts</p>
            <div className="flex gap-3 overflow-x-auto pb-2">
              {recentContacts.map((c) => (
                <motion.button
                  key={c.name}
                  whileTap={{ scale: 0.95 }}
                  onClick={() => setRecipient(c.name)}
                  className="flex flex-col items-center gap-2 min-w-[60px]"
                >
                  <div className="w-12 h-12 rounded-2xl gradient-primary flex items-center justify-center">
                    <span className="text-primary-foreground text-xs font-bold">{c.initials}</span>
                  </div>
                  <span className="text-xs text-muted-foreground">{c.name}</span>
                </motion.button>
              ))}
            </div>
          </div>

          <Button
            className="w-full h-12 rounded-xl gradient-primary text-primary-foreground border-0 hover:opacity-90"
            disabled={!recipient}
            onClick={() => setStep(2)}
          >
            Continue <ArrowRight className="h-4 w-4 ml-2" />
          </Button>
        </motion.div>
      )}

      {step === 2 && (
        <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-5">
          <div className="glass-card p-5 space-y-4">
            <label className="text-sm font-medium flex items-center gap-2">
              <DollarSign className="h-4 w-4 text-primary" />
              Amount
            </label>
            <Input
              type="number"
              placeholder="0.00"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              className="rounded-xl h-16 text-3xl font-bold text-center bg-muted/50 border-transparent focus:border-primary/30"
            />
            <div className="flex gap-2 flex-wrap">
              {quickAmounts.map((a) => (
                <Button
                  key={a}
                  variant="outline"
                  size="sm"
                  className="rounded-xl"
                  onClick={() => setAmount(String(a))}
                >
                  ${a}
                </Button>
              ))}
            </div>
          </div>

          <div className="glass-card p-5 space-y-3">
            <label className="text-sm font-medium flex items-center gap-2">
              <FileText className="h-4 w-4 text-primary" />
              Note (optional)
            </label>
            <Input
              placeholder="What's this for?"
              value={note}
              onChange={(e) => setNote(e.target.value)}
              className="rounded-xl bg-muted/50 border-transparent"
            />
          </div>

          <div className="flex gap-3">
            <Button variant="outline" className="flex-1 h-12 rounded-xl" onClick={() => setStep(1)}>
              Back
            </Button>
            <Button
              className="flex-1 h-12 rounded-xl gradient-primary text-primary-foreground border-0 hover:opacity-90"
              disabled={!amount}
              onClick={() => setStep(3)}
            >
              Review <ArrowRight className="h-4 w-4 ml-2" />
            </Button>
          </div>
        </motion.div>
      )}

      {step === 3 && (
        <motion.div initial={{ opacity: 0, scale: 0.95 }} animate={{ opacity: 1, scale: 1 }} className="space-y-5">
          <div className="glass-card p-6 text-center space-y-4">
            <div className="w-16 h-16 rounded-full gradient-primary flex items-center justify-center mx-auto">
              <CheckCircle className="h-8 w-8 text-primary-foreground" />
            </div>
            <h2 className="text-xl font-bold">Confirm Transfer</h2>
            <div className="space-y-3 text-sm">
              <div className="flex justify-between py-2 border-b border-border">
                <span className="text-muted-foreground">To</span>
                <span className="font-medium">{recipient}</span>
              </div>
              <div className="flex justify-between py-2 border-b border-border">
                <span className="text-muted-foreground">Amount</span>
                <span className="font-bold text-lg">${parseFloat(amount || "0").toFixed(2)}</span>
              </div>
              {note && (
                <div className="flex justify-between py-2 border-b border-border">
                  <span className="text-muted-foreground">Note</span>
                  <span>{note}</span>
                </div>
              )}
              <div className="flex justify-between py-2">
                <span className="text-muted-foreground">Fee</span>
                <span className="text-success font-medium">Free</span>
              </div>
            </div>
          </div>

          <div className="flex gap-3">
            <Button variant="outline" className="flex-1 h-12 rounded-xl" onClick={() => setStep(2)}>
              Back
            </Button>
            <Button
              className="flex-1 h-12 rounded-xl gradient-primary text-primary-foreground border-0 hover:opacity-90"
              onClick={() => setStep(1)}
            >
              Send Money
            </Button>
          </div>
        </motion.div>
      )}
    </motion.div>
  );
}
