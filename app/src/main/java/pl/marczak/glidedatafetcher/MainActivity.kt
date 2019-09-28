package pl.marczak.glidedatafetcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        first.loadThumbnail(ThumbnailRequest(0, "foo"))
        second.loadThumbnail(ThumbnailRequest(1, "bar"))
        third.loadThumbnail(ThumbnailRequest(2, "foobar"))
    }
}


fun AppCompatImageView.loadThumbnail(
    item: ThumbnailRequest,
    placeholder: Int = R.drawable.placeholder
) {
    Glide.with(this)
        .load(item)
        .fallback(placeholder)
        .placeholder(placeholder)
        .transform(CenterCrop())
        .into(this)
}