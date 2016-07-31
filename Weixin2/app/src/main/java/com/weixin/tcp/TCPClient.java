package com.weixin.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by zhao on 2016/7/24 0024.
 */
public class TCPClient implements MessageType, DataType{
    Socket socket;
    OutputStream out;
    InputStream in;
  

    public TCPClient()
    {
        clientInit();
    }
    public void clientInit()
    {
        try
        {
            socket=new Socket("172.16.6.33",15656);
            socket.setKeepAlive(true);
            socket.setSoTimeout(10000*150);


            byte[] mgType = new byte[1];
            mgType[0] = USER_LOGIN_REQUEST;  // USER_LOGINOUT_REQUEST

            byte[] SnedUid ="4d138a46b7144a21a486ea345b40878c".getBytes();

            byte[] sendContentByte = new byte[mgType.length+SnedUid.length];
            System.arraycopy(mgType, 0, sendContentByte, 0, 1);
            System.arraycopy(SnedUid, 0, sendContentByte, 1, SnedUid.length);

            int HeadLenth = mgType.length + SnedUid.length + 2;
            byte[] headLenth = ByteObjConverter.intToByteArray1(HeadLenth);

            byte[] head = ByteObjConverter.intToByteArray1(0XFFFFFFFF);

            short Crc16 = CRC16.CRC16(sendContentByte);
            byte[] crc16 = ByteObjConverter.putShort(Crc16);

            byte[] sendByte = new byte[HeadLenth + 4 + 4];
            System.arraycopy(head, 0, sendByte, 0, 4);
            System.arraycopy(headLenth, 0, sendByte, 4, 4);
            System.arraycopy(sendContentByte, 0, sendByte, 8, sendContentByte.length);
            System.arraycopy(crc16, 0, sendByte, 8 + sendContentByte.length, crc16.length);

            in=socket.getInputStream();
            out=socket.getOutputStream();
            out.write(sendByte);

            int totalBytesRcvd=0;
            int bytesRcvd;
            receive();
            while(totalBytesRcvd<sendByte.length){
            	 InputStream is = socket.getInputStream();
                 byte b[] = new byte[1024];
                 int num1 = 0;
               /* while((num1 = is.read(b))!= -1){
                	//一直读
                	 
                 }*/
                 String  s = new String(b);
                 System.out.println("data:"+b);
                 int num = is.read(b);//返回字节的个数
                 String str = new String(b,0,num,"utf-8");
                 System.out.println(str);
                 byte receive []= str.getBytes("utf-8");//返回服务器返回的字节数组
            	 byte head1[] = subBytes(receive,0,4);
                  if(ByteObjConverter.byteArrayToInt(head) == 0XFFFFFFFF){
                    //获取包长
                	 byte packlength[] = subBytes(receive,4,4);
                	 int packlengthint = ByteObjConverter.byteArrayToInt(packlength);//获取包长
                	 System.out.println("length"+packlengthint);
                	 //获取数据类型
                	 byte typelength[]= subBytes(receive,8,1);
 
                	 System.out.println("type"+(int)typelength[0]);
                	 //获取登录这id
                	 byte content[] = subBytes(receive,9,32);
                	 String contentstr = new String(content);
                	 System.out.println("usid"+contentstr);
                	  
                  }
                if((bytesRcvd=in.read(sendByte, totalBytesRcvd, sendByte.length-totalBytesRcvd))==-1){
                	
                    throw new SocketException("Connection closed prematurely");
                }
                totalBytesRcvd+=bytesRcvd;
            }
            //3.使用Socet类的close（）方法关闭连接
           // socket.close();
        }
        catch(IOException ie)
        {
            ie.toString();
        }
    }
    public static void main(String args[])
    {
        new TCPClient();
    }
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i=begin; i<begin+count; i++) bs[i-begin] = src[i];
        return bs;
    }
    public void receive(){

        byte[] h1 = new byte[1];
        int offsize = 0;
        while (true) {
            try {
                offsize = in.read(h1);
                if (h1[0]!= ByteObjConverter.intToByteArray1(0XFF)[0]) {
                	System.out.println("isTrue"+"h1");
                    continue;
                }
                byte[] h3 = new byte[3];
                offsize = in.read(h3);
                boolean isTrue = true;
                for (int i = 0; i <h3.length ; i++) {
                    if (h3[i]!= ByteObjConverter.intToByteArray1(0XFF)[0]) {
                    	System.out.println("isTrue"+"h3");
                        isTrue =false;
                       break;
                    }
                }
                if(isTrue==false)
                {
                
                    continue;
                }

                System.out.println("isTrue"+"a");
		            byte[] Length = new byte[4];
		            in.read(Length);
		            int length = ByteObjConverter.byteArrayToInt(Length);
		          /*  if (in.read() > length + 4 || length - 2 < 0) {
		                break;
		            }*/

		            byte[] bytes = new byte[length - 2];
		            in.read(bytes);//获取内容
		            byte [] ss = subBytes(bytes,1,length-3);
		            String s = new String(ss,"utf-8");
		            System.out.println(s);
                     
		            //检验CRC是否正确
		            short crc16_server = CRC16.CRC16(bytes);

		            byte[] crcbytes = new byte[2];
		            in.read(crcbytes);
 
		            short crc16_client = ByteObjConverter.getShort(crcbytes);

		            if (crc16_server != crc16_client) {
		                continue;
		            }
            
		            System.out.println("1111");
		        
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        //3.使用Socet类的close（）方法关闭连接
        // socket.close();
    }
   
}
