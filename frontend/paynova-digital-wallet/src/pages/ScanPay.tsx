import { motion } from "framer-motion";
import { QrCode, Camera } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

export default function ScanPay() {
  return (
    <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="max-w-lg mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold">Scan & Pay</h1>
        <p className="text-muted-foreground text-sm mt-1">Scan a QR code to make instant payments</p>
      </div>

      <div className="glass-card p-8 flex flex-col items-center space-y-6">
        <div className="w-64 h-64 rounded-3xl border-2 border-dashed border-primary/30 flex items-center justify-center bg-muted/30 relative overflow-hidden">
          <div className="absolute inset-4 border-2 border-primary/20 rounded-2xl" />
          <div className="flex flex-col items-center gap-3 text-muted-foreground">
            <Camera className="h-12 w-12 text-primary/50" />
            <span className="text-sm font-medium">Camera Preview</span>
            <span className="text-xs">Point at a QR code</span>
          </div>
        </div>

        <Button className="w-full h-12 rounded-xl gradient-primary text-primary-foreground border-0 hover:opacity-90 gap-2">
          <Camera className="h-5 w-5" /> Open Camera
        </Button>

        <div className="flex items-center gap-4 w-full">
          <div className="h-px flex-1 bg-border" />
          <span className="text-sm text-muted-foreground">or enter code</span>
          <div className="h-px flex-1 bg-border" />
        </div>

        <Input placeholder="Enter payment code" className="rounded-xl h-12 bg-muted/50 border-transparent text-center" />
      </div>

      <div className="glass-card p-6 text-center">
        <h3 className="font-semibold mb-3">Your QR Code</h3>
        <div className="w-48 h-48 mx-auto rounded-2xl gradient-primary p-4 flex items-center justify-center">
          <QrCode className="h-32 w-32 text-primary-foreground" />
        </div>
        <p className="text-sm text-muted-foreground mt-3">Share this code to receive payments</p>
      </div>
    </motion.div>
  );
}
