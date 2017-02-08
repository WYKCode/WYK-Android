package college.wyk.app.model.directus

import android.os.Parcel
import android.os.Parcelable
import college.wyk.app.commons.adapter.AdapterBindable
import college.wyk.app.commons.adapter.ViewType
import college.wyk.app.model.Stackable
import com.squareup.moshi.Json

data class DirectusPostRoot(
        val rows: List<DirectusPost>
)

data class DirectusPost(
        val active: Int,
        val title: String,
        val author: String?,
        val date: String,
        val body: String?,
        val header: File?,
        @Json(name = "is_external") val isExternal: Int?,
        @Json(name = "external_url") val externalUrl: String?
) : AdapterBindable, Parcelable, Stackable {

    override fun viewType() = ViewType.directus_item

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelable<File>(File::class.java.classLoader),
            source.readInt(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(active)
        dest?.writeString(title)
        dest?.writeString(author)
        dest?.writeString(date)
        dest?.writeString(body)
        dest?.writeParcelable(header, 0)
        dest?.writeInt(isExternal ?: 0)
        dest?.writeString(externalUrl)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<DirectusPost> = object : Parcelable.Creator<DirectusPost> {
            override fun createFromParcel(source: Parcel): DirectusPost = DirectusPost(source)
            override fun newArray(size: Int): Array<DirectusPost?> = arrayOfNulls(size)
        }
    }

}

data class PublicationRoot(
        val rows: List<Publication>
)

data class Publication(
        val active: Int,
        val id: Int,
        val title: String,
        val tagline: String,
        val cover: File,
        val url: String
) : AdapterBindable, Parcelable, Stackable {

    override fun viewType() = ViewType.publication_item

    constructor(source: Parcel) : this(source.readInt(), source.readInt(), source.readString(), source.readString(), source.readParcelable<File>(File::class.java.classLoader), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(active)
        dest?.writeInt(id)
        dest?.writeString(title)
        dest?.writeString(tagline)
        dest?.writeParcelable(cover, 0)
        dest?.writeString(url)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Publication> = object : Parcelable.Creator<Publication> {
            override fun createFromParcel(source: Parcel): Publication = Publication(source)
            override fun newArray(size: Int): Array<Publication?> = arrayOfNulls(size)
        }
    }

}

data class File(
        val name: String,
        val title: String,
        val caption: String,
        val type: String
) : Parcelable {

    constructor(source: Parcel) : this(source.readString(), source.readString(), source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeString(title)
        dest?.writeString(caption)
        dest?.writeString(type)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<File> = object : Parcelable.Creator<File> {
            override fun createFromParcel(source: Parcel): File = File(source)
            override fun newArray(size: Int): Array<File?> = arrayOfNulls(size)
        }
    }

}