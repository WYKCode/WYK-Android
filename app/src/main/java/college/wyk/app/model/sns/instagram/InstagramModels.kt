package college.wyk.app.model.sns.instagram

import android.os.Parcel
import android.os.Parcelable
import college.wyk.app.commons.adapter.ViewType
import college.wyk.app.model.Stackable
import college.wyk.app.model.sns.SnsPost
import college.wyk.app.model.sns.SnsType
import com.squareup.moshi.Json

data class InstagramPostRoot(
        val pagination: Pagination,
        val data: List<InstagramPost>
)

data class Pagination(
        @Json(name = "next_url") val nextUrl: String?
)

data class InstagramPost(
        val title: String,
        @Json(name = "created_time") val createdTime: Long,
        val link: String,
        val images: Images,
        val caption: Caption?,
        val user: User
) : SnsPost, Stackable {

    override fun viewType() = ViewType.instagram_item

    override fun computeCreationTime() = createdTime * 1000L

    override fun snsType() = SnsType.INSTAGRAM

    constructor(source: Parcel) : this(source.readString(), source.readLong(), source.readString(), source.readParcelable<Images>(Images::class.java.classLoader), source.readParcelable<Caption>(Caption::class.java.classLoader), source.readParcelable<User>(User::class.java.classLoader))

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(title)
        dest?.writeLong(createdTime)
        dest?.writeString(link)
        dest?.writeParcelable(images, 0)
        dest?.writeParcelable(caption, 0)
        dest?.writeParcelable(user, 0)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<InstagramPost> = object : Parcelable.Creator<InstagramPost> {
            override fun createFromParcel(source: Parcel): InstagramPost = InstagramPost(source)
            override fun newArray(size: Int): Array<InstagramPost?> = arrayOfNulls(size)
        }
    }

}

data class Images(
        @Json(name = "low_resolution") val lowResolution: Image,
        @Json(name = "thumbnail") val thumbnail: Image,
        @Json(name = "standard_resolution") val standardResolution: Image
) : Parcelable {

    constructor(source: Parcel) : this(source.readParcelable<Image>(Image::class.java.classLoader), source.readParcelable<Image>(Image::class.java.classLoader), source.readParcelable<Image>(Image::class.java.classLoader))

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeParcelable(lowResolution, 0)
        dest?.writeParcelable(thumbnail, 0)
        dest?.writeParcelable(standardResolution, 0)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Images> = object : Parcelable.Creator<Images> {
            override fun createFromParcel(source: Parcel): Images = Images(source)
            override fun newArray(size: Int): Array<Images?> = arrayOfNulls(size)
        }
    }

}

data class Image(
        val url: String,
        val width: Int,
        val height: Int
) : Parcelable {

    constructor(source: Parcel) : this(source.readString(), source.readInt(), source.readInt())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(url)
        dest?.writeInt(width)
        dest?.writeInt(height)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Image> = object : Parcelable.Creator<Image> {
            override fun createFromParcel(source: Parcel): Image = Image(source)
            override fun newArray(size: Int): Array<Image?> = arrayOfNulls(size)
        }
    }

}

data class Caption(
        val text: String?
) : Parcelable {

    constructor(source: Parcel) : this(source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(text)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Caption> = object : Parcelable.Creator<Caption> {
            override fun createFromParcel(source: Parcel): Caption = Caption(source)
            override fun newArray(size: Int): Array<Caption?> = arrayOfNulls(size)
        }
    }

}

data class User(
        val username: String,
        @Json(name = "full_name") val fullName: String
) : Parcelable {

    constructor(source: Parcel) : this(source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(username)
        dest?.writeString(fullName)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }

}