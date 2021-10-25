package com.example.tmobiletest.model

data class PageResponse(
    val cards: List<CardsResponse>
)

data class CardsResponse(
    val card_type: String,
    val card: Card
)

data class Card(
    val value: String?,
    val attributes: CardAttribute?,
    val title: CardTitle?,
    val description: CardDescription?,
    val image: CardImage?
)

data class CardAttribute(
    val text_color: String,
    val font: CardFont
)

data class CardDescription(
    val value: String,
    val attributes: CardAttribute?
)

data class CardTitle(
    val value: String,
    val attributes: CardAttribute?
)

data class CardImage(
    val url: String,
    val size: CardSize
)

data class CardFont(
    val size: Int
)

data class CardSize(
    val width: Int,
    val height: Int
)

enum class CardTypes(value: String) {
    TEXT("text"),
    TITLE_DESCRIPTION("title_description"),
    IMAGE_TITLE("image_title_description")
}