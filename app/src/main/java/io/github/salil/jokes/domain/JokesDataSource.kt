package io.github.salil.jokes.domain

interface JokesDataSource {
    suspend fun fetchRandomJokes(): String?
}