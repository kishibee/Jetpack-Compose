package com.example.animelist.di

import com.example.animelist.Repository

object Injection {
    fun getRepository(): Repository {
        return Repository.getInstance()
    }
}