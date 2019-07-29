package com.djavid.bitcoinrate.util

import io.reactivex.CompletableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


object RxUtils {

    fun applyCompletableSchedulers(): CompletableTransformer {
        return { tObservable ->
            tObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> applySingleSchedulers(): SingleTransformer<T, T> {
        return { tObservable ->
            tObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> applyObservableSchedulers(): ObservableTransformer<T, T> {
        return { tObservable ->
            tObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> applyOpBeforeAndAfter(
            before: Runnable, after: Runnable): ObservableTransformer<T, T> {
        return { tObservable -> tObservable.doOnComplete(Action { after.run() }).doOnSubscribe({ t -> before.run() }) }
    }

    fun <T> applyOpAfter(after: Runnable): ObservableTransformer<T, T> {
        return { tObservable -> tObservable.doOnComplete(Action { after.run() }) }
    }

    fun <T> applyShortDelay(): ObservableTransformer<T, T> {
        return { tObservable ->
            tObservable
                    .delay(0, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
        }
    }
}
