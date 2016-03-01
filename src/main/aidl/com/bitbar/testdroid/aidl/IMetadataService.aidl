package com.bitbar.testdroid.aidl;

interface IMetadataService {
	void addAction(String name, String type, String currentActivity, String className, String methodName);
	void addDurationToAction();
	void addScreenshotToMetadata(String name, boolean failed, int orientation);
	void saveMetadataFile();
	void changeActionDescription(String name);
	void setErrorMessage(String message);
	void setAllActivitiesFromApplication(in List<String> list);
}
