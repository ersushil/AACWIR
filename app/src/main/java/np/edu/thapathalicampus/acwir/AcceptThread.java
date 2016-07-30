package np.edu.thapathalicampus.acwir;
/**
 * Created by ThapaKAZZI on 10/29/2015.
 */
import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

class AcceptThread extends Thread {
    private BluetoothServerSocket mServerSocket;
    private BluetoothSocket mBluetoothSocket = null;
    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final Handler mHandler;

    public AcceptThread(Handler handler) {
        mHandler = handler;
        try {
            mServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Bluetooth Demo",MainActivity.APP_UUID);
        } catch (IOException e) {
            try {
                mServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Bluetooth Demo",MainActivity.APP_UUIDmodule);
            } catch (Exception ex) {

            }
        }
    }

    public void run() {
        while (true) {
            try {
                mBluetoothSocket = mServerSocket.accept();
                manageConnectedSocket();
                mServerSocket.close();
                break;
            } catch (IOException e1) {
                if (mBluetoothSocket != null) {
                    try {
                        mServerSocket.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    private void manageConnectedSocket() {
        ConnectionThread conn = new ConnectionThread(mBluetoothSocket, mHandler);
        mHandler.obtainMessage(MainActivity.SOCKET_CONNECTED, conn).sendToTarget();
        conn.start();
    }

    public void cancel() {
        try {
            if (null != mServerSocket)
                mServerSocket.close();
        } catch (IOException e) {
        }
    }
}
