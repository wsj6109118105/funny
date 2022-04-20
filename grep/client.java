import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * user:lufei
 * DATE:2022/4/20
 **/
public class client {
    public static void main(String[] args) {
        while (true) {
            Scanner in = new Scanner(System.in);
            String defaultPath = in.next();
            String checkWord = in.next();
            File f = new File(defaultPath);
            if (!f.exists()) {
                System.out.println("路径命不正确！或文件夹不存在！请确认文件夹或路径命");
            }
            check(f, defaultPath, checkWord);
        }
    }

    public static void check(File cur,String defaultPath,String checkWord) {
        String[] list = cur.list();
        Arrays.stream(list).forEach((fileName)->{
            File file = new File(defaultPath+"\\"+fileName);
            if (file.isDirectory()) {
                check(file,defaultPath+"\\"+fileName,checkWord);
            } else {
                CheckWord(file,checkWord);
            }
        });
    }

    private static void CheckWord(File file, String checkWord) {
        if (!file.canRead()) {
            System.out.println("文件："+file.getAbsolutePath()+"没有读取权限");
        }
        try(InputStream in = new FileInputStream(file);
            InputStreamReader ISR = new InputStreamReader(in);
            BufferedReader BR = new BufferedReader(ISR)) {
            String str = null;
            int line = 1;
            while ((str = BR.readLine())!=null) {
                if (KMP(str,checkWord)) {
                    System.out.println(file.getAbsolutePath()+"→第"+line+"行找到"+checkWord);
                }
                line++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("文件："+file.getAbsolutePath()+"读取出错");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean KMP(String str, String checkWord) {
        int len = checkWord.length();
        int[] next = new int[len];
        Next(checkWord,next);
        int i = 0,j = 0;
        while (i<str.length()&&j<checkWord.length()) {
            if  (j==-1||str.charAt(i)==checkWord.charAt(j)) {
                i++;
                j++;
            }else {
                j = next[j];
            }
        }
        return j==checkWord.length();
    }

    private static void Next(String checkWord, int[] next) {
        int j = 0,k = -1;
        next[0] = -1;
        while (j<checkWord.length()-1) {
            if(k==-1||checkWord.charAt(j)==checkWord.charAt(k)) {
                next[++j] = ++k;
            } else {
                k = next[k];
            }
        }
    }
}
