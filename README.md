# CustomGlideDataFetcher

with Glide it is possible to plug custom loaders 

https://bumptech.github.io/glide/tut/custom-modelloader.html#baseglideurlloader

## tl;dr

You usually do 

  Glide.with(context)
        .load("https://baseUrl/very/funny/cat.jpg")
        .into(imageView)


Sometimes you have an API which provides you the HTTP request and you have some model wrapping url you need to load with image loader like:

data class ThumbnailResponse(val height: Int, val width: Int, val url: String)
data class ThumnbailRequest( ... )
interface ThumbnailApi{
    suspend fun getThumbnail(thumnbailRequest: ThumnbailRequest) : ThumbnailResponse
}

in this case you can call API first, then use ThumbnailResponse to load url for glide

This is not the best solution, we can do it better. 

This project demonstrates solution to load your ThumnbailRequest directly to glide, without overhead around fetching urls somewhere else:

  Glide.with(context)
        .load( ThumnbailRequest( ... ) )
        .into(imageView)
