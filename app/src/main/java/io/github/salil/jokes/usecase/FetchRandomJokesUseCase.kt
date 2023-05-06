package io.github.salil.jokes.usecase

import io.github.salil.jokes.domain.JokesDataSource

class FetchRandomJokesUseCase(private val dataSource: JokesDataSource) {
    suspend fun fetchRandomJokes(): String? {
        return dataSource.fetchRandomJokes()
    }
}