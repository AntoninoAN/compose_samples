package com.cvscodechallenge.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.cvscodechallenge.R
import com.cvscodechallenge.model.Item
import com.cvscodechallenge.model.Media
import com.cvscodechallenge.utils.Navigation
import com.cvscodechallenge.utils.UIState
import com.cvscodechallenge.viewmodel.SearchViewModel

private const val TAG = "SearchScreen"

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun SearchScreen(viewModel: SearchViewModel, navController: NavController) {
    val state = viewModel.searchLiveData.collectAsState().value

    Scaffold(topBar = {
        TopAppBar(title = {
            SearchScreenEditText(viewModel)
        })
    }) {
        SearchScreenContent(state, navController)
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
private fun SearchScreenContent(
    state: UIState,
    navController: NavController
) {
    when (state) {
        is UIState.SUCCESS -> {
            Log.d(TAG, "SearchScreen Success: ${state.response}")
            SearchResultList(response = state.response) {
                navController.navigate("details/$it")
            }
            SearchResultLoading(isLoading = false)
        }
        is UIState.ERROR -> {
            Log.d(TAG, "SearchScreen Error: ${state.errorMessage}")
            Card { Text(text = state.errorMessage) }
        }
        is UIState.LOADING -> {
            Log.d(TAG, "SearchScreen Loading: ${state.isLoading}")
            SearchResultLoading(isLoading = state.isLoading)
        }
        is UIState.EMPTY -> {}
        is UIState.PREVIOUS -> {
            Log.d(TAG, "SearchScreenContent Previous: ${state.previousSearch}")
            SearchResultPrevious(state.previousSearch)
        }
    }
}

@Composable
fun SearchResultPrevious(previousSearch: List<String>) {
    LazyColumn{
        items(count = previousSearch.size){
            Box {
               Text(text = previousSearch[it])
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
private fun SearchScreenEditText(
    viewModel: SearchViewModel
) {
    val textSearchInput = remember { mutableStateOf("") }
    val sk = LocalSoftwareKeyboardController.current
    BasicTextField(value = textSearchInput.value,
        onValueChange = { textSearchInput.value = it },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions {
            Log.d(TAG, "SearchTopBar: ")
            viewModel.getSearch(textSearchInput.value)
            sk?.hide()
        },
        decorationBox = { innerTextField ->
            if (textSearchInput.value.isEmpty())
                Text(
                    text = "Input your search...",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            innerTextField()
        }
    )
}

@ExperimentalFoundationApi
@Composable
fun SearchResultList(response: List<Item>, openDetails: (Int) -> Unit) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        state = rememberLazyListState(),
        modifier = Modifier
            .padding(4.dp)
    ) {
        itemsIndexed(items = response) { index, item: Item ->
            SearchResultListContent(item = item) {
                openDetails(index)
            }
        }
    }
}

@Composable
fun SearchResultListContent(item: Item, openDetails: () -> Unit) {
    Log.d(TAG, "SearchResultListContent: $item")
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 15.dp,
        modifier = Modifier
            .padding(4.dp)
            .then(Modifier.fillMaxWidth())
    ) {
        Column(
            modifier = Modifier.clickable { openDetails() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberImagePainter(data = item.media.m,
                    builder = { size(OriginalSize) }),
                contentDescription = stringResource(id = R.string.content_description),
                modifier = Modifier.defaultMinSize(200.dp)
            )
            Text(
                text = item.author,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun SearchResultLoading(isLoading: Boolean) {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Preview
@Composable
fun PreviewSearchListContent() {
    SearchResultListContent(
        item = Item(
            "ddd",
            "",
            Media("https://live.staticflickr.com/65535/51909200511_4559dc4811_m.jpg"),
            "",
            "",
            "",
            "adada",
            ""
        )
    ) {

    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun PreviewSearchList() {
    val dataSet = listOf(
        Item(
            "ddd",
            "",
            Media("https://live.staticflickr.com/65535/51909200511_4559dc4811_m.jpg"),
            "",
            "",
            "",
            "adada",
            ""
        ),
        Item(
            "ddd",
            "",
            Media("https://live.staticflickr.com/65535/51909200511_4559dc4811_m.jpg"),
            "",
            "",
            "",
            "adada",
            ""
        ),
        Item(
            "ddd",
            "",
            Media("https://live.staticflickr.com/65535/51909200511_4559dc4811_m.jpg"),
            "",
            "",
            "",
            "adada",
            ""
        )
    )
    SearchResultList(response = dataSet) {

    }
}

@Preview
@Composable
fun PreviewSearchPrevious(){
    SearchResultPrevious(previousSearch = listOf("dogs", "cats"))
}