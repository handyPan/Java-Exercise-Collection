/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handypan.ex00.SchoolManagementSystem;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
//JsonGenerationException;
//JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Console;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author handyPan
 */
public class Tester {
    
    public void bufferedReaderCharacter() throws IOException {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        char ch;
        System.out.println("Input characters, enter 'q' to exit.");
        
        do {
            ch = (char) br.read();
            System.out.println(ch);
        } while (ch != 'q');
    }
    
    public void bufferedReaderString() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        System.out.println("Enter lines of text. Enter 'end' to exit.");
        
        do {
            str = br.readLine();
            System.out.println(str);
        } while (!str.equals("end"));
    }
    
    public void writeDemo() {
        int b;
        b = 'A';
        System.out.write(b);
        System.out.write('\n');
    }
    
    public void readWriteFile1(String fileName) {
        try {
            byte bWrite[] = {11, 21, 3, 40, 5};
            OutputStream os = new FileOutputStream(fileName);   // "./test.txt"
            for (int x = 0; x < bWrite.length; x ++) {
                os.write(bWrite[x]);
            }
            os.close();
            
            InputStream is = new FileInputStream(fileName);
            int size = is.available();
            
            for (int i = 0; i < size; i++) {
                System.out.print((char) is.read() + " ");
            }
            is.close();
        } catch (IOException e) {
            System.out.print("Exception");
        }
    }
    
    public void readWriteFile2(String fileName) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        File f = new File(fileName);   // "./test2.txt"
        
        FileOutputStream fos = new FileOutputStream(f);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        osw.append("This is line 1.");
        osw.append("\r\n");
        osw.append("中文字符");
        osw.append("\r\n");
        osw.append("This is line 2.");
        osw.close();
        fos.close();
        
        FileInputStream fis = new FileInputStream(f);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        StringBuffer sb = new StringBuffer();
        while (isr.ready()) {
            sb.append((char) isr.read());
        }
        System.out.println(sb.toString());
        isr.close();
    }
    
    public void readWriteBinaryFile1(String fileName, String content) throws FileNotFoundException, IOException {
        // using the file output/input stream
        // write
        byte[] dataToWrite = content.getBytes(StandardCharsets.UTF_8);
        File f = new File(fileName);
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(dataToWrite, 0, dataToWrite.length);
        fos.flush();
        fos.close();
        
        // read
        byte[] dataToRead = new byte[(int)f.length()];
        FileInputStream fis = new FileInputStream(f);
        fis.read(dataToRead, 0, dataToRead.length);
        String str = new String(dataToRead, StandardCharsets.UTF_8);
        System.out.println(str);
        fis.close();
    }
    
    public void readWriteBinaryFile2(String fileName, String content) throws IOException {
        // using Files class
        // write
        Path path = Paths.get(fileName);
        byte[] dataToWrite = content.getBytes(StandardCharsets.UTF_8);
        try {
            Files.write(path, dataToWrite);
            System.out.println("Successfully write!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // read
        try {
            byte[] dataToRead = Files.readAllBytes(path);
            String str = new String(dataToRead, StandardCharsets.UTF_8);
            System.out.println(str);
            System.out.println("Successfully read!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void readWriteBinaryFile3(String fileName, String content) throws FileNotFoundException, IOException {
        File f = new File(fileName);
        // write
        try (FileOutputStream fos = new FileOutputStream(f);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            DataOutputStream dos = new DataOutputStream(bos);
        ){
            dos.writeUTF(content);
            System.out.println("Successfully write!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // read
        try (FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
        ) {
            String line;
            while((line = dis.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("Successfully read!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void readFileByLineUsingBufferedReader(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while (line != null) {
                System.out.println(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void readFileByLineUsingScanner(String fileName) {
        try {
            Scanner scan = new Scanner(new File(fileName));
            while (scan.hasNextLine()) {
                System.out.println(scan.nextLine());
            }
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void readFileByLineUsingFiles(String fileName) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void readFileByLineUsingRandomAccessFile(String fileName) {
        try {
            RandomAccessFile raf = new RandomAccessFile(fileName, "r");
            String line;
            while ((line = raf.readLine()) != null) {
                System.out.println(line);
            }
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void mkdirDemo(String dirName, String dirsName) {
        // String dirName = "./test";
        File dir = new File(dirName);
        System.out.println(dir.mkdir()? String.format("%s created.", dirName) : String.format("%s already exists.", dirName));
        System.out.println(dir.mkdir()? String.format("%s created.", dirName) : String.format("%s already exists.", dirName));
        // String dirsName = "./test2/usr/output";
        File dirs = new File(dirsName);
        System.out.println(dirs.mkdirs()? String.format("%s created.", dirsName) : String.format("%s already exists.", dirsName));
        System.out.println(dirs.mkdirs()? String.format("%s created.", dirsName) : String.format("%s already exists.", dirsName));
    }
    
    public void listDir(String dirName) {
        File dir = new File(dirName);
        if (dir.isDirectory()) {
            System.out.println("Directory: " + dirName);
            String d[] = dir.list();
            for (String _d : d) {
                File f = new File(dir + "/" + _d);
                if (f.isDirectory()) {
                    System.out.println(_d + " is a directory");
                } else {
                    System.out.println(_d + " is a file");
                }
            }
        } else {
            System.out.println(dir + " is a file");
        }
    }
    
    public void deleteDir(String dirName) {
        File dir = new File(dirName);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteDir(f.getName());
                } else {
                    f.delete();
                    System.out.println(String.format("%s is deleted.", f.getPath()));
                }
            }
        }
        dir.delete();
        System.out.println(String.format("%s is deleted.", dir.getPath()));
    }
    
    public void sendKeys() throws InterruptedException {
        try {
            Robot robot = new Robot();
            Scanner scan = new Scanner(System.in);
            
            // simulate a mouse click
            robot.mouseMove(800, 1000);
            TimeUnit.SECONDS.sleep(1);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            TimeUnit.SECONDS.sleep(1);
            
            System.out.print("Input a string:");
            TimeUnit.SECONDS.sleep(1);
            
            // simulate a key press
            /*
            robot.keyPress(KeyEvent.VK_SHIFT);robot.keyPress(KeyEvent.VK_H);robot.keyRelease(KeyEvent.VK_H);robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.setAutoDelay(200);
            robot.keyPress(KeyEvent.VK_E);robot.keyRelease(KeyEvent.VK_E);Thread.sleep(200);
            robot.keyPress(KeyEvent.VK_L);robot.keyRelease(KeyEvent.VK_L);Thread.sleep(200);
            robot.keyPress(KeyEvent.VK_L);robot.keyRelease(KeyEvent.VK_L);Thread.sleep(200);
            robot.keyPress(KeyEvent.VK_O);robot.keyRelease(KeyEvent.VK_O);Thread.sleep(200);
            robot.keyPress(KeyEvent.VK_COMMA);robot.keyRelease(KeyEvent.VK_COMMA);Thread.sleep(200);
            robot.keyPress(KeyEvent.VK_SPACE);robot.keyRelease(KeyEvent.VK_SPACE);Thread.sleep(200);
            robot.keyPress(KeyEvent.VK_W);robot.keyRelease(KeyEvent.VK_W);Thread.sleep(200);
            robot.keyPress(KeyEvent.VK_O);robot.keyRelease(KeyEvent.VK_O);Thread.sleep(200);
            robot.keyPress(KeyEvent.VK_R);robot.keyRelease(KeyEvent.VK_R);Thread.sleep(200);
            robot.keyPress(KeyEvent.VK_L);robot.keyRelease(KeyEvent.VK_L);Thread.sleep(200);
            robot.keyPress(KeyEvent.VK_D);robot.keyRelease(KeyEvent.VK_D);Thread.sleep(200);
            robot.keyPress(KeyEvent.VK_SHIFT);robot.keyPress(KeyEvent.VK_1);robot.keyRelease(KeyEvent.VK_1);robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.setAutoDelay(200);
            robot.keyPress(KeyEvent.VK_ENTER);robot.keyRelease(KeyEvent.VK_ENTER);Thread.sleep(200);
            */
            String msg = "Hello, world!";
            for (char ch : msg.toCharArray()) {
                if (ch == '!') {
                    ch = '1';
                    robot.keyPress(KeyEvent.VK_SHIFT);
                }
                if (Character.isUpperCase(ch)) {
                    robot.keyPress(KeyEvent.VK_SHIFT);
                }
                
                robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(ch));
                robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(ch));
                if (Character.isUpperCase(ch)) {
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                }
                if (ch == '1') {
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                }
                Thread.sleep(200);
            }
            robot.keyPress(KeyEvent.VK_ENTER);robot.keyRelease(KeyEvent.VK_ENTER);Thread.sleep(200);
            
            TimeUnit.SECONDS.sleep(1);
            
            String str = scan.nextLine();
            System.out.println(String.format("The input is %s", str));
            
            
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
    
    // the inner class below is for testing writeJson()
    public class Country {
        public String name;
        public Integer population;
        public Date date;
        public Boolean isValid;
        public Double precision;
        public List<String> states;
        public Map<Integer, String> states2;
        public Map<Integer, Object[]> states3; 
        public Map<Integer, Object> states4;
        public Map<Integer, Object[]> states5;
    }
    
    public void writeJson(String filePath) throws ParseException {
        Country c = new Country();
        c.name = "Canada";
        c.population = 35000000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        c.isValid = true;
        c.precision = 0.002;
        c.date = sdf.parse("2010-10-08");
        /*
        List<String> listOfStates = new ArrayList<String>();
        listOfStates.add("Ontario");
        listOfStates.add("British Columbia");
        listOfStates.add("Montreal");
        c.states = listOfStates;
        */
        c.states = Arrays.asList("Ontario", "British Columbia", "Quebec");
        c.states2 = new TreeMap<Integer, String>() {
            {
                put(0, "Ontario");
                put(1, "British Columbia");
                put(2, "Quebec");
            }  
        };
        c.states3 = new TreeMap<Integer, Object[]>() {
            {
                put(0, new Object[]{"Ontario", "ON"});
                put(1, new Object[]{"British Columbia", "BC"});
                put(2, new Object[]{"Quebec", "QC"});
            }
        };
        
        Map<String, String> map0 = new TreeMap<String, String>() {
            {
                put("capital", "Toronto");
            }
        };
        Map<String, String> map1 = new TreeMap<String, String>() {
            {
                put("capital", "Montreal");
            }
        };
        Map<String, String> map2 = new TreeMap<String, String>() {
            {
                put("capital", "Vancouver");
            }
        };
        Map<String, String> map3 = new TreeMap<String, String>() {
            {
                put("capital", "Winnipeg");
            }
        };
        c.states4 = new TreeMap<Integer, Object>();
        c.states4.put(0, map0);
        c.states4.put(1, map1);
        c.states4.put(2, map2);
        c.states4.put(3, map3);
        
        // each object in Object[] has the data structure of Integer, String, String, Map<Integer, String>
        c.states5 = new TreeMap<Integer, Object[]>();
        // use add() of ArrayList to input values to Object[]
        Object[] obj = new Object[]{};
        ArrayList<Object> tmp = new ArrayList<Object>(Arrays.asList(obj));
        tmp.add(0); tmp.add("Ontario"); tmp.add("ON"); tmp.add(map0);
        c.states5.put(0, tmp.toArray());
        tmp = new ArrayList<Object>(Arrays.asList(obj));
        tmp.add(1); tmp.add("Quebec"); tmp.add("QC"); tmp.add(map1);
        c.states5.put(1, tmp.toArray());
        tmp = new ArrayList<Object>(Arrays.asList(obj));
        tmp.add(2); tmp.add("British Columbia"); tmp.add("BC"); tmp.add(map2);
        c.states5.put(2, tmp.toArray());
        tmp = new ArrayList<Object>(Arrays.asList(obj));
        tmp.add(3); tmp.add("Manitoba"); tmp.add("MA"); tmp.add(map3);
        c.states5.put(3, tmp.toArray());
        
        ObjectMapper om = new ObjectMapper();
        
        try {
            om.writeValue(new File(filePath), c);
            System.out.println(String.format("%s created.", filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void testLoadFile() throws IOException {
        // read studentCourseResults.data line by line and parse the string to fields
        Map<Integer, Object[]> studentCourseResult = new TreeMap<Integer, Object[]>();
        Map<Integer, Object> studentCourseResults = new TreeMap<Integer, Object>();
        Path path = Paths.get("./data/studentCourseResults.data");
        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                // skip the 1st line
                if (lines.indexOf(line) == 0) {
                    continue;
                }
                System.out.println(line);
                List<String> fields = Arrays.asList(line.split(";"));
                // studentCourseResults: student id, course id, is approved, is enrolled, final grade
                studentCourseResult.put(Integer.parseInt(fields.get(1)), new Object[]{Boolean.parseBoolean(fields.get(2)), Boolean.parseBoolean(fields.get(3)), Integer.parseInt(fields.get(4))});
                studentCourseResults.put(Integer.parseInt(fields.get(0)), studentCourseResult);
            }
        }
    }
    
    public void readPasswordAsAsterisk() {
        Console console = System.console();
        if (console == null) {
            System.out.println("No console available.");
            return;
        }
        String pwd = new String(console.readPassword("Enter the password:"));
        System.out.println(String.format("The entered password is: %s", pwd));
    }
    
    
    // the inner class below is for mask password with asterisk in java console
    public class ThreadDisappear implements Runnable {
        private boolean end;
        public ThreadDisappear(String prompt) {
            System.out.print(prompt);
        }
        public void run() {
            end = true;
            while (end) {
                System.out.print("\010*");
                try {
                    Thread.currentThread().sleep(1);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }

        public void maskEnd() {
            this.end = false;
        }

    }
    
    public void readPasswordAsAsteriskByThread() {
        ThreadDisappear td = new ThreadDisappear("Enter your password: ");
        Thread t = new Thread(td);
        t.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String password = br.readLine();
            td.maskEnd();
            System.out.println("\nYour password is: " + password);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        Tester t = new Tester();
        // t.bufferedReaderCharacter();
        // t.bufferedReaderString();
        // t.writeDemo();
        // t.mkdirDemo("./test", "./test2/usr/output");
        // t.readWriteFile1("./test/test.txt");
        // t.readWriteFile2("./test2/usr/test2.txt");
        // t.listDir("./");
        // t.listDir("./test/test.txt");
        // t.listDir("./src");
        // t.listDir("./dummy");
        // t.listDir("./dummy.txt");
        // t.deleteDir("./test");
        // t.deleteDir("./test2"); 
        /*
        t.mkdirDemo("./test", "./test2");
        String str = """
                     This is a test for read and write to binary file using Java.
                     This is line 2.
                     The end line.
                     """;
        */
        
        // String fileName = "./test/test.txt";
        // t.readWriteBinaryFile1(fileName, str);
        // t.readWriteBinaryFile2(fileName, str);
        // t.readWriteBinaryFile3(fileName, str);
        // t.readFileByLineUsingBufferedReader(fileName);
        // t.readFileByLineUsingScanner(fileName);
        // t.readFileByLineUsingFiles(fileName);
        // t.readFileByLineUsingRandomAccessFile(fileName);5/, /
        // t.sendKeys();
        // t.writeJson("./data/test.json");
        
        // t.testLoadFile();
        // t.readPasswordAsAsterisk();  // not working in NetBeans IDE
        // t.readPasswordAsAsteriskByThread();  // not working in NetBeans IDE
    }
   
}
