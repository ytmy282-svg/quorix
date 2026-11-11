-- Quorix additions to the canonical 0001_quorix_core.sql schema.
-- Existing projects/conversations/messages use user_id ownership; preserve that contract.
create table if not exists public.workspaces (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  name text not null default 'My Workspace',
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists public.provider_connections (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  provider_id text not null,
  label text,
  credential_ciphertext text not null,
  credential_key_version text not null,
  status text not null default 'pending' check (status in ('pending', 'connected', 'error', 'disabled')),
  last_tested_at timestamptz,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  unique(user_id, provider_id, label)
);

create index if not exists workspaces_user_updated_idx on public.workspaces(user_id, updated_at desc);
create index if not exists provider_connections_user_idx on public.provider_connections(user_id);

alter table public.workspaces enable row level security;
alter table public.provider_connections enable row level security;

drop policy if exists "Users manage their own workspaces" on public.workspaces;
create policy "Users manage their own workspaces"
  on public.workspaces for all to authenticated
  using ((select auth.uid()) = user_id)
  with check ((select auth.uid()) = user_id);

drop policy if exists "Users manage their own provider connections" on public.provider_connections;
create policy "Users manage their own provider connections"
  on public.provider_connections for all to authenticated
  using ((select auth.uid()) = user_id)
  with check ((select auth.uid()) = user_id);

-- Provider credentials must be encrypted by the server before insertion.
-- Do not expose credential_ciphertext to browser queries or store plaintext keys.
