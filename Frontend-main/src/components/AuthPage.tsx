import { useState } from "react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card";
import { Separator } from "./ui/separator";
import { BookOpenIcon, PenToolIcon, ShareIcon, SmartphoneIcon } from "lucide-react";

interface Props {
  onAuth: () => void;
}

export const AuthPage = ({ onAuth }: Props) => {
  const [isLogin, setIsLogin] = useState(true);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Simulate authentication
    onAuth();
  };

  return (
    <div className="min-h-screen flex relative overflow-hidden">
      {/* Background Effects */}
      <div className="absolute inset-0 bg-gradient-glow opacity-50" />
      <div className="absolute top-0 left-0 w-96 h-96 bg-primary/10 rounded-full blur-3xl animate-pulse" />
      <div className="absolute bottom-0 right-0 w-80 h-80 bg-accent/10 rounded-full blur-3xl animate-pulse delay-1000" />
      
      {/* Left side - Hero */}
      <div className="hidden lg:flex lg:w-1/2 bg-gradient-hero relative overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-br from-black/30 via-black/20 to-transparent" />
        <div className="absolute inset-0 bg-gradient-to-t from-black/40 via-transparent to-transparent" />
        <div className="relative z-10 flex flex-col justify-center p-12 text-white">
          <div className="mb-8 animate-fade-in">
            <div className="flex items-center space-x-3 mb-6 group">
              <div className="w-12 h-12 bg-white/20 rounded-2xl flex items-center justify-center backdrop-blur-sm shadow-floating group-hover:scale-110 transition-all duration-300">
                <BookOpenIcon className="w-6 h-6 text-white" />
              </div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-white to-white/80 bg-clip-text text-transparent">
                Notes Suite
              </h1>
            </div>
            
            <h2 className="text-5xl font-bold leading-tight mb-6 bg-gradient-to-r from-white via-white to-white/90 bg-clip-text text-transparent">
              Notes<span className="text-white/60">.</span>
            </h2>
            
            <p className="text-lg text-white/70 mb-8 leading-relaxed">
              Beautiful notes, everywhere.
            </p>
          </div>

          <div className="grid grid-cols-1 gap-4">
            <div className="flex items-center space-x-4 group hover:translate-x-2 transition-all duration-300">
              <div className="w-12 h-12 bg-gradient-to-br from-white/30 to-white/10 rounded-2xl flex items-center justify-center backdrop-blur-sm shadow-floating group-hover:scale-110 transition-all duration-300">
                <PenToolIcon className="w-5 h-5" />
              </div>
              <div className="flex-1">
                <h3 className="font-semibold text-white">Markdown Editor</h3>
              </div>
            </div>
            
            <div className="flex items-center space-x-4 group hover:translate-x-2 transition-all duration-300 delay-75">
              <div className="w-12 h-12 bg-gradient-to-br from-white/30 to-white/10 rounded-2xl flex items-center justify-center backdrop-blur-sm shadow-floating group-hover:scale-110 transition-all duration-300">
                <ShareIcon className="w-5 h-5" />
              </div>
              <div className="flex-1">
                <h3 className="font-semibold text-white">Smart Sharing</h3>
              </div>
            </div>
            
            <div className="flex items-center space-x-4 group hover:translate-x-2 transition-all duration-300 delay-150">
              <div className="w-12 h-12 bg-gradient-to-br from-white/30 to-white/10 rounded-2xl flex items-center justify-center backdrop-blur-sm shadow-floating group-hover:scale-110 transition-all duration-300">
                <SmartphoneIcon className="w-5 h-5" />
              </div>
              <div className="flex-1">
                <h3 className="font-semibold text-white">Offline Mobile</h3>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Right side - Auth Form */}
      <div className="w-full lg:w-1/2 flex items-center justify-center p-8 bg-gradient-card relative z-10">
        <div className="w-full max-w-md">
          <div className="text-center mb-8 lg:hidden animate-fade-in">
            <div className="flex items-center justify-center space-x-3 mb-4 group">
              <div className="w-12 h-12 bg-gradient-primary rounded-2xl flex items-center justify-center shadow-glow group-hover:scale-110 transition-all duration-300">
                <BookOpenIcon className="w-5 h-5 text-white" />
              </div>
              <h1 className="text-2xl font-bold bg-gradient-to-r from-foreground to-muted-foreground bg-clip-text text-transparent">
                Notes Suite
              </h1>
            </div>
          </div>

          <Card className="shadow-floating border-0 bg-card/80 backdrop-blur-sm hover:shadow-elevated transition-all duration-300">
            <CardHeader className="text-center">
              <CardTitle className="text-2xl">
                {isLogin ? "Welcome back" : "Create account"}
              </CardTitle>
              <CardDescription>
                {isLogin 
                  ? "Sign in to access your notes"
                  : "Start organizing your thoughts today"
                }
              </CardDescription>
            </CardHeader>
            
            <CardContent>
              <form onSubmit={handleSubmit} className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="email">Email</Label>
                  <Input
                    id="email"
                    type="email"
                    placeholder="Enter your email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                  />
                </div>
                
                <div className="space-y-2">
                  <Label htmlFor="password">Password</Label>
                  <Input
                    id="password"
                    type="password"
                    placeholder="Enter your password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                  />
                </div>
                
                {!isLogin && (
                  <div className="space-y-2">
                    <Label htmlFor="confirmPassword">Confirm Password</Label>
                    <Input
                      id="confirmPassword"
                      type="password"
                      placeholder="Confirm your password"
                      value={confirmPassword}
                      onChange={(e) => setConfirmPassword(e.target.value)}
                      required
                    />
                  </div>
                )}
                
                <Button 
                  type="submit" 
                  className="w-full bg-gradient-primary hover:opacity-90 hover:scale-[1.02] active:scale-[0.98] transition-all duration-200 shadow-glow hover:shadow-floating text-white font-medium"
                >
                  {isLogin ? "Sign In" : "Create Account"}
                </Button>
              </form>
              
              <Separator className="my-6" />
              
              <div className="text-center">
                <button
                  type="button"
                  onClick={() => setIsLogin(!isLogin)}
                  className="text-sm text-muted-foreground hover:text-primary transition-colors"
                >
                  {isLogin 
                    ? "Don't have an account? Sign up" 
                    : "Already have an account? Sign in"
                  }
                </button>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
};