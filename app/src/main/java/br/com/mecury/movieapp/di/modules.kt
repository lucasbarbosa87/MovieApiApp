package br.com.mecury.movieapp.di

import androidx.room.Room
import br.com.mecury.movieapp.data.local.AppDatabase
import br.com.mecury.movieapp.data.local.MIGRATION_1_2
import br.com.mecury.movieapp.repository.MoviesRepository
import br.com.mecury.movieapp.repository.MoviesRepositoryImpl
import br.com.mecury.movieapp.ui.detail.DetailViewModel
import br.com.mecury.movieapp.ui.list.ListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var appModules = module {

    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "movieList")
            .addMigrations(MIGRATION_1_2).build()
    }

    single { get<AppDatabase>().movieDao() }
    single { get<AppDatabase>().genreDao() }
    factory<MoviesRepository> { MoviesRepositoryImpl(get(), get()) }
}

var viewModels = module {
    viewModel { ListViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}