package com.thomaskuenneth.androidbuch.accountdemo

import android.accounts.AccountManager
import android.accounts.AccountManager.*
import android.accounts.AccountManagerCallback
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


private const val REQUEST_ACCOUNT = 123
private val TAG = AccountDemoActivity::class.simpleName
class AccountDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            val intent = newChooseAccountIntent(
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
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (RESULT_OK == resultCode
            && REQUEST_ACCOUNT == requestCode
        ) {
            data?.let {
                it.getStringExtra(KEY_ACCOUNT_NAME)?.let { accountName ->
                    it.getStringExtra(KEY_ACCOUNT_TYPE)?.let { accountType ->
                        textview.append("Account-Name: $accountName\n")
                        textview.append("Account-Typ: $accountType\n")
                        accessAccount(accountName, accountType, "cl")
                    }
                }
            }
        }
    }

    private val a = AccountManagerCallback<Bundle> { future ->
        val apiKey = "AIzaSyCMZJ8fniE83au2QdekokVD-okynjGoa4A" // "API-Schlüssel"
        try {
            val result = future.result
            result.getString(KEY_AUTHTOKEN)?.let {
                token = it
                val tasks = getFromServer(
                    "https://www.googleapis.com/auth/tasks/v1/users/@me/lists?key=$apiKey",
                )
                textview.append(tasks)
            }
        } catch (thr: Throwable) {
            // OperationCanceledException, AuthenticatorException, IOException
            Log.e(TAG, thr.message, thr)
        }
    }

    private lateinit var token: String

    private fun accessAccount(name: String, type: String, authTokenType: String) {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX)
        val accountManager = get(this)
        try {
            val accounts = accountManager.getAccountsByType(type)
            accounts.forEach { account ->
                if (name == account.name) {
                    if (::token.isInitialized) {
                        accountManager.invalidateAuthToken(type, token)
                    }
                    accountManager.getAuthToken(
                        account, authTokenType,
                        Bundle(), this, a, null
                    )
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "getAccountsByType()", e)
        }
    }

    private fun getFromServer(url: String): String {
        val sb = StringBuilder()
        try {
            (URL(url).openConnection() as HttpURLConnection).apply {
                addRequestProperty(
                    "client_id",
                    "9119178806-oqsard0foi2bbbbfh10sg84gqn3rju9u.apps.googleusercontent.com"
                )
                addRequestProperty(
                    "client_secret",
                    "NFhak7yI7MVZEpJoHnNlcIi7"
                )
                addRequestProperty("Authorization", "GoogleLogin $token")
//                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStreamReader(
                        inputStream
                    ).use { inputStreamReader ->
                        BufferedReader(inputStreamReader).use { bufferedReader ->
                            bufferedReader.lines().forEach { line ->
                                sb.append(line)
                            }
                        }
                    }
//                }
                disconnect()
                textview.append(responseMessage)
            }
        } catch (ex: IOException) {
            textview.append(ex.toString())
        }
        return sb.toString()
    }
}