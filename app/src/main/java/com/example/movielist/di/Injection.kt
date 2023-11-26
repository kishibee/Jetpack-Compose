package com.example.movielist.di

import com.example.movielist.data.Repository

object Injection {
    fun getRepository(): Repository {
        return Repository.getInstance()
    }
}