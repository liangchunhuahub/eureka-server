package com.liangchunhua.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public enum MD5HashUtil {
    MD5("MD5"),
    SHA1("SHA1"),
    SHA256("SHA-256"),
    SHA512("SHA-512");

    private String name;

    MD5HashUtil(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    private static final char[] NUMBER_CHAR_ARRAY_LOWER = "0123456789abcdef".toCharArray();
    private static final char[] NUMBER_CHAR_ARRAY_UPPER = "0123456789ABCDEF".toCharArray();

    public String byte2HexString(final byte[] data, final char[] charArray){
        String result = "";
        int length = data.length;
        if(length > 0 && charArray.length > 0){
            /*
            char[] resultArray = new char[length << 1];
            for(int i = 0, j = 0; i < length; i++){
                resultArray[j++] = charArray[(0xFF & data[i]) >>> 4];
                resultArray[j++] = charArray[(0x0F & data[i])];

            }
            result = new String(resultArray);
            */

            StringBuilder builder = new StringBuilder(length << 1);
            for(byte b : data){
                builder.append(NUMBER_CHAR_ARRAY_UPPER[(b >>> 4) & 0xF]);
                builder.append(NUMBER_CHAR_ARRAY_UPPER[b & 0xF]);
            }
            result = builder.toString();
        }
        return result;
    }

    /**
     *
     * @param file file object used md5
     * @return return md5 string
     */
    public String getFileMD5String(File file) {
        String result = "";
        if (file != null) {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                MappedByteBuffer fileByte = fileInputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
                MessageDigest messageDigest = MessageDigest.getInstance(getName());
                messageDigest.update(fileByte);
                result = MD5HashUtil.valueOf(getName()).byte2HexString(messageDigest.digest(), NUMBER_CHAR_ARRAY_LOWER);
            } catch (IOException | NoSuchAlgorithmException e) {
                log.debug(e.getMessage());
            }
        }
        return result;
    }

    public String getFileMD5String(String fileName){
        String result = "";
        if(StringUtils.isNotBlank(fileName)){
            File file = new File(fileName);
            if(file.exists()){
                result = MD5HashUtil.valueOf(getName()).getFileMD5String(file);
            }
        }
        return result;
    }

    public String getStringMD5String(String str){
        String result = "";
        if(str != null){
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
                byte[] bytes = messageDigest.digest();
                result = MD5HashUtil.valueOf(getName()).byte2HexString(bytes, NUMBER_CHAR_ARRAY_LOWER);
            } catch (NoSuchAlgorithmException e) {
                log.debug(e.getMessage());
            }
        }
        return result;
    }
}
