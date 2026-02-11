package com.simats.farmmillet

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // --- Authentication ---
    @POST("auth/shg/register")
    suspend fun registerSHG(@Body request: SHGRegisterRequest): Response<ApiResponse<RegistrationResponse>>

    @POST("auth/consumer/register")
    suspend fun registerConsumer(@Body request: ConsumerRegisterRequest): Response<ApiResponse<RegistrationResponse>>

    @POST("auth/farmer/register")
    suspend fun registerFarmer(@Body request: FarmerRegisterRequest): Response<ApiResponse<RegistrationResponse>>

    @POST("auth/shg/login")
    suspend fun loginSHG(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @POST("auth/farmer/login")
    suspend fun loginFarmer(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @POST("auth/consumer/login")
    suspend fun loginConsumer(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @POST("auth/shg/verify-otp")
    suspend fun verifySHGOTP(@Body request: OTPVerifyRequest): Response<ApiResponse<String>>

    @POST("auth/farmer/verify-otp")
    suspend fun verifyFarmerOTP(@Body request: OTPVerifyRequest): Response<ApiResponse<String>>

    @POST("auth/consumer/verify-otp")
    suspend fun verifyConsumerOTP(@Body request: OTPVerifyRequest): Response<ApiResponse<String>>

    @GET("auth/consumer/profile")
    suspend fun getConsumerProfile(): Response<ApiResponse<UserData>>

    @PUT("auth/consumer/profile")
    suspend fun updateConsumerProfile(@Body request: Map<String, String>): Response<ApiResponse<String>>

    // --- Farmer ---
    @POST("farmer/supply")
    suspend fun addSupply(@Body request: AddSupplyRequest): Response<ApiResponse<Map<String, Int>>>

    @GET("farmer/supply")
    suspend fun getMySupplies(): Response<ApiResponse<List<SupplyResponse>>>

    @GET("farmer/payment-history")
    suspend fun getFarmerPaymentHistory(): Response<ApiResponse<List<TransactionResponse>>>

    @GET("farmer/sales-summary")
    suspend fun getFarmerSalesSummary(): Response<ApiResponse<FarmerSalesSummaryResponse>>

    @GET("farmer/dashboard")
    suspend fun getFarmerDashboard(): Response<ApiResponse<FarmerSalesSummaryResponse>>

    @GET("farmer/receipt/{id}")
    suspend fun getReceiptById(@Path("id") id: Int): Response<ApiResponse<ReceiptResponse>>

    @GET("farmer/supply/{id}")
    suspend fun getSupplyById(@Path("id") id: Int): Response<ApiResponse<SupplyResponse>>

    // --- SHG ---
    @GET("shg/supply")
    suspend fun getAllSupplies(): Response<ApiResponse<List<SupplyResponse>>>

    @PUT("shg/supply/accept/{id}")
    suspend fun acceptSupply(@Path("id") supplyId: Int, @Body request: AcceptSupplyRequest): Response<ApiResponse<Map<String, String>>>

    @PUT("shg/supply/complete/{id}")
    suspend fun completeSupply(@Path("id") supplyId: Int): Response<ApiResponse<String>>

    @GET("shg/dashboard")
    suspend fun getShgDashboard(): Response<ApiResponse<SHGDashboardResponse>>

    @GET("shg/payment-history")
    suspend fun getShgPaymentHistory(): Response<ApiResponse<List<TransactionResponse>>>

    @POST("shg/product")
    suspend fun createProduct(@Body request: CreateProductRequest): Response<ApiResponse<Map<String, Int>>>

    @GET("shg/products")
    suspend fun getMyShgProducts(): Response<ApiResponse<List<ProductResponse>>>

    @GET("shg/orders")
    suspend fun getShgOrdersReceived(): Response<ApiResponse<List<OrderResponse>>>

    @POST("shg/payment")
    suspend fun recordPayment(@Body request: RecordPaymentRequest): Response<ApiResponse<Map<String, Int>>>

    @PUT("shg/order/{id}/status")
    suspend fun updateOrderStatus(@Path("id") orderId: Int, @Body request: Map<String, String>): Response<ApiResponse<String>>

    // --- Consumer ---
    @GET("consumer/products")
    suspend fun getProducts(): Response<ApiResponse<List<ProductResponse>>>

    @GET("consumer/product/{id}")
    suspend fun getProduct(@Path("id") productId: Int): Response<ApiResponse<Map<String, ProductResponse>>>

    @POST("consumer/order")
    suspend fun placeOrder(@Body request: OrderRequest): Response<ApiResponse<PlaceOrderResponse>>

    @GET("consumer/orders")
    suspend fun getMyOrders(): Response<ApiResponse<List<OrderResponse>>>

    @GET("consumer/order/{id}/track")
    suspend fun trackOrder(@Path("id") id: Int): Response<ApiResponse<OrderTrackingDetailsResponse>>

    // --- Traceability ---
    @GET("traceability")
    suspend fun getAllTraceability(): Response<ApiResponse<TraceabilityListResponse>>

    @GET("traceability/{id}")
    suspend fun getTraceabilityDetails(@Path("id") id: String): Response<ApiResponse<TraceabilityResponse>>

    // --- Payment ---
    @POST("payment/order")
    suspend fun createPaymentOrder(@Body request: PaymentOrderRequest): Response<ApiResponse<PaymentOrderResponse>>

    @POST("payment/verify")
    suspend fun verifyPayment(@Body request: PaymentVerifyRequest): Response<ApiResponse<PaymentVerifyResponse>>
}
