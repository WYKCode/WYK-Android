package college.wyk.app.commons.adapter

interface AdapterBindable {

    fun viewType(): ViewType

}

enum class ViewType {

    blank,
    loading,
    directus_item,
    facebook_item,
    instagram_item,
    youtube_item,
    publication_item,
    timetable_item

}