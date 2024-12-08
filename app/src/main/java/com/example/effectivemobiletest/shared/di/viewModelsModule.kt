@file:Suppress("RemoveExplicitTypeArguments")

package com.example.effectivemobiletest.shared.di

import com.example.effectivemobiletest.data.repository.IContentRepository
import com.example.effectivemobiletest.data.repository.IGoogleRepository
import com.example.effectivemobiletest.presentation.main.fragments.airtickets.AirTicketsViewModel
import com.example.effectivemobiletest.presentation.main.fragments.alltickets.AllTicketsViewModel
import com.example.effectivemobiletest.presentation.main.fragments.country.CountryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { AirTicketsViewModel(contentRepository = get<IContentRepository>()) }
    viewModel { CountryViewModel(contentRepository = get<IContentRepository>()) }
    viewModel { AllTicketsViewModel(googleRepository = get<IGoogleRepository>()) }
}