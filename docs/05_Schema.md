# Quorix — Data Model and Security Schema

**Status:** Logical schema proposal; reconcile with existing Supabase migrations before implementation  
**Database:** Existing Supabase/Postgres foundation  
**Rule:** Do not create duplicate tables or change existing columns until current migrations and application queries have been inspected.

## 1. Data principles

- Every personal record belongs to an authenticated user or workspace.
- Use UUID primary keys unless the existing schema has an established convention.
- Include created_at and updated_at where appropriate.
- Store provider credentials separately from public provider metadata.
- Keep model catalog data separate from user connections.
- Apply Row Level Security to all user-owned tables.
- Do not store raw API keys in ordinary readable tables.

## 2. Logical entities

### profiles
- id UUID PK, references auth.users(id)
- display_name nullable
- avatar_url nullable
- created_at
- updated_at

### workspaces
- id UUID PK
- owner_user_id UUID FK -> auth.users(id)
- name
- workspace_type (personal/team; team is future)
- created_at
- updated_at

### workspace_members (future multi-user)
- id UUID PK
- workspace_id UUID FK -> workspaces(id)
- user_id UUID FK -> auth.users(id)
- role (owner/admin/member/viewer)
- created_at
Unique(workspace_id, user_id). Defer team behavior until approved.

### projects
- id UUID PK
- workspace_id UUID FK -> workspaces(id)
- created_by UUID FK -> auth.users(id)
- name
- description nullable
- instructions nullable
- status
- created_at
- updated_at

### conversations
- id UUID PK
- workspace_id UUID FK -> workspaces(id)
- project_id UUID nullable FK -> projects(id)
- created_by UUID FK -> auth.users(id)
- title
- status
- pinned_at nullable
- archived_at nullable
- created_at
- updated_at

### messages
- id UUID PK
- conversation_id UUID FK -> conversations(id)
- role (system/user/assistant/tool)
- content (text or structured JSON according to current implementation)
- provider_id nullable
- model_id nullable
- status (pending/streaming/completed/failed/cancelled)
- metadata JSONB
- created_at
Indexes: conversation_id + created_at.

### provider_catalog
- id TEXT PK (stable provider key)
- name
- category (core-model/inference/aggregator/media)
- api_format
- base_url nullable
- documentation_url
- capabilities JSONB
- availability_status
- free_tier_classification (permanent/limited/trial/paid/unknown)
- verification_source_url nullable
- last_verified_at nullable
- created_at
- updated_at

### model_catalog
- id UUID PK
- provider_id TEXT FK -> provider_catalog(id)
- model_key
- display_name
- capabilities JSONB
- context_window nullable
- max_output nullable
- availability_status
- pricing_metadata JSONB nullable
- last_verified_at nullable
Unique(provider_id, model_key).

### provider_connections
- id UUID PK
- workspace_id UUID FK -> workspaces(id)
- provider_id TEXT FK -> provider_catalog(id)
- created_by UUID FK -> auth.users(id)
- connection_label
- auth_method (api_key/oauth/approved_integration)
- secret_reference (opaque reference to secure secret storage; never raw secret)
- status
- last_tested_at nullable
- created_at
- updated_at

### tasks
- id UUID PK
- workspace_id UUID FK -> workspaces(id)
- created_by UUID FK -> auth.users(id)
- project_id UUID nullable FK -> projects(id)
- conversation_id UUID nullable FK -> conversations(id)
- task_type (boss/research/build/review/media/automation)
- status (queued/planning/running/waiting/synthesizing/completed/partial/failed/cancelled)
- input_summary
- result_summary nullable
- metadata JSONB
- started_at nullable
- completed_at nullable
- created_at
- updated_at

### agent_runs
- id UUID PK
- task_id UUID FK -> tasks(id)
- provider_id nullable
- model_id nullable
- agent_role
- status
- input_reference nullable
- output_reference nullable
- error_code nullable
- usage_metadata JSONB nullable
- started_at nullable
- completed_at nullable

### files
- id UUID PK
- workspace_id UUID FK -> workspaces(id)
- project_id UUID nullable FK -> projects(id)
- uploaded_by UUID FK -> auth.users(id)
- storage_path
- file_name
- mime_type
- size_bytes
- processing_status
- created_at

### automations (future phase)
- id UUID PK
- workspace_id UUID FK -> workspaces(id)
- created_by UUID FK -> auth.users(id)
- name
- trigger_config JSONB
- action_config JSONB
- enabled BOOLEAN
- last_run_at nullable
- next_run_at nullable
- created_at
- updated_at

### audit_events
- id UUID PK
- workspace_id UUID nullable FK -> workspaces(id)
- actor_user_id UUID nullable FK -> auth.users(id)
- event_type
- resource_type
- resource_id nullable
- safe_metadata JSONB
- created_at
Do not include secrets or sensitive prompt content by default.

## 3. Relationships

- auth.users 1:1 profiles.
- workspaces 1:N projects, conversations, provider_connections, tasks and files.
- projects 1:N conversations and files.
- conversations 1:N messages.
- tasks 1:N agent_runs.
- provider_catalog 1:N model_catalog and provider_connections.

## 4. RLS and authorization requirements

- Users may access only workspaces they own or are explicitly members of.
- Personal MVP: workspace owner is the only member by default.
- Projects, conversations, messages, files, tasks and connections inherit workspace access checks.
- Provider catalog and model catalog may be readable to authenticated users if they contain public metadata only.
- Provider connection metadata may be visible only to authorized workspace members.
- Secret values must be stored in a backend-controlled secret vault or encrypted server-side store; client roles must never select secret values.
- Audit events must be append-only to ordinary users.
- Service-role credentials must never be shipped in Android/web clients.

## 5. Indexes and constraints

- Index all foreign keys used in access filters.
- Index conversations(workspace_id, updated_at DESC).
- Index messages(conversation_id, created_at).
- Index provider_connections(workspace_id, provider_id).
- Index tasks(workspace_id, created_at DESC).
- Unique provider_catalog(id).
- Unique model_catalog(provider_id, model_key).
- Add check constraints for enumerated status/category fields where compatible with existing conventions.

## 6. Migration procedure

1. Inspect all existing migration files and current Supabase schema.
2. Map current tables/columns to this logical model.
3. Reuse compatible existing tables.
4. Write additive migrations; avoid destructive changes.
5. Add indexes and RLS policies in the same migration where possible.
6. Test as anonymous, authenticated owner and unauthorized user.
7. Apply to development project first.
8. Record migration IDs and rollback approach.
9. Do not apply production migrations without explicit approval.

## 7. Open decisions

The exact physical schema, JSON shapes, secret-vault implementation, storage bucket policies and RLS SQL must be determined from existing source/migrations and official Supabase capabilities. This document is a logical target, not an instruction to blindly create every proposed table.
