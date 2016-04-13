package com.example.shade_000.datahandler.data.source.network;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.shade_000.datahandler.data.models.volley.GsonRequest;

import java.util.concurrent.ExecutorService;

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
        Runnable processorToRun = getProcessor(operationKey, executorService);
        if (processorToRun != null) {
            executorService.execute(processorToRun);
        }
        Log.d(TAG, "startProcessorForRequest(Intent intent) finished");
    }

    private Runnable getProcessor(NetworkOperations operationKey, ExecutorService executorService){
        switch (operationKey){
            case Get_Users:
                return new Runnable() {
                    @Override
                    public void run() {
                    }
                };
            default:
                throw new RuntimeException("Processor not implemented");
        }
    }

    //endregion
}
