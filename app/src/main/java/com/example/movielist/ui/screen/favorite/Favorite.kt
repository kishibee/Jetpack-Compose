package com.example.movielist.ui.screen.favorite

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.animelist.R
import com.example.movielist.ViewModelFactory
import com.example.movielist.component.ListEmpty
import com.example.movielist.di.Injection
import com.example.movielist.model.Movie
import com.example.movielist.ui.common.UiState
import com.example.movielist.ui.screen.home.ListMovie

@Composable
fun Favorite(
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = viewModel(
        factory = ViewModelFactory(Injection.getRepository())
    )
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { stateUi ->
            when (stateUi) {
                is UiState.Loading -> {
                    viewModel.getFavMovie()
                }
                is UiState.Success -> {
                    FavInfo(
                        listMovie = stateUi.data,
                        navigateToDetail = navigateToDetail,
                        onFavIcon = { newState, id ->
                            viewModel.updateMovie(newState, id)
                        }
                    )
                }
                is UiState.Error -> {}
            }
    }
}

@Composable
fun FavInfo(
    listMovie: List<Movie>,
    navigateToDetail: (Int) -> Unit,
    onFavIcon: (newSate: Boolean, id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
    ) {
        if (listMovie.isNotEmpty()) {
            ListMovie(
                listMovie = listMovie,
                navigateToDetail = navigateToDetail,
                onFavIcon = onFavIcon,
                contentPaddingTop = 16.dp
            )
        } else {
            ListEmpty(
                Warning = stringResource(id = R.string.empty_favorite)
            )
        }
    }
}
