# Locomotor Web API

The Locomotor Web API allows to build applications that interact with *Locomotor* in
others ways than the interfaces we provide out of the box.

## Notice

The Web API consists of HTTP-[RPC](https://en.wikipedia.org/wiki/Remote_procedure_call) endpoints, all of the form `https://server.com/api/ENDPOINT`. Each endpoint represents a service.

All endpoints *must* be called using **HTTPS**. Arguments *must* be sent in **POST**.

The response is a JSON object, which will always
contain a top-level boolean property `success`, indicating *success* or *failure*. 

For *success*, the `data` property will contain the results of the request.

```json
{
  "success": true, // the request has successed
  "data": ... // the results object
}
```

In case of a `failure`, the `message` property will contain a short human-readable error message.

```json
{
  "success": false,
  "message": "An error message"
}
```

In the following code fences describing the API responses, only the ` state` is shown, hence the `data` property.

## Token

Except for authentification and registration, **all endpoint require a token**, which identifies the user. 

Our API come with two types of token: **long term** and **short term**.

### Long term token

The long term token is stored by the client. It allows the user to authentificate himself without having to type in a login and a password. It is a 256 characters random string.

### Short term token

The short term token is used to keep track of a client during a session. It has a lifespan of about 2 hours after the last connection. **This is the token require by endpoint.**

# API endpoints

In all the definitions below, the **short term token** given by the user is represented by the character `$`.

## User

### Authentification

This method authentifies the user and provide a short term token, used for others endpoints.

If the client already have a long term token, it can authentificate itself with it instead of asking the user to type in a password and a login.

```
api/user/auth
```

#### With a token

##### Arguments

| Argument | Description         |
| :------- | :------------------ |
| token    | the long term token |

##### Response

```json
{
  "success": true,
  "data": {
    "short-term-token": n
  }
}
```

#### With login

##### Arguments

| Argument | Description  |
| :------- | :----------- |
| login    | the username |
| password | the password |

##### Response

```json
{
  "success": true,
  "data": {
    "short-term-token": n
    "long-term-token": o,
  }
}
```

### Revoke

This method revokes an short term token. For example, call this to log out a user.

```
api/user/revoke
```

##### Arguments

| Argument | Description          |
| :------- | :------------------- |
| token    | the short term token |

##### Response

```json
{
  "success": true,
  "data": {}
}
```

### Register

This method is used to create a new user.

```
api/user/register
```

##### Arguments

| Argument | Description  |
| :------- | :----------- |
| login    | the username |
| password | the password |

##### Response

```json
{
  "success": true,
  "data": {
    "short-term-token": n
    "long-term-token": o,
  }
}
```

### Get notifications

This method is used to get all the pending notifications of the user.

```
api/user/get-notifications
```

##### Arguments

| Argument | Description          |
| :------- | :------------------- |
| token    | the short term token |

##### Response

```json
{
  "success": true,
  "data": {
    "notif": [
     	{...},
       	{...},
      	...
    ]
  }
}
```

The response contains a list of notifications objects.

# Object types

These are the core objects you'll find, as part of API arguments or responses.

### Notifications

A notification object contains information about the user's notification.

```json
{
  "id": "01234",
  "message": "This is a message"
}
```

| Attribute | Type   | Description                              |
| :-------- | :----- | ---------------------------------------- |
| id        | string | the unique identifier of the notification |
| message   | string | the notification message                 |

