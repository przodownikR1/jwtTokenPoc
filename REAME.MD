==JWT TOKEN - SPRING SECURITY POC


`userOne  -> "przodownik@tlen.pl", "przodownik", "Slawek", "Borowiec", bCryptPasswordEncoder.encode("secret1"), Set.of("USER"));

userTwo -> "kowalski@tlen.pl", "kowal", "Jan", "Kowalski", bCryptPasswordEncoder.encode("secret2"), Set.of("USER", "MANAGER")

userThree -> "admin@tlen.pl", "wespa", "Wlodzimierz", "Lenin", bCryptPasswordEncoder.encode("secret3"), Set.of("ADMIN") `

**Useful links :**

https://jwt.io/   -> Encoded jwt token

`"alg": "HS512"
{
  "sub": "przodownik@tlen.pl",
  "iat": 1589414179,
  "exp": 1589414679
}`

http://localhost:8096/swagger-ui.html

**management:** 

http://localhost:8097/ops


**Convenient dev tricks**

./gradlew bootrun -PdebugPort=5005 - enable debug mode

./gradlew docker - create docker image




