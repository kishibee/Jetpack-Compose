package com.example.movielist.ui.screen.home

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.animelist.R
import com.example.movielist.ViewModelFactory
import com.example.movielist.component.ButtonSearch
import com.example.movielist.component.ItemMovie
import com.example.movielist.component.ListEmpty
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
) {
    Column {
        ButtonSearch(
        query = query,
        onQueryChange = onQueryChange
        )
        if (listMovie.isNotEmpty()) {
            ListMovie(
                listMovie = listMovie,
                navigateToDetail = navigateToDetail,
                onFavoriteIcon = onFavoriteIcon
            )
        } else {
            ListEmpty(
                Warning = stringResource(R.string.empty_data))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListMovie(
    listMovie: List<Movie>,
    onFavoriteIcon: (newState: Boolean, id: Int) -> Unit,
    navigateToDetail: (Int) -> Unit,
    contentPaddingTop: Dp = 0.dp
) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = 16.dp,
            bottom = 16.dp,
            end = 16.dp,
            top = contentPaddingTop
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(listMovie, key = { it.id }) { item ->
            ItemMovie(
                id = item.id,
                name = item.name,
                imageUrl = item.imageUrl,
                rate = item.rate,
                isFavorite = item.isFavorite,
                onFavoriteIcon = onFavoriteIcon,
                modifier = Modifier
                    .animateItemPlacement(tween(durationMillis = 200))
                    .clickable { navigateToDetail(item.id) }
            )
        }
    }
}