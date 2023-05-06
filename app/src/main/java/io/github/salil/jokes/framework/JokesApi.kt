package io.github.salil.jokes.framework

import io.github.salil.jokes.data.model.JokeResponse
import retrofit2.Response
import retrofit2.http.GET

interface JokesApi {
    @GET("api?format=json")
    suspend fun fetchRandomJokes(): Response<JokeResponse>
}