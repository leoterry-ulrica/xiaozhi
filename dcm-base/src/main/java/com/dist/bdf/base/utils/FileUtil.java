package com.dist.bdf.base.utils;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 *<p> 用于处理文件及文件夹的工具类</p>
 *主要包括创建、删除、复制、移动以及文件的写入、输出操作
 *
 * @author CaoFengLi
 * @version 1.0 , 2013-8-23
 */
public class FileUtil {
	
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	/**
	 * 文件分隔符
	 */
	public static final String S_SYMBL = "/";
	/**
	 * 无返回值
	 */
	private static final String F_RESULT = "no result";
	/**
	 * 删除失败
	 */
	private static final String F_DELETE = "delete failse";
	/**
	 * 文件不存在
	 */
	private static final String F_EXIST = "no exist";
	/**
	 * 文件夹无子文件
	 */
	private static final String F_CHILDREN = "No children file";
	/**
	 * 不是文件夹类型
	 */
	private static final String F_FOLDER = "no folder";

	/**
	 * 将文件输入流保存至指定的文件中
	 *
	 * @param filePath 文件全路径，包含名称，如:e:\a\b
	 * @param fileName 文件名，如：a.txt
	 * @param is 文件输入流
	 */
	public static void createFileWithInputStream(String filePath, String fileName, InputStream is) {
		File folder = new File(filePath);
		FileOutputStream fos = null;
		//若没有文件夹，就建立。
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File file = new File(new StringBuffer().append(filePath).append("/").append(fileName).toString());
		//若没有文件，就建立。
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fos = new FileOutputStream(file);
			int ch = 0;
			while ((ch = is.read()) != -1) {
				fos.write(ch);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String bytesToHexString(byte[] src) {  
        StringBuilder stringBuilder = new StringBuilder();  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;  
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {  
                stringBuilder.append(0);  
            }  
            stringBuilder.append(hv);  
        }  
        return stringBuilder.toString();  
    }
	/**
	 * 获取文件名的后缀名，注意图片后缀的获取
	 * @param fileName 文件名
	 * @return 后缀名，没有带点号“.”
	 */
	public static String getSuffix(String fileName) {

		Assert.hasLength(fileName);

		String result = "";
		try {

			String hex = "";
			File file = new File(fileName);
			if (!file.exists())
				return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

			InputStream is = new FileInputStream(file);
			byte[] bt = new byte[2];
			is.read(bt);

			hex = bytesToHexString(bt);
			is.close();
			if (hex.equals("ffd8")) {
				result = "jpg";
			} else if (hex.equals("4749")) {
				result = "gif";
			} else if (hex.equals("8950")) {
				result = "png";
			} else {
				result = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage());

		}

		return result;
	}

	/**
	 * <p>读取文件内容</p>
	 * <p>使用FileInputStream读取文件的原始字节流，诸如图像数据。</p>
	 * <p>使用BufferedReader数据的读取，需要设置编码格式，控制读取数据产生乱码。</p>
	 * 使用readLine()进行每一行数据的读取，将读取到的数据存放在一个String变量里，用于返回。
	 *
	 * @param filePathWithName 目标文件全路径，包括完整的文件名称
	 * @return 返回读取的文件内容
	 */
	public static String readFile(String filePathWithName) {
		FileReader is = null;
		BufferedReader reader = null;
		StringBuffer buffer = null;
		try {
			is = new FileReader(filePathWithName);
			buffer = new StringBuffer();

			// 用来保存每行读取的内容
			String line;
			reader = new BufferedReader(is);

			//读取第一行
			line = reader.readLine();
			while (line != null) {
				buffer.append(line);
				buffer.append("\n");

				// 读取下一行
				line = reader.readLine();
			}
			return buffer.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(reader, is);
		}
		return null;
	}

	/**
	 * <p>写入文件内容</p>
	 * <P>旧的文件内容将会覆盖</p>
	 * @param filePathWithName 需要写入内容的文件地址
	 * @param fileContent 需要写入的内容
	 * @return 内容添加成功则返回true，添加失败返回false。
	 * @throws Throwable
	 */
	public static boolean writeFile(String filePathWithName, String fileContent) {
		File file = new File(filePathWithName);
		if (!(file.getParentFile().exists())) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (fileContent != null) {
			FileWriter fWriter = null;
			try {
				fWriter = new FileWriter(file);
				fWriter.write(fileContent);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close(fWriter);
			}
		}
		return false;
	}

	/**
	 * <p>以UTF-8的格式写入文件内容</p>
	 * <P>旧的文件内容将会覆盖</p>
	 * @param filePathWithName 需要写入内容的文件地址
	 * @param fileContent 需要写入的内容
	 * @return 内容添加成功则返回true，添加失败返回false。
	 * @throws Throwable
	 */
	public static boolean writeFileWithUTF8(String filePathWithName, String fileContent) {
		File file = new File(filePathWithName);
		if (!(file.getParentFile().exists())) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (fileContent != null) {
			OutputStreamWriter out = null;
			try {
				out = new OutputStreamWriter(new FileOutputStream(filePathWithName), "UTF-8");
				out.write(fileContent);
				out.flush();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close(out);
			}
		}
		return false;
	}

	/**
	 * <p>写入文件内容</p>
	 *
	 * @param filePathWithName 要追加内容的文件路径
	 * @param fileContent 要追加的内容
	 * @return 内容追加成功则返回true，追加失败返回false。
	 * @throws Throwable
	 */
	public static boolean writeFileContent(String filePathWithName, String fileContent) throws Throwable {
		File file = new File(filePathWithName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.getMessage();
			}
		}
		if (fileContent != null) {
			FileWriter fWriter = null;
			try {
				String fileString = readFile(filePathWithName);
				fWriter = new FileWriter(file);
				fWriter.write(fileString);
				fWriter.write(fileContent);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close(fWriter);
			}
		} else {
			throw new Exception("Content is empty");
		}
		return false;
	}

