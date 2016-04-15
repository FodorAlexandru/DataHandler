package com.example.shade_000.datahandler.data.source.network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by shade_000 on 13/04/2016.
 */
public class NetworkThreadPoolExecutor extends ThreadPoolExecutor {

    //region Fields
    private final TaskFinishListener taskFinishListener;
    //endregion

    //region Constructor

    private NetworkThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,TaskFinishListener taskFinishListener) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.taskFinishListener = taskFinishListener;
    }

    //endregion Constructor

    //region Methods

    public static NetworkThreadPoolExecutor getDefault(TaskFinishListener taskFinishListener){
        return new NetworkThreadPoolExecutor(0,Integer.MAX_VALUE,60L,TimeUnit.SECONDS,new SynchronousQueue<Runnable>(),taskFinishListener);
    }

    //endregion Methods

    //region ThreadPoolExecutor Overrides

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);

        if(taskFinishListener!=null){
            taskFinishListener.onTaskFinished(r,t);
        }
    }

    //endregion ThreadPoolExecutor Overides
}
