package com.zontechx.servidex.utils

object DeeplinkUtils {

    // Base
    private const val DEEPLINK_BASE_URL = "https://servidex.in"

    // Paths
    private const val PATH_SERVICE = "/service"
    private const val PATH_PROVIDER = "/provider"
    private const val PATH_OFFER = "/offer"

    // --- Pure Deeplink Generators ---
    fun getServiceDeeplink(serviceId: String): String {
        return "$DEEPLINK_BASE_URL$PATH_SERVICE/$serviceId"
    }

    fun getProviderDeeplink(providerId: String): String {
        return "$DEEPLINK_BASE_URL$PATH_PROVIDER/$providerId"
    }

    fun getOfferDeeplink(offerId: String): String {
        return "$DEEPLINK_BASE_URL$PATH_OFFER/$offerId"
    }

    // --- Share Templates ---
    fun shareService(providerName: String, serviceName: String, serviceId: String): String {
        return "Check out $providerName's $serviceName on Servidex! Book instantly 👉 ${getServiceDeeplink(serviceId)}"
    }

    fun shareProvider(providerName: String, providerId: String): String {
        return "Looking for trusted services? $providerName is here for you 👉 ${getProviderDeeplink(providerId)}"
    }

    fun shareOffer(offerTitle: String, offerId: String): String {
        return "Don’t miss out on this offer: $offerTitle 🎉 Check details 👉 ${getOfferDeeplink(offerId)}"
    }
}
