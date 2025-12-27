package com.module.wearvocab.data.api

data class DictionaryResponse(
    val word: String,
    val meanings: List<Meaning>,
    val phonetics: List<Phonetic>
)

data class Meaning(
    val definitions: List<Definition>
)

data class Definition(
    val definition: String,
    val example: String? = null
)

data class Phonetic(
    val audio: String? = null
)
