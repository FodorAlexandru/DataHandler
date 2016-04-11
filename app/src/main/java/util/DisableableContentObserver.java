package util;

import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

/**
 * Created by shade_000 on 3/26/2016.
 */
public class DisableableContentObserver extends ContentObserver {
    private final ContentObserver mWrappedObserver;
    private boolean mIsEnabled = true;
    private final String TAG = getClass().getSimpleName();

    public DisableableContentObserver(ContentObserver wrappedObserver) {
        super(new Handler());
        mWrappedObserver = wrappedObserver;
    }

    @Override
    public void onChange(boolean selfChange) {
        if (mIsEnabled) {
            mWrappedObserver.onChange(selfChange);
            Log.i(TAG, "detected change:" + selfChange);
        }
    }

    public void setEnabled(boolean isEnabled) {
        mIsEnabled = isEnabled;
        Log.i(TAG, "Observer enable state:" + isEnabled);
    }
}
