package com.example.shade_000.datahandler.data.source.network;

import android.app.Service;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.example.shade_000.datahandler.data.models.User;
import com.example.shade_000.datahandler.data.models.eventBuss.UserErrorMessage;
import com.example.shade_000.datahandler.data.models.volley.GsonRequest;
import com.example.shade_000.datahandler.data.source.local.DatabaseContract;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import common.constants.EnumConstants;
import util.NetworkUtils;

import static common.constants.EnumConstants.*;

/**
 * Created by shade_000 on 12/04/2016.
 */
public class NetworkBackgroundProcessingService extends Service implements TaskFinishListener{

    //region Fields
    private ExecutorService executorService;
    private final String TAG = getClass().getSimpleName();
    //endregion

    //region Constructors

    public NetworkBackgroundProcessingService(){
        Log.d(TAG, "NetworkBackgroundProcessingService ==> creating new Pool");
        executorService = NetworkThreadPoolExecutor.getDefault(this);
    }

    //endregion Constructors

    //region Service Overrides

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startProcessingRequest(intent);
        Log.d(TAG, "executorService activeCount: " + ((NetworkThreadPoolExecutor) executorService).getActiveCount() +
                "\nexecutorService task count: " + ((NetworkThreadPoolExecutor) executorService).getTaskCount() +
                "\nexecutorService queue size: " + ((NetworkThreadPoolExecutor) executorService).getQueue().size() +
                "\nexecutorService pool size: " + ((NetworkThreadPoolExecutor) executorService).getPoolSize()
        );
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskFinished(Runnable r, Throwable t) {
        if(t != null)
        ((RequestProcessor)r).onError(new VolleyError(t));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown())
            executorService.shutdown();
    }

    //endregion Service Overrides

    //region Methods

    private void startProcessingRequest(Intent intent){
        Log.d(TAG, "startProcessorForRequest(Intent intent) started");
        NetworkOperations operationKey = NetworkOperations.getType(intent.getExtras().getInt(NetworkUtils.OPERATION_KEY));
        Runnable processorToRun = getProcessor(operationKey);
        if (processorToRun != null) {
            executorService.execute(processorToRun);
        }
        Log.d(TAG, "startProcessorForRequest(Intent intent) finished");
    }

    private Runnable getProcessor(final NetworkOperations operationKey){
        switch (operationKey){
            case Get_Users:
                return new RequestProcessor<ArrayList<User>>(operationKey.getId(),getApplicationContext(),NetworkUtils.TIMEOUT,NetworkUtils.TIMEUNIT) {
                    @Override
                    public Request getRequest(RequestFuture<ArrayList<User>> requestFuture) {
                        Type type = new TypeToken<ArrayList<User>>(){}.getType();
                        return  new GsonRequest<>(
                                NetworkUtils.getUrl(operationKey.getId()),
                                type,
                                null,
                                null,
                                Request.Method.POST,
                                requestFuture,
                                requestFuture
                        );
                    }

                    @Override
                    protected void onError(Exception e) {
                        EventBus.getDefault().postSticky(new UserErrorMessage(NetworkUtils.USER_NETWORK_ERROR,new VolleyError(e)));
                    }

                    @Override
                    protected void onResult(ArrayList<User> result) {
                        ArrayList<ContentProviderOperation> ops =  new ArrayList<>();
                        for (User user : result) {
                            ops.add(ContentProviderOperation.newInsert(DatabaseContract.User.buildTasksUri())
                                    .withValue(DatabaseContract.User.COLUMN_NAME_NAME, user.getName())
                                    .withValue(DatabaseContract.User.COLUMN_NAME_EMAIL, user.getEmail())
                                    .withValue(DatabaseContract.User.COLUMN_NAME_PHONE,user.getPhone())
                                    .withValue(DatabaseContract.User.COLUMN_NAME_ALIAS,user.getAlias())
                                    .withValue(BaseColumns._ID,user.getId())
                                    .build());
                        }
                        try {
                            ContentResolver resolver = getContentResolver();
                            resolver.applyBatch(DatabaseContract.CONTENT_AUTHORITY, ops);
                        } catch (OperationApplicationException | RemoteException e) {
                            Log.e("wemonit", "cannot apply batch: " + e.getLocalizedMessage(), e);
                        }
                    }
                };
            default:
                throw new RuntimeException("Processor not implemented");
        }
    }

    //endregion
}
