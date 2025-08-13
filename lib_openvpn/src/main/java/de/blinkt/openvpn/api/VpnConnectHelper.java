package de.blinkt.openvpn.api;

import static de.blinkt.openvpn.api.ExternalOpenVPNService.EXTRA_PASSWORD;
import static de.blinkt.openvpn.api.ExternalOpenVPNService.EXTRA_USER_NAME;
import static de.blinkt.openvpn.core.ConnectionStatus.LEVEL_CONNECTED;
import static de.blinkt.openvpn.core.ConnectionStatus.LEVEL_VPNPAUSED;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.spongycastle.util.MainHandler;

import de.blinkt.openvpn.core.ConnectionStatus;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: VpnConnectHelper
 * Author: Victor
 * Date: 2025/8/13 8:47
 * Description:
 * -----------------------------------------------------------------
 */
public class VpnConnectHelper {
    private String TAG = getClass().getSimpleName();

    private IOpenVPNAPIService mService = null;

    private Context mContext;
    private IOpenVPNStatusCallback mIOpenVPNStatusCallback;

    public ConnectionStatus getConnectionStatus() {
        return mConnectionStatus;
    }

    private ConnectionStatus mConnectionStatus = ConnectionStatus.UNKNOWN_LEVEL;

    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IOpenVPNAPIService.Stub.asInterface(service);
            try {
                // Request permission to use the API
                Intent intent = mService.prepare(mContext.getPackageName());
                /*if (intent != null) {
                    mActivity.startActivityForResult(i, REGISTER_CALL_BACK_CODE);
                } else {
                    onActivityResult(REGISTER_CALL_BACK_CODE, Activity.RESULT_OK, null);
                }*/

                mService.registerStatusCallback(mVpnStatusCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.d(TAG, "openvpn service connection failed: " + e.getMessage());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    private final IOpenVPNStatusCallback mVpnStatusCallback = new IOpenVPNStatusCallback.Stub() {
        @Override
        public void newStatus(String uuid, String state, String message, String level) throws RemoteException {
            Log.d(TAG, "newStatus()-state = " + state);
            onVpnConnectState(uuid,state,message,level);
        }
    };

    public VpnConnectHelper(Context context,IOpenVPNStatusCallback callback) {
        mContext = context;
        mIOpenVPNStatusCallback = callback;
        init();
    }

    private void init() {
        bindVpnApiService();
    }

    private void bindVpnApiService() {
        try {
            Intent vpnApiService = new Intent(IOpenVPNAPIService.class.getName());
            vpnApiService.setPackage(mContext.getPackageName());
            mContext.bindService(vpnApiService, mConnection, AppCompatActivity.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onVpnConnectState(String uuid, String state, String message, String level) {
        Log.i(TAG,"onVpnConnectState()-level = " + level);
        mConnectionStatus = ConnectionStatus.fromString(level);

        MainHandler.get().runMainThread(() -> {
            try {
                mIOpenVPNStatusCallback.newStatus(uuid,state,message,level);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

        switch (ConnectionStatus.fromString(level)) {
            case LEVEL_START:
                // 开始连接
                break;
            case LEVEL_CONNECTED:
                // 已连接
                break;
            case LEVEL_VPNPAUSED:
                // 暂停
                break;
            case LEVEL_NONETWORK:
                // 无网络
                break;
            case LEVEL_CONNECTING_SERVER_REPLIED:
                // 服务器答应
                break;
            case LEVEL_CONNECTING_NO_SERVER_REPLY_YET:
                // 服务器不答应
                break;
            case LEVEL_NOTCONNECTED:
                // 连接关闭
                break;
            case LEVEL_AUTH_FAILED:
                // 认证失败
                break;
            case LEVEL_WAITING_FOR_USER_INPUT:
                // 等待用户输入
                break;
            case UNKNOWN_LEVEL:
                // 未知错误
                break;
        }
    }

    public void startVpn(String vpnName,String userName,String password,String config) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_USER_NAME,userName);
            bundle.putString(EXTRA_PASSWORD,password);

            APIVpnProfile profile = mService.addNewVPNProfileWithExtras(vpnName,false,config,bundle);
            mService.startProfile(profile.mUUID);
            Log.d(TAG, "startVpn()......profile.mUUID = " + profile.mUUID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void stopVpn() {
        try {
            mService.disconnect();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void pauseVpn() {
        try {
            mService.pause();
            onVpnConnectState("","","",LEVEL_VPNPAUSED.name());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void resumeVpn() {
        try {
            mService.resume();
            onVpnConnectState("","","",LEVEL_CONNECTED.name());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        try {
            mContext.unbindService(mConnection);
            mService.unregisterStatusCallback(mVpnStatusCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
