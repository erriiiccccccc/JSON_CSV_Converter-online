<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSON to CSV Converter</title>
<style>
  .convert-button {
    padding: 1.2em 2em; 
    font-size: 1.2em; 
    background-color: #007bff; 
    color: #fff;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    text-decoration: none;
    display: inline-block;
    box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
  }
  .convert-button:hover {
    background-color: #0056b3; 
  }
  body {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    margin: 0;
    flex-direction: column; 
    font-family: Arial, sans-serif; 
  }
  h1 {
    margin-bottom: 20px;
  }
  form {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  label {
    margin-bottom: 10px;
  }
  input[type="file"] {
    width: 200px; 
  }
  .file-input-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 20px;
  }
  .file-input-container input[type="file"] {
    margin-bottom: 5px;
  }
</style>
</head>
<body>
    <h1>JSON to CSV Converter</h1>
    <form action="/JSON_CSV_Converter/FileUploadServlet" method="post" enctype="multipart/form-data">
        <div class="file-input-container">
            <label for="fileInput">Select a .json file:</label>
            <input type="file" name="fileInput" id="fileInput">
        </div>
        <button type="submit" class="convert-button">Convert!</button>
    </form>
</body>
</html>
