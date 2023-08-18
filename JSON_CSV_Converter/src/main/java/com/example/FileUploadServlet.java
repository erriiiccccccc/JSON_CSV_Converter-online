package com.example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/FileUploadServlet")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {
	
    
	
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
    
    private static String UPLOAD_DIRECTORY = "";
    private static String OUTPUT_DIRECTORY = "";

//    String resourcesDir = getServletContext().getRealPath("/resources") + File.separator;
//    String uploadPath = resourcesDir + "upload";
//    private final String UPLOAD_DIRECTORY = uploadPath;

	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	Properties properties = new Properties();

        try (InputStream fis = FileUploadServlet.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(fis);

            UPLOAD_DIRECTORY = properties.getProperty("UPLOAD_DIRECTORY");
            OUTPUT_DIRECTORY = properties.getProperty("OUTPUT_DIRECTORY");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	deleteFolderContents(new File(OUTPUT_DIRECTORY));
        deleteFolderContents(new File(UPLOAD_DIRECTORY));
        
        try {
            Part filePart = request.getPart("fileInput"); 
            if (filePart != null && filePart.getSize() > 0) {

            	String submittedFileName = extractFileName(filePart);

                if (!submittedFileName.toLowerCase().endsWith(".json")) {
                    String errorMessage = "Please select a JSON file to upload.";
                    response.getWriter().println(getErrorHtmlResponse(errorMessage));
                    return;
                }
            	
                InputStream fileContent = filePart.getInputStream();

                File folder = new File(UPLOAD_DIRECTORY);
                if(!folder.exists()) {folder.mkdir();}

                Files.copy(fileContent, Paths.get(UPLOAD_DIRECTORY, submittedFileName), StandardCopyOption.REPLACE_EXISTING);

                response.getWriter().println(getErrorHtmlResponse("File uploaded successfully!" + UPLOAD_DIRECTORY));
                
                executePythonScript("main.py");

//				  executing python script in own
//                Process process = Runtime.getRuntime().exec("python D:/Python/main.py");
//                process.waitFor();

                response.sendRedirect("/JSON_CSV_Converter/UI/download.jsp");
            } else {
                String errorMessage = "Please select a file to upload.";
                response.getWriter().println(getErrorHtmlResponse(errorMessage));
            }
        } catch (Exception e) {
            String errorMessage = "File upload failed due to: " + e.getMessage();
            response.getWriter().println(getErrorHtmlResponse(errorMessage));        
        }
    }
    
    private void executePythonScript(String scriptName) throws IOException, InterruptedException {
        String pythonDir = getServletContext().getRealPath("/WEB-INF/classes/python") + File.separator;
        String pythonScriptPath = pythonDir + scriptName;

        ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);
        Process process = processBuilder.start();
        process.waitFor();
    }

    private String extractFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                return element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "unknown";
    }
    
    private String getErrorHtmlResponse(String message) {
        return "<html><head><title>File Upload Error</title><style>h2 { color: black; font-family: Arial, sans-serif; }</style></head><body><h2>"
                + message
                + "</h2></body></html>";
    }

}

