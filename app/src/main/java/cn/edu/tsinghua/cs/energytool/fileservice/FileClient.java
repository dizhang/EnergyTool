/**
 * Copyright (c) 2018 Di Zhang
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cn.edu.tsinghua.cs.energytool.fileservice;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class FileClient extends AsyncTask<Object, Object, Object> {

    private String serviceIp;
    private int servicePort;
    private String fileName;
    private FileService fileService;

    private boolean isTransferOk;
    private String errorMessage;

    public FileClient(String serviceIp, int servicePort, String fileName, FileService fileService) {
        this.serviceIp = serviceIp;
        this.servicePort = servicePort;
        this.fileName = fileName;
        this.fileService = fileService;
        this.isTransferOk = true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        fileService.notifyFileStatus(1, true, "start");
        Log.v("EnergyTool", "FileClient onPreExecute");
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);

        fileService.notifyFileStatus(2, isTransferOk, errorMessage);
        Log.v("EnergyTool", "FileClient onPostExecute");
    }

    @Override
    protected Object doInBackground(Object... params) {


        byte[] buf = new byte[512];

        OutputStream outputStream = null;
        InputStream inputStream = null;

        Socket socket = new Socket();

        try {
            // set timeout time to 10 seconds
            socket.connect(new InetSocketAddress(serviceIp, servicePort), 10000);

            // set read timeout to 60 seconds
            socket.setSoTimeout(60000);

            outputStream = socket.getOutputStream();
            outputStream.write(fileName.getBytes());
            outputStream.flush();
            Log.v("EnergyTool", "FileClient send file name: " + fileName);

            inputStream = socket.getInputStream();

            String fileDir = Environment.getExternalStorageDirectory().getPath() + File.separator
                    + "EnergyTool" + File.separator + "download";
            File dirFile = new File(fileDir);

            if (!dirFile.exists()) {
                if (!dirFile.mkdir()) {
                    Log.e("EnergyTool", "create download dir for fileclient error");
                }
            }

            File file = new File(fileDir + File.separator + fileName);

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

            int len;
            while ((len = inputStream.read(buf)) > 0) {
                bufferedOutputStream.write(buf, 0, len);
            }

            bufferedOutputStream.flush();
            fileOutputStream.flush();

            bufferedOutputStream.close();
            fileOutputStream.close();

        } catch (IOException e) {
            isTransferOk = false;
            errorMessage = e.getMessage() + "\t" + e.toString() + "\t" + e.getCause();
            Log.e("EnergyTool", "ERROR 1: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                isTransferOk = false;
                errorMessage = e.getMessage() + "\t" + e.toString() + "\t" + e.getCause();
                Log.e("EnergyTool", "ERROR 2: " + e.getMessage());
            }
        }

        Log.v("EnergyTool", "FileClient download file thread done");

        return null;
    }
}
