package college.wyk.app.model.sns.facebook

import android.os.Parcel
import android.os.Parcelable
import college.wyk.app.commons.adapter.ViewType
import college.wyk.app.model.Stackable
import college.wyk.app.model.sns.SnsPost
import college.wyk.app.model.sns.SnsType
import com.squareup.moshi.Json
import java.text.SimpleDateFormat

data class FacebookPagePostRoot(
        val data: List<FacebookPagePost>,
        val paging: Paging?
)

data class Paging(
        val previous: String,
        val next: String
)

data class FacebookPagePost(
        val type: PostType,
        val message: String?,
        @Json(name = "object_id") val objectId: String?,
        @Json(name = "full_picture") val fullPicture: String?,
        @Json(name = "created_time") val createdTime: String,
        val link: String?,
        val id: String
) : SnsPost, Stackable, Parcelable {

    override fun viewType() = ViewType.facebook_item

    override fun computeCreationTime() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS").parse(createdTime).time

    override fun snsType() = SnsType.FACEBOOK

    constructor(source: Parcel) : this(source.readSerializable() as PostType, source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeSerializable(type)
        dest?.writeString(message)
        dest?.writeString(objectId)
        dest?.writeString(fullPicture)
        dest?.writeString(createdTime)
        dest?.writeString(link)
        dest?.writeString(id)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<FacebookPagePost> = object : Parcelable.Creator<FacebookPagePost> {
            override fun createFromParcel(source: Parcel): FacebookPagePost = FacebookPagePost(source)
            override fun newArray(size: Int): Array<FacebookPagePost?> = arrayOfNulls(size)
        }
    }

}

enum class PostType {

    link, status, photo, video, offer, event

}