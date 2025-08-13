package com.victor.openvpn3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.victor.openvpn3.databinding.ActivityMainBinding
import de.blinkt.openvpn.api.IOpenVPNStatusCallback
import de.blinkt.openvpn.api.VpnConnectHelper
import de.blinkt.openvpn.core.ConnectionStatus

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val gfw = "client\n" +
    "dev tun\n" +
    "proto tcp\n" +
    "remote 106.75.248.189 56004\n" +
    "resolv-retry infinite\n" +
    "nobind\n" +
    "persist-key\n" +
    "persist-tun\n" +
    "cipher AES-256-CBC\n" +
    "verb 3\n" +
    "auth-user-pass\n\n" +
    "<ca>\n" +
    "-----BEGIN CERTIFICATE-----\n" +
    "MIIDBTCCAe2gAwIBAgIJAOmhyl26xvvGMA0GCSqGSIb3DQEBCwUAMBkxFzAVBgNV\n" +
    "BAMMDmV4YW1wbGUuY2EuY29tMB4XDTI0MDYzMDAyNTkyNFoXDTM0MDYyODAyNTky\n" +
    "NFowGTEXMBUGA1UEAwwOZXhhbXBsZS5jYS5jb20wggEiMA0GCSqGSIb3DQEBAQUA\n" +
    "A4IBDwAwggEKAoIBAQDCNBFIWVIpyYUsp+VyszbK31jRuMUuzTAnWavcBbnqtxpn\n" +
    "+PwmMbp/p9X+9b08ynnUWBl2xsvD9loMsPfKElXUp9+dFcM8SnAq19s7sxKFKgfM\n" +
    "mDNxZWDR3juLHT7m24WJgGXXbfxI65wbqcaEhnVIpKqW+Tibf9oE1W104UnuvSHf\n" +
    "nOa47vGU7L+P5PofFS8jawATMU59GX9D5ST+ScmyD2IgstNGG83JKdGBSVwi+BHq\n" +
    "qNradutyCRlOtuQxHdYqqB7/2zWHjZq0xHHiutVPuq4xCjPYZexUkuMGGX6EbvKw\n" +
    "5H7tFE+bZLEzRs24f69gIOgqb+O38gxFcz42+T+BAgMBAAGjUDBOMB0GA1UdDgQW\n" +
    "BBSBJfglKFf2wiupjCU78aRuqAf0xDAfBgNVHSMEGDAWgBSBJfglKFf2wiupjCU7\n" +
    "8aRuqAf0xDAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBCwUAA4IBAQBlJHOKjUGJ\n" +
    "UtAM4bvpN304doMqUV+G12ohvM84SOE5xRfLcH5vyvpHbelvKiRPKDPfLo/qSc/t\n" +
    "yCyOxlNaEAd6EnPjLYKSAJ55bKap/lHgem6IFOsM5/1U9O10AFj55Kuwo71KJU3N\n" +
    "eA3V6W1nMzSTqrshVcQV1NDdNcH6YF+KiMMopq5tQtz5dkeb/wd4V0QC1h+49vvR\n" +
    "scujfPNC+qTxLfTUAGworJcUPsmsAPERdszLPzN5Le5cQtzCCfP+eiWZViqhd6rC\n" +
    "RAhGTZFugWoqCC/VVuEy/rhrAQhrHYIhNzevGLA8i+rvdChFehuGwdpnfDHouz7l\n" +
    "s7+1l9SjTfvz\n" +
    "-----END CERTIFICATE-----\n" +
    "</ca>"

    var mVpnConnectHelper: VpnConnectHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        mVpnConnectHelper = VpnConnectHelper(
            this,
            object : IOpenVPNStatusCallback.Stub() {
                override fun newStatus(
                    uuid: String?,
                    state: String?,
                    message: String?,
                    level: String?
                ) {
                    onVpnConnectState(level)
                }
            })

        binding.mBtnConnect.setOnClickListener { view ->
            val connectionStatus = mVpnConnectHelper?.connectionStatus
            if (connectionStatus == ConnectionStatus.LEVEL_CONNECTED) {
                mVpnConnectHelper?.stopVpn()
            } else if (connectionStatus == ConnectionStatus.LEVEL_VPNPAUSED) {
                mVpnConnectHelper?.resumeVpn()
            } else {
                val server = Server(
                    2, 420, "GFW",
                    gfw,
                    "106.75.248.189",
                    "longkangwei",
                    "lkw2025!",
                    56004
                )
                mVpnConnectHelper?.startVpn(server.country,server.ovpnUserName,server.ovpnUserPassword,server.flagUrl)
            }
        }
    }

    fun onVpnConnectState(level: String?) {
        val connectionStatus = mVpnConnectHelper?.connectionStatus
        binding.mTvStatus.text = level
        if (connectionStatus == ConnectionStatus.LEVEL_START) {
            binding.mBtnConnect.text = "连接中"
        } else if (connectionStatus == ConnectionStatus.LEVEL_CONNECTED) {
            binding.mBtnConnect.text = "断开"
        } else if (connectionStatus == ConnectionStatus.LEVEL_NOTCONNECTED) {
            binding.mBtnConnect.text = "连接"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mVpnConnectHelper?.onDestroy()
        mVpnConnectHelper = null
    }
}