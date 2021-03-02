# Tools

Tools repository contains multiple small but useful tools. Some of the tools will be created for specific uses.

## Account Generator

Account Generator (AG) creates user account on Postgresql database User and Authority tables. These tables might be vary based on customized usage.

### Step to run

1. Configure accountGenerator.properties file under src/main/resources folder

   * Database information setup
     ```properties
     datasource.url=jdbc:postgresql://localhost:5432/db_name
     datasource.username=username
     datasource.password=password
     datasource.driver-class-name=org.postgresql.Driver
     ```

   * Password strength setup

     ```properties
     # BCrypt hashing function strength: value between 4 and 31. Default is 10
     password.encoder.strength=10
     ```

2. Execute AccountGenerator.java

   ```
   Enter username:
   New username
   Enter password: 
   New password
   ```

Once the following message pops up, the new account has been created:

```
New account: New username has been created!
```

