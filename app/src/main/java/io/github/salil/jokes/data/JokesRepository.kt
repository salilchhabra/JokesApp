package io.github.salil.jokes.data

import io.github.salil.jokes.data.model.JokeResponse

interface JokesRepository {
    suspend fun fetchRandomJokes(): JokeResponse
}