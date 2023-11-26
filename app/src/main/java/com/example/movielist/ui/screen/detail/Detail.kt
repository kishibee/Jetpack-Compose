package com.example.movielist.ui.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.animelist.R
import com.example.movielist.ViewModelFactory
import com.example.movielist.di.Injection
import com.example.movielist.ui.common.UiState

@Composable
fun detail(
    movieId: Int,
    backNavigation: () -> Unit,
    viewModel: DetailViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.getRepository()
        )
    )
) {
  viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { stateUi ->
      when (stateUi) {
          is UiState.Loading -> {
              viewModel.getMovie(movieId)
          }
          is UiState.Success -> {
              val data = stateUi.data
              DetailInfo(
                  imageUrl = data.imageUrl,
                  name = data.name,
                  id = data.id,
                  overview = data.overview,
                  rate = data.rate,
                  release = data.releaseDate,
                  isFavorite = data.isFavorite,
                  backNavigation = backNavigation,
                  onFavButton = { state, id ->
                      viewModel.updateMovie(state, id)
                  }
              )
          }
          is UiState.Error -> {}
      }
  }
}

@Composable
fun DetailInfo(
    imageUrl: String,
    name: String,
    id: Int,
    rate: Double,
    overview: String,
    isFavorite: Boolean,
    release: String,
    backNavigation: () -> Unit,
    onFavButton: (state: Boolean, id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column (
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Column (
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                    )
                    Text(
                        text = rate.toString(),
                        modifier = Modifier
                            .padding(start = 4.dp, end = 10.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                    )
                    Text(
                        text = release,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier
                            .padding(start = 6.dp)
                    )
                }
            }
            Divider(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                )
            )
            Text(
                text = overview,
                fontSize = 20.sp,
                lineHeight = 34.sp,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }
        IconButton(
            onClick = backNavigation,
            modifier = Modifier
                .align(Alignment.TopStart)
                .clip(CircleShape)
                .background(Color.Black)
                .padding(start = 14.dp, top = 7.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back)
            )
        }
        IconButton(
            onClick = {
                onFavButton(isFavorite, id)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(44.dp)
                .padding(end = 14.dp, top = 7.dp)
                .background(Color.Black)
                .clip(CircleShape)
        ) {
            Icon(
                imageVector =
                if (!isFavorite) Icons.Default.FavoriteBorder
                else Icons.Default.Favorite,
                contentDescription =
                if (!isFavorite) stringResource(R.string.add_fav)
                else stringResource(R.string.remove_fav),
                tint = if (!isFavorite) Color.Black
                else Color.Red
            )
        }
    }

}