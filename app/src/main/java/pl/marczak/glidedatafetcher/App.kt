package pl.marczak.glidedatafetcher

import android.app.Application
import kotlinx.coroutines.delay
import pl.marczak.glidedatafetcher.useCase.ThumbnailResponse
import pl.marczak.glidedatafetcher.useCase.ThumbnailUseCase
import java.util.*

class App : Application() {

    companion object {

        lateinit var myUseCase: ThumbnailUseCase
    }

    val random = Random(System.currentTimeMillis())

    val randomImages = arrayListOf(
        "https://media.gettyimages.com/photos/moraine-lake-in-banff-national-park-canada-picture-id500177214?s=612x612",
        "https://www.stockfootageforfree.com/wp-content/uploads/2016/01/800-min.png",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQQA1tGNVugDlrCb0bjr1dz89esUMsgL8toTAk4dH0QrTLD5gt9",
        "https://elements-video-cover-images-0.imgix.net/files/240428679/preview.jpg?auto=compress%2Cformat&fit=min&h=394&w=700&s=97ea941557fc7e26c86cc4e1d2d2da1f"
    )

    override fun onCreate() {
        super.onCreate()

        myUseCase = {
            delay(100L + random.nextInt(1_000))
            ThumbnailResponse(pickImageAtIndex(it.id), it.id)
        }
    }

    private fun pickImageAtIndex(
        index: Int
    ): String {
        return randomImages.getOrNull(index) ?: ""
    }
}