package io.github.salil.jokes.domain.entity

data class Joke(
    val category: String,
    val delivery: String,
    val id: String,
    val setup: String
)