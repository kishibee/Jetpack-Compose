package com.example.movielist.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movielist.ViewModelFactory
import com.example.movielist.di.Injection
import com.example.movielist.model.Movie
import com.example.movielist.ui.common.UiState

@Composable
fun Home(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.getRepository())),
    navigateToDetail: (Int) -> Unit
) {
    val query by viewModel.query
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { stateUi ->
        when (stateUi) {
            is UiState.Loading -> {
                viewModel.search(query)
            }
            is UiState.Success -> {
                Content(
                    query = query,
                    onQueryChange = viewModel::search,
                    listMovie = stateUi.data,
                    onFavoriteIcon = { newState, id ->
                        viewModel.updateMovie(newState, id)
                    },
                    navigateToDetail = navigateToDetail
                )
            }
            else -> {}
        }
    }
}

@Composable
fun Content(
    query: String,
    onQueryChange: (String) -> Unit,
    listMovie: List<Movie>,
    onFavoriteIcon: (newState: Boolean, id: Int) -> Unit,
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        SearchButton(
        query = query,
        onQueryChange = onQueryChange
        )
    }
}