import { NextRequest, NextResponse } from "next/server";
import { createClient } from "@/lib/supabase/server";

export const runtime = "nodejs";
export const dynamic = "force-dynamic";

const PROVIDER_CONFIG: Record<string, { endpoint: string; keyName: string; defaultModel: string }> = {
  openai: { endpoint: "https://api.openai.com/v1/chat/completions", keyName: "OPENAI_API_KEY", defaultModel: "gpt-4o-mini" },
  openrouter: { endpoint: "https://openrouter.ai/api/v1/chat/completions", keyName: "OPENROUTER_API_KEY", defaultModel: "openai/gpt-4o-mini" },
  groq: { endpoint: "https://api.groq.com/openai/v1/chat/completions", keyName: "GROQ_API_KEY", defaultModel: "llama-3.3-70b-versatile" },
  deepseek: { endpoint: "https://api.deepseek.com/chat/completions", keyName: "DEEPSEEK_API_KEY", defaultModel: "deepseek-chat" },
};

type ChatMessage = { role: "user" | "assistant" | "system"; content: string };

export async function POST(request: NextRequest) {
  const supabaseUrl = process.env.NEXT_PUBLIC_SUPABASE_URL;
  const supabaseKey = process.env.NEXT_PUBLIC_SUPABASE_PUBLISHABLE_KEY;
  if (!supabaseUrl || !supabaseKey) {
    return NextResponse.json({ error: "Authentication is not configured yet." }, { status: 503 });
  }

  const supabase = await createClient();
  const { data: { user }, error: authError } = await supabase.auth.getUser();
  if (authError || !user) {
    return NextResponse.json({ error: "Sign in to use Quorix Chat." }, { status: 401 });
  }

  let payload: { providerId?: string; model?: string; messages?: ChatMessage[] };
  try {
    payload = await request.json();
  } catch {
    return NextResponse.json({ error: "Request body must be valid JSON." }, { status: 400 });
  }

  const providerId = payload.providerId ?? "openai";
  const config = PROVIDER_CONFIG[providerId];
  if (!config) {
    return NextResponse.json({ error: "This provider adapter is not available yet." }, { status: 400 });
  }

  const messages = payload.messages;
  if (!Array.isArray(messages) || messages.length === 0 || messages.length > 60) {
    return NextResponse.json({ error: "Send between 1 and 60 messages." }, { status: 400 });
  }
  const validMessages = messages.every((message) =>
    message && ["user", "assistant", "system"].includes(message.role) &&
    typeof message.content === "string" && message.content.length > 0 && message.content.length <= 20000
  );
  if (!validMessages) {
    return NextResponse.json({ error: "One or more messages are invalid." }, { status: 400 });
  }

  const apiKey = process.env[config.keyName];
  if (!apiKey) {
    return NextResponse.json({ error: config.keyName + " is not configured on the server." }, { status: 503 });
  }

  const model = typeof payload.model === "string" && /^[a-zA-Z0-9._:/-]{1,120}$/.test(payload.model)
    ? payload.model
    : config.defaultModel;

  try {
    const upstream = await fetch(config.endpoint, {
      method: "POST",
      headers: {
        Authorization: "Bearer " + apiKey,
        "Content-Type": "application/json",
        ...(providerId === "openrouter" ? { "HTTP-Referer": process.env.APP_URL ?? "https://quorix.app", "X-Title": "Quorix" } : {}),
      },
      body: JSON.stringify({ model, messages, stream: true }),
      signal: request.signal,
    });

    if (!upstream.ok) {
      const detail = await upstream.text();
      return NextResponse.json(
        { error: "AI provider request failed.", providerStatus: upstream.status, detail: detail.slice(0, 1200) },
        { status: upstream.status >= 400 && upstream.status < 600 ? upstream.status : 502 },
      );
    }
    if (!upstream.body) {
      return NextResponse.json({ error: "Provider returned an empty response." }, { status: 502 });
    }
    return new Response(upstream.body, {
      status: 200,
      headers: {
        "Content-Type": "text/event-stream; charset=utf-8",
        "Cache-Control": "no-cache, no-transform",
        Connection: "keep-alive",
        "X-Accel-Buffering": "no",
      },
    });
  } catch (error) {
    const message = error instanceof Error ? error.message : "Unknown upstream error";
    return NextResponse.json({ error: "Could not reach the AI provider.", detail: message }, { status: 502 });
  }
}
