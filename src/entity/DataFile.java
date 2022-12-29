package entity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;


public class DataFile implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public String fileName;
	private byte[] data;
	
	public DataFile(String fileName, String path) throws Exception {
		this.fileName = fileName;
		data = new byte[1024*1000]; // 1MB
		data = Files.readAllBytes(Paths.get(path+"\\"+fileName));
	}
	
	public void saveFile(String path) {
		File savefile = new File(path+"\\"+fileName);
		try (FileOutputStream fos = new FileOutputStream(savefile)) {
			fos.write(data);
			System.out.println("Da luu file "+fileName+" vao "+path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
