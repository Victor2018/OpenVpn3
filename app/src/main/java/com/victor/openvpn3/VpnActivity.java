package com.victor.openvpn3;

import static de.blinkt.openvpn.api.ExternalOpenVPNService.EXTRA_PASSWORD;
import static de.blinkt.openvpn.api.ExternalOpenVPNService.EXTRA_USER_NAME;
import static de.blinkt.openvpn.core.ConnectionStatus.LEVEL_CONNECTED;
import static de.blinkt.openvpn.core.ConnectionStatus.LEVEL_VPNPAUSED;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.blinkt.openvpn.api.APIVpnProfile;
import de.blinkt.openvpn.api.IOpenVPNAPIService;
import de.blinkt.openvpn.api.IOpenVPNStatusCallback;
import de.blinkt.openvpn.core.ConnectionStatus;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: VpnActivity
 * Author: Victor
 * Date: 2025/8/11 17:17
 * Description:
 * -----------------------------------------------------------------
 */
public class VpnActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();

    private IOpenVPNAPIService mService = null;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IOpenVPNAPIService.Stub.asInterface(service);
            try {
                // Request permission to use the API
                Intent i = mService.prepare(getPackageName());
                if (i != null) {
                    startActivityForResult(i, 7);
                } else {
                    onActivityResult(7, Activity.RESULT_OK, null);
                }
            } catch (RemoteException e) {
                Log.d("testconectreq", "openvpn service connection failed: " + e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    private IOpenVPNStatusCallback mVpnStatusCallback = new IOpenVPNStatusCallback.Stub() {
        @Override
        public void newStatus(String uuid, String state, String message, String level) throws RemoteException {
            Log.d(getClass().getSimpleName(), "newStatus()-state = " + state);
            onVpnConnectState(level);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        bindVpnApiService();
    }

    private void bindVpnApiService() {
        Intent vpnApiService = new Intent(IOpenVPNAPIService.class.getName());
        vpnApiService.setPackage(getPackageName());
        bindService(vpnApiService, mConnection, AppCompatActivity.BIND_AUTO_CREATE);
    }

    public void startVpn(Server server) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_USER_NAME,server.getOvpnUserName());
            bundle.putString(EXTRA_PASSWORD,server.getOvpnUserPassword());

            APIVpnProfile profile = mService.addNewVPNProfileWithExtras(server.getCountry(),false,server.getFlagUrl(),bundle);
            mService.startProfile(profile.mUUID);
            Log.d(getClass().getSimpleName(), "startVpn()......profile.mUUID = " + profile.mUUID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopVpn() {
        if (mService != null) {
            try {
                mService.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Handle when mService is null
        }
    }

    public void pauseVpn() {
        try {
            mService.pause();
            onVpnConnectState(LEVEL_VPNPAUSED.name());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void resumeVpn() {
        try {
            mService.resume();
            onVpnConnectState(LEVEL_CONNECTED.name());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onVpnConnectState(String level) {
        Log.i(TAG,"onVpnConnectState()-level = " + level);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7) {
            try {
                mService.registerStatusCallback(mVpnStatusCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        try {
            mService.unregisterStatusCallback(mVpnStatusCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
