LOCAL_MODULE:= mylib 
LOCAL_MODULE_FILENAME:= libmylib 
LOCAL_SHARED_LIBRARIES := libliquidfun_jni
include $(BUILD_SHARED_LIBRARY)
# DONT FORGET TO SET UP NDK_ROOT AND NDK_MODULE_PATH ENVIRONMENT VARIABLES
$(call import-module,Box2D/swig/jni)
LOCAL_LDLIBS:=-llog -landroid -lEGL -lGLESv1_CM
LOCAL_ARM_MODE:=arm
LOCAL_LDFLAGS += -fopenmp
LOCAL_CFLAGS=-ffast-math -O3 -funroll-loops -fopenmp