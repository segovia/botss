APP_PLATFORM := android-10
APP_STL := stlport_static
APP_ABI := armeabi-v7a-hard
#APP_CFLAGS+=-Wall
APP_CFLAGS+=-Werror -Wno-long-long -Wno-variadic-macros -Wno-array-bounds -mfloat-abi=hard
NDK_MODULE_PATH := C:\Users\gustavo\workspace\botss\Android\liquidfun\