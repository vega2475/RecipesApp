    package edu.vega.recipesapp.utils

    import androidx.core.text.HtmlCompat

    fun String.stripHtml(): String {
        return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    }