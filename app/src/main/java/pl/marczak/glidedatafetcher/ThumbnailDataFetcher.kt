package pl.marczak.glidedatafetcher

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import okhttp3.*
import java.io.IOException

data class ThumbnailDataFetcherException(val failure: Throwable) : Exception()
data class ServerErrorException(val code: Int) : Exception()

typealias DataFetcherCallback = DataFetcher.DataCallback<in ResultType>

class ThumbnailDataFetcher(
    private val thumbnailUseCase: ThumbnailUseCase,
    private val okHttpClient: OkHttpClient,
    private val request: RequestType
) : DataFetcher<ResultType> {

    private var job: Job? = null
    private var call: Call? = null

    override fun getDataClass(): Class<ResultType> = ResultType::class.java

    override fun cleanup() {}

    override fun getDataSource(): DataSource = DataSource.REMOTE

    override fun cancel() {
        call?.cancel()
        job?.cancel()
    }

    override fun loadData(priority: Priority, callback: DataFetcherCallback) {
        job = GlobalScope.async {
            fetchThumbnailImage({
                callback.onDataReady(it)
            }) {
                callback.onLoadFailed(it)
            }
        }
    }

    private suspend fun fetchThumbnailImage(
        resultConsumer: ((ResultType?) -> Unit),
        errorConsumer: (Exception) -> Unit
    ) {
        try {
            val response = thumbnailUseCase.invoke(request)

            val legitUrl = response.url

            fetchImage(legitUrl, { resultConsumer(it) },
                { failure -> errorConsumer(failure) })
        } catch (x: Exception) {
            errorConsumer(ThumbnailDataFetcherException(x))
        }
    }

    private fun fetchImage(
        url: String,
        resultConsumer: ((ResultType?) -> Unit),
        errorConsumer: (Exception) -> Unit
    ) {
        val request = Request.Builder().url(url).build()

        //todo: provide additional authorization if your API requires it
        call = okHttpClient.newCall(request)
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorConsumer(ThumbnailDataFetcherException(e))
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    resultConsumer(response.body()?.byteStream())
                } else {
                    errorConsumer(ServerErrorException(response.code()))
                }
            }
        })
    }
}