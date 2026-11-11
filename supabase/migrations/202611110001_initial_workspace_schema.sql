-- Quorix initial shared workspace schema.
-- Apply with Supabase migrations after reviewing the target project.
create extension if not exists pgcrypto;

create table if not exists public.profiles (
  id uuid primary key references auth.users(id) on delete cascade,
  display_name text,
  avatar_url text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists public.workspaces (
  id uuid primary key default gen_random_uuid(),
  owner_id uuid not null references auth.users(id) on delete cascade,
  name text not null default 'My Workspace',
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists public.projects (
  id uuid primary key default gen_random_uuid(),
  workspace_id uuid not null references public.workspaces(id) on delete cascade,
  owner_id uuid not null references auth.users(id) on delete cascade,
  name text not null,
  description text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists public.conversations (
  id uuid primary key default gen_random_uuid(),
  workspace_id uuid not null references public.workspaces(id) on delete cascade,
  project_id uuid references public.projects(id) on delete set null,
  owner_id uuid not null references auth.users(id) on delete cascade,
  title text not null default 'New chat',
  mode text not null default 'normal' check (mode in ('normal', 'boss')),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists public.messages (
  id uuid primary key default gen_random_uuid(),
  conversation_id uuid not null references public.conversations(id) on delete cascade,
  owner_id uuid not null references auth.users(id) on delete cascade,
  role text not null check (role in ('system', 'user', 'assistant', 'tool')),
  content text not null default '',
  provider_id text,
  model_id text,
  metadata jsonb not null default '{}'::jsonb,
  created_at timestamptz not null default now()
);

create table if not exists public.provider_connections (
  id uuid primary key default gen_random_uuid(),
  owner_id uuid not null references auth.users(id) on delete cascade,
  provider_id text not null,
  label text,
  credential_ciphertext text not null,
  credential_key_version text not null,
  status text not null default 'pending' check (status in ('pending', 'connected', 'error', 'disabled')),
  last_tested_at timestamptz,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  unique(owner_id, provider_id, label)
);

create index if not exists projects_owner_updated_idx on public.projects(owner_id, updated_at desc);
create index if not exists conversations_owner_updated_idx on public.conversations(owner_id, updated_at desc);
create index if not exists messages_conversation_created_idx on public.messages(conversation_id, created_at);
create index if not exists provider_connections_owner_idx on public.provider_connections(owner_id);

alter table public.profiles enable row level security;
alter table public.workspaces enable row level security;
alter table public.projects enable row level security;
alter table public.conversations enable row level security;
alter table public.messages enable row level security;
alter table public.provider_connections enable row level security;

create policy "profiles_select_own" on public.profiles for select using (auth.uid() = id);
create policy "profiles_insert_own" on public.profiles for insert with check (auth.uid() = id);
create policy "profiles_update_own" on public.profiles for update using (auth.uid() = id) with check (auth.uid() = id);

create policy "workspaces_owner_all" on public.workspaces for all using (auth.uid() = owner_id) with check (auth.uid() = owner_id);
create policy "projects_owner_all" on public.projects for all using (auth.uid() = owner_id) with check (auth.uid() = owner_id);
create policy "conversations_owner_all" on public.conversations for all using (auth.uid() = owner_id) with check (auth.uid() = owner_id);
create policy "messages_owner_all" on public.messages for all using (auth.uid() = owner_id) with check (auth.uid() = owner_id);
create policy "provider_connections_owner_all" on public.provider_connections for all using (auth.uid() = owner_id) with check (auth.uid() = owner_id);

-- Provider credentials must be encrypted by the server with a key stored outside the database.
-- Never expose this table through an unauthenticated client or store plaintext API keys.
