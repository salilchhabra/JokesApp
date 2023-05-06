package io.github.salil.jokes.data

import io.github.salil.jokes.data.model.JokeResponse
import io.github.salil.jokes.domain.JokesDataSource

class JokesDataSourceImpl(private val repository: JokesRepository) : JokesDataSource {
    override suspend fun fetchRandomJokes(): String? {
        val response: JokeResponse = repository.fetchRandomJokes()

        return response.joke
    }
}