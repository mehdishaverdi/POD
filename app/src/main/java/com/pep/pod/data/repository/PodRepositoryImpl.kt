package com.pep.pod.data.repository

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.pep.pod.R
import com.pep.pod.data.Const.PAGE_SIZE
import com.pep.pod.data.db.dao.PodDao
import com.pep.pod.data.dto.NeedSettlement
import com.pep.pod.data.dto.ResourceMetadata
import com.pep.pod.data.remote.MmsRemote
import com.pep.pod.data.remote.PodRemote
import com.pep.pod.data.remote.ResourceRemote
import com.pep.pod.data.remote.mediator.InvoiceMediator
import com.pep.pod.domain.dto.Business
import com.pep.pod.domain.dto.PodInvoiceModel
import com.pep.pod.domain.dto.PodNeedSettlement
import com.pep.pod.domain.repository.PodRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.PublicKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PodRepositoryImpl @Inject constructor(
    private val dao: PodDao,
    private val podRemote: PodRemote,
    private val invoiceMediator: InvoiceMediator,
    private val resourceRemote: ResourceRemote,
    private val mmsRemote: MmsRemote,
    @ApplicationContext
    private val context: Context
) : PodRepository {

    private val RSA_ALGORITHM = "RSA"
    private val CIPHER_TYPE_FOR_RSA = "RSA/ECB/PKCS1Padding"
    private val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
    private val cipher = Cipher.getInstance(CIPHER_TYPE_FOR_RSA)
    lateinit var pubKey: PublicKey
    lateinit var priKey: PrivateKey
    lateinit var strPubKey: String

    private fun generateKey(){
        try {
            val kpg: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
            kpg.initialize(2048)
            val keyPair: KeyPair = kpg.genKeyPair()

            pubKey = keyPair.public
            priKey = keyPair.private

            val modulus = (pubKey as RSAPublicKey).modulus
            val exponent = (pubKey as RSAPublicKey).publicExponent

            strPubKey = "<RSAKeyValue><Modulus>$modulus</Modulus><Exponent>$exponent</Exponent></RSAKeyValue>"
            Log.d("pod_pos", "Public Key: $strPubKey")

//            val publicKey = Base64.encodeToString(pubKey.encoded, Base64.DEFAULT)
//            val privateKey = Base64.encodeToString(priKey.encoded, Base64.DEFAULT)
//
//            val decoded: ByteArray = Base64.decode(publicKey, Base64.DEFAULT)
//            val spec = X509EncodedKeySpec(decoded)
//            val kf = KeyFactory.getInstance("RSA")
//            val generatePublic = kf.generatePublic(spec) as RSAPublicKey
//            val modulus = generatePublic.modulus
//            val exponent = generatePublic.publicExponent
//
//            val pk = "<RSAKeyValue><Modulus> $modulus </Modulus><Exponent> $exponent </Exponent></RSAKeyValue>"
//
//            Log.d("SSHKeyManager", "Public Key: " + pk)
//            Log.d("SSHKeyManager", "Public Key: " + publicKey)
//            Log.d("SSHKeyManager", "Private Key: " + privateKey)
        } catch (ex: NoSuchAlgorithmException) {
            Log.e("SSHKeyManager", ex.toString())
        }
    }

    fun getPublicKeyFromString(publicKeyString: String): PublicKey? =
        try {
            val keySpec = X509EncodedKeySpec(Base64.decode(publicKeyString.toByteArray(), Base64.DEFAULT))
            keyFactory.generatePublic(keySpec)
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }

    fun getPrivateKeyFromString(privateKeyString: String): PrivateKey? =
        try {
            val keySpec = PKCS8EncodedKeySpec(Base64.decode(privateKeyString.toByteArray(), Base64.DEFAULT))
            keyFactory.generatePrivate(keySpec)
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }

    fun encryptText(plainText: String, publicKey: PublicKey): String? =
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            Base64.encodeToString(cipher.doFinal(plainText.toByteArray()), Base64.DEFAULT).replace("(\\r|\\n)".toRegex(), "")
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }

    fun decryptText(encryptedText: String, privateKey: PrivateKey): String? =
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            String(cipher.doFinal(Base64.decode(encryptedText, Base64.DEFAULT)))
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }


