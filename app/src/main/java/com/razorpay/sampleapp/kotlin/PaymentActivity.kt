package com.razorpay.sampleapp.kotlin

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.razorpay.*
import com.razorpay.sampleapp.Api
import com.razorpay.sampleapp.CreatePaymentOrderRequest
import com.razorpay.sampleapp.KtorClient
import com.razorpay.sampleapp.MainActivity
import com.razorpay.sampleapp.OrderResponse
import com.razorpay.sampleapp.R
import com.razorpay.sampleapp.networking.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception

class PaymentActivity: Activity(), PaymentResultWithDataListener, ExternalWalletListener, DialogInterface.OnClickListener {

    val TAG:String = PaymentActivity::class.toString()
    private lateinit var alertDialogBuilder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        /*
        * To ensure faster loading of the Checkout form,
        * call this method as early as possible in your checkout flow
        * */
        Checkout.preload(applicationContext)
        alertDialogBuilder = AlertDialog.Builder(this@PaymentActivity)
        alertDialogBuilder.setTitle("Payment Result")
        alertDialogBuilder.setCancelable(true)
        alertDialogBuilder.setPositiveButton("Ok",this)
        val button: Button = findViewById(R.id.btn_pay)
        button.setOnClickListener {
            startPayment()
        }
    }

    private fun startPayment() {
        val activity: Activity = this
        val co = Checkout()
        val etApiKey = findViewById<EditText>(R.id.et_api_key)

        if (!TextUtils.isEmpty(etApiKey.text.toString())) {
            co.setKeyID(etApiKey.text.toString())
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                // 🔸 Build the request
                val request = CreatePaymentOrderRequest(
                    amount = 50000, // ₹500 in paise
                    currency = "INR",
                    notes = mapOf("user_id" to "1234") // Customize as needed
                )

                val api = Api(KtorClient.instance)

                // ✅ Make the API call
                val result = api.paymentOrder(request)

                when (result) {
                    is Result.Success<*> -> {
                        val order = result.data as OrderResponse

                        // 🔸 Prepare Razorpay options
                        val options = JSONObject().apply {
                            put("name", "Razorpay Corp")
                            put("description", "Payment for booking")
                            put("order_id", order.orderId) // Razorpay Order ID
                            put("currency", "INR")
                            put("amount", 100) // Must be string
                            put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
                            put("send_sms_hash", true)

                            val prefill = JSONObject().apply {
                                put("email", "test@example.com")
                                put("contact", "9999999999")
                            }
                            put("prefill", prefill)
                        }

                        // 🔸 Start Razorpay Checkout
                        co.open(activity, options)
                    }

                    is Result.Error<*> -> {
                        Toast.makeText(
                            activity,
                            "Failed to create order: ${result.error}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e(TAG, "API Error: ${result.error}")
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(activity, "Unexpected error: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e(TAG, "Exception in payment", e)
            }
        }
    }


    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        try{
            alertDialogBuilder.setMessage("Payment Successful : Payment ID: $p0\nPayment Data: ${p1?.data}")
            alertDialogBuilder.show()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        try {
            alertDialogBuilder.setMessage("Payment Failed : Payment Data: ${p2?.data}")
            alertDialogBuilder.show()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {
        try{
            alertDialogBuilder.setMessage("External wallet was selected : Payment Data: ${p1?.data}")
            alertDialogBuilder.show()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
    }
}