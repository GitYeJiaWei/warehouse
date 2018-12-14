package com.ioter.warehouse.common.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

/**
 * @author chenc
 *         <p>
 *         转换数值对应的数据类型的工具类
 */
public class DataUtil
{
    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str)
    {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str)
    {
        return str != null && str.trim().length() > 0;
    }

    /**
     * 将字符串的值转为二进制数组的值
     *
     * @param value
     * @return
     */
    public static byte[] string2ByteArray(String value)
    {
        if (value == null)
        {
            return null;
        }
        try
        {
            return value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            DebugUtil.printe("DataUtil", "string2ByteArray, UnsupportedEncodingException:" + e);
        }
        return null;
    }

    /**
     * 将short数组的值转为二进制数组的值
     *
     * @param value
     * @return
     */
    public static byte[] shortArray2ByteArray(short[] value)
    {
        if (value == null || value.length < 1)
        {
            return null;
        }
        byte[] result = new byte[value.length * 2];
        for (int i = 0; i < value.length; i++)
        {
            result[i * 2] = (byte) (value[i] & 0x00ff);
            result[1 + i * 2] = (byte) ((value[i] & 0xff00) >>> 8);
        }
        return result;
    }

    /**
     * 将int数组的值转为二进制数组的值
     *
     * @param value
     * @return
     */
    public static byte[] intArray2ByteArray(int[] value)
    {
        if (value == null || value.length < 1)
        {
            return null;
        }
        byte[] result = new byte[value.length * 4];
        for (int i = 0; i < value.length; i++)
        {
            result[i * 4] = (byte) (value[i] & 0x000000ff);
            result[1 + i * 4] = (byte) ((value[i] & 0x0000ff00) >>> 8);
            result[2 + i * 4] = (byte) ((value[i] & 0x00ff0000) >>> 16);
            result[3 + i * 4] = (byte) ((value[i] & 0xff000000) >>> 24);
        }
        return result;
    }

    /**
     * 将long数组的值转为二进制数组的值
     *
     * @param value
     * @return
     */
    public static byte[] longArray2ByteArray(long[] value)
    {
        if (value == null || value.length < 1)
        {
            return null;
        }
        byte[] result = new byte[value.length * 8];
        for (int i = 0; i < value.length; i++)
        {
            result[i * 8] = (byte) (value[i] & 0x00000000000000ffL);
            result[1 + i * 8] = (byte) ((value[i] & 0x000000000000ff00L) >>> 8);
            result[2 + i * 8] = (byte) ((value[i] & 0x0000000000ff0000L) >>> 16);
            result[3 + i * 8] = (byte) ((value[i] & 0x00000000ff000000L) >>> 24);
            result[4 + i * 8] = (byte) ((value[i] & 0x000000ff00000000L) >>> 32);
            result[5 + i * 8] = (byte) ((value[i] & 0x0000ff0000000000L) >>> 40);
            result[6 + i * 8] = (byte) ((value[i] & 0x00ff000000000000L) >>> 48);
            result[7 + i * 8] = (byte) ((value[i] & 0xff00000000000000L) >>> 56);
        }
        return result;
    }

    /**
     * 将short的值转为二进制数组的值
     *
     * @param value
     * @return
     */
    public static byte[] short2ByteArray(short value)
    {
        byte[] result = new byte[2];
        result[0] = (byte) (value & 0x00ff);
        result[1] = (byte) ((value & 0xff00) >>> 8);
        return result;
    }

    /**
     * 将int的值转为二进制数组的值
     *
     * @param value
     * @return
     */
    public static byte[] int2ByteArray(int value)
    {
        byte[] result = new byte[4];
        result[0] = (byte) (value & 0x000000ff);
        result[1] = (byte) ((value & 0x0000ff00) >>> 8);
        result[2] = (byte) ((value & 0x00ff0000) >>> 16);
        result[3] = (byte) ((value & 0xff000000) >>> 24);
        return result;
    }

    /**
     * 将long的值转为二进制数组的值
     *
     * @param value
     * @return
     */
    public static byte[] long2ByteArray(long value)
    {
        byte[] result = new byte[8];
        result[0] = (byte) (value & 0x00000000000000ffL);
        result[1] = (byte) ((value & 0x000000000000ff00L) >>> 8);
        result[2] = (byte) ((value & 0x0000000000ff0000L) >>> 16);
        result[3] = (byte) ((value & 0x00000000ff000000L) >>> 24);
        result[4] = (byte) ((value & 0x000000ff00000000L) >>> 32);
        result[5] = (byte) ((value & 0x0000ff0000000000L) >>> 40);
        result[6] = (byte) ((value & 0x00ff000000000000L) >>> 48);
        result[7] = (byte) ((value & 0xff00000000000000L) >>> 56);
        return result;
    }

    /**
     * 根据long的值大小，将long的值转为不同长度的二进制数组的值
     *
     * @param value
     * @return
     */
    public static byte[] long2ByteArrayByFact(long value)
    {
        byte value0 = (byte) (value & 0x00000000000000ffL);
        byte value1 = (byte) ((value & 0x000000000000ff00L) >>> 8);
        byte value2 = (byte) ((value & 0x0000000000ff0000L) >>> 16);
        byte value3 = (byte) ((value & 0x00000000ff000000L) >>> 24);
        byte value4 = (byte) ((value & 0x000000ff00000000L) >>> 32);
        byte value5 = (byte) ((value & 0x0000ff0000000000L) >>> 40);
        byte value6 = (byte) ((value & 0x00ff000000000000L) >>> 48);
        byte value7 = (byte) ((value & 0xff00000000000000L) >>> 56);
        if (value4 != 0 || value5 != 0 || value6 != 0 || value7 != 0)
        {
            return new byte[]{value0, value1, value2, value3, value4, value5, value6, value7};
        }
        if (value3 != 0 || value2 != 0)
        {
            return new byte[]{value0, value1, value2, value3};
        }
        if (value1 != 0)
        {
            return new byte[]{value0, value1};
        }
        return new byte[]{value0};
    }

    /**
     * 将Object的值转为二进制数组的值
     *
     * @param value
     * @return
     */
    public static byte[] obj2ByteArray(Object value)
    {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try
        {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(value);
            oos.flush();
            return bos.toByteArray();
        } catch (Exception e)
        {
            DebugUtil.printe("DataUtil", "obj2ByteArray, Exception:" + e);
        } finally
        {
            if (oos != null)
            {
                try
                {
                    oos.close();
                } catch (IOException e)
                {
                    DebugUtil.printe("DataUtil", "obj2ByteArray, IOException:" + e);
                }
                oos = null;
            }
            if (bos != null)
            {
                try
                {
                    bos.close();
                } catch (IOException e)
                {
                    DebugUtil.printe("DataUtil", "obj2ByteArray, IOException:" + e);
                }
                bos = null;
            }
        }
        return null;
    }

    /**
     * 将二进制数组的值转为字符串的值
     *
     * @param value
     * @return
     */
    public static String byteArray2String(byte[] value)
    {
        if (value == null)
        {
            return null;
        }
        try
        {
            return new String(value, "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            DebugUtil.printe("DataUtil", "byteArray2String, UnsupportedEncodingException:" + e);
        }
        return null;
    }

    /**
     * 将二进制数组的值转为short数组的值
     *
     * @param value
     * @return
     */
    public static short[] byteArray2ShortArray(byte[] value)
    {
        if (value == null || value.length < 2)
        {
            return null;
        }
        short[] array = new short[value.length / 2];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = (short) byteArray2Long(value, i * 2, 2);
        }
        return array;
    }

    /**
     * 将二进制数组的值转为int数组的值
     *
     * @param value
     * @return
     */
    public static int[] byteArray2IntArray(byte[] value)
    {
        if (value == null || value.length < 4)
        {
            return null;
        }
        int[] array = new int[value.length / 4];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = (int) byteArray2Long(value, i * 4, 4);
        }
        return array;
    }

    /**
     * 将二进制数组的值转为long数组的值
     *
     * @param value
     * @return
     */
    public static long[] byteArray2LongArray(byte[] value)
    {
        if (value == null || value.length < 8)
        {
            return null;
        }
        long[] array = new long[value.length / 8];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = byteArray2Long(value, i * 8, 8);
        }
        return array;
    }

    /**
     * 将二进制数组的值转为short的值
     *
     * @param value
     * @return
     */
    public static short byteArray2Short(byte[] value)
    {
        if (value == null)
        {
            return 0;
        }
        return (short) byteArray2Long(value, 0, value.length);
    }

    /**
     * 将二进制数组的值转为int的值
     *
     * @param value
     * @return
     */
    public static int byteArray2Int(byte[] value)
    {
        if (value == null)
        {
            return 0;
        }
        return (int) byteArray2Long(value, 0, value.length);
    }

    /**
     * 将二进制数组的值转为long的值
     *
     * @param value
     * @return
     */
    public static long byteArray2Long(byte[] value)
    {
        if (value == null)
        {
            return 0;
        }
        return byteArray2Long(value, 0, value.length);
    }

    /**
     * 将二进制数组的值转为long的值
     *
     * @param value  二进制数组
     * @param offset 二进制数组的查询起始索引
     * @param length 二进制数组的查询长度
     * @return
     */
    public static long byteArray2Long(byte[] value, int offset, int length)
    {
        int len = value.length - offset;
        len = len > length ? length : len;
        if (len < 1)
        {
            return 0;
        }
        long result = (long) (value[offset] & 0xff);
        if (len < 2)
        {
            return result;
        }
        result |= (long) (value[offset + 1] & 0xff) << 8;
        if (len < 3)
        {
            return result;
        }
        result |= (long) (value[offset + 2] & 0xff) << 16;
        if (len < 4)
        {
            return result;
        }
        result |= (long) (value[offset + 3] & 0xff) << 24;
        if (len < 5)
        {
            return result;
        }
        result |= (long) (value[offset + 4] & 0xff) << 32;
        if (len < 6)
        {
            return result;
        }
        result |= (long) (value[offset + 5] & 0xff) << 40;
        if (len < 7)
        {
            return result;
        }
        result |= (long) (value[offset + 6] & 0xff) << 48;
        if (len < 8)
        {
            return result;
        }
        result |= (long) (value[offset + 7] & 0xff) << 56;
        return result;
    }

    /**
     * 将二进制数组的值转为Object的值
     *
     * @param value
     * @return
     */
    public static Object byteArray2Object(byte[] value)
    {
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try
        {
            bis = new ByteArrayInputStream(value);
            ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (StreamCorruptedException e)
        {
            DebugUtil.printe("DataUtil", "byteArray2Object, StreamCorruptedException:" + e);
        } catch (IOException e)
        {
            DebugUtil.printe("DataUtil", "byteArray2Object, IOException:" + e);
        } catch (ClassNotFoundException e)
        {
            DebugUtil.printe("DataUtil", "byteArray2Object, ClassNotFoundException:" + e);
        } finally
        {
            if (ois != null)
            {
                try
                {
                    ois.close();
                } catch (IOException e)
                {
                    DebugUtil.printe("DataUtil", "byteArray2Object, IOException:" + e);
                }
                ois = null;
            }
            if (bis != null)
            {
                try
                {
                    bis.close();
                } catch (IOException e)
                {
                    DebugUtil.printe("DataUtil", "byteArray2Object, IOException:" + e);
                }
                bis = null;
            }
        }
        return null;
    }

    /**
     * 二进制转16进制
     */
    public static String hextoBcdString(byte[] array)
    {
        if (array == null || array.length < 1)
        {
            return "";
        }
        String[] strList = {"A", "B", "C", "D", "E", "F"};
        String ret = "";
        for (int i = 0; i < array.length; i++)
        {
            byte temp = array[i];
            int hight = (temp >> 4) & 0x0F;
            if (hight >= 0 && hight <= 9)
            {
                ret += hight;
            } else
            {
                ret += strList[hight - 10];
            }
            int low = (temp & 0x0F);
            if (low >= 0 && low <= 9)
                ret += low;
            else
                ret += strList[low - 10];
        }
        return ret;
    }

    public static byte[] bcdStringtoHex(String bcd)
    {
        String temp = bcd.toUpperCase();
        byte[] bytes = temp.getBytes();
        byte[] bsBcd = new byte[bytes.length / 2];
        // int j=0;
        for (int i = 0; i < bytes.length / 2; i++)
        {
            int hight = bytes[i * 2];
            int low = bytes[i * 2 + 1];
            if ((hight >= '0') && (hight <= '9'))
                hight = hight - '0';
            else
                hight = hight - 'A' + 10;

            if ((low >= '0') && (low <= '9'))
                low = low - '0';
            else
                low = low - 'A' + 10;

            bsBcd[i] = (byte) (((((hight << 4) & 0xF0) + (low & 0x0F))) & 0xFF);

        }
        return bsBcd;
    }

    public static byte[] hexString2ByteArray(String value)
    {
        if (isEmpty(value))
        {
            return null;
        }
        value = value.toUpperCase();
        char[] array = value.toCharArray();
        byte[] result = new byte[array.length / 2];
        for (int i = 0; i < result.length; i++)
        {
            byte temp1 = (byte) ("0123456789ABCDEF".indexOf(array[i * 2]));
            byte temp2 = (byte) ("0123456789ABCDEF".indexOf(array[i * 2 + 1]));
            result[i] = (byte) (temp1 << 4 | temp2);
        }
        return result;
    }

    /**
     * 获取指定长度的二维long数组
     *
     * @param xLength 一维数组长度
     * @param yLength 一维数组的数组长度
     * @return
     */
    public static long[][] getLongArray(int xLength, int yLength, byte[] mContent)
    {
        if (xLength < 1 || yLength < 1)
        {
            return new long[0][0];
        }
        long[][] array = new long[xLength][yLength];
        int mOffset = 0;
        for (int i = 0; i < array.length; i++)
        {
            for (int j = 0; j < array[i].length; j++)
            {
                mOffset = i * yLength * 8 + j * 8;
                array[i][j] = byteArray2Long(mContent, mOffset, 8);
            }
        }
        return array;
    }

    /**
     * 获取指定长度的二维byte数组
     *
     * @param xLength    一维数组长度
     * @param yLength    一维数组的数组长度
     * @param elemLength 每个单元的长度
     * @param mContent   二维数组数据源
     * @return
     */
    public static Object[][] getByteArrayObjects(int xLength, int yLength, int elemLength, byte[] mContent)
    {
        if (xLength < 1 || yLength < 1)
        {
            return new Object[0][0];
        }
        Object[][] array = new Object[xLength][yLength];
        int mOffset = 0;
        for (int i = 0; i < array.length; i++)
        {
            for (int j = 0; j < array[i].length; j++)
            {
                mOffset = i * yLength * elemLength + j * elemLength;
                // System.arraycopy(mContent, mOffset, array[i][j], 0,
                // elemLength);
                array[i][j] = getByteArray(elemLength, mContent, mOffset);
            }
        }
        return array;
    }

    /**
     * 获取指定长度的byte数组
     *
     * @param length
     * @return
     */
    public static byte[] getByteArray(int length, byte[] mContent, int mOffset)
    {
        int len = mContent.length - mOffset;
        len = len > length ? length : len;
        if (len < 1)
        {
            return new byte[0];
        }
        byte[] array = new byte[len];
        System.arraycopy(mContent, mOffset, array, 0, len);
        return array;
    }


    /**
     * 验证身份证号是否符合规则
     *
     * @param text 身份证号
     * @return
     */
    public static boolean personIdValidation(String text)
    {
        String regx = "[0-9]{17}x";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) || text.matches(reg1) || text.matches(regex);
    }

    /**
     * 验证护照正则表达式
     *
     * @param text
     * @return
     */
    public static boolean passPortValidation(String text)
    {
        String regx = "^1[45][0-9]{7}|G[0-9]{8}|P[0-9]{7}|S[0-9]{7,8}|D[0-9]+$";
        return text.matches(regx);
    }

    /**
     * 半角转全角
     *
     * @param input String.
     * @return 全角字符串.
     */
    public static String ToSBC(String input)
    {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++)
        {
            if (c[i] == ' ')
            {
                c[i] = '\u3000';
            } else if (c[i] < '\177')
            { // 采用八进制,相当于十进制的127
                c[i] = (char) (c[i] + 65248);

            }
        }
        return new String(c);
    }

    public static int parseInt(String value)
    {
        try
        {
            return Integer.parseInt(value);
        } catch (NumberFormatException e)
        {
            return 0;
        }
    }

    public static int parseInt(String value, int radix)
    {
        try
        {
            return Integer.parseInt(value, radix);
        } catch (NumberFormatException e)
        {
            return 0;
        }
    }

    public static long parseLong(String value)
    {
        try
        {
            return Long.parseLong(value);
        } catch (NumberFormatException e)
        {
            return 0;
        }
    }

    public static long parseLong(String value, int radix)
    {
        try
        {
            return Long.parseLong(value, radix);
        } catch (NumberFormatException e)
        {
            return 0;
        }
    }

    public static float parseFloat(String value)
    {
        try
        {
            return Float.parseFloat(value);
        } catch (NumberFormatException e)
        {
            return 0;
        }
    }

    public static double parseDouble(String value)
    {
        try
        {
            return Double.parseDouble(value);
        } catch (NumberFormatException e)
        {
            return 0;
        }
    }

    /**
     * 设置textview不同颜色
     *
     * @param str
     * @param start           不同色值部分起点
     * @param length          不同色值部分长度
     * @param foregroundColor 不同的色值
     * @return
     */
    public static SpannableString setDifferentText(String str, int start, int length, int foregroundColor)
    {
        SpannableString spannableString = new SpannableString(str);
        ForegroundColorSpan span = new ForegroundColorSpan(foregroundColor);
        spannableString.setSpan(span, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 设置textview前后不同颜色
     *
     * @param str             前半部文字
     * @param unit            后半部文字
     * @param foregroundColor 前半部文学的颜色
     * @return
     */
    public static SpannableString setDifferentText(String str, String unit, int foregroundColor)
    {
        if (unit == null)
            unit = "";
        String content = str + (unit.equals("") ? "" : " " + unit);
        SpannableString spannableString = new SpannableString(content);
        ForegroundColorSpan span = new ForegroundColorSpan(foregroundColor);
        spannableString.setSpan(span, 0, content.length() - unit.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 千位分隔
     *
     * @param num
     * @return
     */
    public static String FormatThousand(long num)
    {
        DecimalFormat format = new DecimalFormat(",##0");
        return format.format(num);
    }

    /**
     * 添加带html格式的字符串
     *
     * @return
     */
    public static String getHtmlText(String text, String textColor)
    {
        return "<font color='" + textColor + "'>" + text + "</font>";
    }

    //ASCII码转换为16进制
    public static String convertStringToHex(String str)
    {

        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        return hex.toString();
    }

    //16进制转换为ASCII
    public static String convertHexToString(String hex)
    {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2)
        {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }

        return sb.toString();
    }

    /**
     * 字符串转换成十六进制字符串
     */
    public static String str2HexStr(String str)
    {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * MD5 加密算法
     * @param string
     * @return
     */
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}