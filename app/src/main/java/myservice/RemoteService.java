package myservice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.hjl.myprocess.IMyAidlInterface;

public class RemoteService extends Service {
    private RemoteBinder remoteBinder;
    private RemoteServiceConnection connection;

    public RemoteService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (remoteBinder == null) {
            remoteBinder = new RemoteBinder();
        }
        connection = new RemoteServiceConnection();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return remoteBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.bindService(new Intent(this, LocalService.class), connection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    class RemoteBinder extends IMyAidlInterface.Stub {
        @Override
        public String getProcessName() throws RemoteException {
            return "remoteService";
        }
    }

    class RemoteServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            RemoteService.this.startService(new Intent(RemoteService.this, LocalService.class));
            RemoteService.this.bindService(new Intent(RemoteService.this, LocalService.class), connection, Context.BIND_IMPORTANT);
        }
    }
}
