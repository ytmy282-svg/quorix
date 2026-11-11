export type ProviderCategory = "core-model" | "inference" | "aggregator" | "media";
export type ProviderCapability = "chat" | "reasoning" | "vision" | "embeddings" | "image" | "video" | "audio" | "transcription" | "speech";

export type ProviderDefinition = {
  id: string;
  name: string;
  category: ProviderCategory;
  capabilities: ProviderCapability[];
  connection: "api-key" | "oauth-or-api-key" | "local-or-hosted";
  docsUrl: string;
  enabledByDefault: boolean;
};

// Catalog metadata only. A provider becomes usable after credentials and a working adapter are configured.
export const PROVIDERS: ProviderDefinition[] = [
  { id: "openai", name: "OpenAI", category: "core-model", capabilities: ["chat", "reasoning", "vision", "embeddings", "image", "audio", "speech"], connection: "api-key", docsUrl: "https://platform.openai.com/docs", enabledByDefault: true },
  { id: "anthropic", name: "Anthropic", category: "core-model", capabilities: ["chat", "reasoning", "vision"], connection: "api-key", docsUrl: "https://docs.anthropic.com", enabledByDefault: true },
  { id: "google-gemini", name: "Google Gemini", category: "core-model", capabilities: ["chat", "reasoning", "vision", "embeddings", "image", "audio"], connection: "api-key", docsUrl: "https://ai.google.dev/gemini-api/docs", enabledByDefault: true },
  { id: "deepseek", name: "DeepSeek", category: "core-model", capabilities: ["chat", "reasoning"], connection: "api-key", docsUrl: "https://api-docs.deepseek.com", enabledByDefault: true },
  { id: "xai", name: "xAI / Grok", category: "core-model", capabilities: ["chat", "reasoning", "vision"], connection: "api-key", docsUrl: "https://docs.x.ai", enabledByDefault: true },
  { id: "perplexity", name: "Perplexity", category: "core-model", capabilities: ["chat", "reasoning"], connection: "api-key", docsUrl: "https://docs.perplexity.ai", enabledByDefault: true },
  { id: "mistral", name: "Mistral", category: "core-model", capabilities: ["chat", "reasoning", "vision", "embeddings"], connection: "api-key", docsUrl: "https://docs.mistral.ai", enabledByDefault: true },
  { id: "qwen", name: "Alibaba / Qwen", category: "core-model", capabilities: ["chat", "reasoning", "vision", "embeddings"], connection: "api-key", docsUrl: "https://help.aliyun.com/zh/model-studio", enabledByDefault: true },
  { id: "meta-llama", name: "Meta / Llama", category: "core-model", capabilities: ["chat", "reasoning", "vision"], connection: "local-or-hosted", docsUrl: "https://www.llama.com/docs", enabledByDefault: true },
  { id: "azure-ai", name: "Microsoft / Azure AI", category: "core-model", capabilities: ["chat", "reasoning", "vision", "embeddings"], connection: "api-key", docsUrl: "https://learn.microsoft.com/azure/ai-services", enabledByDefault: true },
  { id: "cohere", name: "Cohere", category: "core-model", capabilities: ["chat", "embeddings"], connection: "api-key", docsUrl: "https://docs.cohere.com", enabledByDefault: true },
  { id: "amazon-bedrock", name: "Amazon Bedrock", category: "core-model", capabilities: ["chat", "reasoning", "vision", "embeddings"], connection: "api-key", docsUrl: "https://docs.aws.amazon.com/bedrock", enabledByDefault: true },
  { id: "groq", name: "Groq", category: "inference", capabilities: ["chat", "reasoning", "vision"], connection: "api-key", docsUrl: "https://console.groq.com/docs", enabledByDefault: true },
  { id: "cerebras", name: "Cerebras", category: "inference", capabilities: ["chat", "reasoning"], connection: "api-key", docsUrl: "https://inference-docs.cerebras.ai", enabledByDefault: true },
  { id: "together", name: "Together AI", category: "inference", capabilities: ["chat", "reasoning", "vision", "embeddings", "image"], connection: "api-key", docsUrl: "https://docs.together.ai", enabledByDefault: true },
  { id: "fireworks", name: "Fireworks AI", category: "inference", capabilities: ["chat", "reasoning", "vision", "embeddings", "image", "audio"], connection: "api-key", docsUrl: "https://docs.fireworks.ai", enabledByDefault: true },
  { id: "nvidia-nim", name: "NVIDIA NIM", category: "inference", capabilities: ["chat", "reasoning", "vision", "embeddings"], connection: "api-key", docsUrl: "https://docs.nvidia.com/nim", enabledByDefault: true },
  { id: "hugging-face", name: "Hugging Face", category: "inference", capabilities: ["chat", "reasoning", "vision", "embeddings", "image", "audio"], connection: "api-key", docsUrl: "https://huggingface.co/docs", enabledByDefault: true },
  { id: "openrouter", name: "OpenRouter", category: "aggregator", capabilities: ["chat", "reasoning", "vision"], connection: "api-key", docsUrl: "https://openrouter.ai/docs", enabledByDefault: true },
  { id: "moonshot", name: "Moonshot / Kimi", category: "core-model", capabilities: ["chat", "reasoning", "vision"], connection: "api-key", docsUrl: "https://platform.moonshot.ai/docs", enabledByDefault: true },
  { id: "elevenlabs", name: "ElevenLabs", category: "media", capabilities: ["audio", "speech", "transcription"], connection: "api-key", docsUrl: "https://elevenlabs.io/docs", enabledByDefault: true },
  { id: "stability", name: "Stability AI", category: "media", capabilities: ["image", "video"], connection: "api-key", docsUrl: "https://platform.stability.ai/docs", enabledByDefault: true },
  { id: "black-forest-labs", name: "Black Forest Labs / FLUX", category: "media", capabilities: ["image"], connection: "api-key", docsUrl: "https://docs.bfl.ai", enabledByDefault: true },
  { id: "runway", name: "Runway", category: "media", capabilities: ["video", "image"], connection: "api-key", docsUrl: "https://docs.dev.runwayml.com", enabledByDefault: true },
  { id: "fal", name: "fal.ai", category: "media", capabilities: ["image", "video", "audio"], connection: "api-key", docsUrl: "https://fal.ai/docs", enabledByDefault: true },
  { id: "deepgram", name: "Deepgram", category: "media", capabilities: ["audio", "transcription", "speech"], connection: "api-key", docsUrl: "https://developers.deepgram.com", enabledByDefault: true },
];

export function getProvidersByCapability(capability: ProviderCapability) {
  return PROVIDERS.filter((provider) => provider.capabilities.includes(capability));
}
