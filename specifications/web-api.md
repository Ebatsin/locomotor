# Locomotor HTTPS API

The core of *Locomotor* allows JSON exchanges bewteen himself and the client over HTTPS. Once the client is connected to the server, it receives a token which allows him to exchange with the server and stay authentificated.

The server proposes an API with which the client can request services. All endpoint accept parameters sent in **POST**. The server send back a json object of the following form :

```json
{
  "success": true, // the request has successed
  "data": ... // the core response
}
```

In case of a failure, the response is of the following form :

```json
{
  "success": false,
  "message": "An error message"
}
```

In the following code fences describing the server responses, only the "data" attribute is shown.

## Connection & Authentification

The first step to exchange with the server is to authentificate the client. To do so, the client must send a login and a password. To which the server will respond with a long term token and a short term token.

### Long term token

The long term token is stored by the client. It allows the user to authentificate himself without having to type in a login and a password. It is a 256 characters random string.

### Short term token

The short term token is used to keep track of a client during a session. It has a lifespan of about 2 hours after the last connection.

### Connection with a token

If the client have a long term token, it can authentificate itself with it instead of asking the user to type in a password and a login

# API endpoints

In all the definitions below, the short term token given by the user is represented by the character **$**.

All requests must be served to `https://serveraddress/api/...`

## Authentification

### With login

\> api/user/auth

**Parameters :**

* **login**: the username of the user
* **password**: the password of the user

**Returns:**

```json
{
  "short-term-token": n,
  "long-term-token": o
}
```



### With token

\> api/user/auth

**Parameters :**

* **token**: the user's token

**Returns:**

```json
{
  "short-term-token": n
}
```



## Get the notifications

\> api/user/get-all-notifications

**Parameters:**

* **token**: the user's token

**Returns:**

```json
[
  "notification 1",
  "notification 2",
  ...
]
```



## Get the model

\> api/model/get

**Parameters:**

none

**Returns:**

```

```