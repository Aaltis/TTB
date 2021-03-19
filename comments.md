Jotta aclobjectidentityrepository l�yt�� luokan, tulee luokalla olla getID()
---

päivittäessä version 5.0... 5.3.6 aclcontect caching muuttunut, dokumentaatiota ei ole.
lähin idea löytyi stackoverflowssta:  
    //https://stackoverflow.com/questions/56157479/spring-security-acl-with-ehcache-3

----

SPRINGissä on sisäsyntyinen ongelma jossa yritetään käyttää "class_id_type" tyypin löytämiseen.
aiheuttaa errorin mutta ei estä toimintaa.

ERROR:
2021-01-25 15:07:09.399 DEBUG 7752 --- [nio-8080-exec-3] o.s.security.acls.jdbc.AclClassIdUtils   : Unable to obtain the class id type

org.postgresql.util.PSQLException: The column name class_id_type was not found in this ResultSet.

https://stackoverflow.com/questions/56237719/aclclassidutils-unable-to-obtain-the-class-id-type

"class_id_type is selected from acl_class in default BasicLookupStrategy, but class_id_type column has been changed to class in spring security official site"

VMP:
https://github.com/spring-projects/spring-security/issues/7978

Ratkaisu:
Uudelleenimplementoi aclclassidutils ja basiclookupstrategy että voi muutta myclassidutils rigvin 24 class_id_type -> class

---
SWAGGER:
- Swagger implemenattio vaati ei tykkää @EnableWebMvc
https://github.com/springdoc/springdoc-openapi/issues/236

-Turhat oletusendpointit pois (Tietty apipolku palauttaa oletuksena 201, oletuksena swagger n�ytt�� 200)
.useDefaultResponseMessages(false);

---
Paremmat repo queryt:
https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#query-by-example.introduction

