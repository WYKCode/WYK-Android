package college.wyk.app.ui.publications

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import college.wyk.app.R
import college.wyk.app.commons.*
import college.wyk.app.commons.adapter.AdapterBindable
import college.wyk.app.commons.adapter.ViewType
import college.wyk.app.commons.adapter.ViewTypeDelegateAdapter
import college.wyk.app.commons.gallery.CardAdapterHelper
import college.wyk.app.commons.gallery.CardScaleHelper
import college.wyk.app.commons.gallery.GalleryBackground
import college.wyk.app.model.directus.Directus
import college.wyk.app.model.directus.DirectusObserver
import college.wyk.app.model.directus.Publication
import college.wyk.app.model.directus.PublicationStack
import college.wyk.app.ui.feed.sns.adapter.DelegatedAdapter
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.coolerfall.download.DownloadCallback
import kotlinx.android.synthetic.main.fragment_publications.*
import kotlinx.android.synthetic.main.fragment_publications.view.*
import kotlinx.android.synthetic.main.item_publication.view.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import kotlin.properties.Delegates

class PublicationFragment : SubscribedFragment() {

    lateinit var helper: CardScaleHelper
    var blurRunnable: Runnable? = null
    var lastPosition: Int = -1

    private var stack: PublicationStack? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_publications, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = "Latest Publications"
        landingActivity.updateToolbar(toolbar)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        publication_gallery.layoutManager = linearLayoutManager
        publication_gallery.adapter = PublicationAdapter(CardAdapterHelper())

        helper = CardScaleHelper()
        helper.attachToRecyclerView(publication_gallery)

        requestPublications()

        blur_view.setImageDrawable(ColorDrawable(resources.getColor(R.color.container_background)))

        publication_gallery.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    notifyBackgroundChange()
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()

        blur_view?.removeCallbacks(blurRunnable)
    }

    private fun notifyBackgroundChange() {
        if (publication_gallery == null || helper.currentItemPos + 1 >= publication_gallery.adapter.itemCount) return
        if (lastPosition === helper.currentItemPos || blur_view == null) return
        lastPosition = helper.currentItemPos

        blur_view.removeCallbacks(blurRunnable)
        blurRunnable = Runnable {
            stack?.let {
                if (helper.currentItemPos + 1 >= publication_gallery.adapter.itemCount) return@Runnable
                val publication = it.items[helper.currentItemPos]
                Glide.with(this).load(Directus.mediaUrl(publication.cover.name)).asBitmap().into(createTarget())
            }
        }
        blur_view.postDelayed(blurRunnable, 500)
    }

    private fun createTarget() = object : SimpleTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
            GalleryBackground.crossfade(blur_view, GalleryBackground.blur(blur_view.context, resource, 10f))
        }
    }

    private fun requestPublications() {
        val subscription = DirectusObserver
                .pullPublications()
                .subscribeOn(Schedulers.io()) // we want to request posts on the I/O thread
                .observeOn(AndroidSchedulers.mainThread()) // though, we want to handle posts on the main thread
                .subscribe(
                        { retrievedStack ->
                            stack = retrievedStack
                            if (publication_gallery == null) return@subscribe

                            (publication_gallery.adapter as PublicationAdapter).apply {
                                setPublications(retrievedStack.items)
                            }

                            notifyBackgroundChange()
                        },
                        { e ->
                            Log.e("WYK", e.message)
                            Log.e("WYK", e.stackTrace.joinToString(separator = "\n"))
                            Snackbar.make(root_view, e.message ?: "", Snackbar.LENGTH_LONG).show()
                        }
                )
        super.subscriptions.add(subscription)
    }

}

class PublicationAdapter(val helper: CardAdapterHelper) : DelegatedAdapter() {

    init {
        delegateAdapters.put(ViewType.publication_item.ordinal, PublicationDelegateAdapter())
    }

