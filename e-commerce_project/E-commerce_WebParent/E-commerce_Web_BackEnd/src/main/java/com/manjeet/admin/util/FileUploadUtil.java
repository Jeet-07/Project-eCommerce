package com.manjeet.admin.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.manjeet.common.entity.product.Product;
import org.springframework.web.multipart.MultipartFile;

public final class FileUploadUtil {
	private FileUploadUtil(){}

	public static void saveFile(String uploadDir,String fileName,MultipartFile multipartFile) {
		Path uploadPath = Paths.get(uploadDir);

		if(!Files.exists(uploadPath)) {
			try {
				Files.createDirectories(uploadPath);
			} catch (IOException e) {
				System.out.println("Could not create directory: "+uploadPath);
			}
		}

		try(InputStream inputStream = multipartFile.getInputStream()){
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath,StandardCopyOption.REPLACE_EXISTING);
		}catch(IOException ex) {
			System.out.println("Could not save file: "+fileName);
			System.out.println("Exception Occurred: "+ex);
		}

	}

	public static void clean(String dir){
		Path cleanPath = Paths.get(dir);
        if(Files.exists(cleanPath) ){
			try {
				Files.walk(cleanPath)
						.sorted(Comparator.reverseOrder())
						.forEach(path -> {
							try {
								Files.delete(path);
							} catch (IOException e) {
								System.out.println("Could not Delete: " + path);
								e.printStackTrace();
							}
						});
			} catch (IOException e) {
				System.out.println("Could not walk directory.");
				e.printStackTrace();
			}

		}
	}

	public static void cleanProductExtras(String dir, Product product){
		Path cleanPath = Paths.get(dir);
		if(Files.exists(cleanPath) ){
			try {
				Files.walk(cleanPath)
						.sorted(Comparator.reverseOrder())
						.forEach(path -> {
							String fullPath = path.toString();
							int lastIndexOfSlash = fullPath.lastIndexOf("/");
							String fileName = fullPath.substring(lastIndexOfSlash + 1, fullPath.length());
							if(!product.containsImageName(fileName)) {
								try {
									Files.delete(path);
								} catch (IOException e) {
									System.out.println("Could not Delete: " + path);
									e.printStackTrace();
								}
							}
						});
			} catch (IOException e) {
				System.out.println("Could not walk directory.");
				e.printStackTrace();
			}
		}
	}

	public static void deleteFile(String dir,String fileName){
		Path deletePath = Paths.get(dir,fileName);
		if(Files.exists(deletePath) ){
			try {
				System.out.println(deletePath);
//				Files.delete(deletePath);
			} catch (Exception e) {
				System.out.println("Could not Delete: " + deletePath);
				e.printStackTrace();
			}
		}
	}

}
