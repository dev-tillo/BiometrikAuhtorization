package com.example.biometrikauhtorization

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import android.view.View
import android.widget.Button
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import android.security.keystore.KeyProperties
import android.security.keystore.KeyGenParameterSpec
import androidx.biometric.BiometricPrompt.PromptInfo
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import android.content.DialogInterface
import android.R.attr.negativeButtonText
import android.R.attr.description
import android.R.attr.subtitle
import android.annotation.SuppressLint
import android.content.Intent
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.Executor


@RequiresApi(api = Build.VERSION_CODES.P)
class MainActivity : AppCompatActivity() {

    private val KEY_NAME = "KeyName"
    private val ANDROID_KEY_STORE = "AndroidKeyStore"
    private val ALLOWED_AUTHENTICATORS = 1000
    private var VALIDITY_DURATION_SECONDS = 1000
    private var REQUEST_CODE = 128

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        promptInfo = PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()


        val biometricManager = BiometricManager.from(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS ->
                    Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                    Log.e("MY_APP_TAG", "No biometric features available on this device.")
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                    Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                        )
                    }
                    startActivityForResult(enrollIntent, REQUEST_CODE)
                }
            }
        }

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        applicationContext,
                        "Authentication succeeded!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@MainActivity, Login::class.java))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        biometricPrompt.authenticate(promptInfo)

    }
}

//        biometricLoginButton.setOnClickListener {
//            val cipher = getCipher()
//            val secretKey = getSecretKey()
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
//            biometricPrompt.authenticate(
//                promptInfo,
//                BiometricPrompt.CryptoObject(cipher)
//            )
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            generateSecretKey(
//                KeyGenParameterSpec.Builder(
//                    KEY_NAME,
//                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
//                )
//                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
//                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
//                    .setUserAuthenticationRequired(true)
//                    .setInvalidatedByBiometricEnrollment(true)
//                    .build()
//            )
//        }
//    }

//    private fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
//        val keyGenerator = KeyGenerator.getInstance(
//            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
//        )
//        keyGenerator.init(keyGenParameterSpec)
//        keyGenerator.generateKey()
//    }

//    override fun onAuthenticationSucceeded(
//        result: BiometricPrompt.AuthenticationResult
//    ) {
//        val encryptedInfo: ByteArray = result.cryptoObject?.cipher?.doFinal(
//            plaintext - string.toByteArray(Charset.defaultCharset())
//        )!!
//        Log.d(
//            "MY_APP_TAG", "Encrypted information: " +
//                    Arrays.toString(encryptedInfo)
//        )
//    }


//    private fun getSecretKey(): SecretKey {
//        val keyStore = KeyStore.getInstance("AndroidKeyStore")
//
//        // Before the keystore can be accessed, it must be loaded.
//        keyStore.load(null)
//        return keyStore.getKey(KEY_NAME, null) as SecretKey
//    }

//    private fun getCipher(): Cipher {
//        return Cipher.getInstance(
//            KeyProperties.KEY_ALGORITHM_AES + "/"
//                    + KeyProperties.BLOCK_MODE_CBC + "/"
//                    + KeyProperties.ENCRYPTION_PADDING_PKCS7
//        )
//    }