	/**
	 * <p>新建文件并写入文件内容</p>
	 *
	 * @param filePathWithName 文件路径以及完整的文件名
	 * @param fileContent 文件内容，可以为空
	 * @return 创建成功返回true，创建失败选择false
	 * @throws Throwable
	 *
	 */
	public static boolean createFile(String filePathWithName, String fileContent) throws Throwable {
		File file = new File(filePathWithName);
		if (!(file.getParentFile().exists())) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		if (fileContent != null) {
			return writeFileContent(filePathWithName, fileContent);
		}
		return false;
	}

	/**
	 *<p> 新建文件夹，支持多级目录文件创建</p>
	 *
	 * <p>使用File构造函数创建文件实例，</p>
	 * <p>使用exists()判断目录文件是否存在，如果已经存在，则抛出文件不存在异常进行提示；</p>
	 * 如果目录文件不存在，则使用mkdirs()创建目录文件。
	 *
	 * @param folderPath 目录文件路径
	 * @return 创建成功返回true，创建失败或者文件目录已经存在则返回false
	 * @throws Throwable
	 */
	public static boolean createFolder(String folderPath) throws Throwable {
		File file = new File(folderPath);
		if (file.exists()) {
			throw new Throwable(file.getPath() + "existed");
		} else {
			return file.mkdir();
		}
	}

	/**
	 * <p>根据指定的文件夹路径创建文件夹，若文件夹已经存在，则不处理。</p>
	 *
	 * @param folderPath 需要创建的文件夹路径
	 */
	public static void createFolders(String... foldersPath) {
		for (String folderPath : foldersPath) {
			if (StringUtil.isEmpty(folderPath)) {
				return;
			}
			File file = new File(folderPath);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}

	/**
	 * <p>根据指定的文件路径创建文件，若文件已经存在，则不处理。</p>
	 *
	 * @param filePath 需要创建的文件路径
	 */
	public static void createFiles(String... filesPath) {
		for (String filePath : filesPath) {
			if (StringUtil.isEmpty(filePath)) {
				return;
			}
			File file = new File(filePath);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * <p> 删除指定文件或者空文件夹</p>
	 *
	 *要删除的文件夹必须是空文件夹，否则会删除失败。
	 *
	 * @param filePathWithName 文件路径以及完整的文件名
	 * @return 删除成功返回true，删除失败返回false
	 * @throws Throwable
	 */
	public static boolean removeFile(String filePathWithName) throws Throwable {
		File file = new File(filePathWithName);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files.length != 0) {
					return false;
				}
			}
			file.delete();
			return true;
		} else {
			throw new Throwable(file.getPath() + F_EXIST);
		}

	}

