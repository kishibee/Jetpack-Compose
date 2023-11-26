package com.example.movielist.ui.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.data.Repository
import com.example.movielist.model.Movie
import com.example.movielist.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    private val _uiState: MutableStateFlow<UiState<List<Movie>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<Movie>>>
        get() = _uiState

    fun updateMovie (newState: Boolean, id: Int) =
        viewModelScope.launch {
            repository.updateMovie(newState, id)
                .collect {
                    if (it) search(_query.value)
                }
        }

    fun search(newQuery: String) =
        viewModelScope.launch {
            repository.searchMovie(_query.value)
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
}