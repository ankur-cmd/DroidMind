package com.droidmind.feature.chat.data.remote

/**
 * Centralized constants for external API endpoints used by the chat
 * data layer.
 *
 * Keeping base URLs here (rather than scattered across Hilt modules or
 * Retrofit service files) makes it easy to see, at a glance, every
 * external service this module talks to — and to update a URL in exactly
 * one place if it ever changes.
 */
object ApiConstants {

    /** Base URL for Google's Gemini API. */
    const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/"

    // const val OPENAI_BASE_URL = "https://api.openai.com/"
    // const val CLAUDE_BASE_URL = "https://api.anthropic.com/"
}