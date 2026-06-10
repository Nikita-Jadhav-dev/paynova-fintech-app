import { motion } from "framer-motion";
import { QrCode, Copy, Share2, Link } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

export default function Receive() {
  return (
    <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="max-w-lg mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold">Receive Money</h1>
        <p className="text-muted-foreground text-sm mt-1">Share your payment link or QR code</p>
      </div>

      <div className="glass-card p-8 flex flex-col items-center space-y-6">
        <div className="w-52 h-52 rounded-3xl gradient-primary p-5 flex items-center justify-center">
          <QrCode className="h-36 w-36 text-primary-foreground" />
        </div>
        <div>
          <p className="text-center font-semibold text-lg">Alex Johnson</p>
          <p className="text-center text-sm text-muted-foreground">@alexjohnson</p>
        </div>
      </div>

      <div className="glass-card p-5 space-y-4">
        <label className="text-sm font-medium">Request Amount (optional)</label>
        <Input placeholder="$0.00" className="rounded-xl h-12 bg-muted/50 border-transparent text-center text-2xl font-bold" />
      </div>

      <div className="glass-card p-5 space-y-3">
        <label className="text-sm font-medium flex items-center gap-2">
          <Link className="h-4 w-4 text-primary" /> Payment Link
        </label>
        <div className="flex gap-2">
          <Input value="https://paynova.pay/alexjohnson" readOnly className="rounded-xl bg-muted/50 border-transparent text-sm" />
          <Button variant="outline" size="icon" className="rounded-xl shrink-0">
            <Copy className="h-4 w-4" />
          </Button>
        </div>
      </div>

      <Button className="w-full h-12 rounded-xl gradient-primary text-primary-foreground border-0 hover:opacity-90 gap-2">
        <Share2 className="h-4 w-4" /> Share Payment Link
      </Button>
    </motion.div>
  );
}
