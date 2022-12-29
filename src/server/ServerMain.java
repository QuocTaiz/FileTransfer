package server;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import entity.DataFile;

public class ServerMain {
	
	private static String pathSave = "D:\\LocalServerFilePBL4Server";
	
	public static String getPathSave() {
		return pathSave;
	}
	
	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(2208);
		System.out.println("Server dang chay cong 2208");
		try {
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println("Server da ket noi toi client");
				ReceiveObjectServer sfs = new ReceiveObjectServer(socket);
				sfs.start();
			}
		} catch (Exception e) {
			serverSocket.close();
			e.printStackTrace();
		}
	}
}


class ReceiveObjectServer extends Thread{
	private Socket socket;
	private ObjectInputStream ois;
	
	public ReceiveObjectServer(Socket socket) throws Exception{
		this.socket = socket;
		this.ois = new ObjectInputStream(socket.getInputStream());
	}
	
	@Override
	public void run() {
		try {
			
				System.out.println("Receiving...");
				Object obj = ois.readObject();
				if (obj instanceof String) {
					readString(obj.toString());
					System.out.println("Server nhan duoc string: "+obj.toString());
				}
				
				if (obj instanceof DataFile) {
					readDataFile((DataFile)obj);
					System.out.println("Server nhan duoc file"+((DataFile)obj).fileName);
				}
			
		} catch (Exception e) {
			try {
				socket.close();
				ois.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	public void readString(String s) throws Exception {
		if(s.contains("SEARCH_FILE")) {
			String[] array = s.split("--");
			String keyword = "";
			if (array.length == 2) {
				keyword = array[1];
			}
			ArrayList<String> listFile = searchFile(keyword);
			String[] listFileString = listFile.toArray(new String[listFile.size()]);
			SendObjectServer sos = new SendObjectServer(socket, listFileString);
			sos.start();
		} else if (s.contains("DOWNLOAD")) {
			String fileName = s.split("--")[1];
			DataFile dataFile = getDataFile(fileName);
			SendObjectServer sos = new SendObjectServer(socket, dataFile);
			sos.start();
		} else if (s.contains("DELETE")) {
			String fileName = s.split("--")[1];
			String mess;
			if (deleteFile(fileName)) {
				mess = "Xoa thanh cong!";
			} else mess = "Xoa khong thanh cong!";
//			SendObjectServer sos = new SendObjectServer(socket, mess);
//			sos.start();
		}
	}
	
	public ArrayList<String> searchFile(String keyword){
		File folder = new File(ServerMain.getPathSave());
		ArrayList<String> listFile = new ArrayList<String>();
		for (File file : folder.listFiles()) {
			if(file.getName().contains(keyword)) listFile.add(file.getName());
		}
		return listFile;
	}
	
	public DataFile getDataFile(String fileName) throws Exception {
		return new DataFile(fileName, ServerMain.getPathSave());
	}
	
	public boolean deleteFile(String fileName) {
		File file = new File(ServerMain.getPathSave()+"\\"+fileName);
		if (file.delete()) {
			System.out.println("Xoa thanh cong file "+fileName);
			return true;
		} else {
			System.out.println("Xoa khong thanh cong");
		}
		return false;
	}
	
	public void readDataFile(DataFile dataFile) throws Exception  {
		String mess;
		if (!checkFileExist(dataFile.fileName)) {
			dataFile.saveFile(ServerMain.getPathSave());
			mess = "Upload file thanh cong!";
		} else {
			mess = "File da ton tai!";
		}
		SendObjectServer sos = new SendObjectServer(socket, mess);
		sos.start();
		
	}
	
	public boolean checkFileExist(String fileName) {
		return new File(ServerMain.getPathSave()+"\\"+fileName).exists();
	}
}


class SendObjectServer extends Thread{
	private Socket socket;
	private Object obj;
	private ObjectOutputStream oos;
	
	public SendObjectServer(Socket socket, Object obj) throws Exception {
		this.socket = socket;
		this.obj = obj;
		this.oos = new ObjectOutputStream(socket.getOutputStream());
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			oos.writeObject(obj);
			System.out.println("Server gui da gui mot Object ");
		} catch (Exception e) {
			try {
				socket.close();
				oos.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
			e.printStackTrace();
		}
	}
}