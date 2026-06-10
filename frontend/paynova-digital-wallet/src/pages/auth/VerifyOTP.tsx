import { motion } from "framer-motion";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { InputOTP, InputOTPGroup, InputOTPSlot } from "@/components/ui/input-otp";
import { ShieldCheck } from "lucide-react";

export default function VerifyOTP() {
  const [otp, setOtp] = useState("");
  const navigate = useNavigate();

  return (
    <div className="min-h-screen flex items-center justify-center p-4 bg-background">
      <motion.div initial={{ opacity: 0, y: 30 }} animate={{ opacity: 1, y: 0 }} className="w-full max-w-md space-y-8 text-center">
        <div>
          <div className="w-14 h-14 rounded-2xl gradient-primary flex items-center justify-center mx-auto mb-4">
            <ShieldCheck className="h-7 w-7 text-primary-foreground" />
          </div>
          <h1 className="text-2xl font-bold">Verify your identity</h1>
          <p className="text-muted-foreground text-sm mt-1">Enter the 6-digit code sent to your email</p>
        </div>

        <div className="glass-card p-8 flex justify-center">
          <InputOTP maxLength={6} value={otp} onChange={setOtp}>
            <InputOTPGroup>
              <InputOTPSlot index={0} />
              <InputOTPSlot index={1} />
              <InputOTPSlot index={2} />
              <InputOTPSlot index={3} />
              <InputOTPSlot index={4} />
              <InputOTPSlot index={5} />
            </InputOTPGroup>
          </InputOTP>
        </div>

        <Button
          className="w-full h-12 rounded-xl gradient-primary text-primary-foreground border-0 hover:opacity-90"
          disabled={otp.length !== 6}
          onClick={() => navigate("/")}
        >
          Verify & Continue
        </Button>

        <p className="text-sm text-muted-foreground">
          Didn't receive the code? <button className="text-primary font-medium hover:underline">Resend</button>
        </p>
      </motion.div>
    </div>
  );
}
