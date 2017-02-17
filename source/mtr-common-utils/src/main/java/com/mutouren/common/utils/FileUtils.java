package com.mutouren.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.lang3.StringUtils;
import com.mutouren.common.exception.ExceptionUtils;

public class FileUtils {
	
	public static void createFolderIfNoExist(String folderPath) {
		try {
			File file = new File(folderPath);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
			throw ExceptionUtils.doUnChecked(e);
		}
	}
	
	public static void createFileIfNoExist(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {
			throw ExceptionUtils.doUnChecked(e);
		}
	}	
	
	public static void deleteFile(String filePath) {		
		if (StringUtils.isBlank(filePath)) return;

		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			file.delete();
		}
	}	
	
	public static boolean isExist(String filePath) {
		File file=new File(filePath); 
		return file.exists();
	}
	
	public static void write(String filePath, String text, String charset, boolean append){
		//try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
		//		new FileOutputStream(filePath, append), charset)))) {
		try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath, append), charset)) {
			writer.write(text);
		} catch(Exception e) {
			throw ExceptionUtils.doUnChecked(e);
		}
	}
	
	public static void write(String filePath, byte[] buffer, boolean append){
		try (FileOutputStream writer = new FileOutputStream(filePath, append)) {
		//try (DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filePath, append)))) {
			writer.write(buffer);
		} catch(Exception e) {
			throw ExceptionUtils.doUnChecked(e);
		}
	}
	
	public static byte[] read(String fileName) {
		try (BufferedInputStream bf = new BufferedInputStream(new FileInputStream(new File(fileName)))) {		
			byte[] data = new byte[bf.available()];
			bf.read(data);
			return data;
		}catch (Exception e) {
			throw ExceptionUtils.doUnChecked(e);
		}
	}	
	
	public static String read(String filePath, String charset) {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset))) {
			StringBuilder sb = new StringBuilder();	
			
			char[] buffer = new char[10];
			int len = 0;
			do {
				len = reader.read(buffer);
				if (len > 0) {
					sb.append(buffer, 0, len);
				}
			} while(len > 0);
			
			return sb.toString();
		}catch (Exception e) {
			throw ExceptionUtils.doUnChecked(e);
		}
	}	

	public static void main(String[] args) {

	}
	
}
