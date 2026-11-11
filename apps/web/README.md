# Quorix Web

The web workspace is one client of the shared Quorix ecosystem. Android and Desktop clients will use the same backend contracts and account data.

## Local development

```bash
npm install
npm run dev
```

Open http://localhost:3000.

## Provider catalog

`GET /api/providers` returns the expandable provider catalog and capability metadata. This endpoint does not imply that provider credentials or model adapters are connected.

## Environment

Copy `.env.example` to `.env.local` for local development. Never commit real credentials.

## Current status

The dashboard is an early UI preview. The provider catalog and initial database migration are being added. Authentication, encrypted credential storage, provider adapters, streaming chat, BOSS orchestration, native Android operations and desktop packaging remain separate implementation milestones.
