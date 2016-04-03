package common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shade_000 on 3/20/2016.
 */
public abstract class BaseFragment extends Fragment{

    //region Overrides
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(),container,false);
    }
    //endregion

    //region Abstract Methods

    public abstract int getLayoutId();

    //endregion
}
