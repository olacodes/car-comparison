package com.example.carcomparison.dto

data class VehicleListing(
    var content: List<Content>? = null,
)

data class Image(
    var url: String = "",
)

data class Content(
    var id: String? = null,
    var heycarId: String? = null,
    var images: List<Image>? = emptyList(),
)
