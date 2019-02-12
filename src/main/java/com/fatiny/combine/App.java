package com.fatiny.combine;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fatiny.combine.business.Config;
import com.fatiny.combine.core.param.ParamAnalysis;
import com.fatiny.combine.core.param.PropertiesBase;

/**
 * 从固定的目录拷贝目录文件到新生成的目录
 * 
 * @auth Jeremy
 * @date 2019年2月12日下午2:10:46
 */
public class App {

	public static final Logger logger = LoggerFactory.getLogger(App.class);

	private static String fileName = "project/"; // 文件名
	private static String configPath = ""; // 配置文件路径
	private static String projectPath = ""; // 合服工具源码路径
	private static String dumpdbName = ""; // 备份文件名
	private static String hequRun = ""; // 脚本执行文件名

	private static void config() {

	}

	/**
	 * 
	 * 
	 * @return void
	 * @date 2019年2月12日下午2:22:05
	 */
	private static void create() {

	}

	public static void main(String[] args) {
		// 1. 读取配置.
		// 2. 根据配置路径生成对应目录, 并把项目文件拷贝过去.
		// 3. 根据配置生成对应的合服项目
		PropertiesBase.properties();
		try {
			ParamAnalysis.analysis();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 拷贝文件
		String output = Config.outputPath + "/" + new SimpleDateFormat("yyyyMMdd").format(new Date());
		File file = new File(output);
		if (file.exists()) {
			file.mkdirs();
		}
		try {
			FileUtils.copyDirectory(new File(fileName), file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
