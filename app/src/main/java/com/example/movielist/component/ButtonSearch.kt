package com.example.movielist.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.animelist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ButtonSearch(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query ,
        onValueChange =onQueryChange,
        leadingIcon = {
            Icon(imageVector = Icons.Rounded.Search,
                 contentDescription = null
            )},
        shape = RoundedCornerShape(40),
        singleLine = true,
        placeholder = {
            Text(stringResource(id = R.string.search_text))
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .shadow(48.dp)
            .padding(10.dp)
    )
}