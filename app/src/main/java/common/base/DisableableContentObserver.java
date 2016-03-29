package common.base;

import android.database.ContentObserver;
import android.os.Handler;

/**
 * Created by shade_000 on 3/26/2016.
 */
public class DisableableContentObserver extends ContentObserver {
    private final ContentObserver mWrappedObserver;
    private boolean mIsEnabled = true;

    public DisableableContentObserver(ContentObserver wrappedObserver) {
        super(new Handler());
        mWrappedObserver = wrappedObserver;
    }

    @Override
    public void onChange(boolean selfChange) {
        if (mIsEnabled) {
            mWrappedObserver.onChange(selfChange);
        }
    }

    public void setEnabled(boolean isEnabled) {
        mIsEnabled = isEnabled;
    }
}
