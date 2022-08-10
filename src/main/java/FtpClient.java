import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


public class FtpClient {
    private String user;
    private String password;
    private String ip;
    private String localPath;
    private String serverPath;


    public FtpClient(String user, String password, String ip, String localPath, String serverPath) {
        this.user = user;
        this.password = password;
        this.ip = ip;
        this.localPath = localPath;
        this.serverPath = serverPath;
    }

    private void downloadFile() {
        try {
            String url = String.format("ftp://%s:%s@%s/%s", user, password, ip, serverPath);
            URLConnection connection = new URL(url).openConnection();

            try(InputStream inputStream = connection.getInputStream();
            OutputStream outputStream = new FileOutputStream(localPath)) {

                byte[] buffer = new byte[1024];
                while (inputStream.available() > 0) {
                    int count = inputStream.read(buffer);
                    outputStream.write(buffer, 0, count);
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void uploadFile(File file) {
        try {
            String url = String.format("ftp://%s:%s@%s/%s", user, password, ip, serverPath);
            URLConnection connection = new URL(url).openConnection();

            try (OutputStream outputStream = connection.getOutputStream();
            InputStream inputStream = new FileInputStream(file)){
                byte[] buffer = new byte[1024];
                while (inputStream.available() > 0) {
                    int count = inputStream.read(buffer);
                    outputStream.write(buffer, 0, count);
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<Student> getStudentsByName(String name) {
        return null;
    }

    public StudentDto getById(Integer id) {
        return null;
    }

    public boolean addStudent(StudentDto student) {
        return true;
    }

    public boolean deleteById(Integer id) {
        return true;
    }

 }

