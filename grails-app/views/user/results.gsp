<!DOCTYPE html>
<html>
<head>
  <title>Search Results</title>
  <meta name="layout" content="main" />
</head>
<body>
  <h1>Results</h1>
  <p>Searched ${totalUsers} records for 
    items matching <em>${term}</em>.
    Found <strong>${users.size} hits.</strong>
  </p>
  <ul>
    <g:each var="user" in ="${users}">
      <li>${user.loginId}</li>
    </g:each>
  </ul>
  <g:link action="search">Search again</g:link>
</body>
</html>