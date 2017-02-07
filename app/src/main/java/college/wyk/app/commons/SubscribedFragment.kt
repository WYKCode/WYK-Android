package college.wyk.app.commons

import android.support.v4.app.Fragment
import rx.subscriptions.CompositeSubscription

open class SubscribedFragment() : Fragment() {

    protected var subscriptions = CompositeSubscription()

    override fun onResume() {
        super.onResume()
        subscriptions = CompositeSubscription()
    }

    override fun onPause() {
        super.onPause()
        if (!subscriptions.isUnsubscribed) subscriptions.unsubscribe()
        subscriptions.clear()
    }

}