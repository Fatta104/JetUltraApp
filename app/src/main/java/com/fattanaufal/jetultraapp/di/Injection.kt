package com.fattanaufal.jetultraapp.di

import com.fattanaufal.jetultraapp.data.UltraRepository

object Injection {
    fun provideRepository(): UltraRepository {
        return UltraRepository.getInstance()
    }
}