@echo off
"D:\\soft\\Android\\Sdk\\cmake\\3.22.1\\bin\\cmake.exe" ^
  "-HD:\\AndroidProjects\\OpenVpn3\\lib_openvpn\\src\\main\\cpp" ^
  "-DCMAKE_SYSTEM_NAME=Android" ^
  "-DCMAKE_EXPORT_COMPILE_COMMANDS=ON" ^
  "-DCMAKE_SYSTEM_VERSION=21" ^
  "-DANDROID_PLATFORM=android-21" ^
  "-DANDROID_ABI=arm64-v8a" ^
  "-DCMAKE_ANDROID_ARCH_ABI=arm64-v8a" ^
  "-DANDROID_NDK=D:\\soft\\Android\\Sdk\\ndk\\28.0.13004108" ^
  "-DCMAKE_ANDROID_NDK=D:\\soft\\Android\\Sdk\\ndk\\28.0.13004108" ^
  "-DCMAKE_TOOLCHAIN_FILE=D:\\soft\\Android\\Sdk\\ndk\\28.0.13004108\\build\\cmake\\android.toolchain.cmake" ^
  "-DCMAKE_MAKE_PROGRAM=D:\\soft\\Android\\Sdk\\cmake\\3.22.1\\bin\\ninja.exe" ^
  "-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=D:\\AndroidProjects\\OpenVpn3\\lib_openvpn\\build\\intermediates\\cxx\\Debug\\6a5in4m3\\obj\\arm64-v8a" ^
  "-DCMAKE_RUNTIME_OUTPUT_DIRECTORY=D:\\AndroidProjects\\OpenVpn3\\lib_openvpn\\build\\intermediates\\cxx\\Debug\\6a5in4m3\\obj\\arm64-v8a" ^
  "-DCMAKE_BUILD_TYPE=Debug" ^
  "-BD:\\AndroidProjects\\OpenVpn3\\lib_openvpn\\.cxx\\Debug\\6a5in4m3\\arm64-v8a" ^
  -GNinja
