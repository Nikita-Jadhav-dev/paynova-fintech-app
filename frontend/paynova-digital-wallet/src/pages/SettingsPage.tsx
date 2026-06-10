import { motion } from "framer-motion";
import { User, Bell, Shield, Palette, Globe } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Switch } from "@/components/ui/switch";

export default function SettingsPage() {
  return (
    <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="max-w-3xl mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold">Settings</h1>
        <p className="text-muted-foreground text-sm mt-1">Manage your account preferences</p>
      </div>

      <div className="glass-card p-6 space-y-6">
        <h3 className="font-semibold flex items-center gap-2"><User className="h-4 w-4 text-primary" /> Profile</h3>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label className="text-sm text-muted-foreground">Full Name</label>
            <Input defaultValue="Alex Johnson" className="mt-1 rounded-xl bg-muted/50 border-transparent" />
          </div>
          <div>
            <label className="text-sm text-muted-foreground">Email</label>
            <Input defaultValue="alex@paynova.com" className="mt-1 rounded-xl bg-muted/50 border-transparent" />
          </div>
          <div>
            <label className="text-sm text-muted-foreground">Phone</label>
            <Input defaultValue="+1 (555) 123-4567" className="mt-1 rounded-xl bg-muted/50 border-transparent" />
          </div>
          <div>
            <label className="text-sm text-muted-foreground">Username</label>
            <Input defaultValue="@alexjohnson" className="mt-1 rounded-xl bg-muted/50 border-transparent" />
          </div>
        </div>
        <Button className="rounded-xl gradient-primary text-primary-foreground border-0">Save Changes</Button>
      </div>

      <div className="glass-card p-6 space-y-5">
        <h3 className="font-semibold flex items-center gap-2"><Bell className="h-4 w-4 text-primary" /> Notifications</h3>
        {[
          { label: "Push Notifications", desc: "Receive push notifications for transactions" },
          { label: "Email Alerts", desc: "Get email updates for important activity" },
          { label: "SMS Alerts", desc: "Receive SMS for large transactions" },
          { label: "Marketing Emails", desc: "Receive promotional offers and updates" },
        ].map((item) => (
          <div key={item.label} className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium">{item.label}</p>
              <p className="text-xs text-muted-foreground">{item.desc}</p>
            </div>
            <Switch />
          </div>
        ))}
      </div>

      <div className="glass-card p-6 space-y-5">
        <h3 className="font-semibold flex items-center gap-2"><Shield className="h-4 w-4 text-primary" /> Security</h3>
        {[
          { label: "Two-Factor Authentication", desc: "Add extra security to your account" },
          { label: "Biometric Login", desc: "Use fingerprint or face recognition" },
          { label: "Login Alerts", desc: "Get notified of new login attempts" },
        ].map((item) => (
          <div key={item.label} className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium">{item.label}</p>
              <p className="text-xs text-muted-foreground">{item.desc}</p>
            </div>
            <Switch />
          </div>
        ))}
      </div>
    </motion.div>
  );
}
