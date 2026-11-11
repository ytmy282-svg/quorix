import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Quorix — Your AI Workspace",
  description: "One workspace for AI chat, agent collaboration, building, research, and media.",
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return <html lang="en"><body>{children}</body></html>;
}
