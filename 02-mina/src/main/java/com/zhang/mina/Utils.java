package com.zhang.mina;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;

public class Utils {
    public static Boolean isEmpty(Object o){
        if(o == null){
            return true ;
        }
        if(o instanceof List ){
            if(((List) o).size()==0){
                return true ;
            }
        }else if(o instanceof String){
            if(StringUtils.isBlank((String)o)){
                return true ;
            }
        }
        return false ;
    }

    public static Boolean isNotEmpty(Object o){
        return !isEmpty(o) ;
    }

    public static Boolean isIntegerOverZero(Integer value){
        if(value != null && value >0){
            return true ;
        }else{
            return false ;
        }
    }

    public static Integer parseIntData(Object o) {
        try {
            if (o != null && isNotEmpty(o.toString().trim())) {
                return Integer.parseInt(o.toString().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -8888;
    }

    //  AES-128 数据加密的
    public static byte[] Encrypt(byte[] sSrc, byte[] sKey){
        try{
            SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc);
            return encrypted;
        }catch(Exception ex){
            return null;
        }
    }

    //  AES-128 数据解密的
    public static byte[] Decrypt(byte[] sSrc, byte[] sKey){
        try{
            SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] dncrypted = cipher.doFinal(sSrc);
            return dncrypted;
        }catch(Exception ex){
            return null;
        }
    }

    //CRC16校验码
    public static String getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return Integer.toHexString(CRC);
    }


}
