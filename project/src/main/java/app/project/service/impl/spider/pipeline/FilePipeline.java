package app.project.service.impl.spider.pipeline;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import utils.tools.random.RandomUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Jimmy
 */
@Slf4j
public class FilePipeline {

	private static final String ROOT_PATH = "E://zulily//";

	/**
	 * @param path
	 * @param content
	 * @author jimmy.Yang
	 * @description 创建本地文件
	 */
	public static void saveToLocalFile(String path, String content){

		path =  ROOT_PATH + path + "//";
		try {
			File pathFile = new File(path);;
			if(!pathFile.exists()){
				pathFile.mkdir();
			}
			File file = new File(path + DateUtil.today() + ".csv");
			PrintWriter printWriter = null;
			if(!file.exists()){
				content = "eventId, pid, count, name, price, img, detail\n" + content;
				printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
			}else{
				// 追加
				printWriter = new PrintWriter(new FileWriter(file.getAbsolutePath(), true), true);
			}

			printWriter.append(content);
			printWriter.close();
		} catch (IOException e) {
			log.warn(" write file error", e);
		}
	}

	public static void deletedCsvFile(String path){
		path = ROOT_PATH + path + "//";
		try {
			File pathFile = new File(path + DateUtil.today() + ".csv");;
			if(pathFile.exists()){
				pathFile.renameTo(new File(path + DateUtil.today() +"_"+ RandomUtil.randomNumber(50) + ".csv"));
			}
		} catch (Exception e) {
			log.warn(" deleted file error, path={} e={}", path, e);
		}
	}

	public static ThreadLocal<List<File>> localFileCache = new ThreadLocal<>();

	/**
	 *
	 * @param path  保存目录
	 * @param url  图片url
	 * @param pid 图片商品ID
	 * @param index 图片展示位置排名
	 * @return
	 */
	public static Integer saveImg(String path, String url, String pid, String index){

		path = ROOT_PATH + path + "//imgs//";
		try {
			File pathFile = new File(path);;
			if(!pathFile.exists()){
				pathFile.mkdir();
			}

			if(Objects.isNull(localFileCache) || Objects.isNull(localFileCache.get())){
				localFileCache.set(listFiles(pathFile));
			}
			List<String> fileNames = localFileCache.get().stream().map(p -> p.getName()).collect(Collectors.toList());
			Optional<String> findName = fileNames.stream().filter(p -> p.contains(String.format("-%s-", pid)) ).findFirst();
			if(findName.isPresent()){
				//log.info(findName.get());
				int count = Integer.valueOf(findName.get().split("-")[0]);
				String oldFileName = path + findName.get();
				String newFileName = path +  String.format("%03d-%s-%s.jpg", ++count, pid, index);
				FileUtil.rename(new File(oldFileName), newFileName, false, true);
				return count;
			}else{
				path = path +  String.format("%03d-%s-%s.jpg", 1, pid, index);
				HttpUtil.downloadFile(url, path);
				return 1;
			}
		} catch (Exception e) {
			ThreadUtil.sleep(2000L);
			log.warn(" download img error, url={} ,e={}", url, e.getMessage());
		}
		return 1;
	}

	public static void main(String[] args) {
		File file = new File("E:\\zulily\\img.jpg");
		HttpUtil.downloadFile("https://cfcdn.zulily.com/images/cache/product/290x348/612611/zu112798675_main_tm1678371950.jpg", file);
	}

	public static List<File> listFiles(File pathFile) {
		File[] files = pathFile.listFiles();
		List<File> fs = new ArrayList<>();
		if(Objects.isNull(files)){
			return fs;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				fs.addAll(listFiles(file));
			} else {
				fs.add(file);
			}
		}
		return fs;
	}

}
