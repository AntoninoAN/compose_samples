package com.example.tmobiletest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.tmobiletest.model.*
import com.example.tmobiletest.model.remote.Network
import com.example.tmobiletest.repository.Repository
import com.example.tmobiletest.repository.RepositoryImpl
import com.example.tmobiletest.repository.UIState
import com.example.tmobiletest.ui.theme.TmobileTestTheme
import com.example.tmobiletest.viewmodel.CardViewModel

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: CardViewModel

    private val repository: Repository by lazy {
        RepositoryImpl(Network.getService())
    }

    private val viewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CardViewModel(repository = repository) as T
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalUnitApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TmobileTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    viewModel = viewModel(this, factory = viewModelFactory)
                    CardState(cardViewModel = viewModel)
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalUnitApi
@Composable
fun CardState(cardViewModel: CardViewModel) {
    val uiState: UIState? by cardViewModel.cards.observeAsState()
    uiState?.let {
        Log.d(TAG, "CardState: ${uiState}")
        when (it) {
            is UIState.Response -> ListCards(cardState = it.data.page.cards)
            is UIState.Error -> CardError()
            is UIState.Loading -> CardLoading(isLoading = it.isLoading)
        }
    }
}

@ExperimentalUnitApi
@Composable
fun ListCards(cardState: List<CardsResponse>) {
    LazyColumn(
        state = rememberLazyListState(),
        modifier =Modifier.padding(4.dp)
    ) {

        items(
            count = cardState.size
        ) { index ->
            when (cardState[index].card_type) {
                CardTypes.TEXT.value -> {
                    ViewCardTitle(
                        cardTitle = cardState[index].card.value!!,
                        cardAttribute = cardState[index].card.attributes!!
                    )
                }
                CardTypes.TITLE_DESCRIPTION.value -> {
                    ViewCardTitleDescription(
                        cardTitle = cardState[index].card.title!!,
                        cardDescription = cardState[index].card.description!!
                    )
                }
                CardTypes.IMAGE_TITLE.value -> {
                    Log.d(TAG, "ListCards: ${cardState[index].card}")
                    CardTitleDescriptionImage(
                        cardImage = cardState[index].card.image!!,
                        cardTitle = cardState[index].card.title!!,
                        cardDescription = cardState[index].card.description!!
                    )
                }
            }
        }
    }
}

@ExperimentalUnitApi
@Composable
fun ViewCardTitle(cardTitle: String, cardAttribute: CardAttribute) {
    val color: Int = android.graphics.Color.parseColor(cardAttribute?.text_color)
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 15.dp
    ) {
        Text(
            text = cardTitle,
            color = Color(color),
            fontSize = cardAttribute?.font?.size?.sp ?: 12.sp
        )
    }
}

@Composable
fun ViewCardTitleDescription(cardTitle: CardTitle, cardDescription: CardDescription) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = cardTitle.value,
            color = Color(android.graphics.Color.parseColor(cardTitle.attributes?.text_color)),
            fontSize = cardTitle.attributes?.font?.size?.sp ?: 12.sp,
        )
        Text(
            text = cardDescription.value,
            color = Color(android.graphics.Color.parseColor(cardDescription.attributes?.text_color)),
            fontSize = cardDescription.attributes?.font?.size?.sp ?: 12.sp
        )
    }
}

@Composable
fun CardTitleDescriptionImage(
    cardImage: CardImage,
    cardTitle: CardTitle,
    cardDescription: CardDescription
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 15.dp
    ) {
        Image(
            painter = rememberImagePainter(data = cardImage.url,
                builder = {
                    crossfade(true)
                }),
            contentDescription = stringResource(id = R.string.image_description),
            Modifier.size(cardImage.size.width.dp)//, height = cardImage.size.height.dp)
        )
        ViewCardTitleDescription(cardTitle = cardTitle, cardDescription = cardDescription)
    }
}

@Composable
fun CardError() {
    Card(
        shape = RoundedCornerShape(8.dp), elevation = 15.dp,
        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
    ) {
        Text(text = "Random Error")
    }
}

@ExperimentalAnimationApi
@Composable
fun CardLoading(isLoading: Boolean) {
    AnimatedVisibility(visible = isLoading) {
        CircularProgressIndicator()
    }
}

// region

@ExperimentalUnitApi
@Preview
@Composable
fun DefaultCardTitle() {
    ViewCardTitle(
        cardTitle =
        "something",
        cardAttribute = CardAttribute("#ffffff", CardFont(20))
    )
}

@Preview
@Composable
fun DefaultCardTitleDescription() {
    ViewCardTitleDescription(
        cardTitle = CardTitle(
            "something",
            CardAttribute("#ffffff", CardFont(20))
        ),
        cardDescription = CardDescription(
            stringResource(id = R.string.test_large_text),
            CardAttribute("#cacaca", CardFont(15))
        )
    )
}

@Preview
@Composable
fun DefaultCardTitleDescriptionImage() {
    CardTitleDescriptionImage(
        CardImage(
            "https://qaevolution.blob.core.windows.net/assets/ios/3x/Featured@4.76x.png",
            CardSize(1170, 1498)
        ),
        CardTitle(
            "This is the title of the moview",
            CardAttribute("#ffffff", CardFont(20))
        ),
        CardDescription(
            stringResource(id = R.string.test_large_text),
            CardAttribute("#cacaca", CardFont(15))
        )
    )
}

@Preview
@Composable
fun DefaultTestImage() {
    Image(
        painter = rememberImagePainter(data = "https://qaevolution.blob.core.windows.net/assets/ios/3x/Featured@4.76x.png",
            builder = {
                crossfade(true)
            }),
        contentDescription = stringResource(id = R.string.image_description),
        Modifier.size(width = 1170.dp, height = 1498.dp)
    )
}

@ExperimentalUnitApi
@Preview
@Composable
fun DefaultListCards() {
    ListCards(
        PageResponse(
            PageCards(
                listOf(
                    CardsResponse(
                        "text", Card(
                            value = "Hello, Welcome to App!",
                            attributes = CardAttribute("#262626", CardFont(30)), null, null, null
                        )
                    ),
                    CardsResponse(
                        "title_description", Card(
                            null,
                            null,
                            title = CardTitle(
                                "Check out our App every week for exciting offers.",
                                CardAttribute("#262626", CardFont(24))
                            ),
                            description = CardDescription(
                                "Offers available every week!",
                                CardAttribute("#262626", CardFont(18))
                            ),
                            null
                        )
                    ),
                    CardsResponse(
                        "image_title_description", Card(
                            null,
                            null,
                            title = CardTitle(
                                "Movie ticket to Dark Phoenix!",
                                CardAttribute("#FFFFFF", CardFont(18))
                            ),
                            description = CardDescription(
                                "Tap to see offer dates and descriptions.",
                                CardAttribute("#FFFFFF", CardFont(12))
                            ),
                            image = CardImage(
                                "https://qaevolution.blob.core.windows.net/assets/ios/3x/Featured@4.76x.png",
                                CardSize(1170, 1498)
                            )
                        )
                    )
                )
            )
        ).page.cards
    )
}

@ExperimentalAnimationApi
@Preview(showSystemUi = true, heightDp = 30, widthDp = 30)
@Composable
fun DefaultLoading() {
    CardLoading(isLoading = true)
}
// endregion