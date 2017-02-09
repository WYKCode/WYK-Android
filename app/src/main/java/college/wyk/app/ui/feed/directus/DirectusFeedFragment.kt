package college.wyk.app.ui.feed.directus

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import college.wyk.app.R
import college.wyk.app.commons.OnScrollListener
import college.wyk.app.commons.SubscribedFragment
import college.wyk.app.commons.inflate
import college.wyk.app.model.directus.DirectusObserver
import college.wyk.app.model.directus.FeedStack
import college.wyk.app.ui.feed.directus.adapter.DirectusPostAdapter
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator
import kotlinx.android.synthetic.main.fragment_feed_page.*
import org.parceler.Parcels
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class DirectusFeedFragment : SubscribedFragment() {

    companion object {
        private val FEED_STACK = "feed_stack"
    }

    private var stack: FeedStack? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_feed_page)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val linearLayout = LinearLayoutManager(context)

        if (post_list != null) { // to let Instant Run work properly

            post_list.apply {
                setHasFixedSize(true)
                layoutManager = linearLayout
                clearOnScrollListeners()
                addOnScrollListener(OnScrollListener({ requestPosts() }, linearLayout))

                // use header decorator
                addItemDecoration(MaterialViewPagerHeaderDecorator())
            }

            if (post_list.adapter == null) {
                post_list.adapter = DirectusPostAdapter()
            }

            swipe_refresh_layout.setOnRefreshListener { requestPosts(clear = true) }
            swipe_refresh_layout.setProgressBackgroundColorSchemeResource(R.color.school)
            swipe_refresh_layout.setColorSchemeResources(R.color.md_white_1000)

        }

        if (savedInstanceState != null) {
            this.stack = Parcels.unwrap(savedInstanceState.getParcelable(FEED_STACK))
            if (this.stack != null) {
                if (post_list != null) {
                    (post_list.adapter as DirectusPostAdapter).apply {
                        setPosts(stack!!.items)
                        markEnd()
                    }
                }
            }
        } else {
            requestPosts() // for the first time
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (post_list != null) {
            val posts = (post_list.adapter as DirectusPostAdapter).getPosts()
            if (stack != null && posts.isNotEmpty()) {
                outState.putParcelable(FEED_STACK, Parcels.wrap(stack!!.copy(posts)))
            }
        }
    }

    private fun requestPosts(clear: Boolean = false) {
        if (!clear && this.stack?.noMoreUpdates ?: false) return
        if (clear) this.stack?.currentPage = -1
        val subscription = DirectusObserver
                .pullFeed(currentPage = (stack?.currentPage?.plus(1)) ?: 0)
                .subscribeOn(Schedulers.io()) // we want to request posts on the I/O thread
                .observeOn(AndroidSchedulers.mainThread()) // though, we want to handle posts on the main thread
                .subscribe(
                    { retrievedStack ->
                        if (post_list == null) return@subscribe

                        swipe_refresh_layout.isRefreshing = false

                        stack = retrievedStack

                        (post_list.adapter as DirectusPostAdapter).apply {
                            if (clear) {
                                setPosts(retrievedStack.items)
                                notifyDataSetChanged()
                            } else addPosts(retrievedStack.items)
                        }

                        if (retrievedStack.noMoreUpdates) {
                            (post_list.adapter as DirectusPostAdapter).markEnd()
                        }

                    },
                    { e ->
                        swipe_refresh_layout?.isRefreshing = false

                        Log.e("WYK", e.message)
                        Snackbar.make(post_list, e.message ?: "", Snackbar.LENGTH_LONG).show()
                    }
            )
        super.subscriptions.add(subscription)
    }

}