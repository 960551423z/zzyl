package com.zzyl.utils;

/**
 *  工具类补全
 */
public class NoProcessing {

    /***
     *  处理补全编号，所有编号都是15位，共5层，每层关系如下：
     * 第一层：100000000000000
     * 第二层：100001000000000
     * 第三层：100001001000000
     * 第四层：100001001001000
     * 第五层：100001001001001
     * @param input 如果为001000000000000处理完成后则变为001
     * @return
 * @return: java.lang.String
     */
    public static String processString(String input) {
        int step = input.length() / 3;
        for (int i =0;i<step;i++ ){
            String targetString = input.substring(input.length()-3,input.length());
            if ("000".equals(targetString)){
                input = input.substring(0,input.length()-3);
            }else {
                break;
            }
        }
        return input;
    }

    public static void main(String[] args) {
//        String input = "100001001001000";
        String input = "100000000000000";
        String processedString = createNo(input,false);
        System.out.println(processedString);
    }

    /***
     *  生产层级编号
     * @param input 输入编号
     * @param peerNode 是否下属节点
     * @return
     * @return: java.lang.String
     */
    public static String createNo(String input,boolean peerNode) {
        int step = input.length() / 3;
        int supplement = 0;
        for (int i =0;i<step;i++ ){
            String targetString = input.substring(input.length()-3,input.length());
            if ("000".equals(targetString)){
                input = input.substring(0,input.length()-3);
                supplement++;
            }else {
                break;
            }
        }
        if (peerNode){
            input = String.valueOf(Long.valueOf(input) + 1L);
            for (int i =0;i<supplement;i++ ){
                input = input+"000";
            }
        }else {
            input = String.valueOf(Long.valueOf(input+"001"));
            for (int i =0;i<supplement-1;i++ ){
                input = input+"000";
            }
        }
        return input;
    }



}
