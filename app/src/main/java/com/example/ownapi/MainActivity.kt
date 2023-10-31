package com.example.ownapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import java.security.MessageDigest
import java.math.BigInteger
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private fun stringToMd5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    val timestamp = System.currentTimeMillis()
    val publicKey = "2151a24b0f28163128973d77f92a8511"
    val privateKey = "1459af86ba19046af5a11537638fbf9333b739eb"
    val hash = stringToMd5("$timestamp$privateKey$publicKey")

    val url = "https://gateway.marvel.com/v1/public/characters?ts=$timestamp&apikey=$publicKey&hash=$hash"

    var characterName = ""
    var characterImageURL = ""
    var characterId = ""
    private lateinit var marvelList: MutableList<String>
    private lateinit var marvelID: MutableList<String>
    private lateinit var marvelName: MutableList<String>
    private lateinit var rvMarvels: RecyclerView
    private fun getMarvelImageURL() {
        val client = AsyncHttpClient()
        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Marvel", "response successful$json")
                val data = json.jsonObject.getJSONObject("data")
                val results = data.getJSONArray("results")
                if (results.length() > 0) {
                    for (i in 0 until results.length()) {
                        val randomCharacter = results.getJSONObject(i)
                        characterName = randomCharacter.getString("name")
                        characterId = randomCharacter.getString("id")

                        val thumbnail = randomCharacter.getJSONObject("thumbnail")
                        val imagePath = thumbnail.getString("path")
                        val imageExtension = thumbnail.getString("extension")
                        characterImageURL = "$imagePath.$imageExtension"
                        marvelList.add(characterImageURL)
                        marvelID.add(characterId)
                        marvelName.add(characterName)
                    }
                    val adapter = MarvelAdapter(marvelList, marvelName, marvelID)
                    rvMarvels.adapter = adapter
                    rvMarvels.layoutManager = LinearLayoutManager(this@MainActivity)
                } else {
                    Log.d("Marvel", "No character found!")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Marvel Error", errorResponse)
            }
        }]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvMarvels = findViewById(R.id.marvel_list)
        marvelList = mutableListOf()
        marvelID = mutableListOf()
        marvelName = mutableListOf()
        getMarvelImageURL()
    }
}