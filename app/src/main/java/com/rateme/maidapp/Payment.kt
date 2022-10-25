package com.rateme.maidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.androidstudy.daraja.Daraja
import com.androidstudy.daraja.DarajaListener
import com.androidstudy.daraja.model.AccessToken
import com.androidstudy.daraja.model.LNMExpress
import com.androidstudy.daraja.model.LNMResult
import com.androidstudy.daraja.util.Env
import com.androidstudy.daraja.util.TransactionType
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.rateme.maidapp.Classes.User

class Payment : AppCompatActivity() {
    lateinit var daraja: Daraja
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        var button = findViewById(R.id.button) as Button
        var phone = findViewById(R.id.phone) as EditText
        val phonebro = intent.getStringExtra("key")
        var amountme = findViewById(R.id.amount) as EditText
        var phonepay = findViewById(R.id.phonepay) as EditText
        phonepay.text = Editable.Factory.getInstance().newEditable(phonebro)
        daraja = Daraja.with(
            "mwDIgoe2Tu0yA1tRZOcSuBSCQoYjasLc",
            "WIOIXrpXTJkPKcGe",
            Env.SANDBOX, //for Test use Env.PRODUCTION when in production
            object : DarajaListener<AccessToken> {
                override fun onResult(accessToken: AccessToken) {

                    Toast.makeText(
                        this@Payment,
                        "MPESA TOKEN : ${accessToken.access_token}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(error: String) {

                }
            })

        button.setOnClickListener {
            val phoneNumber = phone.text.trim().toString().trim()
            val amountmelo = amountme.text.trim().toString().trim()
            val lnmExpress = LNMExpress(
                "174379",
                "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                TransactionType.CustomerPayBillOnline,
                amountmelo,
                phoneNumber,
                "174379",
                phoneNumber,
                "https://mycallback.com",
                "001ABC",
                "Goods Payment"

            )

            daraja.requestMPESAExpress(lnmExpress,
                object : DarajaListener<LNMResult> {
                    override fun onResult(lnmResult: LNMResult) {
                        Toast.makeText(
                            this@Payment,
                            "Response here ${lnmResult.ResponseDescription}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onError(error: String) {

                        Toast.makeText(
                            this@Payment,
                            "Error here $error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }

    }
}