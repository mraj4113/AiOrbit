package com.aiorbit.companies.config;

import com.aiorbit.companies.domain.Company;
import com.aiorbit.companies.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CompanyRepository companyRepository;

    @Override
    public void run(String... args) {
        if (companyRepository.count() > 0) {
            return;
        }

        companyRepository.saveAll(java.util.List.of(
            Company.builder().name("OpenAI").slug("openai")
                .description("AI research and deployment company behind the GPT family of large language models, focused on building safe and broadly beneficial artificial intelligence.")
                .category("Large Language Models").headquarters("San Francisco, USA")
                .foundedYear(2015).employees("501-1000").website("https://openai.com")
                .logoUrl("https://logo.clearbit.com/openai.com")
                .rating(4.8).reviewCount(1240).verified(true).featured(true).build(),

            Company.builder().name("Anthropic").slug("anthropic")
                .description("AI safety company building reliable, interpretable, and steerable AI systems, creators of the Claude family of assistants.")
                .category("Large Language Models").headquarters("San Francisco, USA")
                .foundedYear(2021).employees("201-500").website("https://anthropic.com")
                .logoUrl("https://logo.clearbit.com/anthropic.com")
                .rating(4.7).reviewCount(860).verified(true).featured(true).build(),

            Company.builder().name("Hugging Face").slug("hugging-face")
                .description("Open-source platform and model hub for machine learning, hosting transformers, datasets, and demos for the global AI community.")
                .category("ML Infrastructure").headquarters("New York, USA")
                .foundedYear(2016).employees("201-500").website("https://huggingface.co")
                .logoUrl("https://logo.clearbit.com/huggingface.co")
                .rating(4.6).reviewCount(1530).verified(true).featured(true).build(),

            Company.builder().name("Stability AI").slug("stability-ai")
                .description("Open generative AI company building foundation models for image, audio, video, and language, including the Stable Diffusion family.")
                .category("Generative Media").headquarters("London, UK")
                .foundedYear(2020).employees("101-200").website("https://stability.ai")
                .logoUrl("https://logo.clearbit.com/stability.ai")
                .rating(4.2).reviewCount(540).verified(true).featured(false).build(),

            Company.builder().name("Mistral AI").slug("mistral-ai")
                .description("European AI lab developing high-performance, open-weight large language models with a focus on efficiency and portability.")
                .category("Large Language Models").headquarters("Paris, France")
                .foundedYear(2023).employees("51-200").website("https://mistral.ai")
                .logoUrl("https://logo.clearbit.com/mistral.ai")
                .rating(4.5).reviewCount(410).verified(true).featured(true).build(),

            Company.builder().name("Cohere").slug("cohere")
                .description("Enterprise AI platform specializing in language models and retrieval-augmented generation for business use cases.")
                .category("Large Language Models").headquarters("Toronto, Canada")
                .foundedYear(2019).employees("201-500").website("https://cohere.com")
                .logoUrl("https://logo.clearbit.com/cohere.com")
                .rating(4.4).reviewCount(380).verified(true).featured(false).build(),

            Company.builder().name("Runway").slug("runway")
                .description("Applied research company building creative tools for video generation and editing powered by generative AI models.")
                .category("Generative Media").headquarters("New York, USA")
                .foundedYear(2018).employees("101-200").website("https://runwayml.com")
                .logoUrl("https://logo.clearbit.com/runwayml.com")
                .rating(4.3).reviewCount(620).verified(true).featured(false).build(),

            Company.builder().name("Perplexity AI").slug("perplexity-ai")
                .description("Conversational answer engine that combines live web search with LLMs to deliver cited, up-to-date responses.")
                .category("Search & Assistant").headquarters("San Francisco, USA")
                .foundedYear(2022).employees("51-200").website("https://perplexity.ai")
                .logoUrl("https://logo.clearbit.com/perplexity.ai")
                .rating(4.5).reviewCount(720).verified(true).featured(true).build(),

            Company.builder().name("Scale AI").slug("scale-ai")
                .description("Data infrastructure platform providing high-quality training data, annotation, and evaluation for frontier AI models.")
                .category("ML Infrastructure").headquarters("San Francisco, USA")
                .foundedYear(2016).employees("501-1000").website("https://scale.com")
                .logoUrl("https://logo.clearbit.com/scale.com")
                .rating(4.1).reviewCount(290).verified(true).featured(false).build(),

            Company.builder().name("Reka").slug("reka")
                .description("Research lab building multimodal foundation models that reason across text, images, and audio.")
                .category("Multimodal AI").headquarters("Singapore")
                .foundedYear(2022).employees("11-50").website("https://reka.ai")
                .logoUrl("https://logo.clearbit.com/reka.ai")
                .rating(3.9).reviewCount(95).verified(false).featured(false).build()
        ));
    }
}