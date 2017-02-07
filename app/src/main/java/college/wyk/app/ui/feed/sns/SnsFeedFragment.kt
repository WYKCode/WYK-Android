package college.wyk.app.ui.feed.sns

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import college.wyk.app.R
import college.wyk.app.WykApplication
import college.wyk.app.commons.OnScrollListener
import college.wyk.app.commons.SubscribedFragment
import college.wyk.app.commons.inflate
import college.wyk.app.model.sns.SnsPostManager
import college.wyk.app.model.sns.SnsStack
import college.wyk.app.ui.feed.sns.adapter.SnsPostAdapter
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator
import kotlinx.android.synthetic.main.fragment_feed_page.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

const val MILLIS_IN_A_MONTH = 2628000000L

class SnsFeedFragment : SubscribedFragment() {

    companion object {

        fun newInstance(id: String) = SnsFeedFragment().apply {
            val bundle = Bundle(1)
            bundle.putString("id", id)
            arguments = bundle
        }

    }

    lateinit var id: String

    private var stack: SnsStack? = null

    private val postManager by lazy { SnsPostManager() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_feed_page)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        id = arguments.getString("id")

        val linearLayout = LinearLayoutManager(context)

        post_list.apply {
            setHasFixedSize(true)
            layoutManager = linearLayout
            clearOnScrollListeners()
            addOnScrollListener(OnScrollListener({ requestPosts() }, linearLayout))

            // use header decorator
            addItemDecoration(MaterialViewPagerHeaderDecorator())
        }

        if (post_list.adapter == null) {
            post_list.adapter = SnsPostAdapter()
        }

        swipe_refresh_layout.setOnRefreshListener { requestPosts(clear = true) }
        swipe_refresh_layout.setProgressBackgroundColorSchemeResource(if (id == "SA") R.color.sa else R.color.ma)
        swipe_refresh_layout.setColorSchemeResources(R.color.md_white_1000)

        if (savedInstanceState != null) {
            val instanceState = WykApplication.instance.snsStacks[id]
            if (instanceState != null && instanceState.items.size > 0) {
                (post_list.adapter as SnsPostAdapter).apply {
                    setPosts(instanceState.items)
                    markEnd()
                }
            } else {
                requestPosts(clear = true)
            }
            WykApplication.instance.snsStacks.remove(id)
        } else {
            requestPosts() // for the first time
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // since the size for this parcelable is too large... let's just save it into the Application singleton
        this.stack?.let { WykApplication.instance.snsStacks[id] = it }
    }

    private fun requestPosts(clear: Boolean = false) {
        if (!clear && this.stack?.noMoreUpdates ?: false) return
        if (clear) this.stack?.since = Date().time
        val subscription = postManager
                .pullStack(id, sinceMillis = (this.stack?.since ?: Date().time) - (MILLIS_IN_A_MONTH * 6))
                .subscribeOn(Schedulers.io()) // we want to request posts on the I/O thread
                .observeOn(AndroidSchedulers.mainThread()) // though, we want to handle posts on the main thread
                .subscribe(
                        { retrievedState ->
                            if (post_list == null) return@subscribe

                            swipe_refresh_layout.isRefreshing = false

                            if (retrievedState.items.size == 0) {
                                Log.i("WYK", retrievedState.items.size.toString())
                                Log.i("WYK", (this.stack?.items?.size ?: -1).toString())
                                (post_list.adapter as SnsPostAdapter).markEnd()
                                this.stack?.noMoreUpdates = true
                            } else {
                                this.stack = retrievedState
                                (post_list.adapter as SnsPostAdapter).apply {
                                    retrievedState.items.let {
                                        if (clear) {
                                            setPosts(it)
                                            notifyDataSetChanged()
                                        } else addPosts(it)
                                    }
                                }
                                Log.i("WYK", retrievedState.items.size.toString())
                            }

                        },
                        { e ->
                            swipe_refresh_layout?.isRefreshing = false

                            Log.e("WYK", e.message)
                            Log.e("WYK", e.stackTrace.joinToString(separator = "\n"))
                            Snackbar.make(post_list, e.message ?: "", Snackbar.LENGTH_LONG).show()
                        }
                )
        super.subscriptions.add(subscription)
    }

}