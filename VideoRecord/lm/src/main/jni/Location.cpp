//
// Created by 李敏 on 2016/07/18.
//
#include <jni.h>
#include <stdlib.h>
#include "formula.h"

jobject gps_to_google(JNIEnv *env, jclass clz, jobject obj) {
	jclass cls = env->GetObjectClass(obj);
	
	jfieldID latID = env->GetFieldID(cls, "latitude", "D");
	jfieldID lonID = env->GetFieldID(cls, "longitude", "D");
	
	jdouble lat = env->GetDoubleField(obj, latID);
	jdouble lon = env->GetDoubleField(obj, lonID);

	jdouble out_lat, out_lon;

	wgs84_2_gcj02(lat, lon, &out_lat, &out_lon);

	jmethodID initID = env->GetMethodID(cls, "<init>", "(DD)V");

	return env->NewObject(cls, initID, out_lat, out_lon);
}

static const char *CLASS_NAME = "lm/location/LMLocationUtil";

static const JNINativeMethod METHODS[] = {
		{"toGoogle", "(Llm/location/LMLocation;)Llm/location/LMLocation;", (void *)gps_to_google},
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
	JNIEnv *env = NULL;
	vm->GetEnv((void **)&env, JNI_VERSION_1_6);

	if(!env) {
		return JNI_ERR;
	}

	jclass cls = env->FindClass(CLASS_NAME);
	if(!cls) {
		return JNI_ERR;
	}

	jint ret = env->RegisterNatives(cls, METHODS, 1);
	if(ret != JNI_OK) {
		return JNI_ERR;
	}

	return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM* vm, void* reserved) {
	vm->DestroyJavaVM();
}