//    lateinit var pubKey: PublicKey
//    lateinit var priKey: PrivateKey
//    lateinit var encryptedBytes: ByteArray
//    lateinit var decryptedBytes: ByteArray
//    lateinit var cipher: Cipher
//    lateinit var cipher1: Cipher
//    var encrypted: String? = null
//    var decrypted: String? = null
//
//
//    private fun createKeyTest(){
//        try
//        {
//            val kpg: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
//            kpg.initialize(2048)
//            val keyPair: KeyPair = kpg.genKeyPair()
//
//            pubKey = keyPair.public
//            priKey = keyPair.private
//
//            val publicKey = Base64.encodeToString(pubKey.encoded, Base64.DEFAULT)
//            val privateKey = Base64.encodeToString(priKey.encoded, Base64.DEFAULT)
//
//            val decoded: ByteArray = Base64.decode(publicKey, Base64.DEFAULT)
//            val spec = X509EncodedKeySpec(decoded)
//            val kf = KeyFactory.getInstance("RSA")
//            val generatePublic = kf.generatePublic(spec) as RSAPublicKey
//            val modulus = generatePublic.modulus
//            val exponent = generatePublic.publicExponent
//
//            val pk = "<RSAKeyValue><Modulus> $modulus </Modulus><Exponent> $exponent </Exponent></RSAKeyValue>"
//
//            Log.d("SSHKeyManager", "Public Key: " + publicKey)
//            Log.d("SSHKeyManager", "Public Key: " + pk)
//            Log.d("SSHKeyManager", "Private Key: " + privateKey)
//        }
//        catch (ex: NoSuchAlgorithmException)
//        {
//            Log.e("SSHKeyManager", ex.toString())
//        }
//    }
//
//    fun encryptRSAToString(text: String, strPublicKey: String): String? {
//        var cipherText: ByteArray? = null
//        var strEncryInfoData = ""
//        try {
//            val keyFac = KeyFactory.getInstance("RSA")
//            val keySpec: KeySpec = X509EncodedKeySpec(Base64.decode(strPublicKey.trim { it <= ' ' }
//                .toByteArray(), Base64.DEFAULT))
//            val publicKey: Key = keyFac.generatePublic(keySpec)
//
//            // get an RSA cipher object and print the provider
//            val cipher = Cipher.getInstance("RSA")
//            // encrypt the plain text using the public key
//            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
//            cipherText = cipher.doFinal(text.toByteArray())
//            strEncryInfoData = String(Base64.encode(cipherText, Base64.DEFAULT))
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return strEncryInfoData.replace("(\\r|\\n)".toRegex(), "")
//    }
//
//    fun RSAEncrypt(plain: String, publicKey: PublicKey): String? {
//        try {
//            encryptedBytes = cipher.doFinal(plain.toByteArray())
//            encrypted = String(encryptedBytes)
//            println("EEncrypted?????$encrypted")
//        } catch (e: Exception) {
//         e.printStackTrace()
//        }
//        return encrypted
//    }
//
//    @Throws(NoSuchAlgorithmException::class, NoSuchPaddingException::class, InvalidKeyException::class, IllegalBlockSizeException::class, BadPaddingException::class)
//    fun RSADecrypt(result: String, privateKey: PrivateKey): String? {
//        cipher1.init(Cipher.DECRYPT_MODE, privateKey)
//        decryptedBytes = cipher1.doFinal(result.toByteArray())
//        decrypted = String(decryptedBytes)
//        println("DDecrypted?????$decrypted")
//        return decrypted
//    }

    private fun createUniqueId(input: String) =
        MessageDigest.getInstance("SHA-1").digest(input.toByteArray()).joinToString("") {
                eachByte -> "%02x".format(eachByte)
        }

    suspend fun getToken(userName: String, password: String) {
        val response = mmsRemote.getToken(userName, password)
        if (response.Success) {

        } else
            false
    }

    override suspend fun getResource(serial: String): Boolean {
        Log.d("pod_pos", "serial: " + serial)

        val uId = createUniqueId(serial)

        generateKey()
        val x = encryptText(serial, pubKey);
        x?.let {
            val y = decryptText(it, priKey)
            if (y != ""){
                generateKey()
            }
        }

        val response = resourceRemote.getResource(uId)
        return if (!response.hasError) {
            response.metadata?.let { metaStr ->
                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val adapter: JsonAdapter<ResourceMetadata> = moshi.adapter(ResourceMetadata::class.java)
                adapter.fromJson(metaStr)?.let { metaObj ->
                    saveMetadata(metaObj)
                    metaObj.businessList?.first()?.apiToken?.isNotEmpty() ?: false
                } ?: false
            } ?: false
        } else
            false
    }

    private fun saveMetadata(resourceMetadata: ResourceMetadata) {
        with(context.getSharedPreferences(context.getString(R.string.pref_enc_file_name), Context.MODE_PRIVATE).edit()) {
            putString(context.getString(R.string.business_token_key), resourceMetadata.businessList?.first()?.apiToken)
            putString(context.getString(R.string.business_icon_key), resourceMetadata.businessList?.first()?.icon)
            putString(context.getString(R.string.business_name_key), resourceMetadata.businessList?.first()?.name)
            apply()
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getInvoices(id: String): Flow<PagingData<PodInvoiceModel>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = 2,
                initialLoadSize = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                dao.pagingSource()
            },
            remoteMediator = invoiceMediator.setId(id)
        ).flow
            .catch {
                    e -> e.printStackTrace()
            }
            .map {
                it.map { item ->
                    withContext(Dispatchers.IO) {
                        PodInvoiceModel(
                            id = item.id.toString(),
                            businessName = item.business?.name ?: "",
                            billNumber = (item.billNumber ?: 0L).toString().replace("\"", ""),
                            billAmount = (item.payableAmount ?: 0L).toString(),
                            needSettlement = dao.isNeedSettlement(item.id)
                        )
                    }
                }
            }

    override fun isBusinessValid(): Flow<Business> = flow {
        with(context.getSharedPreferences(context.getString(R.string.pref_enc_file_name), Context.MODE_PRIVATE)) {
            if (getString(context.getString(R.string.business_token_key), "")?.isNotEmpty() == true) {
                emit(Business(
                    name = getString(context.getString(R.string.business_name_key), "") ?: "",
                    icon = getString(context.getString(R.string.business_icon_key), "") ?: ""
                ))
            } else {
                emit(Business(name = "", icon = ""))
            }
        }
    }

    override fun podSettlement(): Flow<Boolean> = flow {
        dao.getNotSettlement().forEach {transaction->
            podRemote.settlementInvoice(
                merchant = transaction.merchant,
                terminal = transaction.terminal,
                sequence = transaction.sequence,
                invoiceId = transaction.id.toString(),
                date = transaction.date,
                reference = transaction.reference,
            ).also {
                    response->
                if(!response.hasError) {
                    removeFromNeedSettlement(transaction.id)
                }
                else {
                    emit(false)
                    return@forEach
                }
            }.also {
                emit(true)
            }
        }
    }

    private suspend fun removeFromNeedSettlement(id:Long){
        dao.deleteFromSettlement(id)
    }

    override fun insertNeedSettlement(receipt: PodNeedSettlement): Flow<Unit> = flow {
        dao.insertNeedSettlement(
            NeedSettlement(
                id = receipt.id,
                date = receipt.date,
                terminal = receipt.terminal,
                merchant = receipt.merchant,
                reference = receipt.reference,
                sequence = receipt.sequence
            )
        ).also {
            emit(Unit)
        }
    }
}


