package com.cvscodechallenge.utils

import org.junit.Assert
import org.junit.Test

class UtilsTest {

    @Test
    fun `description contains width`(){
        val testData = "<p><a href=\"https://www.flickr.com/people/coyoty/\">Coyoty</a> posted a photo:</p> <p><a href=\"https://www.flickr.com/photos/coyoty/51906915093/\" title=\"Porcupine Puffer Fish\"><img src=\"https://live.staticflickr.com/65535/51906915093_563114363b_m.jpg\" width=\"240\" height=\"105\" alt=\"Porcupine Puffer Fish\" /></a></p> <p><i>Diodon holocanthus</i>, also known as a globefish or balloonfish. In the PPG Aquarium at the Pittsburgh Zoo in Pittsburgh, PA.</p>"

        Assert.assertNotNull(testData.width())

        Assert.assertEquals("240", testData.width())
    }
}