package app.project.service.impl.spider.pipeline;

import app.project.service.impl.spider.ZulilyPageProcessor;
import app.project.service.impl.spider.constant.Constants;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import utils.tools.random.RandomUtil;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jimmy
 */
@Slf4j
public class FilePipeline {


	/**
	 * @param path
	 * @param content
	 * @author jimmy.Yang
	 * @description 创建本地文件
	 */
	public static void saveToLocalFile(String path, String content){

		path =  Constants.ROOT_PATH + path + "//";
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


	/**
	 * @param path
	 * @param content
	 * @author jimmy.Yang
	 * @description 创建本地文件
	 */
	public static void saveToDetailFile(String path, String content){

		path =  Constants.ROOT_PATH + path + "//";
		try {
			File pathFile = new File(path);;
			if(!pathFile.exists()){
				pathFile.mkdir();
			}
			File file = new File(path + DateUtil.today() + ".csv");
			PrintWriter printWriter = null;
			if(!file.exists()){
				// Vendor Name	Vendor Brand Name	Vendor Style Name	Vendor SKU	Parentage (Parent/ Child)	Parent Vendor SKU	Vendor Size	Vendor Product Description
				// Vendor Product Color	Gender	Age Group	Vendor Inventory
				// Cost	Wholesale Cost	MSRP	Care Instructions	Country of Origin	Consumer Qty Of Unit/ Piece Count
				// Keyword, SEO	Zulily Exclusive	Material Content	Warranty/Guarantee
				content = "Vendor Name, Vendor Brand Name, Vendor Style Name, Vendor SKU, Parentage (Parent/ Child), Parent Vendor SKU, Vendor Size, Vendor Product Description, " +
						"Vendor Product Color, Gender, Age Group , Vendor Inventory, " +
						"Cost, Wholesale Cost ,MSRP, Care Instructions, Country of Origin, Consumer Qty Of Unit/ Piece Count," +
						" Keyword SEO, Zulily Exclusive, Material Content, Warranty/Guarantee, 分隔, " +
						"Product Width,Product Depth,Product Height,Product Dimension Units,Product Weight,Product Weight Units , 分隔," +
						"Ship Height,Ship Width,Ship Length,Ship Weight,Ship Weight Units\n" + content;
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
		path = Constants.ROOT_PATH + path + "//";
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
	 * @param category  保存目录
	 * @param url  图片url
	 * @param pid 图片商品ID
	 * @param index 图片展示位置排名
	 * @return
	 */
	public static Integer saveImg(String category, String url, String pid, String price, String index, Boolean owner){

		String path = Constants.ROOT_PATH + category + "//imgs//";
		try {
			File pathFile = new File(path);;
			if(!pathFile.exists()){
				pathFile.mkdir();
			}

			if(Objects.isNull(localFileCache) || Objects.isNull(localFileCache.get())){
				localFileCache.set(listFiles(pathFile, true));
			}
			List<String> fileNames = localFileCache.get().stream().map(p -> p.getName()).collect(Collectors.toList());
			Optional<String> findName = fileNames.stream().filter(p -> p.contains(String.format("-%s-", pid)) ).findFirst();
			if(findName.isPresent()){
				//log.info(findName.get());
				int count = Integer.valueOf(findName.get().split("-")[0]);
				++count;
				String oldFileName = path + findName.get();
				String newFileName = path + String.format("%03d-%s-%s-%s.jpg", count, pid, price, index);
				if(owner){
					String ownerFileName = Constants.ROOT_PATH + Constants.OWNER + "//" +  String.format("%s-%03d-%s-%s-%s.jpg", DateUtil.format(DateUtil.date(), "MMdd"), count, pid, index, category);
					FileUtil.copyFile(oldFileName, ownerFileName);
				}
				FileUtil.rename(new File(oldFileName), newFileName, false, true);
				return count;
			}else{
				path = path +  String.format("%03d-%s-%s-%s.jpg", 1, pid, price, index);
				HttpUtil.downloadFile(url.replace("https", "http"), path);
				if(owner){
					String ownerFileName = Constants.ROOT_PATH + Constants.OWNER + "//" +  String.format("%s-%03d-%s-%s-%s.jpg", DateUtil.format(DateUtil.date(), "MMdd"), 1, pid, index, category);
					FileUtil.copyFile(path, ownerFileName);
				}
				return 1;
			}
		} catch (Exception e) {
			ThreadUtil.sleep(2000L);
			log.warn(" download img error, url={} ,e={}", url, e.getMessage());
		}
		return 1;
	}


	public static ThreadLocal<List<File>> localCopyFileCache = new ThreadLocal<>();

	public static Map<String, String> allDetailUrl = new HashMap<>();
	public static Boolean copyProduct(Page page, String category, String url, String pid, String detailUrl){

		String path = Constants.ROOT_PATH + Constants.COPY ;
		try {
			File pathFile = new File(path);
			if(!pathFile.exists()){
				pathFile.mkdir();
			}

			if(Objects.isNull(localCopyFileCache) || Objects.isNull(localCopyFileCache.get())){
				localCopyFileCache.set(listFiles(pathFile, false));
			}
			List<String> fileNames = localCopyFileCache.get().stream().map(p -> p.getName()).collect(Collectors.toList());
			Optional<String> findName = fileNames.stream().filter(p -> p.contains(String.format("-%s-", pid)) ).findFirst();
			if(findName.isPresent()){
				// 进行产品信息复制
				String pidPath = path + "//" + pid;
				File copyFile = new File(pidPath);
				if(!copyFile.exists()){
					copyFile.mkdir();

					// 添加下载详情到待爬取队列中
					allDetailUrl.put(pid, detailUrl);

					return true;
				}
			}
		} catch (Exception e) {
			ThreadUtil.sleep(2000L);
			log.warn(" download img error, url={} ,e={}", url, e.getMessage());
		}
		return false;
	}

	public static void main(String[] args) {
		File file = new File("E:\\zulily\\img.jpg");
		HttpUtil.downloadFile("https://cfcdn.zulily.com/images/cache/product/290x348/612611/zu112798675_main_tm1678371950.jpg", file);
	}

	public static List<File> listFiles(File pathFile, boolean isAll) {
		File[] files = pathFile.listFiles();
		List<File> fs = new ArrayList<>();
		if(Objects.isNull(files)){
			return fs;
		}
		for (File file : files) {
			if (isAll && file.isDirectory()) {
				fs.addAll(listFiles(file, isAll));
			} else {
				fs.add(file);
			}
		}
		return fs;
	}

}
