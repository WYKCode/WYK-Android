package college.wyk.app.model.sns.youtube

import android.os.Parcel
import android.os.Parcelable
import college.wyk.app.commons.adapter.ViewType
import college.wyk.app.model.Stackable
import college.wyk.app.model.sns.SnsPost
import college.wyk.app.model.sns.SnsType

data class YouTubeStatisticsResultRoot(
        val items: List<YoutubeStatisticsItem>
) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<YouTubeStatisticsResultRoot> = object : Parcelable.Creator<YouTubeStatisticsResultRoot> {
            override fun createFromParcel(source: Parcel): YouTubeStatisticsResultRoot = YouTubeStatisticsResultRoot(source)
            override fun newArray(size: Int): Array<YouTubeStatisticsResultRoot?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.createTypedArrayList(YoutubeStatisticsItem.CREATOR))

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeTypedList(items)
    }

}

data class YoutubeStatisticsItem(
        val id: String,
        val statistics: YouTubeStatisticsItemStatistics
) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<YoutubeStatisticsItem> = object : Parcelable.Creator<YoutubeStatisticsItem> {
            override fun createFromParcel(source: Parcel): YoutubeStatisticsItem = YoutubeStatisticsItem(source)
            override fun newArray(size: Int): Array<YoutubeStatisticsItem?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readParcelable<YouTubeStatisticsItemStatistics>(YouTubeStatisticsItemStatistics::class.java.classLoader))

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(id)
        dest?.writeParcelable(statistics, 0)
    }

}

data class YouTubeStatisticsItemStatistics(
        val viewCount: Int,
        val likeCount: Int,
        val dislikeCount: Int,
        val favouriteCount: Int,
        val commentCount: Int
) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<YouTubeStatisticsItemStatistics> = object : Parcelable.Creator<YouTubeStatisticsItemStatistics> {
            override fun createFromParcel(source: Parcel): YouTubeStatisticsItemStatistics = YouTubeStatisticsItemStatistics(source)
            override fun newArray(size: Int): Array<YouTubeStatisticsItemStatistics?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readInt(), source.readInt(), source.readInt(), source.readInt(), source.readInt())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(viewCount)
        dest?.writeInt(likeCount)
        dest?.writeInt(dislikeCount)
        dest?.writeInt(favouriteCount)
        dest?.writeInt(commentCount)
    }

}

data class YouTubeSearchResultRoot(
        val nextPageToken: String?,
        val items: List<YouTubeItem>
)

data class YouTubeItem(
        val id: YouTubeItemId,
        val snippet: YouTubeItemSnippet,
        var statistics: YouTubeStatisticsItemStatistics? = null
) : SnsPost, Stackable, Parcelable {

    override fun viewType() = ViewType.youtube_item

    override fun computeCreationTime() = YouTube.dateFormat.parse(snippet.publishedAt).time

    override fun snsType() = SnsType.YOUTUBE

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<YouTubeItem> = object : Parcelable.Creator<YouTubeItem> {
            override fun createFromParcel(source: Parcel): YouTubeItem = YouTubeItem(source)
            override fun newArray(size: Int): Array<YouTubeItem?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readParcelable<YouTubeItemId>(YouTubeItemId::class.java.classLoader), source.readParcelable<YouTubeItemSnippet>(YouTubeItemSnippet::class.java.classLoader), source.readParcelable<YouTubeStatisticsItemStatistics>(YouTubeStatisticsItemStatistics::class.java.classLoader))

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeParcelable(id, 0)
        dest?.writeParcelable(snippet, 0)
        dest?.writeParcelable(statistics, 0)
    }

}

data class YouTubeItemId(
        val kind: String,
        val videoId: String?
) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<YouTubeItemId> = object : Parcelable.Creator<YouTubeItemId> {
            override fun createFromParcel(source: Parcel): YouTubeItemId = YouTubeItemId(source)
            override fun newArray(size: Int): Array<YouTubeItemId?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(kind)
        dest?.writeString(videoId)
    }

}

data class YouTubeItemSnippet(
        val publishedAt: String,
        val channelId: String,
        val title: String,
        val description: String?,
        val thumbnails: YouTubeItemThumbnailRoot
) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<YouTubeItemSnippet> = object : Parcelable.Creator<YouTubeItemSnippet> {
            override fun createFromParcel(source: Parcel): YouTubeItemSnippet = YouTubeItemSnippet(source)
            override fun newArray(size: Int): Array<YouTubeItemSnippet?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readString(), source.readString(), source.readString(), source.readParcelable<YouTubeItemThumbnailRoot>(YouTubeItemThumbnailRoot::
    class.java.classLoader))

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(publishedAt)
        dest?.writeString(channelId)
        dest?.writeString(title)
        dest?.writeString(description)
        dest?.writeParcelable(thumbnails, 0)
    }

}

data class YouTubeItemThumbnailRoot(
        val default: YouTubeItemThumbnail,
        val medium: YouTubeItemThumbnail,
        val high: YouTubeItemThumbnail
) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<YouTubeItemThumbnailRoot> = object : Parcelable.Creator<YouTubeItemThumbnailRoot> {
            override fun createFromParcel(source: Parcel): YouTubeItemThumbnailRoot = YouTubeItemThumbnailRoot(source)
            override fun newArray(size: Int): Array<YouTubeItemThumbnailRoot?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readParcelable<YouTubeItemThumbnail>(YouTubeItemThumbnail::class.java.classLoader), source.readParcelable<YouTubeItemThumbnail>(YouTubeItemThumbnail::class.java.classLoader), source.readParcelable<YouTubeItemThumbnail>(YouTubeItemThumbnail::class.java.classLoader))

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeParcelable(default, 0)
        dest?.writeParcelable(medium, 0)
        dest?.writeParcelable(high, 0)
    }

}

data class YouTubeItemThumbnail(
        val url: String,
        val width: Int,
        val height: Int
) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<YouTubeItemThumbnail> = object : Parcelable.Creator<YouTubeItemThumbnail> {
            override fun createFromParcel(source: Parcel): YouTubeItemThumbnail = YouTubeItemThumbnail(source)
            override fun newArray(size: Int): Array<YouTubeItemThumbnail?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readInt(), source.readInt())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(url)
        dest?.writeInt(width)
        dest?.writeInt(height)
    }

}