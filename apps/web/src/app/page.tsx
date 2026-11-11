"use client";

import { FormEvent, useState } from "react";
import Link from "next/link";

type ChatMessage = { role: "user" | "assistant"; content: string };
const groups = [
  { label: "WORKSPACE", items: ["Home", "Normal Chat", "BOSS Network", "Projects", "Library"] },
  { label: "CREATE", items: ["AI Builder", "Research", "Media Studio", "Automation"] },
  { label: "SYSTEM", items: ["Device Control", "Plugins", "API Manager", "AI Monitor", "Review Center"] },
];
const cards = [
  ["✣", "BOSS Network", "Coordinate AI agents and review their work."],
  ["⌘", "AI Builder", "Build websites, apps, bots and tools."],
  ["⌕", "Research", "Search sources and prepare cited reports."],
  ["▣", "Device Control", "Manage supported Android devices."],
];
const providerOptions = [
  { id: "openai", name: "OpenAI", model: "gpt-4o-mini" },
  { id: "openrouter", name: "OpenRouter", model: "openai/gpt-4o-mini" },
  { id: "groq", name: "Groq", model: "llama-3.3-70b-versatile" },
  { id: "deepseek", name: "DeepSeek", model: "deepseek-chat" },
];

export default function Home() {
  const [active, setActive] = useState("Home");
  const [mode, setMode] = useState("Normal Chat");
  const [prompt, setPrompt] = useState("");
  const [notice, setNotice] = useState("");
  const [providerId, setProviderId] = useState("openai");
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [busy, setBusy] = useState(false);

  async function sendMessage(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const text = prompt.trim();
    if (!text || busy) return;
    const nextMessages: ChatMessage[] = [...messages, { role: "user", content: text }];
    setMessages([...nextMessages, { role: "assistant", content: "" }]);
    setPrompt("");
    setNotice("");
    setBusy(true);
    try {
      const provider = providerOptions.find((item) => item.id === providerId) ?? providerOptions[0];
      const response = await fetch("/api/chat", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ providerId, model: provider.model, messages: nextMessages }),
      });
      if (!response.ok) {
        const error = await response.json().catch(() => ({ error: "Chat request failed." }));
        throw new Error(error.error ?? "Chat request failed.");
      }
      if (!response.body) throw new Error("The server did not return a response stream.");
      const reader = response.body.getReader();
      const decoder = new TextDecoder();
      let buffer = "";
      let answer = "";
      while (true) {
        const chunk = await reader.read();
        if (chunk.done) break;
        buffer += decoder.decode(chunk.value, { stream: true });
        const lines = buffer.split("\n");
        buffer = lines.pop() ?? "";
        for (const line of lines) {
          if (!line.startsWith("data:")) continue;
          const data = line.slice(5).trim();
          if (!data || data === "[DONE]") continue;
          try {
            const packet = JSON.parse(data);
            answer += packet.choices?.[0]?.delta?.content ?? "";
            setMessages([...nextMessages, { role: "assistant", content: answer }]);
          } catch {
            // Ignore incomplete or provider-specific SSE packets.
          }
        }
      }
      // Process the final unterminated SSE line and any decoder-buffered bytes.
      buffer += decoder.decode();
      if (buffer.startsWith("data:")) {
        const data = buffer.slice(5).trim();
        if (data && data !== "[DONE]") {
          try {
            const packet = JSON.parse(data);
            answer += packet.choices?.[0]?.delta?.content ?? "";
            setMessages([...nextMessages, { role: "assistant", content: answer }]);
          } catch {
            // Ignore a malformed final provider packet.
          }
        }
      }
      if (!answer) setNotice("The provider returned no text.");
    } catch (error) {
      setMessages(nextMessages);
      setNotice(error instanceof Error ? error.message : "Chat request failed.");
    } finally {
      setBusy(false);
    }
  }

  function selectSection(item: string) {
    setActive(item);
    if (item === "BOSS Network" || item === "Normal Chat") setMode(item);
    if (item !== "Normal Chat") setMessages([]);
  }

  return <main className="app-shell">
    <aside className="sidebar">
      <div className="brand"><div className="brand-mark">Q</div><div><b>QUORIX</b><small>AI WORKSPACE</small></div></div>
      <button className="new-chat" onClick={() => {setActive("Normal Chat");setMode("Normal Chat");setMessages([]);setPrompt("");setNotice("");}}>＋ <span>New chat</span></button>
      <nav>{groups.map(group => <section className="nav-group" key={group.label}><p>{group.label}</p>{group.items.map(item => <button key={item} className={active===item?"nav-item active":"nav-item"} onClick={()=>selectSection(item)}><span>◇</span>{item}{item==="BOSS Network"&&<i>BETA</i>}</button>)}</section>)}</nav>
      <div className="account"><div className="avatar">Q</div><div><b>My Workspace</b><small>Personal account</small></div></div>
    </aside>
    <section className="main-panel">
      <header className="topbar"><span>Workspace　/　<b>{active}</b></span><div><Link href="/login" className="auth-link">Sign in</Link><Link href="/signup" className="auth-link primary">Create account</Link><i className="online"/> Preview workspace <span className="avatar">Q</span></div></header>
      <div className="content">
        <div className="eyebrow">YOUR AI WORKSPACE</div>
        <h1>{active==="Home"?<>One workspace.<br/><em>Every intelligence.</em></>:active}</h1>
        <p className="subtitle">{active==="Home"?"Welcome to Quorix. What would you like to work on today?":`Your Quorix ${active} workspace.`}</p>
        <div className="mode-switch"><button className={mode==="Normal Chat"?"chosen":""} onClick={()=>{setMode("Normal Chat");setActive("Normal Chat");}}>◉ Normal Chat</button><button className={mode==="BOSS Network"?"chosen":""} onClick={()=>{setMode("BOSS Network");setActive("BOSS Network");}}>✣ BOSS Network</button></div>
        {active==="Normal Chat" || active==="Home" ? <section className="composer-wrap">
          {messages.length>0&&<div className="chat-transcript">{messages.map((message,index)=><article className={message.role==="user"?"chat-message user-message":"chat-message assistant-message"} key={index}><b>{message.role==="user"?"You":"Quorix"}</b><p>{message.content || (busy && index===messages.length-1 ? "Thinking…" : "")}</p></article>)}</div>}
          <form className="composer" onSubmit={sendMessage}>
            <div className="composer-title"><b>Ask Quorix anything.</b><small>Streaming chat · Sign in and configure a server provider key to use AI.</small></div>
            <textarea value={prompt} onChange={e=>setPrompt(e.target.value)} placeholder="Message Quorix..." rows={3}/>
            <div className="composer-bottom"><div><select aria-label="AI provider" value={providerId} onChange={e=>setProviderId(e.target.value)}>{providerOptions.map(item=><option key={item.id} value={item.id}>{item.name}</option>)}</select><button type="button" onClick={()=>setActive("API Manager")}>◉ API Manager</button></div><button className="send" type="submit" disabled={busy || !prompt.trim()}>{busy?"…":"↑"}</button></div>
            {notice&&<small className="notice" role="status">{notice}</small>}
          </form>
        </section> : <section className="composer"><div className="composer-title"><b>{active==="BOSS Network"?"What should your AI team work on?":`Your ${active} workspace`}</b><small>{active==="BOSS Network"?"BOSS orchestration is the next implementation phase.":"This module is being built into the shared Quorix workspace."}</small></div><p className="provider-note">This workspace is currently a preview. Its backend and platform integrations are being implemented.</p></section>}
        <div className="section-title"><div><span>GET STARTED</span><h2>Explore your workspace</h2></div><button onClick={()=>setActive("API Manager")}>Connect AI providers ↗</button></div>
        <div className="cards">{cards.map(([icon,title,desc])=><button className="feature-card" key={title} onClick={()=>selectSection(title)}><span>{icon}</span><b>{title}</b><small>{desc}</small><i>↗</i></button>)}</div>
        <div className="section-title providers-title"><div><span>MODEL ECOSYSTEM</span><h2>One expandable provider list</h2></div><small>20 core providers + media · expandable</small></div>
        <div className="provider-note">Provider catalog is available at <code>/api/providers</code>. Initial chat adapters: OpenAI, OpenRouter, Groq and DeepSeek. Configure server-side keys before use.</div>
        <footer>QUORIX <span>·</span> YOUR AI WORKSPACE <span>·</span> EARLY PREVIEW</footer>
      </div>
    </section>
  </main>;
}
