# Android Instagram Connector
Android Instagram Connector: use Instagram API

### Implementation example:

```java
Instagram instagram = Instagram.newInstance(this, "<CLIENT_ID>", "<CLIENT_SECRET>", "http://callback");
instagram.getSession(new InstagramListener() {
    @Override
    public void onConnect(InstagramSession session) {
        session.execute("/users/self", new RequestCallback() {
            @Override
            public void onResponse(int resultCode, @Nullable String body) {
                // body = json response
            }
        });
    }

    @Override
    public void onError(ConnectionError error) {

    }
});
```
### Authorization scope:
To manage login permission, [for more detail](https://www.instagram.com/developer/authorization/) .
```java
Instagram instagram = Instagram.newInstance(this, "<CLIENT_ID>", "<CLIENT_SECRET>", "http://callback");
instagram.addScopes(new HashSet<Scope>() {{
    add(Scope.BASIC);
    add(Scope.PUBLIC);
    add(Scope.FOLLOWER);
}});

instagram.getSession(new InstagramListener() {
    //... implementation
});
```
