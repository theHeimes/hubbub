<!DOCTYPE html>
<html>
<head>
  <title>Login</title>
  <meta name="layout" content="main">
</head>
<body>
  <h1>Login</h1>
  <g:form action="authenticate">
    <fieldset class="form">
      <div class="fieldcontain required">
        <label for="loginId">Login ID</label>
        <g:textField name="loginId" value="${user?.loginId}"></g:textField>
      </div>
      <div class="fieldcontain required">
        <label for="password">Password</label>
        <g:passwordField name="password"></g:passwordField>
      </div>
    </fieldset>
    <fieldset class="buttons">
      <g:submitButton name="register" value="Login"></g:submitButton>
    </fieldset>
  </g:form>
</body>
</html>