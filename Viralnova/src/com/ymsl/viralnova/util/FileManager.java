package com.ymsl.viralnova.util;

public class FileManager {

	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "com.ymsl.viralnova/files";
		} else {
			return CommonUtil.getRootFilePath() + "com.ymsl.viralnova/files";
		}
	}
}
