<!DOCTYPE html>
<html>
<head>
  <title>Upload Image</title>
  <meta name="layout" content="main">
</head>
<body>
  <h1>Upload an image</h1>
  <g:uploadForm action="upload">
    <label for="loginId">User ID:</label>
    <g:select name="loginId" from="${userList}" 
      optionKey="loginId" optionValue="loginId"></g:select>
    <p></p>
    <label for="photo">Photo:</label>
    <input name="photo" type="file"></input>
    <g:submitButton name="upload" value="Upload"></g:submitButton>
  </g:uploadForm>
</body>
</html>