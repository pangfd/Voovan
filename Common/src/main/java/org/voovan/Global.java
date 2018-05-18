package org.voovan;

import org.voovan.tools.TFile;
import org.voovan.tools.TObject;
import org.voovan.tools.TProperties;
import org.voovan.tools.hashwheeltimer.HashWheelTimer;
import org.voovan.tools.task.TaskManager;
import org.voovan.tools.threadpool.ThreadPool;

import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 全局对象
 *
 * @author helyho
 *
 * Voovan Framework.
 * WebSite: https://github.com/helyho/Voovan
 * Licence: Apache v2 License
 */
public class Global {

    public static String NAME = "Voovan";

    private static ThreadPoolExecutor threadPool;
    private static HashWheelTimer hashWheelTimer;
    private static TaskManager taskManager;

    public static volatile Boolean NO_HEAP_MANUAL_RELEASE = getNoHeapManualRelease();

    /**
     * 非对内存是否采用手工释放
     * @return true: 收工释放, false: JVM自动释放
     */
    private  static boolean getNoHeapManualRelease() {
        if(NO_HEAP_MANUAL_RELEASE == null) {
            boolean value = false;
            value = TProperties.getBoolean("framework", "NoHeapManualRelease");
            NO_HEAP_MANUAL_RELEASE = TObject.nullDefault(value, false);
            System.out.println("[SYSTEM] NoHeap Manual Release: " + NO_HEAP_MANUAL_RELEASE);
        }

        return NO_HEAP_MANUAL_RELEASE;
    }

    /**
     * 返回公用线程池
     * @return 公用线程池
     */
    public synchronized static ThreadPoolExecutor getThreadPool(){
        if(threadPool==null || threadPool.isShutdown()){
            threadPool = ThreadPool.getNewThreadPool();
        }

        return threadPool;
    }

    /**
     * 获取一个全局的秒定时器
     *      60个槽位, 每个槽位步长1s
     * @return HashWheelTimer对象
     */
    public synchronized static HashWheelTimer getHashWheelTimer(){
        if(hashWheelTimer == null) {
            hashWheelTimer = new HashWheelTimer(60, 1000);
            hashWheelTimer.rotate();
        }

        return hashWheelTimer;
    }

    /**
     * 获取一个全局的秒定时器
     *      60个槽位, 每个槽位步长1ms
     * @return HashWheelTimer对象
     */
    public synchronized static TaskManager getTaskManager(){
        if(taskManager == null) {
            taskManager = new TaskManager();
            taskManager.scanTask();
        }

        return taskManager;
    }

    /**
     * 获取当前 Voovan 版本号
     * @return Voovan 版本号
     */
    public static String getVersion(){
        return "3.2.0";
    }
}
