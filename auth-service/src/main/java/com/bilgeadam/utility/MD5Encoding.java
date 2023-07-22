package com.bilgeadam.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encoding {
    public static String md5(String plainText){ //plainText --> kullanıcının gönderdiği şifre - pure data
        String chipherText = convertMD5(plainText); //chipher --> pure data'nın şifrelendiği hali
        for (int i=0; i<2; i++){
            chipherText = convertMD5(chipherText);
        }
        return chipherText;
    }



    //Şifreleme algoritması
    public static String convertMD5(String md5){
        try {
            //MessageDigest sınıfı getInstance metodu aracılığıyla içerisine aldığı String formattaki algoritma isimlerine göre
            //şifreleme işlemi yapar. Bu algoritmalara örnek: SHA-1, SHA-2, CRC
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Şifrelenecek verinin MD5 değerini depolamak ve daha sonra kullanabilmek için byte[] kullanılır.
            //Her bir karakterin iki sistemde(binary) bir karşılığı vardır ve bir byte code değeri vardır.
            byte[] array = md.digest(md5.getBytes());

            //StringBuffer değiştirilebilir karakter dizilerini temsil eder.
            //Byte dizisini okuyarak her bir byte' ın hexadecimal(16 lık sistem) değerini okuyarak array formatında tutar.
            StringBuffer sb = new StringBuffer();
            for (int i=0; i< array.length; i++){
                //--> & 0xFF, byte'in hexadecimal değerini almak için kullanılır. Ve byte' ın en düşük olan 8 bitini temsil eder.
                //0xFF ve 0x100 hexadecimal aralığındaki karakterlerin kullanılacağını belirtmeliyiz.
                //substring sayesinde de belirli bir basamak değeri alınır ve hexadecimal değerlerinin önündeki 0 değeri atılmış olur.
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        }catch (NoSuchAlgorithmException e){
            e.getMessage();
        }
        return null;
    }
}
