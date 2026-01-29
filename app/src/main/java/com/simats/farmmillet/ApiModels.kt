package com.simats.farmmillet

import com.google.gson.annotations.SerializedName

/**
 * Common API Response Wrapper
 */
data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T? = null,
    @SerializedName("errors") val errors: Map<String, String>? = null
)

data class RegistrationResponse(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("email") val email: String
)

/**
 * Authentication Models
 */
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: UserData
)

data class UserData(
    val id: Int,
    val email: String,
    val role: String,
    val name: String,
    val street: String?,
    val city: String?,
    val pincode: String?
)

data class OTPVerifyRequest(
    val email: String,
    val otp: String
)

data class FarmerRegisterRequest(
    val name: String,
    val email: String,
    val mobile: String,
    val password: String,
    val street: String,
    val city: String,
    val pincode: String,
    @SerializedName("farm_location") val farmLocation: String? = null,
    @SerializedName("bank_account_number") val bankAccountNumber: String? = null,
    @SerializedName("bank_ifsc_code") val bankIfscCode: String? = null
)

data class SHGRegisterRequest(
    @SerializedName("contact_person_name")  val name: String,
    val email: String,
    val mobile: String,
    val password: String,
    val street: String,
    val city: String,
    val pincode: String,
    @SerializedName("organization_name") val organizationName: String,
    @SerializedName("license_number") val licenseNumber: String
)

data class ConsumerRegisterRequest(
    val name: String,
    val email: String,
    val mobile: String,
    val password: String,
    val street: String,
    val city: String,
    val pincode: String
)

/**
 * Business Models
 */
data class AddSupplyRequest(
    @SerializedName("millet_type") val milletType: String,
    @SerializedName("quantity_kg") val quantityKg: Double,
    @SerializedName("harvest_date") val harvestDate: String,
    @SerializedName("packaging_date") val packagingDate: String,
    val location: String
)

data class SupplyResponse(
    val id: Int,
    @SerializedName("farmer_id") val farmerId: Int, // Added
    @SerializedName("millet_type") val milletType: String,
    @SerializedName("quantity_kg") val quantityKg: Double,
    @SerializedName("quality_grade") val qualityGrade: String,
    val status: String,
    @SerializedName("collection_by") val collectionBy: String? = null,
    @SerializedName("collection_date") val collectionDate: String? = null,
    @SerializedName("shg_name") val shgName: String? = null,
    @SerializedName("farmer_name") val farmerName: String? = null,
    @SerializedName("payment_status") val paymentStatus: String? = null, // Added
    @SerializedName("created_at") val createdAt: String
)

data class RecordPaymentRequest(
    @SerializedName("farmer_id") val farmerId: Int,
    val amount: Double,
    @SerializedName("payment_method") val paymentMethod: String,
    @SerializedName("supply_id") val supplyId: Int? = null // Added
)

data class ProductResponse(
    val id: Int,
    @SerializedName("millet_type") val milletType: String,
    @SerializedName("quantity_kg") val quantityKg: Double,
    @SerializedName("price_per_kg") val pricePerKg: Double,
    @SerializedName("quality_grade") val qualityGrade: String,
    val description: String?,
    @SerializedName("shg_name") val shgName: String?,
    @SerializedName("traceability_id") val traceabilityId: String? = null
)

data class OrderRequest(
    val items: List<OrderItemRequest>,
    @SerializedName("dropoff_location") val dropoffLocation: String
)

data class OrderItemRequest(
    @SerializedName("product_id") val productId: Int,
    @SerializedName("quantity_kg") val quantityKg: Double
)

data class PlaceOrderResponse(
    @SerializedName("order_id") val orderId: Int,
    @SerializedName("order_number") val orderNumber: String,
    @SerializedName("total_amount") val totalAmount: Double
)

data class CreateProductRequest(
    @SerializedName("supply_id") val supplyId: Int? = null,
    @SerializedName("millet_type") val milletType: String,
    @SerializedName("quantity_kg") val quantityKg: Double,
    @SerializedName("price_per_kg") val pricePerKg: Double,
    @SerializedName("quality_grade") val qualityGrade: String,
    @SerializedName("packaging_date") val packagingDate: String,
    @SerializedName("source_farmer_name") val sourceFarmerName: String? = null,
    val description: String? = null
)

/**
 * Order & Tracking Models
 */
data class OrderResponse(
    val id: Int,
    @SerializedName("total_amount") val totalAmount: Double,
    val status: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("consumer_name") val consumerName: String? = null,
    @SerializedName("consumer_city") val consumerCity: String? = null,
    @SerializedName("consumer_address") val consumerAddress: String? = null,
    @SerializedName("consumer_mobile") val consumerMobile: String? = null,
    val items: List<OrderItemResponse>? = null
)

