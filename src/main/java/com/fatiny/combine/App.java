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

	private static String combine = "project/combine"; // 合服代码
	private static String classification = "project/classification"; // 需要生成的对应子模块
	private static String configPath = ""; // 配置文件路径
	private static String projectPath = ""; // 合服工具源码路径
	private static String dumpdbName = ""; // 备份文件名
	private static String hequRun = ""; // 脚本执行文件名

	private static String outputDir = ""; // 输出目录

	/**
	 * 注解配置
	 * 
	 * @return void
	 * @date 2019年2月13日下午5:51:42
	 */
	private static void config() {
		PropertiesBase.properties();
		try {
			ParamAnalysis.analysis();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建新的目录, 用于生成新的合服文件
	 * 
	 * @return void
	 * @throws IOException
	 * @date 2019年2月12日下午2:22:05
	 */
	private static void create() throws IOException {
		// 拷贝文件
		outputDir = Config.outputPath + "/" + new SimpleDateFormat("yyyyMMdd").format(new Date());
		File file = new File(outputDir);
		if (file.exists()) {
			file.mkdirs();
		}
		FileUtils.copyDirectory(new File(classification), file);
		// 复制项目combine到对应子目录
		// File combineFile = new File(combine);
		// File outputDirFile = new File(outputDir);
		// for (File odf : outputDirFile.listFiles()) {
		// if (odf.isDirectory()) {
		// FileUtils.copyDirectory(combineFile, odf);
		// }
		// }
	}

	public static void main(String[] args) {
		try {
			// 1. 读取配置.
			config();
			// 2. 根据配置路径生成对应目录, 并把项目文件拷贝过去.
			create();
			// 3. 根据配置生成对应的合服项目
			Config.print();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
