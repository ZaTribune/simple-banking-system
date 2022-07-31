## simple-banking-system

Everything goes digital these days, and so does money.  
Today, most people have credit cards, which save us time, energy and nerves.  
From not having to carry a wallet full of cash to consumer protection, cards make our lives easier in many ways.  
In this simple project, we will develop a simple CLI banking system with database.  

### OverView

This was a project I studied and worked on via this awesome educational platform 
called [JetBrains Academy](https://hyperskill.org/).  
What's more interesting is that you can access it locally via IntelliJ's 
[EduTools plugin](https://plugins.jetbrains.com/plugin/10081-edutools).   

<p align="center">
<img height="300" src="preview.png" alt="overview"/>
</p>

### Technologies
- Java 11
- gradle
- JDBC
- SQLite db

### Notes
- This project uses an "SQLite database" added on project directory.
- We need to pass the database file location as a JVM arg when running the application.

### Testing & Deployment
- Run the application via gradle command:
```
./gradlew  :app:run  --args="databaseName card.s3db"
```

## Authors
[![Linkedin](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white&label=Muhammad%20Ali)](https://linkedin.com/in/zatribune)
