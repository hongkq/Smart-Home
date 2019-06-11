package com.youdian.shixun02.Datamanipulation;

public class FROGuan {
    private static final String TAG="FROGuan";

    /**
     * 解析烟雾数据
     * @param rightLen
     * @param nodeNum
     * @param read_buffs 整个返回值
     * @return
     */
    public static Float getDatad(int rightLen, int nodeNum,byte[] read_buffs) {

        Float data= null;
        if (read_buffs!=null) {
            // 长度是否正确，节点号是否正确，CRC是否正确
            if ((read_buffs.length == rightLen && read_buffs[0] == nodeNum)
                    && CRCValidate.isCRCConfig(read_buffs)) {
                /******************** CRC校验正确之后做的，解析数据 ********************/
                // 参数（要拷贝的数组源，拷贝的开始位置，要拷贝的目标数组，填写的开始位置，拷贝的长度）
                byte[] data_buff = new byte[2];//存放数据数组
                //数据开始位,第四位开始
                int dataOffset=3;
                //抠出数据，放进data_buff
                System.arraycopy(read_buffs, dataOffset, data_buff, 0, 2);
                //解析数据data_buff（16进制转10进制）
                data= ByteToFloatUtil.hBytesToFloat(data_buff);
                /*********直接返回数据**********/
                return data;
            }
        }
        return data;// 返回数据
    }
}
