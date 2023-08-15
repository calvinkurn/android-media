package com.tokopedia.sellerapp.di

import android.app.Application
import com.tokopedia.encryption.security.AESEncryptorECB
import com.tokopedia.encryption.utils.RSAKeys
import com.tokopedia.logger.LogManager.Companion.init
import com.tokopedia.logger.LoggerProxy
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.sellerapp.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import java.security.interfaces.RSAPrivateKey
import javax.crypto.SecretKey

@HiltAndroidApp
class SellerApp : Application() {
    var remoteConfig: FirebaseRemoteConfigImpl? = null

    override fun onCreate() {
        initRemoteConfig()
        super.onCreate()
        initLogManager()
    }
    private fun initRemoteConfig() {
        remoteConfig = FirebaseRemoteConfigImpl(this@SellerApp)
    }
    private fun initLogManager() {
        init(this@SellerApp, object : LoggerProxy {
            val encryptor: AESEncryptorECB = AESEncryptorECB()
            val secretKey: SecretKey =
                encryptor.generateKey(ENCRYPTION_KEY)
            val encryptorRSA: com.tokopedia.encryption.security.RSA =
                com.tokopedia.encryption.security.RSA()
            val privateKeyRSA: RSAPrivateKey =
                encryptorRSA.stringToPrivateKey(RSAKeys.PRIVATE_RSA_KEY_STR)
            override val decrypt: ((String) -> String)
                get() = { s: String? -> encryptor.decrypt(s?:"", secretKey) }
            override val encrypt: ((String) -> String)
                get() = { s: String? -> encryptor.encrypt(s?:"", secretKey) }
            override val decryptNrKey: ((String) -> String)
                get() = { s: String? ->
                    encryptorRSA.decrypt(
                        s?:"",
                        privateKeyRSA,
                        com.tokopedia.encryption.utils.Constants.RSA_OAEP_ALGORITHM
                    )
                }
            override val versionName: String
                get() = BuildConfig.VERSION_NAME
            override val versionCode: Int
                get() = BuildConfig.VERSION_CODE
            override val scalyrToken: String
                get() = com.tokopedia.keys.Keys.AUTH_SCALYR_API_KEY
            override val newRelicToken: String
                get() = com.tokopedia.keys.Keys.AUTH_NEW_RELIC_API_KEY
            override val newRelicUserId: String
                get() = com.tokopedia.keys.Keys.AUTH_NEW_RELIC_USER_ID
            override val isDebug: Boolean
                get() = false
            override val userId: String
                get() = "-"
            override val parserScalyr: String
                get() = "android-seller-app-p%s"
            override val scalyrConfig: String
                get() = remoteConfig?.getString(REMOTE_CONFIG_SCALYR_KEY_LOG)?:""
            override val newRelicConfig: String
                get() = remoteConfig?.getString(REMOTE_CONFIG_NEW_RELIC_KEY_LOG)?:""
            override val embraceConfig: String
                get() = remoteConfig?.getString(REMOTE_CONFIG_EMBRACE_KEY_LOG)?:""
        })
    }

    companion object {
        private val REMOTE_CONFIG_SCALYR_KEY_LOG = "android_sellerapp_log_config_scalyr"
        private val REMOTE_CONFIG_NEW_RELIC_KEY_LOG = "android_sellerapp_log_config_v3_new_relic"
        private val REMOTE_CONFIG_EMBRACE_KEY_LOG = "android_sellerapp_log_config_embrace"
        private var ENCRYPTION_KEY = String(
            charArrayOf(
                113.toChar(),
                40.toChar(),
                101.toChar(),
                35.toChar(),
                37.toChar(),
                71.toChar(),
                102.toChar(),
                64.toChar(),
                111.toChar(),
                105.toChar(),
                62.toChar(),
                108.toChar(),
                107.toChar(),
                66.toChar(),
                126.toChar(),
                104.toChar()
            )
        )

    }
}
