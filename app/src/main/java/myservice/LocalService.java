package myservice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.hjl.myprocess.IMyAidlInterface;

public class LocalService extends Service {
    private MyBinder myBinder;
    private MyServiceConnection connection;

    public LocalService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (myBinder == null) {
            myBinder = new MyBinder();
        }
        connection = new MyServiceConnection();

        new Thread(){
            @Override
            public void run() {
                super.run();
                long time = System.currentTimeMillis();
                while (true)

                    if (System.currentTimeMillis() - time > 60000) {
                        Log.e("TAG", "TAG" + System.currentTimeMillis());
                        time = System.currentTimeMillis();
                    }
            }
        }.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.bindService(new Intent(LocalService.this, RemoteService.class), connection, Context.BIND_IMPORTANT);
//        Notification.Builder notification = new Notification.Builder(this);
//        PendingIntent pendingIntent = PendingIntent.getService(this,0,intent,0);
//        notification.setL
        return START_STICKY;
    }

    class MyBinder extends IMyAidlInterface.Stub {

        @Override
        public String getProcessName() throws RemoteException {
            return "LocalService";
        }
    }

    class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("Info", "service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LocalService.this.startService(new Intent(LocalService.this, RemoteService.class));
            LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class), connection, Context.BIND_IMPORTANT);
        }
    }
}
