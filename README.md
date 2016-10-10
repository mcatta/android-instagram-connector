# Android Instagram Connector
Android Instagram Connector: use Instagram API

## USE
Easy to use, only two steps.

### Example:

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
## LICENSE

> Copyright 2016 Marco Cattaneo

> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this file except in compliance with the License.
> You may obtain a copy of the License at

>    http://www.apache.org/licenses/LICENSE-2.0

> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.
