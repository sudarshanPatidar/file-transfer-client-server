
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.io.OutputStream;
import java.net.Socket;



public class FTClient {
    public static void main(String[] gg) {
    String fileName = gg[0];
    try{
   
    File file = new File("fileName");
    if(file.exists()==false)
    {
        System.out.println("File " + fileName + "doesn't exists.");
        return;
    }
    if(file.isDirectory())
    {
        System.out.println(fileName + " is a directory");
        return;
    }
    String name = file.getName();
    byte[] header = new byte[1024];
    byte[] ack = new byte[1];
    long lengthOfFile = file.length();
    long x = lengthOfFile;
    int i = 0;
    while(x>0)
    {
        header[i] = (byte)(x%10);
        x=x/10;
        i++;
    }
    header[i]=(byte)',';
    i++;
    int k =0;
    while(i<name.length())
    {
        header[i]=(byte)name.charAt(k);
        k++;
        i++;
    }
    while(i<1024)
    {
        header[i]=(byte)32;
    }
    Socket socket = new Socket("localhost",5500);
    OutputStream os = socket.getOutputStream();
    InputStream is = socket.getInputStream();
    os.write(header,0,1024);
    os.flush();
    System.out.println("Header sent lenght : " + lengthOfFile + " name : " + name);
    //header sent now recive ack
    int bytesRC;
    while(true) {
        bytesRC = is.read(ack);
        if(bytesRC==-1) continue;
        break;
    }
    //ack recived send request
    int chunckSize = 4096;
    FileInputStream fis = new FileInputStream(file);
    int j = 0;
    byte[] bytes = new byte[4096];
    while(j<lengthOfFile)
    {
        bytesRC = fis.read(bytes);
        os.write(bytes, j, chunckSize);
        os.flush();
        j = j + bytesRC;
    }
    fis.close();
    System.out.println("Request sent ");
    //now recive the response header
   
    while(true)
    {
        bytesRC = is.read(ack);
        if(bytesRC==-1) continue;
        break;
    }
    socket.close();
    System.out.println("Socket Closed ");
   
    }catch(Exception E)
    {
        System.out.println(E);
    }
    }
}
