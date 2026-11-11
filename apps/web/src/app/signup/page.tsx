"use client";

import { FormEvent, useState } from "react";
import Link from "next/link";
import { createClient } from "@/lib/supabase/client";

export default function SignupPage() {
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
      const { data, error } = await supabase.auth.signUp({ email, password });
      if (error) throw error;
      setMessage(data.session ? "Account created. You can now sign in." : "Check your email to confirm your account, then sign in.");
    } catch (error) {
      setMessage(error instanceof Error ? error.message : "Account creation failed.");
    } finally {
      setBusy(false);
    }
  }

  return (
    <main className="auth-shell">
      <section className="auth-card">
        <Link href="/" className="auth-brand"><span>Q</span><b>QUORIX</b></Link>
        <p className="eyebrow">CREATE YOUR WORKSPACE</p>
        <h1>Get started.</h1>
        <p className="auth-copy">Create your personal Quorix account.</p>
        <form onSubmit={submit} className="auth-form">
          <label htmlFor="email">Email</label>
          <input id="email" type="email" autoComplete="email" required value={email} onChange={(event) => setEmail(event.target.value)} />
          <label htmlFor="password">Password</label>
          <input id="password" type="password" autoComplete="new-password" required minLength={8} value={password} onChange={(event) => setPassword(event.target.value)} />
          <button type="submit" disabled={busy}>{busy ? "Creating account..." : "Create account"}</button>
        </form>
        {message && <p className="auth-message" role="status">{message}</p>}
        <p className="auth-foot">Already registered? <Link href="/login">Sign in</Link></p>
      </section>
    </main>
  );
}
