package college.wyk.app.commons

import android.support.v7.app.AppCompatActivity
import rx.subscriptions.CompositeSubscription

open class SubscribedActivity() : AppCompatActivity() {

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