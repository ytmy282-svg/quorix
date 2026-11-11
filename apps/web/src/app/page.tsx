"use client";

import { useState } from "react";

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
export default function Home() {
  const [active, setActive] = useState("Home");
  const [mode, setMode] = useState("Normal Chat");
  const [prompt, setPrompt] = useState("");
  const [notice, setNotice] = useState("");
  return <main className="app-shell">
    <aside className="sidebar">
      <div className="brand"><div className="brand-mark">Q</div><div><b>QUORIX</b><small>AI WORKSPACE</small></div></div>
      <button className="new-chat" onClick={() => {setActive("Normal Chat");setMode("Normal Chat");setPrompt("");}}>＋ <span>New chat</span></button>
      <nav>{groups.map(group => <section className="nav-group" key={group.label}><p>{group.label}</p>{group.items.map(item => <button key={item} className={active===item?"nav-item active":"nav-item"} onClick={()=>{setActive(item);if(item==="BOSS Network")setMode(item);if(item==="Normal Chat")setMode(item);}}><span>◇</span>{item}{item==="BOSS Network"&&<i>BETA</i>}</button>)}</section>)}</nav>
      <div className="account"><div className="avatar">Q</div><div><b>My Workspace</b><small>Personal account</small></div></div>
    </aside>
    <section className="main-panel">
      <header className="topbar"><span>Workspace　/　<b>{active}</b></span><div><i className="online"/> Preview workspace <span className="avatar">Q</span></div></header>
      <div className="content">
        <div className="eyebrow">YOUR AI WORKSPACE</div>
        <h1>{active==="Home"?<>One workspace.<br/><em>Every intelligence.</em></>:active}</h1>
        <p className="subtitle">{active==="Home"?"Welcome to Quorix. What would you like to work on today?":`Your Quorix ${active} workspace.`}</p>
        <div className="mode-switch"><button className={mode==="Normal Chat"?"chosen":""} onClick={()=>{setMode("Normal Chat");setActive("Normal Chat");}}>◉ Normal Chat</button><button className={mode==="BOSS Network"?"chosen":""} onClick={()=>{setMode("BOSS Network");setActive("BOSS Network");}}>✣ BOSS Network</button></div>
        <section className="composer"><div className="composer-title"><b>{mode==="BOSS Network"?"What should your AI team work on?":"Ask Quorix anything."}</b><small>{mode==="BOSS Network"?"BOSS can plan, delegate and review tasks.":"Choose a model after connecting a provider."}</small></div><textarea value={prompt} onChange={e=>setPrompt(e.target.value)} placeholder={mode==="BOSS Network"?"Describe a task for your AI team...":"Message Quorix..."} /><div className="composer-bottom"><div><button onClick={()=>setNotice("File upload will be added with the storage integration.")}>＋ Attach</button><button onClick={()=>setNotice("Web search will be added in the Research phase.")}>⌕ Web search</button><button onClick={()=>setActive("API Manager")}>◉ Models</button></div><button className="send" onClick={()=>setNotice(prompt.trim()?"Chat interface preview only — AI provider connection is the next phase.":"Write a message first.")}>↑</button></div>{notice&&<small className="notice">{notice}</small>}</section>
        <div className="section-title"><div><span>GET STARTED</span><h2>Explore your workspace</h2></div><button onClick={()=>setActive("API Manager")}>Connect AI providers ↗</button></div>
        <div className="cards">{cards.map(([icon,title,desc])=><button className="feature-card" key={title} onClick={()=>setActive(title)}><span>{icon}</span><b>{title}</b><small>{desc}</small><i>↗</i></button>)}</div>
        <div className="section-title providers-title"><div><span>MODEL ECOSYSTEM</span><h2>One expandable provider list</h2></div><small>20 core providers + media · expandable</small></div>
        <div className="provider-note">Connect provider API keys or officially supported account authorization in API Manager. No provider is connected yet.</div>
        <footer>QUORIX <span>·</span> YOUR AI WORKSPACE <span>·</span> EARLY PREVIEW</footer>
      </div>
    </section>
  </main>;
}
