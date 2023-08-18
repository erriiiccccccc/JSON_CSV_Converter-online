<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Download CSV</title>
<style>
  .download-button {
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
  .download-button:hover {
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
    background-image: url("interns.jpg");
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
  }
  h2 {
    margin-bottom: 20px;
    background-color: white;
    padding: 5px;
  }
  .convert-button {
    padding: 0.8em 1.5em; 
    font-size: 1em; 
    background-color: #fff; 
    color: #007bff; 
    border: 2px solid #007bff; 
    border-radius: 8px;
    cursor: pointer;
    text-decoration: none;
    display: inline-block;
    box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
  }
  .convert-button:hover {
    background-color: #007bff; 
    color: #fff; 
  }
    .logo-image {
    max-width: 300px;
    height: auto;
    margin-bottom: 10px;
  }
</style>
</head>
<body>
    <h2>Download your CSV here :D</h2>
    <form action="/JSON_CSV_Converter/DownloadServlet" method="get">
        <input type="hidden" name="file" value="%s" />
        <button type="submit" class="download-button">Download!</button>
    </form>
    <br />
    <form action="/JSON_CSV_Converter/UI/index.jsp" method="get">
        <button type="submit" class="convert-button">Convert Another File</button>
    </form>
</body>
</html>
