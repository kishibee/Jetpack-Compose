package com.example.movielist.data

import com.example.movielist.model.Movie
import com.example.movielist.model.MovieData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class Repository {
    private val dummyMovie = mutableListOf<Movie>()

    init {
        if (dummyMovie.isEmpty()) {
            MovieData.dummyMovie.forEach {
                dummyMovie.add(it)
            }
        }
    }

    fun getMovie(movieId: Int): Movie {
        return dummyMovie.first {
            it.id == movieId
        }
    }

    fun searchMovie(query: String) = flow {
        val data = dummyMovie.filter {
            it.name.contains(query, ignoreCase = true)
        }
        emit(data)
    }

    fun updateMovie(newState: Boolean, movieId: Int): Flow<Boolean> {
        val index = dummyMovie.indexOfFirst {
            it.id == movieId
        }
        val result = if (index >= 0) {
            val movie = dummyMovie[index]
            dummyMovie[index] = movie.copy(isFavorite = newState)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    fun getFavMovie(): Flow<List<Movie>> {
        return flowOf(dummyMovie.filter {
            it.isFavorite
        })
    }

    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(): Repository =
            instance ?: synchronized(this) {
                Repository().apply {
                    instance = this
                }
            }
    }
}