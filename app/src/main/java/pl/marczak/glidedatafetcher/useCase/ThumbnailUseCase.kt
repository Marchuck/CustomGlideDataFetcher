package pl.marczak.glidedatafetcher.useCase

typealias ThumbnailUseCase = suspend (ThumbnailRequest) -> ThumbnailResponse

data class Dimension(val width: Int, val height: Int)

data class ThumbnailRequest(val id: Int, val type: String, val dimension: Dimension? = null)

data class ThumbnailResponse(val url: String, val id: Int)