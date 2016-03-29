package common.base;

import android.content.Context;
import android.database.ContentObserver;

/**
 * Created by shade_000 on 3/26/2016.
 */
public abstract class AbstractObservingLoader<T> extends AbstractLoader<T> {
    protected final DisableableContentObserver mObserver;
    private boolean mIsRegistered;

    public AbstractObservingLoader(Context context) {
        super(context);
        mObserver = new DisableableContentObserver(new ForceLoadContentObserver());
    }

    @Override
    protected void onStartLoading() {
        mObserver.setEnabled(true);
        super.onStartLoading();
    }

    @Override
    protected void onAbandon() {
        mObserver.setEnabled(false);
        unregisterObserver(mObserver);
        mIsRegistered = false;
    }

    @Override
    protected void onReset() {
        mObserver.setEnabled(false);
        unregisterObserver(mObserver);
        mIsRegistered = false;
        super.onReset();
    }

    @Override
    protected void onNewDataDelivered(T data) {
        if (!mIsRegistered) {
            mIsRegistered = true;
            registerObserver(mObserver);
        }
    }

    protected abstract void registerObserver(ContentObserver observer);
    protected abstract void unregisterObserver(ContentObserver observer);
}

