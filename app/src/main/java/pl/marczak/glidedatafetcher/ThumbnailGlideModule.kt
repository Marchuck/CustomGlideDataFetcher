package pl.marczak.glidedatafetcher

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import okhttp3.OkHttpClient
import pl.marczak.glidedatafetcher.useCase.Dimension
import pl.marczak.glidedatafetcher.useCase.ThumbnailRequest
import pl.marczak.glidedatafetcher.useCase.ThumbnailUseCase
import java.io.InputStream

typealias RequestType = ThumbnailRequest
typealias ResultType = InputStream

@GlideModule
class ThumbnailGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
    }

    override fun registerComponents(
        context: Context, glide: Glide, registry: Registry
    ) {
        val remoteFetcher = App.myUseCase
        val okHttpClient = OkHttpClient()
        val dataFetcherFactory = ThumbnailDataFetcherFactory(remoteFetcher, okHttpClient)

        registry.append(
            RequestType::class.java,
            ResultType::class.java,
            FetchThumbnailRequestFactory(dataFetcherFactory)
        )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}

class ThumbnailDataFetcherFactory(
    private val remoteFetcher: ThumbnailUseCase,
    private val okHttpClient: OkHttpClient
) {
    fun create(request: RequestType) = ThumbnailDataFetcher(
        remoteFetcher,
        okHttpClient,
        request
    )
}

class FetchThumbnailRequestFactory(
    private val factory: ThumbnailDataFetcherFactory
) :
    ModelLoaderFactory<RequestType,ResultType> {

    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<RequestType, ResultType> {
        return GetThumbnailRequestModelLoader(factory)
    }

    override fun teardown() {
    }
}

class GetThumbnailRequestModelLoader(private val factory: ThumbnailDataFetcherFactory) :
    ModelLoader<RequestType, ResultType> {
    override fun buildLoadData(
        model: ThumbnailRequest,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<ResultType>? {

        val validWidth = model.dimension?.width ?: width
        val validHeight = model.dimension?.height ?: height

        val request = ThumbnailRequest(model.id, model.type, Dimension(validWidth, validHeight))

        val sourceKey = ObjectKey(request)

        val dataFetcher = factory.create(request)
        return ModelLoader.LoadData(sourceKey, dataFetcher)
    }

    override fun handles(model: ThumbnailRequest) = true
}