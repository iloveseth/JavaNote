import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件解压缩
 *
 * @author Tony
 *
 */
class FileToZip {

    /**
     * 将存放在sourceFilePath目录下的源文件,打包成fileName名称的ZIP文件,并存放到zipFilePath。
     *
     * @param sourceFilePath
     *            待压缩的文件路径
     * @param zipFilePath
     *            压缩后存放路径
     * @param fileName
     *            压缩后文件的名称
     * @return flag
     */
    public static boolean fileToZip(String sourceFilePath, String zipFilePath,
                                    String fileName) {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if (sourceFile.exists() == false) {
            System.out.println(">>>>>> 待压缩的文件目录：" + sourceFilePath
                    + " 不存在. <<<<<<");
        } else {
            try {
                File zipFile = new File(zipFilePath + "/" + fileName + ".rar");
                //                File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
                if (zipFile.exists()) {
                    System.out.println(">>>>>> " + zipFilePath + " 目录下存在名字为："
                            + fileName + ".RAR" + " 打包文件. <<<<<<");
                } else {
                    File[] sourceFiles = sourceFile.listFiles();
                    if (null == sourceFiles || sourceFiles.length < 1) {
                        System.out.println(">>>>>> 待压缩的文件目录：" + sourceFilePath
                                + " 里面不存在文件,无需压缩. <<<<<<");
                    } else {
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[1024 * 1024];
                        for (int i = 0; i < sourceFiles.length; i++) {
                            // 创建ZIP实体,并添加进压缩包
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i]
                                    .getName());
                            zos.putNextEntry(zipEntry);
                            // 读取待压缩的文件并写进压缩包里
                            fis = new FileInputStream(sourceFiles[i]);
                            bis = new BufferedInputStream(fis, 1024 * 1024);
                            int read = 0;
                            while ((read = bis.read(bufs, 0, 1024 * 1024)) != -1) {
                                zos.write(bufs, 0, read);
                            }
                        }
                        flag = true;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                // 关闭流
                try {
                    if (null != bis)
                        bis.close();
                    if (null != zos)
                        zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }

        return flag;
    }

    public static void unZip(String sourceFilePath, String unzipFilePath) {
        File sourceFile = new File(sourceFilePath);
        ZipFile zipFile = null;
        ZipEntry zipEntry = null;
        ZipInputStream zis = null;
        FileOutputStream fos = null;
        FileInputStream fis = null;

        if (sourceFile.exists() == false) {
            System.out.println(">>>>>> 待解压的文件目录：" + sourceFilePath
                    + " 不存在. <<<<<<");
        } else {
            try {
                System.out.println(">>>>>> 开始解压：" + sourceFilePath + " <<<<<<");
                zipFile = new ZipFile(sourceFile);
                zis = new ZipInputStream(new FileInputStream(sourceFile));
                while ((zipEntry = zis.getNextEntry()) != null) {
                    String fileName = zipEntry.getName();
                    File temp = new File(unzipFilePath + "\\" + fileName);
                    System.out.println(fileName + ">>>>>>解压到" + unzipFilePath);
                    if (!temp.getParentFile().exists()) {
                        temp.getParentFile().mkdirs();
                    }
                    fos = new FileOutputStream(temp);
                    InputStream is = zipFile.getInputStream(zipEntry);
                    int len = 0;
                    while ((len = is.read()) != -1) {
                        fos.write(len);
                    }
                    is.close();
                }

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ZipException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                // 关闭流
                try {
                    if (null != fos)
                        fos.close();
                    if (null != fis)
                        fis.close();
                    if (null != zis)
                        zis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }

    }
}


public class TestMain {
    public static void main(String[] args) {
        String sourceFilePath = "D:\\media";
        String zipFilePath = "D:\\MyWork";
        // String fileName = "lp20120301";
        boolean flag = FileToZip.fileToZip(sourceFilePath, zipFilePath,
        "3");
         if(flag) {
         System.out.println(">>>>>> 文件打包成功. <<<<<<");
         } else {
         System.out.println(">>>>>> 文件打包失败. <<<<<<");
         }
        FileToZip.unZip("d:\\MyWork\\3.rar", "d:\\MyData");
    }
}
