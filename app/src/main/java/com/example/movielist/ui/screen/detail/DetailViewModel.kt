package com.example.movielist.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.Repository
import com.example.movielist.model.Movie
import com.example.movielist.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<Movie>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Movie>>
        get() = _uiState

    fun getMovie(id: Int) =
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getMovie(id))
        }

    fun updateMovie(newState: Boolean, id: Int) =
        viewModelScope.launch {
            repository.updateMovie(!newState, id)
                .collect { isUpdate ->
                    if (isUpdate) getMovie(id)
                }
        }
}