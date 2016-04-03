package common.base;

import android.content.Context;
import android.database.ContentObserver;
import android.util.Log;

/**
 * Created by shade_000 on 3/26/2016.
 */
public abstract class AbstractObservingLoader<T> extends AbstractLoader<T> {
    //region Fields
    private final String TAG = getClass().getSimpleName();
    protected final DisableableContentObserver mObserver;
    private boolean mIsRegistered;
    //endregion

    //region Constructor
    public AbstractObservingLoader(Context context) {
        super(context);
        mObserver = new DisableableContentObserver(new ForceLoadContentObserver());
    }
    //endregion

    //region Overrides
    @Override
    protected void onStartLoading() {
        mObserver.setEnabled(true);
        super.onStartLoading();
    }

    @Override
    protected void onAbandon() {
        prepareForUnregister();
    }

    @Override
    protected void onReset() {
        prepareForUnregister();
        super.onReset();
    }

    @Override
    protected void onNewDataDelivered(T data) {
        if (!mIsRegistered) {
            mIsRegistered = true;
            registerObserver(mObserver);
        }
    }
    //endregion

    //region Methods

    private void prepareForUnregister(){
        if(mIsRegistered){
            mIsRegistered = false;
            mObserver.setEnabled(false);
            unregisterObserver(mObserver);
            Log.i(TAG,"Prepare for unregister observer");
        }
    }

    //endregion

    //region Abstract Methods
    protected abstract void registerObserver(ContentObserver observer);
    protected abstract void unregisterObserver(ContentObserver observer);
    //endregion
}

