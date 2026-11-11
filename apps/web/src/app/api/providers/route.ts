import { NextResponse } from "next/server";
import { PROVIDERS } from "@/lib/providers";


export async function GET() {
  return NextResponse.json(
    {
      providers: PROVIDERS,
      total: PROVIDERS.length,
      categories: ["core-model", "inference", "aggregator", "media"],
      note: "Catalog metadata only. A provider is usable only after its adapter and credentials are configured.",
    },
    {
      headers: {
        "Cache-Control": "public, max-age=300, stale-while-revalidate=600",
      },
    },
  );
}
