package com.ioter.warehouse.common.util;

/**
 */
public class EpcUtil
{

    private static String Keys = "-ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 将服装标签码+序列号和EPC码转义
     * 服装标签码+序列号格式为：KSIAC3031FSN138-2018012900007215，16位标签码+4位年份+2位月份+2位日期+8位流水号
     * 限制：年份在2010年-2035年之间，流水号不得大于16777215
     */
    /**
     * 将服装标签码+序列号转义为EPC码
     *
     * @throws Exception
     */
    public static String serializeEPC(String source)
    {
        if (source == null || source.length() != 32)
        {
            System.out.println("格式错误！");
            return null;
        }
        StringBuffer binaryCode = new StringBuffer();

        // 转义1-13位
        for (int i = 0; i < 13; i++)
        {
            String s = source.substring(i, i + 1);
            int sIndex = 0;

            // 第1-5位，第10-12位，获取字母在Keys中的位置
            if (i <= 4 || (i >= 9 && i <= 11))
            {
                sIndex = Keys.indexOf(s);
            } else
            {
                sIndex = Integer.valueOf(s);
            }

            binaryCode.append(String.format("%05d", Integer.valueOf(Integer.toBinaryString(sIndex))));// 转换为5位长度的二进制数
        }

        // 转义14-16位
        String sizeCode = source.substring(13, 13 + 3);
        if ("S--".equals(sizeCode) || "M--".equals(sizeCode) || "L--".equals(sizeCode) || "XL-".equals(sizeCode)
                || "XXL".equals(sizeCode))
        {
            for (int i = 0; i < sizeCode.length(); i++)
            {
                char c = sizeCode.charAt(i);
                binaryCode.append(String.format("%05d", Integer.valueOf(Integer.toBinaryString(Keys.indexOf(c)))));
            }
        } else
        {
            for (int i = 0; i < sizeCode.length(); i++)
            {
                char c = sizeCode.charAt(i);
                if (c == '-')
                {
                    binaryCode.append(String.format("%05d", Integer.valueOf(Integer.toBinaryString(Keys.indexOf(c)))));
                } else if (c == '0')
                {
                    binaryCode.append(String.format("%05d", Integer.valueOf(Integer.toBinaryString(Keys.indexOf('Z')))));
                } else
                {
                    binaryCode.append(
                            String.format("%05d", Integer.valueOf(Integer.toBinaryString(Integer.valueOf(c + "")))));
                }
            }
        }

        // 转义17-24，年月日;支持到2035年

        // 转义年
        int year = Integer.valueOf(source.substring(16, 16 + 4)) - 2010;
        binaryCode.append(String.format("%05d", Integer.valueOf(Integer.toBinaryString(year))));
        if (year < 0 || year > 25)
            return "1004";
        // 转义月、日
        int month = Integer.valueOf(source.substring(20, 20 + 2));
        int day = Integer.valueOf(source.substring(22, 22 + 2));

        if (day > 26)
        {
            month += 12;
            day -= 26;
        }

        binaryCode.append(String.format("%05d", Integer.valueOf(Integer.toBinaryString(month))));
        binaryCode.append(String.format("%05d", Integer.valueOf(Integer.toBinaryString(day))));

        // 转义流水号
        int serialNo = Integer.valueOf(source.substring(24, 24 + 8));
        try
        {
            if (serialNo > 16777215)
            {
                System.out.println("流水号超出最大数值");
                return null;
            }
        } catch (Exception e)
        {
        }
        System.out.println(binaryCode.length());
        String serialNoHex = Integer.toHexString(serialNo).toUpperCase();// 转为十六进制字符串
        if (serialNoHex.length() < 6)// 补到六位十六进制值
        {
            serialNoHex = "000000".substring(0, 6 - serialNoHex.length()) + serialNoHex;
        }

        for (int i = 0; i < serialNoHex.length(); i++)
        {
            CharSequence c = serialNoHex.charAt(i) + "";
            if (Keys.contains(c))
            {
                binaryCode.append(String.format("%05d",
                        Integer.valueOf(Integer.toBinaryString(Keys.indexOf(serialNoHex.charAt(i))))));// 字母，直接转换为5位长度的二进制数
            } else
            {
                binaryCode.append(String.format("%05d",
                        Integer.valueOf(Integer.toBinaryString(Integer.valueOf((String) c) + 7))));// 数值，值加7后对应到G-Q的字母后转换为5位长度的二进制数
            }
        }

        int checkNum = getCheckCode(binaryCode.toString());//获取校验码
        String checkCode = String.format("%03d", Integer.valueOf(Integer.toBinaryString(checkNum % 8)));

        System.out.println(binaryCode);

        //将字符串分为5段，各自反转后合并
        StringBuffer reverseString = new StringBuffer();
        for (int i = 0; i < 25; i++)
        {
            String reverseChar = binaryCode.substring(i * 5, i * 5 + 5);
            reverseString.append(new StringBuffer(reverseChar).reverse().toString());
        }
        reverseString = reverseString.reverse();//再次反转
        StringBuffer resultCode = new StringBuffer();
        resultCode.append(checkCode).append(reverseString);//加上三位校验码

        String result = "";

        for (int i = 0; i < 16; i++)
        {
            String hexStr = Integer.toHexString(Integer.valueOf(resultCode.substring(i * 8, i * 8 + 8), 2) + i * checkNum % 5)
                    .toUpperCase();

            System.out.println(hexStr);

            result += hexStr.length() == 1 ? "0" + hexStr : hexStr;
        }

        return result;
    }

