package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {

//    String resourcesDir = getServletContext().getRealPath("/resources") + File.separator;
//    String uploadPath = resourcesDir + "upload";
//    String outputPath = resourcesDir + "output";
//    
//    private final String UPLOAD_DIRECTORY = uploadPath;
//    private final String OUTPUT_DIRECTORY = outputPath;
    
    private static String UPLOAD_DIRECTORY = "";
    private static String OUTPUT_DIRECTORY = "";
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	Properties properties = new Properties();

        try (InputStream fis = FileUploadServlet.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(fis);

            UPLOAD_DIRECTORY = properties.getProperty("UPLOAD_DIRECTORY");
            OUTPUT_DIRECTORY = properties.getProperty("OUTPUT_DIRECTORY");

        } catch (IOException e) {
            e.printStackTrace();
        }
    	
        File folder = new File(OUTPUT_DIRECTORY);
        if (folder.exists() && folder.isDirectory()) {
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"csv_files.zip\"");

            try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {

            	for (File file : folder.listFiles()) {
                    if (file.isFile() && file.getName().endsWith(".csv")) {
                        try (FileInputStream fileInputStream = new FileInputStream(file)) {

                        	zipOut.putNextEntry(new ZipEntry(file.getName()));
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                                zipOut.write(buffer, 0, bytesRead);
                            }
                            zipOut.closeEntry();
                        }
                    }
                }
//                deleteFolderContents(new File(OUTPUT_DIRECTORY));
//                deleteFolderContents(new File(UPLOAD_DIRECTORY));
            }
        } else {
            response.getWriter().println("CSV files folder not found.");
        }
    }
    
    public static void deleteFolderContents(File folder) {
        if (folder.isDirectory()) {
            File[] contents = folder.listFiles();
            if (contents != null) {
                for (File file : contents) {
                    deleteFolderContents(file);
                }
            }
        }
        try {
            Files.delete(folder.toPath());
            System.out.println("Deleted: " + folder.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Failed to delete: " + folder.getAbsolutePath());
            e.printStackTrace();
        }
    }
}
