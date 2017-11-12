package com.pek.notificationtemplate

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by rina on 11/12/17.
 */
class Comment : Serializable {
    @SerializedName("postId")
    private var postId: String? = null
    @SerializedName("id")
    private var id: String? = null
    @SerializedName("name")
    private var name: String? = null
    @SerializedName("email")
    private var email: String? = null
    @SerializedName("body")
    private var body: String? = null


}