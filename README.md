# android-instagram-connector
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
