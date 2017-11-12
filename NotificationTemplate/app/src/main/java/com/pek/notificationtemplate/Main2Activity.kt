package com.pek.notificationtemplate

import android.app.NotificationManager
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.NotificationCompat
import android.util.Log
import android.widget.Button
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import kotlin.properties.Delegates

class Main2Activity : AppCompatActivity() {
    lateinit var  mBuilder: NotificationCompat.Builder
    lateinit var mNotifyManager: NotificationManager
    internal var id = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        mNotifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mBuilder = NotificationCompat.Builder(this)
        mBuilder.setContentTitle("Download")
                .setSmallIcon(R.mipmap.ic_launcher)

      var btnSynce = findViewById(R.id.btnSynce) as Button
        btnSynce.setOnClickListener  { view ->
            val synce = SynceData()
            synce.execute()
        }

    }


    internal inner class SynceData : AsyncTask<Void,Int,Array<Comment>>(){
        var legthItem = 0
        override fun onPostExecute(comment: Array<Comment>) {
            super.onPostExecute(comment)
            mBuilder.setContentText("Download complete")
            // Removes the progress bar

            mNotifyManager.notify(id, mBuilder.build())

        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            values[0]?.let { mBuilder.setProgress(legthItem, it, false) }
            mNotifyManager.notify(id, mBuilder.build())
            super.onProgressUpdate(*values)
        }





        override fun onPreExecute() {
            super.onPreExecute()

            // Displays the progress bar for the first time.
            mBuilder.setProgress(1, 0, false)
            mNotifyManager.notify(id, mBuilder.build())
        }

        override fun doInBackground(vararg voids: Void): Array<Comment> {
            val client = OkHttpClient()
            var comment:Array<Comment> by Delegates.notNull()
            val mediaType = MediaType.parse("application/x-www-form-urlencoded")
            val body = RequestBody.create(mediaType, "grant_type=password&username=admin&password=admin")
            val request = Request.Builder()
                    .url("https://jsonplaceholder.typicode.com/comments")
                    .get()
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("authorization", "Basic ZGVtb2NsaWVudDpkZW1vY2xpZW50c2VjcmV0")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "3f6017e8-fbb8-ef9a-5438-e21fe8012ae5")
                    .build()

            try {
                val response = client.newCall(request).execute()

                val gson = Gson()
                 comment = gson.fromJson(response.body()!!.string().toString(), Array<Comment>::class.java)
                legthItem = comment.size
                for (i in comment.indices) {
                    mBuilder.setContentText((i + 1).toString() + "/" + comment.size)
                    Log.e("HI", "onHandleIntent: " + comment[i])

                    publishProgress(Math.min(i, comment.size))

                }
                return comment
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return comment
        }
    }
}

