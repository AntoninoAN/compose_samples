package com.example.tmobiletest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TmobileTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                    viewModel = viewModel(this, factory = viewModelFactory)
                }
            }
        }
    }
}

@Composable
fun CardState(cardViewModel: CardViewModel) {
    val uiState: UIState? by cardViewModel.cards.observeAsState()

}

@Composable
fun SomeState(): LazyListState {
    return LazyListState(0, 0)
}

@ExperimentalUnitApi
@Composable
fun ListCards(cardState: PageResponse) {
    val ss = SomeState()
    LazyColumn {
        items(
            count = cardState.cards.size,
            key = null
        ) { index ->
            when (cardState.cards[index].card_type) {
                CardTypes.TEXT.name -> {
                    ViewCardTitle(cardTitle = cardState.cards[index].card.title!!)
                }
                CardTypes.IMAGE_TITLE.name -> {
                    ViewCardTitleDescription(
                        cardTitle = cardState.cards[index].card.title!!,
                        cardDescription = cardState.cards[index].card.description!!
                    )
                }
                CardTypes.TITLE_DESCRIPTION.name -> {
                    CardTitleDescriptionImage(
                        cardImage = cardState.cards[index].card.image!!,
                        cardTitle = cardState.cards[index].card.title!!,
                        cardDescription = cardState.cards[index].card.description!!
                    )
                }
            }
        }
    }
}

@ExperimentalUnitApi
@Composable
fun ViewCardTitle(cardTitle: CardTitle) {
    val color: Int = android.graphics.Color.parseColor(cardTitle.attributes?.text_color)
    Box {
        Text(
            text = cardTitle.value,
            color = Color(color),
            fontSize = cardTitle.attributes?.font?.size?.sp ?: 12.sp
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
    //Drawable.createFromStream()
    Box(contentAlignment = Alignment.Center) {
        Image(
            painter = rememberImagePainter(data = cardImage.url,
                builder = {
                    crossfade(true)
                }),
            contentDescription = stringResource(id = R.string.image_description),
            Modifier.size(width = cardImage.size.width.dp, height = cardImage.size.height.dp)
        )
        ViewCardTitleDescription(cardTitle = cardTitle, cardDescription = cardDescription)
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

// region
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TmobileTestTheme {
        Greeting("Android")
    }
}

@ExperimentalUnitApi
@Preview
@Composable
fun DefaultCardTitle() {
    ViewCardTitle(
        cardTitle = CardTitle(
            "something",
            CardAttribute("#ffffff", CardFont(20))
        )
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
            "something",
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

@Composable
fun HelloContent(name: String, onNameChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        if (name.isNotEmpty()) {
            Text(
                text = "Hello, $name!",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.h5
            )
        }
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name") }
        )
    }
}

@Composable
fun HelloScreen() {
    var name by remember { mutableStateOf("Tony") }
    HelloContent(name) {
        name = it
    }
}


@Preview
@Composable
fun Testing() {
    HelloScreen()
}

@ExperimentalUnitApi
@Preview
@Composable
fun DefaultListCards() {
    ListCards(
        PageResponse(
            listOf(
                CardsResponse(
                    "text", Card(
                        "Hello, Welcome to App!",
                        CardAttribute("#262626", CardFont(30)), null, null, null
                    )
                ),
                CardsResponse(
                    "title_description", Card(
                        null,
                        null,
                        CardTitle("Check out our App every week for exciting offers.",
                            CardAttribute("#262626", CardFont(24))),
                        CardDescription("Offers available every week!",
                            CardAttribute("#262626", CardFont(18))),
                        null
                    )
                ),
                CardsResponse(
                    "image_title_description", Card(
                        null,
                        null,
                        CardTitle("Movie ticket to Dark Phoenix!",
                            CardAttribute("#FFFFFF", CardFont(18))),
                        CardDescription("Tap to see offer dates and descriptions.",
                            CardAttribute("#FFFFFF", CardFont(12))),
                        CardImage("https://qaevolution.blob.core.windows.net/assets/ios/3x/Featured@4.76x.png",
                            CardSize(1170, 1498))
                    )
                )
            )
        )
    )
}
// endregion