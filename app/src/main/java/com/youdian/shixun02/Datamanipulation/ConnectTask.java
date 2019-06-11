package com.youdian.shixun02.Datamanipulation;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ConnectTask extends AsyncTask<Void, Void, Void> {
    private Context context;
    private Data data;
    private TextView wendu;
    private TextView shidu;
    private TextView yanwu;
    private TextView guanzhao;
    private Float smoke;
    private Float dataTemp;
    private Float guanzh;

    private Socket mSocket;
    private Socket wSocket;
    private Socket gSocket;
    private SocketAddress wSocketAddress;
    private SocketAddress mSocketAddress;
    private SocketAddress gSocketAddress;
    private InputStream inputStream;
    private InputStream inputStreamat;
    private InputStream inputStreamgun;
    private OutputStream outputStream;
    private OutputStream outputStreamat;
    private OutputStream outputStreamgun;

    private byte[] read_buff;
    private byte[] read_buffs;
    private Boolean STATU = false;
    private Boolean CIRCLE = false;
    public ConnectTask(Context context, Data data, TextView wendu, TextView shidu, TextView yanwu,TextView guanzhao) {
      this.context=context;
      this.data=data;
      this.wendu=wendu;
      this.shidu=shidu;
      this.yanwu=yanwu;
      this.guanzhao=guanzhao;
    }
    /**
     * 更新界面
     */
    @Override
    protected void onProgressUpdate(Void... values) {
        if (STATU) {

            Toast.makeText(this.context, "连接正常", Toast.LENGTH_SHORT)
                    .show();

                Thread.interrupted();

        } else {
            Toast.makeText(this.context, "断开连接", Toast.LENGTH_SHORT)
                    .show();


        }

        yanwu.setText(String.valueOf(data.getYanwu()));
        wendu.setText(String.valueOf(data.getWendu()));
        shidu.setText(String.valueOf(data.getShidu()));
        guanzhao.setText(String.valueOf(data.getGuanzhao()));
    }

//开启子线程

    @Override
    protected Void doInBackground(Void... voids) {
        mSocket = new Socket();
        mSocketAddress = new InetSocketAddress(Constant.IP, Constant.port);
        wSocket=new Socket();
        wSocketAddress=new InetSocketAddress(Constant.IPs,Constant.portat);
        gSocket=new Socket();
        gSocketAddress=new InetSocketAddress(Constant.IPguan,Constant.portguan);
    try {
            // socket连接
            mSocket.connect(mSocketAddress, 3000);// 设置连接超时时间为3秒
            wSocket.connect(wSocketAddress,2000);
            gSocket.connect(gSocketAddress,2500);
            if (mSocket.isConnected()) {
                setSTATU(true);
                inputStream = mSocket.getInputStream();// 得到输入流
                outputStream = mSocket.getOutputStream();// 得到输出流
            } else {
                setSTATU(false);
            }
        if (wSocket.isConnected()) {
            setSTATU(true);
            inputStreamat = wSocket.getInputStream();// 得到输入流
            outputStreamat = wSocket.getOutputStream();// 得到输出流
        } else {
            setSTATU(false);
        }
        if (gSocket.isConnected()) {
            setSTATU(true);
            inputStreamgun = gSocket.getInputStream();// 得到输入流
            outputStreamgun = gSocket.getOutputStream();// 得到输出流
        } else {
            setSTATU(false);
        }

            // 循环读取数据
            while (CIRCLE) {
                // 查询烟雾值
                StreamUtil.writeCommand(outputStream, Constant.SMOKE_CHK);
                Thread.sleep(200);
                read_buff = StreamUtil.readData(inputStream);
                smoke = FROSmoke.getData(Constant.SMOKE_LEN, Constant.SMOKE_NUM, read_buff);

                if (smoke != null) {
                    data.setYanwu((int)(float)smoke);
                }
                //查询温度
                StreamUtil.writeCommand(outputStreamat,Constant.TemHum);
                Thread.sleep(200);
                read_buff = StreamUtil.readData(inputStreamat);
                dataTemp = FROTemHum.getTemData(Constant.TemHum_LEN, Constant.TemHum_NUM, read_buff);
                if (dataTemp != null) {
                    data.setWendu((int)(float)dataTemp);
                }
                dataTemp = FROTemHum.getHumData(Constant.TemHum_LEN, Constant.TemHum_NUM, read_buff);
                if (dataTemp != null) {
                    data.setShidu((int)(float)dataTemp);
                }
                //查询光照
                StreamUtil.writeCommand(outputStreamgun, Constant.GunHum);
                Thread.sleep(200);
                read_buffs = StreamUtil.readData(inputStreamgun);
                guanzh = FROGuan.getDatad(Constant.GunHum_LEN, Constant.GunHum_NUM, read_buffs);

                if (guanzh != null) {
                    data.setGuanzhao((int)(float)guanzh);
                }
                // 更新界面
                publishProgress();
                Thread.sleep(200);
            }

        } catch (IOException e) {
            setSTATU(false);
            publishProgress();
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断socket是否还在连接
     *
     * @return
     */
    public Boolean isSuccess() {
        return mSocket.isConnected();
    }

    /**
     * 获取socket
     *
     * @return
     */
    public Socket getmSocket() {
        return mSocket;
    }

    /**
     * 获取输入流
     *
     * @return
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * 获取输出流
     *
     * @return
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    public Boolean getSTATU() {
        return STATU;
    }

    public void setSTATU(Boolean sTATU) {
        STATU = sTATU;
    }

    public Boolean getCIRCLE() {
        return CIRCLE;
    }

    public void setCIRCLE(Boolean cIRCLE) {
        CIRCLE = cIRCLE;
    }

}