data class OrderItemResponse(
    @SerializedName("product_name") val productName: String? = null,
    @SerializedName("millet_type") val milletType: String? = null,
    val quantity: Double? = null,
    @SerializedName("quantity_kg") val quantityKg: Double? = null,
    val price: Double? = null,
    @SerializedName("price_per_kg") val pricePerKg: Double? = null,
    @SerializedName("farmer_name") val farmerName: String? = null,
    @SerializedName("traceability_id") val traceabilityId: String? = null
)

data class OrderTrackingDetailsResponse(
    val order: OrderDetailsData
)

data class OrderDetailsData(
    val id: Int,
    @SerializedName("order_number") val orderNumber: String,
    @SerializedName("total_amount") val totalAmount: Double,
    val status: String,
    @SerializedName("pickup_location") val pickupLocation: String?,
    @SerializedName("dropoff_location") val dropoffLocation: String?,
    @SerializedName("shg_name") val shgName: String?,
    @SerializedName("shg_contact") val shgContact: String?,
    @SerializedName("shg_mobile") val shgMobile: String?,
    @SerializedName("created_at") val createdAt: String,
    val items: List<OrderItemResponse>,
    @SerializedName("status_history") val statusHistory: List<OrderStatusHistory>? = null
)

data class OrderStatusHistory(
    val status: String,
    val notes: String?,
    @SerializedName("created_at") val createdAt: String
)

data class TraceabilityRecordResponse(
    val id: Int,
    @SerializedName("traceability_id") val traceabilityId: String,
    @SerializedName("millet_type") val milletType: String,
    @SerializedName("farmer_name") val farmerName: String,
    @SerializedName("quality_grade") val qualityGrade: String,
    @SerializedName("harvest_date") val harvestDate: String,
    @SerializedName("packaging_date") val packagingDate: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("supply_status") val supplyStatus: String,
    @SerializedName("location") val location: String? = null,
    @SerializedName("collection_by") val collectionBy: String? = null,
    @SerializedName("collection_date") val collectionDate: String? = null,
    @SerializedName("farm_location") val farmLocation: String? = null
)

data class TraceabilityResponse(
    val traceability: TraceabilityRecordResponse,
    val journey: List<JourneyPoint> = emptyList()
)

data class JourneyPoint(
    val stage: String,
    val location: String,
    val date: String,
    val actor: String
)

data class TraceabilityListResponse(
    val records: List<TraceabilityRecordResponse>,
    val total: Int
)

/**
 * Payment Models
 */
data class PaymentOrderRequest(
    val amount: Double,
    @SerializedName("order_id") val orderId: Int
)

data class PaymentOrderResponse(
    @SerializedName("razorpay_order_id") val razorpayOrderId: String,
    val amount: Double,
    val currency: String,
    @SerializedName("key_id") val keyId: String
)

data class PaymentVerifyRequest(
    @SerializedName("razorpay_order_id") val razorpayOrderId: String,
    @SerializedName("razorpay_payment_id") val razorpayPaymentId: String,
    @SerializedName("razorpay_signature") val razorpaySignature: String
)

data class PaymentVerifyResponse(
    @SerializedName("payment_id") val paymentId: Int,
    val status: String
)

data class FarmerSalesSummaryResponse(
    val summary: FarmerStats,
    @SerializedName("millet_breakdown") val milletBreakdown: List<MilletBreakdown>
)

data class FarmerStats(
    @SerializedName("total_supplies") val totalSupplies: Int,
    @SerializedName("total_quantity") val totalQuantity: Double,
    @SerializedName("pending_count") val pendingCount: Int,
    @SerializedName("accepted_count") val acceptedCount: Int,
    @SerializedName("completed_count") val completedCount: Int,
    @SerializedName("total_earnings") val totalEarnings: Double,
    @SerializedName("total_payments") val totalPayments: Int
)

data class MilletBreakdown(
    @SerializedName("millet_type") val milletType: String,
    @SerializedName("supply_count") val supplyCount: Int,
    @SerializedName("total_quantity") val totalQuantity: Double
)

data class TransactionResponse(
    val id: Int,
    @SerializedName("payment_type") val paymentType: String,
    val amount: Double,
    val status: String,
    @SerializedName("payment_method") val paymentMethod: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("paid_by") val paidBy: String? = null
)

data class SHGDashboardResponse(
    @SerializedName("total_revenue") val totalRevenue: Double,
    @SerializedName("total_orders") val totalOrders: Int,
    @SerializedName("pending_orders") val pendingOrders: Int,
    @SerializedName("total_products") val totalProducts: Int,
    @SerializedName("recent_orders") val recentOrders: List<OrderResponse>
)

data class AcceptSupplyRequest(
    @SerializedName("collection_by") val collectionBy: String,
    @SerializedName("collection_date") val collectionDate: String,
    @SerializedName("quality_grade") val qualityGrade: String
)
