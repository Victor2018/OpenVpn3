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

    var test215: String = """
        client
        dev tun
        proto tcp
        remote 192.168.1.215 61194
        resolv-retry infinite
        nobind
        persist-key
        persist-tun
        remote-cert-tls server
        cipher AES-256-CBC
        verb 3
        auth-user-pass
        
        <ca>
        -----BEGIN CERTIFICATE-----
        MIIDNTCCAh2gAwIBAgIJANRDd43ifFz9MA0GCSqGSIb3DQEBCwUAMBYxFDASBgNV
        BAMMC0Vhc3ktUlNBIENBMB4XDTIyMDUwMTEyMDIxM1oXDTMyMDQyODEyMDIxM1ow
        FjEUMBIGA1UEAwwLRWFzeS1SU0EgQ0EwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAw
        ggEKAoIBAQDTRGvOI7pRDFPCWo8EuOMy2V/ezw2L3PaHqDpyKWhGgWWqzYsXFxkF
        iBGJOhpdpUd5R89SAGaF+DxNqFA4ai9NwxfVAq3O+0xKytQozI9bLklmkmhOEg2L
        oLQmHub2QGRXok1ZB3NlYt4cSNnkASTPwJY+4vjRaJYEtwJuQrXTCWr1lL4sw4O4
        EAhc7lK45a28dRFwpLVeMtpbz88eucVkJeT5aoFLpsHYxR3CVfs4l+N72eFC1m+e
        5xZECKGyYWAm5wUhgMQ62Dvr2GMrGDq2PDqh0ohXNWWowdJmgkbPhPMxGX9S0M5X
        ASRK/WtT+vRcIWXB9U2pAc1ATLYM4MyvAgMBAAGjgYUwgYIwHQYDVR0OBBYEFFZC
        ApN09CQwIwv8hrUTvyrTOuoCMEYGA1UdIwQ/MD2AFFZCApN09CQwIwv8hrUTvyrT
        OuoCoRqkGDAWMRQwEgYDVQQDDAtFYXN5LVJTQSBDQYIJANRDd43ifFz9MAwGA1Ud
        EwQFMAMBAf8wCwYDVR0PBAQDAgEGMA0GCSqGSIb3DQEBCwUAA4IBAQCAKRblI7r/
        pmNW3jxdoAJ+cP40JMITgyaCpD/HykRAq0p9ZTQ6wxDF5yz7wWgw/7KVcwZhUpwW
        C3yWGe68BeFNzw4xRlXlpocQM/wMyPWqzw61CbVFnm1Ze6f8P/Sv9nuiRhU6Us8e
        5GNwanP1ItmKOplJ8PN60vy76q2xZwzIp9JIYT7zwPy7QMLqCpuvTA+6fkSqyKgc
        0EMd9kKSbn+D3y8NpYHXoioVghWOOwpVCNAbfb7wUJxX6tAAiF0xjPKuY+tRbEYm
        ZvATwmmYEhh4J9uliPGOTdRFwF55jKbcLAbIWmwvDo8+H2N8irFR7mdi2El5PzkJ
        rKBnzfDYh9U6
        -----END CERTIFICATE-----
        </ca>
        """.trimIndent()


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
                val server189 = Server(
                    2, 420, "GFW",
                    gfw,
                    "106.75.248.189",
                    "longkangwei",
                    "lkw2025!",
                    56004
                )
                val server215 = Server(
                    0, 400, "中国香港215",
                    test215,
                    "192.168.1.215",
                    "Hongkong-02",
                    "123456",
                    61194
                )
                mVpnConnectHelper?.startVpn(server189.country,server189.ovpnUserName,server189.ovpnUserPassword,server189.flagUrl)
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