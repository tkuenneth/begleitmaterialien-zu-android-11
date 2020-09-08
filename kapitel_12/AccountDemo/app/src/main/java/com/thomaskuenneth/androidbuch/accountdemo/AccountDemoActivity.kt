package com.thomaskuenneth.androidbuch.accountdemo

import android.accounts.AccountManager
import android.accounts.AccountManager.KEY_ACCOUNT_NAME
import android.accounts.AccountManager.KEY_ACCOUNT_TYPE
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_ACCOUNT = 123
class AccountDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            val intent = AccountManager.newChooseAccountIntent(
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
            startActivityForResult(intent, REQUEST_ACCOUNT)
        }
    }

    override fun onStart() {
        super.onStart()
        textview.text = ""
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (RESULT_OK == resultCode
            && REQUEST_ACCOUNT == requestCode
        ) {
            data?.let {
                val accountName = it.getStringExtra(KEY_ACCOUNT_NAME)
                val accountType = it.getStringExtra(KEY_ACCOUNT_TYPE)
                textview.append("Account-Name: $accountName\n")
                textview.append("Account-Typ: $accountType\n")
            }
        }
    }
}