    /// <summary>
    /// 将EPC码转义为服装标签码+序列号
    /// </summary>
    /// <param name="epcCode"></param>
    /// <returns></returns>
    public static String deserializeEPC(String epcCode)
    {
        String result = "";
        try
        {
            if (epcCode == null || epcCode.length() != 32)
            {
                ToastUtil.toast("格式错误！");
                return null;
            }

            //获取前三位的校验码
            String firstCodeToBinary = Integer.toBinaryString(Integer.valueOf(epcCode.charAt(0) + "", 16));//将第一位转为二进制字符串
            if (firstCodeToBinary.length() < 4)// 补到六位十六进制值
            {
                firstCodeToBinary = "0000".substring(0, 4 - firstCodeToBinary.length()) + firstCodeToBinary;
            }
            String checkCode = firstCodeToBinary.substring(0, 3);//将二进制字符串补齐4位后取前三位，即为校验码
            int checkNum = Integer.valueOf(checkCode, 2);


            //1. 将十六进制EPC码转为二进制码
            String binaryCode = "";

            for (int i = 0; i < 16; i++)
            {
                String hexStr = epcCode.substring(i * 2, i * 2 + 2);//十六进制值
                int strValue = Integer.valueOf(hexStr, 16) - i * checkNum % 5;//复原值
                String binaryString = Integer.toBinaryString(strValue);
                if (binaryString.length() < 8)// 补到六位十六进制值
                {
                    binaryString = "00000000".substring(0, 8 - binaryString.length()) + binaryString;
                }
                binaryCode += binaryString;//将复原后的值转为二进制字符串，补位8位
            }

            binaryCode = binaryCode.substring(3);//去掉前3位的校验码
            //先整体反转一次
            String reverseString = new StringBuffer(binaryCode).reverse().toString();
            //将字符串分为5段，各自反转后合并
            StringBuffer reverseResult = new StringBuffer();
            for (int i = 0; i < 25; i++)
            {
                String reverseChar = reverseString.substring(i * 5, i * 5 + 5);
                reverseResult.append(new StringBuffer(reverseChar).reverse().toString());
            }

            binaryCode = reverseResult.toString();

            if (checkNum != getCheckCode(binaryCode))
                return "2002";

            //2. 将125位二进制码转义为25位的大写字母字符串
            String characterCode = "";
            for (int i = 0; i < 25; i++)
            {
                String bCode = binaryCode.substring(i * 5, i * 5 + 5);

                int keyIndex = Integer.valueOf(bCode, 2);//转为10进制数，获取在Key串的位置
                characterCode += Keys.charAt(keyIndex);
            }

            //1-5位直接读取
            result += characterCode.substring(0, 5);
            //6-9位转义为数字
            for (int i = 5; i < 9; i++)
            {
                String s = characterCode.substring(i, i + 1);
                result += Keys.indexOf(s);
            }
            //10-12位直接读取
            result += characterCode.substring(9, 9 + 3);
            //13位转义为数字
            result += Keys.indexOf(characterCode.substring(12, 12 + 1));
            //14-16位
            String sizeCode = characterCode.substring(13, 13 + 3);
            if (sizeCode == "S--" || sizeCode == "M--" || sizeCode == "L--" || sizeCode == "XL-" || sizeCode == "XXL")
                result += sizeCode;
            else
            {
                for (int i = 0; i < sizeCode.length(); i++)
                {
                    char c = sizeCode.charAt(i);
                    if (c == '-')
                        result += c;
                    else if (c == 'Z')
                        result += "0";
                    else
                        result += Keys.indexOf(c);
                }
            }
            //17位，年
            result += Keys.indexOf(characterCode.substring(16, 16 + 1)) + 2010;
            //18位:月；19位:日
            int month = Keys.indexOf(characterCode.substring(17, 17 + 1));
            int day = Keys.indexOf(characterCode.substring(18, 18 + 1));
            if (month > 12)
            {
                month -= 12;
                day += 26;
            }

            result += String.format("%02d", month);
            result += String.format("%02d", day);

            // 20-25 流水号
            String serialCode = "";
            for (int i = 0; i < characterCode.substring(19).length(); i++)
            {
                char c = characterCode.substring(19).charAt(i);
                if (Keys.indexOf(c) > 6)
                    serialCode += (Keys.indexOf(c) - 7);// 将G-Q替换为0-9
                else
                    serialCode += c;
            }
            // 将十六进制字符串转为十进制字符串
            result += String.format("%08d", Integer.valueOf(serialCode, 16));
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }


    /// <summary>
    /// 获取校验码
    /// </summary>
    private static int getCheckCode(String sourceCode)
    {
        int index = Integer.valueOf(sourceCode.substring(0, 6), 2) % 11 + 1;//取前六位二进制转为十进制后除11的余数加上1作为插入的步长

        StringBuffer code = new StringBuffer(sourceCode);
        //将原二进制字符串补到135位
        for (int i = 1; i <= 10; i++)
        {
            code.insert(i * 11 + index, sourceCode.charAt(i * 12));
        }

        //将重新组合后的135位二进制字符串每9个组成一个二进制串，转为15个十进制数，按照规律乘以数模后依次求和，再除以7取得余数，所得余数加1转为二进制即为校验码
        int[] modelNum = new int[]{6, 2, 7, 10, 8, 3, 5, 2, 1, 4, 6, 2, 8, 10, 9};
        long sum = 0;
        for (int i = 0; i < 15; i++)
        {
            sum += Integer.valueOf(code.substring(i * 9, i * 9 + 9), 2) * modelNum[i];
        }

        return (int) (sum % 7) + 1;
    }


}