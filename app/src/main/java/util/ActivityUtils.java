package util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


/**
 * Created by shade_000 on 09/04/2016.
 */
public class ActivityUtils {
    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment,
                                             int frameId,
                                             boolean saveToBackStack,
                                             String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment, tag);
        if(saveToBackStack){
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public static void removeFragmentFromActivity(@NonNull FragmentManager fragmentManager,
                                                  @NonNull Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    public static void replaceFragmentFromActivity(@NonNull FragmentManager fragmentManager,
                                                   @NonNull Fragment oldFragment,
                                                   @NonNull Fragment newFragment,
                                                   int frameId,
                                                   boolean saveToBackStack,
                                                   String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(oldFragment);
        transaction.add(frameId, newFragment, tag);
        if(saveToBackStack){
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }


    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addNonUIFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                                  @NonNull Fragment nonUIFragment, String fragmentTag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(nonUIFragment, fragmentTag);
        transaction.commit();
    }
}