	/**
	 * <p>清空文件夹</p>
	 *
	 * <p>删除文件夹中的文件，保留文件结构</p>
	 * @param folderPath 要删除的文件目录路径
	 * @return 删除成功返回true，删除失败返回false
	 * @throws Throwable
	 */
	public static boolean clearFolder(String folderPath) throws Throwable {
		File file = new File(folderPath);
		if (isExists(file)) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				int sum = 0;
				if (files.length != 0) {
					for (File chilFile : files) {
						if (chilFile.isFile()) {
							try {
								removeFile(chilFile.getPath());
							} catch (Throwable e) {
								e.printStackTrace();
							}
						} else {
							sum = sum + 1;
							if (files.length != sum) {
								clearFolder(chilFile.getPath());
							}
						}
					}
					return true;
				}
			} else {
				throw new Throwable(file.getPath() + F_FOLDER);
			}
		}
		return false;
	}

	/**
	 * <p>删除文件夹</p>
	 *
	 * @param folderPath 文件夹路径
	 * @return 删除成功返回true，删除失败返回false
	 * @throws Throwable
	 */
	public static boolean removeFolder(String folderPath) {
		File file = new File(folderPath);
		if (file.exists()) {
			try {
				if (clearFolder(folderPath)) {
					File[] files = file.listFiles();
					if (files.length > 0) {
						for (File childfile : files) {
							if (childfile.listFiles().length != 0) {
								removeFolder(childfile.getPath());
							}
							childfile.delete();
						}
					}
					return file.delete();
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * <p>复制单个文件</p>
	 *
	 * @param srcPath 原文件路径
	 * @param targetPath 目标路径
	 * @return 复制成功则返回true，复制失败则返回false
	 * @throws Throwable
	 */
	public static boolean copyFile(String srcPath, String targetPath) throws Throwable {
		File srcfile = new File(srcPath);
		File tagetfile = new File(targetPath);
		if (srcfile.exists()) {
			if (!tagetfile.exists()) {
				createFolder(targetPath);
			}
			if (srcfile.isFile()) {
				String string = readFile(srcPath);
				if (tagetfile.isDirectory()) {
					createFile(targetPath + S_SYMBL + srcfile.getName(), null);
				}
				writeFileContent(targetPath + S_SYMBL + srcfile.getName(), string);
				return true;
			} else {
				throw new Throwable(srcfile.getPath() + F_FOLDER);
			}
		}
		return false;
	}

	/**
	 * <p>复制多个文件</p>
	 *
	 * @param targetPath 目标路径
	 * @param srcPath 要复制的文件路径数组
	 * @return 如果有一个文件复制成功，则返回true;如果没有一个文件复制成功，则返回false
	 * @throws Throwable copyFile(srcPath，targetPath)返回false，则拼接文件名，抛出异常进行提示
	 *
	 */
	public static boolean copyFiles(String targetPath, String... srcPath) throws Throwable {
		File file = new File(targetPath);
		if (!file.exists()) {
			createFolder(targetPath);
		}
		int sum = 0;
		for (String srcpath : srcPath) {
			File fil = new File(srcpath);
			if (fil.exists()) {
				if (fil.isFile()) {
					if (!copyFile(fil.getPath(), targetPath)) {
						sum = +1;

					}
				}

			} else {
				throw new Exception(fil.getPath() + F_EXIST);
			}
		}
		if (sum == srcPath.length) {
			return false;
		}
		return true;
	}

	/**
	 * <p>复制文件夹以及文件夹包含的子文件</p>
	 *
	 * @param srcPath 原文件路径
	 * @param targetPath 目标文件目录
	 * @return 复制成功则返回true，复制失败则返回false
	 * @throws Throwable 文件不存在
	 */
	public static boolean copyFolder(String srcPath, String targetPath) throws Throwable {
		File srcFile = new File(srcPath);
		File targetFile = new File(targetPath);
		if (srcFile.isDirectory()) {
			if (srcFile.exists()) {
				if (!targetFile.exists()) {
					createFolder(targetPath);
				}
				createFolder(targetPath + S_SYMBL + srcFile.getName());
				File[] files = srcFile.listFiles();
				if (files.length > 0) {
					for (File file : files) {
						if (file.isDirectory()) {
							copyFolder(file.getPath(), targetPath + S_SYMBL + srcFile.getName());
						} else {
							copyFile(file.getPath(), targetPath + S_SYMBL + srcFile.getName());
						}
					}
				} else {
					srcFile.delete();
				}
			}
		} else {
			throw new Throwable(srcFile.getPath() + "is File");
		}
		return true;
	}

	/**
	 *  <p>复制多个文件夹</p>
	 *
	 * @param targetPath 目标路径
	 * @param srcPath 要复制的文件夹路径数组
	 * @return 如果有一个文件夹复制成功，则返回true;如果没有一个文件夹复制成功，则返回false
	 * @throws Throwable copyFolder(srcPath，targetPath)返回false，则拼接文件名，抛出异常进行提示
	 */
	public static boolean copyFolders(String targetPath, String... srcPath) throws Throwable {
		File targetFile = new File(targetPath);
		if (!targetFile.exists()) {
			createFolder(targetPath);
		}
		if (targetFile.isDirectory()) {
			int sum = 0;
			for (String srcpath : srcPath) {
				File srcFile = new File(srcpath);
				if (srcFile.exists()) {
					if (srcFile.isDirectory()) {
						if (!copyFolder(srcpath, targetPath)) {
							throw new Throwable(srcFile.getPath() + "copy failure");
						}
					} else {
						sum = +1;
					}
				} else {
					throw new Throwable(srcFile.getPath() + F_EXIST);
				}
			}
			if (srcPath.length == sum) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * <p>将文件(夹)移到指定的文件目录下</p>
	 *
	 * @param srcPath 原文件全路径
	 * @param targetPath 目标文件目录路径
	 * @return 文件移动成功返回true，移动失败返回false
	 * @throws Throwable 文件不存在，文件目录创建失败
	 */
	public static boolean moveFile(String srcPath, String targetPath) throws Throwable {
		File srcFile = new File(srcPath);
		File tarFile = new File(targetPath);
		if (srcFile.exists()) {
			if (!tarFile.exists()) {
				createFile(targetPath, null);
			}
			if (tarFile.isFile()) {
				if (copyFile(srcPath, targetPath)) {
					return removeFolder(srcPath);
				}
			} else {
				if (copyFolder(srcPath, targetPath)) {
					return removeFolder(srcPath);
				}
			}
		} else {
			throw new Throwable(srcFile.getPath() + F_EXIST);
		}
		return false;
	}

	/**
	 * <p>移动多个文件</p>
	 *
	 * @param targetPath 目标路径
	 * @param srcPath 要移动的文件夹路径数组
	 * @return 如果有一个文件夹移动成功，则返回true;如果没有一个文件夹移动成功，则返回false
	 * @throws Throwable 如果moveFile(srcPath，targetPath)返回false，则拼接文件名，抛出异常进行提示
	 */
	public static boolean moveFiles(String targetPath, String... srcPath) throws Throwable {
		File targetFile = new File(targetPath);
		if (!targetFile.exists()) {
			createFolder(targetPath);
		}
		for (String srcpath : srcPath) {
			File srcFile = new File(srcpath);
			if (srcFile.exists()) {
				if (srcFile.isDirectory()) {
					moveFile(srcpath, targetPath);
				} else {
					throw new Throwable(srcFile.getPath() + F_FOLDER);
				}
			} else {
				throw new Throwable(srcFile.getPath() + F_EXIST);
			}
		}
		return true;
	}

	/**
	 * <p>删除文件夹下的某一种文件</p>
	 *
	 * @param srcPath 目标文件夹路径
	 * @param suffix 需要删除的文件后缀名
	 * @return 如果有一个delete()返回false，则返回false，否则返回true
	 * @throws Throwable 文件集合的长度小于0
	 */
	public static boolean removeFilesWithSuffix(String srcPath, String suffix) throws Throwable {
		List<File> list = findFilesBySuffix(srcPath, suffix);
		if (list.size() > 0) {
			for (File fill : list) {
				if (!fill.delete()) {
					throw new Throwable(fill.getPath() + F_DELETE);
				}
			}
		} else {
			throw new Throwable("find" + F_RESULT);
		}
		return true;
	}

	/**
	 *<p>删除文件夹下不是指定文件类型的文件</p>
	 *
	 * @param srcPath 目标文件夹路径
	 * @param suffix 需要删除的文件后缀名
	 * @return 如果有一个delete()返回false，则返回false，否则返回true
	 * @throws Throwable 删除失败 ，则拼接文件名，抛出异常进行提示
	 */
	public static boolean removeFilesWithoutSuffix(String srcPath, String suffix) throws Throwable {
		List<File> list = findFilesExceptSuffix(srcPath, suffix);
		if (list.size() > 0) {
			for (File fil : list) {
				if (!fil.delete()) {
					throw new Throwable(fil.getPath() + F_DELETE);
				}
			}

		} else {
			throw new Throwable(F_RESULT);
		}
		return true;
	}

	/**
	 * <p>删除指定文件夹下多种类型的文件</p>
	 *
	 * @param srcPath 目标文件夹路径
	 * @param suffixs 后缀名数组
	 * @return 如果有一个removeFilesWithSuffix()返回false，则返回false，否则返回true
	 * @throws Throwable
	 */
	public static boolean removeFilesWithSuffix(String srcPath, String... suffixs) throws Throwable {
		List<File> list = findFilesBySuffix(srcPath, suffixs);
		if (list.size() > 0) {
			for (File fil : list) {
				if (!fil.delete()) {
					throw new Throwable(fil.getPath() + " " + F_DELETE);
				}
			}
		} else {
			throw new Throwable(F_RESULT);
		}
		return true;
	}

	/**
	 * <p>删除文件夹下的文件，除指定类型的文件</p>
	 *
	 * @param srcPath 目标文件夹路径
	 * @param suffixs suffixs 后缀名数组
	 * @return 如果有一个removeFilesWithoutSuffix()返回false，则返回false，否则返回true
	 * @throws Throwable
	 */
	public static boolean removeFilesWithoutSuffix(String srcPath, String... suffixs) throws Throwable {
		if (suffixs.length > 0) {
			for (String suffx : suffixs) {
				if (!removeFilesWithoutSuffix(srcPath, suffx)) {
					throw new Throwable("remove failes");
				}
			}
		}
		return true;
	}

	/**
	 * <p>查询指定后缀名的文件(可以有多种文件)</p>
	 *
	 * @param srcPath 目标文件夹路径
	 * @param suffix 需要查询的文件后缀名
	 * @return 返回查询到的文件集合
	 * @throws Throwable
	 */
	public static List<File> findFilesBySuffix(String srcPath, String... suffix) throws Throwable {
		File file = new File(srcPath);
		if (isExists(file)) {
			if (isDirectory(file)) {
				List<File> files = findAllFile(srcPath);
				List<File> suffixList = new ArrayList<File>();
				if (files.size() > 0) {
					for (File fil : files) {
						for (String suffi : suffix) {
							if (fil.getName().endsWith(suffi)) {
								suffixList.add(fil);
							}
						}
					}
					return suffixList;
				}
			}
		}
		return null;
	}

	/**
	 * <p>查询除了指定文件类型的其他文件</p>
	 *
	 * @param srcPath 目标文件夹路径
	 * @param suffix 需要查询的文件后缀名
	 * @return 返回查询到的文件集合
	 * @throws Throwable
	 */
	public static List<File> findFilesExceptSuffix(String srcPath, String... suffix) throws Throwable {
		File srcfile = new File(srcPath);
		if (srcfile.exists()) {
			if (srcfile.isDirectory()) {
				List<File> files = findAllFile(srcPath);
				List<File> list = new ArrayList<File>();
				if (files.size() > 0) {
					for (File fil : files) {
						for (String suf : suffix) {
							if (!fil.getName().endsWith(suf)) {
								list.add(fil);
							}
						}
					}
					return list;
				}
			} else {
				throw new Throwable(srcfile.getPath() + F_FOLDER);
			}
		} else {
			throw new Throwable(F_EXIST);

		}
		return null;
	}

	/**
	 * 查找文件中的所有文件
	 *
	 * @author
	 * @param srcPath 文件夹路径
	 * @return 标准文集数组
	 * @throws Throwable
	 */
	public static List<File> findAllFile(String srcPath) throws Throwable {
		File file = new File(srcPath);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				List<File> allList = new ArrayList<File>();
				List<File> lit = new ArrayList<File>();
				if (files.length > 0) {
					for (File fil : files) {
						if (fil.isFile()) {
							allList.add(fil);
						} else {
							lit = findAllFile(fil.getPath());
							if (lit != null) {
								allList.addAll(lit);
							}
						}
					}
					return allList;
				}
			} else {
				throw new Throwable(file.getPath() + F_FOLDER);
			}
		} else {
			throw new Throwable(F_EXIST);
		}
		return null;

	}

	/**
	 * <p>删除文件夹下的标准文件</p>
	 *
	 * <p> 删除文件夹下的所有文件，保留子文件夹</p>
	 *
	 * @param srcPath 目标文件夹路径
	 * @return 如果所有delete()返回true则返回true，否则返回false
	 * @throws Throwable
	 */
	public static boolean removeFilesInFolder(String srcPath) throws Throwable {
		File file = new File(srcPath);
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files.length > 0) {
					for (File fil : files) {
						if (fil.isFile()) {
							fil.delete();
						} else {
							removeFilesInFolder(fil.getPath());
						}
					}
					return true;
				} else {
					throw new Throwable(F_CHILDREN);
				}
			} else {
				throw new Throwable(file.getPath() + F_FOLDER);
			}

		} else {
			throw new Throwable(F_EXIST);
		}
	}

	/**
	 * <p>删除文件夹下的文件夹</p>
	 *
	 * <p> 删除文件夹下的文件夹，不删除文件夹下的文件，并将文件提出至根文件夹处</p>
	 *
	 * @param srcPath 目标文件夹路径
	 * @return 如果所有delete()返回true则返回true，否则返回false
	 * @throws Throwable
	 */
	public static boolean removeFoldersInFolder(String srcPath) throws Throwable {
		List<File> list = findAllFile(srcPath);
		for (File file : list) {
			copyFiles(srcPath, file.getPath());
		}
		File srcfFile = new File(srcPath);
		File[] files = srcfFile.listFiles();
		if (files.length != 0) {
			for (File file : files) {
				if (file.isDirectory()) {
					if (file.listFiles().length != 0) {
						removeFolder(file.getPath());
					} else {
						file.delete();
					}
				}
			}
		}
		return true;
	}

	/**
	 * 关闭多个流
	 *
	 * @param closeables  所需要关闭的多个流
	 */
	public static void close(Closeable... closeables) {
		try {
			for (Closeable closeable : closeables) {
				if (closeable != null) {
					closeable.close();
				}
			}
		} catch (IOException e) {
			e.getMessage();
		}
	}

	/**
	 * 判断文件是否存在
	 *
	 * @param file 文件
	 * @return true 存在
	 * @throws Throwable 不存在时文件不存在信息
	 */
	private static boolean isExists(File file) throws Throwable {
		if (file.exists()) {
			return true;
		} else {
			throw new Throwable(file.getPath() + F_EXIST);
		}
	}

	/**
	 * 判断文件是否是文件夹
	 *
	 * @param file 文件
	 * @return true 是文件夹
	 * @throws Throwable 不是文件夹时抛出文件不是文件夹信息
	 */
	private static boolean isDirectory(File file) throws Throwable {
		if (file.isDirectory()) {
			return true;
		} else {
			throw new Throwable(file.getPath() + F_FOLDER);
		}
	}

	//删除文件夹
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); //删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); //删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * @author 何顺
	 * @param path
	 * @return
	 */
	public static boolean delete(String path) {
		File file = new File(path);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	//删除指定文件夹下所有文件
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);//再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	/**
	 *
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSizes(File f) {//取得文件大小\
		try {
			long s = 0;
			if (f.exists()) {
				FileInputStream fis = null;
				fis = new FileInputStream(f);
				s = fis.available();
			} else {
				f.createNewFile();
				System.out.println("文件不存在");
			}
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;

	}

	// 递归
	public static long getFileSize(File f) {
		try {
			long size = 0;
			File flist[] = f.listFiles();
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getFileSize(flist[i]);
				} else {
					size = size + flist[i].length();
				}
			}
			return size;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;

	}

	public static String formatFileSize(long fileS) {//转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
	
	/** 
     * 获得指定文件的byte数组 
     */  
    public static byte[] getBytes(String filePath) {  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }  
    
    /** 
     * 获得指定文件的byte数组 
     */  
    public static byte[] getBytes( File file) {  
        byte[] buffer = null;  
        try {  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }  
    /**
     * 获取不定大小的inputstream流大小
     * @param in
     * @return
     * @throws Exception
     */
    public static byte[] getBytes(InputStream in)  throws Exception {
    	
  	  byte[] b=null;

  	  ByteArrayOutputStream os=new ByteArrayOutputStream();
  	  int c;
  	  while((c=in.read())!=-1){
  	   os.write(c);
  	  }
  	  b=os.toByteArray();
  	  return b;
  	 }
    
    /**
     * 根据byte数组，生成文件 
     * @param bfile
     * @param fileDir 存放文件目录
     * @param fileName 文件名
     */
    public static boolean createFile(byte[] bfile, String fileDir,String fileName) {  
        BufferedOutputStream bos = null;  
        FileOutputStream fos = null;  
        File file = null;  
        try {  
            File dir = new File(fileDir);  
            if(!dir.exists()){//判断文件目录是否存在  
                dir.mkdirs();    
            }  
            
            file = new File(dir.getAbsolutePath()+File.separatorChar+fileName);  
            System.out.println(">>>文件绝对路径："+file.getAbsolutePath()); 
            fos = new FileOutputStream(file.getAbsolutePath());  
            bos = new BufferedOutputStream(fos);  
            bos.write(bfile);  
            
            return true;
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e1) {  
                   e1.printStackTrace();  
                }  
            }  
            if (fos != null) {  
                try {  
                    fos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
        }  
        return false;
    }
    
	 /**
     * 传入路径，返回是否是绝对路径，是绝对路径返回true，反之返回false
     * @param path
     * @return
     * @since 2015年4月21日
     */
    public static boolean isAbsolutePath(String path) {
    
     if(new File(path).exists()) return true;
     
     if (!path.startsWith("/") && path.indexOf(":") > 0) {
      return true;
     }
     return false;
    }
    /**
     * java web获取classes目录
     * @return
     */
    public static String getClassesResourcePath() {
    	
    	return FileUtil.class.getClassLoader().getResource("/").getPath();
    }
    
	/**
	 * base64转inputStream
	 * @param base64string
	 * @return
	 */
	public static InputStream base64ToInputStream(String base64string) {
		ByteArrayInputStream stream = null;
		try {
			byte[] bytes = Base64Utils.decode(base64string);
			stream = new ByteArrayInputStream(bytes);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return stream;
	}
	
    public static void main(String[]args){
    	
    	System.out.println(new File("").getAbsolutePath());
    	System.out.println(System.getProperty("user.dir"));
    	
    	System.out.println(FileUtil.getSuffix("123.jpg"));
    }
    
}