    fun setPublications(publications: List<Publication>) {
        items.clear()
        items.addAll(publications)
        items.add(Publication(1, -1, "fake", "fake", college.wyk.app.model.directus.File("fake", "fake", "fake", "fake"), "fake"))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = super.onCreateViewHolder(parent, viewType)
        helper.onCreateViewHolder(parent, viewHolder.itemView)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        helper.onBindViewHolder(holder.itemView, position, itemCount)
        super.onBindViewHolder(holder, position)
    }

}

class PublicationDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val holder = PublicationViewHolder(parent)
        holder.itemView.setOnClickListener {

            val item = holder.boundItem

            val folder = Cache.publicationFolder()

            fun openPDF(filename: String) {
                val file = File(folder, filename)
                val contentUri = FileProvider.getUriForFile(app, "college.wyk.app.fileprovider", file)

                if (contentUri != null) {

                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
                    intent.setDataAndType(contentUri, app.contentResolver.getType(contentUri))
                    intent.putExtra(Intent.EXTRA_STREAM, contentUri)

                    try {
                        app.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        newThemedDialog(parent.context).title("No PDF viewer found").content("A PDF viewer is required to view the publications.")
                                .positiveText("Install Google's PDF Viewer")
                                .negativeText("Cancel")
                                .onNegative { dialog, action -> dialog.cancel() }
                                .onPositive { dialog, action ->
                                    val packageName = "com.google.android.apps.pdfviewer"
                                    try {
                                        app.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)))
                                    } catch (e: ActivityNotFoundException) {
                                        app.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)))
                                    }
                                }
                                .show()
                    }

                }
            }

            fun cancelDownload(id: Int) {
                Log.i("WYK", "Cancelled $id")
                Cache.downloadManager.cancel(id)
            }

            val filename = item.id.toString() + ".pdf"

            val localFile = Cache.publicationFile(filename)
            if (localFile.exists()) {
                openPDF(filename)
                return@setOnClickListener
            }

            var dialog: MaterialDialog? = null

            val downloadId = Cache.download(folder, filename, item.url, object : DownloadCallback() {

                override fun onFailure(downloadId: Int, statusCode: Int, errMsg: String?) {
                    Log.i("WYK", "$downloadId / $statusCode / $errMsg")
                }

                override fun onProgress(downloadId: Int, bytesWritten: Long, totalBytes: Long) {
                    dialog?.apply {
                        val percentage = (bytesWritten.toDouble() / totalBytes.toDouble() * 100.0).toInt()
                        setProgress(percentage)
                        setProgressNumberFormat(bytesWritten.toHumanReadableSize() + " / " + totalBytes.toHumanReadableSize())
                    }
                }

                override fun onSuccess(downloadId: Int, filePath: String) {
                    dialog?.dismiss()
                    Log.i("WYK", filePath)
                    openPDF(filename)
                    parent.rootView.publication_gallery.adapter.notifyDataSetChanged()
                }

            })

            dialog = newThemedDialog(parent.context).title("Downloading...").content("Please wait.").progress(false, 100, true)
                    .positiveText("Background")
                    .negativeText("Cancel")
                    .progressNumberFormat("Initializing")
                    .onNegative { dialog, action -> dialog.cancel() }
                    .onPositive { dialog, action -> }
                    .cancelListener { cancelDownload(downloadId) }
                    .canceledOnTouchOutside(false)
                    .show()

        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: AdapterBindable) {
        holder as PublicationViewHolder
        holder.bind(item as Publication)
    }

    class PublicationViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_publication)) {

        var boundItem: Publication by Delegates.notNull()

        fun bind(item: Publication) = with(itemView) {
            boundItem = item
            Log.i("WYK", "Set $item.title")

            if (item.id == -1) {
                publication_cover.visibility = View.GONE
                publication_fake_cover.visibility = View.VISIBLE
                publication_card.setCardBackgroundColor(resources.getColor(R.color.publications))
                return@with
            }

            publication_cover.loadWithGlide(Directus.mediaUrl(item.cover.name))
            if (!Cache.publicationFile(item.id.toString() + ".pdf").exists()) publication_cover.isLabelVisual = false
        }

    }

}