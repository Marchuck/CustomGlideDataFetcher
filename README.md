# CustomGlideDataFetcher

with Glide it is possible to define custom Model Loaders

## what this repo is about?

I bet you usually do

```
  Glide.with(context)
  .load("https://baseUrl/very/cute/cat.jpg")
  .into(imageView)
```

Sometimes you need to use an external API in order to get your URL for Glide:

```
data class ThumbnailRequest( ... )

data class ThumbnailResponse(val height: Int, val width: Int, val url: String)

interface ThumbnailApi{
    suspend fun getThumbnail(request: ThumbnailRequest) : ThumbnailResponse
}
```

in this case you need to call API first, then use `ThumbnailResponse.url` to load image with Glide.

This is not the best solution, we can do it better! :rocket: 

This project demonstrates solution to load your `ThumbnailRequest` directly to Glide, without overhead around fetching urls somewhere else:

```
Glide.with(context)
        .load( ThumnbailRequest( ... ) )
        .into(imageView)
```

:tada: :tada: :tada:

This is possible with custom `ModelLoader` 

`https://bumptech.github.io/glide/tut/custom-modelloader.html#writing-a-custom-modelloader`

This requires a lot of boilerplate code, but I got hands dirty and eased this process as much as possible.

You need to focus on following models:
```
typealias ThumbnailUseCase = suspend (ThumbnailRequest) -> ThumbnailResponse //your API
typealias RequestType = ThumbnailRequest
```

Behind the scenes following flow of transformations happens:
```
(ThumbnailRequest) ==> (ThumbnailResponse) ==> (InputStream == your image)
```


Do not hesitate to leave a :star: if this solution helped you :smiley: 

Enjoy!
