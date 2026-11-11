"use client";

import { FormEvent, useState } from "react";
import Link from "next/link";
import { createClient } from "@/lib/supabase/client";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [busy, setBusy] = useState(false);
  const [message, setMessage] = useState("");

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setBusy(true);
    setMessage("");
    try {
      const supabase = createClient();
      const { error } = await supabase.auth.signInWithPassword({ email, password });
      if (error) throw error;
      window.location.assign("/");
    } catch (error) {
      setMessage(error instanceof Error ? error.message : "Sign-in failed.");
    } finally {
      setBusy(false);
    }
  }

  return (
    <main className="auth-shell">
      <section className="auth-card">
        <Link href="/" className="auth-brand"><span>Q</span><b>QUORIX</b></Link>
        <p className="eyebrow">YOUR AI WORKSPACE</p>
        <h1>Welcome back.</h1>
        <p className="auth-copy">Sign in to continue to your Quorix workspace.</p>
        <form onSubmit={submit} className="auth-form">
          <label htmlFor="email">Email</label>
          <input id="email" type="email" autoComplete="email" required value={email} onChange={(event) => setEmail(event.target.value)} />
          <label htmlFor="password">Password</label>
          <input id="password" type="password" autoComplete="current-password" required minLength={6} value={password} onChange={(event) => setPassword(event.target.value)} />
          <button type="submit" disabled={busy}>{busy ? "Signing in..." : "Sign in"}</button>
        </form>
        {message && <p className="auth-message" role="alert">{message}</p>}
        <p className="auth-foot">New to Quorix? <Link href="/signup">Create an account</Link></p>
        <p className="auth-foot"><Link href="/">← Back to workspace preview</Link></p>
      </section>
    </main>
  );
}
