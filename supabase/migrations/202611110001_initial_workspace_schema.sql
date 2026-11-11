-- Quorix workspace extensions. The canonical base schema is 0001_quorix_core.sql.
-- Keep the existing user_id ownership model consistent across all workspace tables.
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

alter table public.provider_connections enable row level security;
drop policy if exists "Users manage their own provider connections" on public.provider_connections;
create policy "Users manage their own provider connections"
  on public.provider_connections for all to authenticated
  using ((select auth.uid()) = user_id)
  with check ((select auth.uid()) = user_id);

-- Store only authenticated, server-encrypted credentials in this table.
-- Do not expose credential_ciphertext to browser queries or store plaintext keys.
