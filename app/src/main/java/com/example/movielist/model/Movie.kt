package com.example.movielist.model

data class Movie (
    val id: Int,
    val imageUrl: String,
    val name: String,
    val rate: Double,
    val overview: String,
    val releaseDate: String,
    val isFavorite: Boolean = false
)