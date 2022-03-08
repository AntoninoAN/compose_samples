package com.cvscodechallenge.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.cvscodechallenge.R
import com.cvscodechallenge.model.Item
import com.cvscodechallenge.model.Media
import com.cvscodechallenge.utils.UIState
import com.cvscodechallenge.utils.height
import com.cvscodechallenge.utils.width
import com.cvscodechallenge.viewmodel.SearchViewModel

@ExperimentalAnimationApi
@Composable
fun DetailScreen(viewModel: SearchViewModel, position: Int) {
    val state = viewModel.itemDetailsLiveData.collectAsState().value
    viewModel.getDetailsItem(position = position)

    Scaffold(topBar = { TopAppBar { Text(text = "Details") } }) {
        DetailScreenContent(state)
    }


}

@ExperimentalAnimationApi
@Composable
fun DetailScreenContent(response: UIState) {
    when (response) {
        is UIState.SUCCESS -> DetailScreenContent(itemDetail = response.response[0])
        is UIState.ERROR -> {
            Card { Text(text = response.errorMessage) }
        }
        is UIState.LOADING -> {
            DetailScreenLoading(response.isLoading)
        }
    }
}

@Composable
fun DetailScreenContent(itemDetail: Item) {
    Box(contentAlignment = Alignment.Center) {
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = 15.dp,
            modifier = Modifier
                .padding(4.dp)
//                .then(Modifier.fillMaxWidth())
//                .then(Modifier.align(Alignment.Center))
        ) {
            Column(modifier = Modifier.padding(4.dp)) {
                Image(
                    painter = rememberImagePainter(itemDetail.media.m),
                    contentDescription = stringResource(id = R.string.content_description),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(id = R.string.title, itemDetail.title),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(id = R.string.description, itemDetail.title),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(
                        id = R.string.image_width,
                        itemDetail.description.width()
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(
                        id = R.string.image_height,
                        itemDetail.description.height()
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(id = R.string.author, itemDetail.author),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun DetailScreenLoading(isLoading: Boolean) {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Preview
@Composable
fun DetailScreenContentPrev() {
    DetailScreenContent(
        itemDetail = Item(
            "ddd",
            "",
            Media("https://live.staticflickr.com/65535/51909200511_4559dc4811_m.jpg"),
            "",
            "<p><a href=\"https://www.flickr.com/people/coyoty/\">Coyoty</a> posted a photo:</p> <p><a href=\"https://www.flickr.com/photos/coyoty/51906915093/\" title=\"Porcupine Puffer Fish\"><img src=\"https://live.staticflickr.com/65535/51906915093_563114363b_m.jpg\" width=\"240\" height=\"105\" alt=\"Porcupine Puffer Fish\" /></a></p> <p><i>Diodon holocanthus</i>, also known as a globefish or balloonfish. In the PPG Aquarium at the Pittsburgh Zoo in Pittsburgh, PA.</p>",
            "",
            "adada",
            ""
        )
    )
}

// recent search if no previous ("You have no recent searches. Enter a search term above.")
// 1.-
// 2.-
//The recent searches list should have a max of 5 items, 6th different search will remove "older"...
// use a set to store previous search pattern, when set.size == 5...