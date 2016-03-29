package common.base;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by shade_000 on 3/25/2016.
 */
public abstract class AbstractLoader<T> extends AsyncTaskLoader<T> {
    T mResult;

    public AbstractLoader(Context context) {
        super(context);
    }

    /**
     * If loader is in reset state releases newly delivered data
     * The data from cache is being swapped and based on the state will either deliver the results
     * Also old resources are being released from previously cached data
     * @param result
     */
    @Override
    public void deliverResult(T result) {
        if (isReset()) {
            releaseResources(result);
            return;
        }

        T oldResult = mResult;
        mResult = result;

        if (isStarted()) {
            if (oldResult != result) {
                onNewDataDelivered(result);
            }
            super.deliverResult(result);
        }

        if (oldResult != result && oldResult != null) {
            releaseResources(oldResult);
        }
    }


    /**
     * When the loader is canceled marks that the data should not be delivered on the main thread and also cancel resources if needed
     * @param result
     */
    @Override
    public void onCanceled(T result) {
        super.onCanceled(result);
        releaseResources(result);
    }

    /**
     * Ensures the loader is stopped by stopping loading and clears cache and releases resources
     */
    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        releaseResources(mResult);
        mResult = null;
    }

    /**
     * Delivers either the cache or in case it is empty or the loader was notified for data change forces new load
     */
    @Override
    protected void onStartLoading() {
        if (mResult != null) {
            deliverResult(mResult);
        }
        if (takeContentChanged() || mResult == null) {
            forceLoad();
        }
    }

    /**
     *  Stops the data that was loaded to deliver new results
     */
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    /**
     * Resources should be released as the loader is dispatched
     * @param result
     */
    protected void releaseResources(T result) {
    }

    /**
     * Useful for registering Content Observer after the first data is delivered, not after the first data is loaded
     * @param data
     */
    protected void onNewDataDelivered(T data) {
    }
}
