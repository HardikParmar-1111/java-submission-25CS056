# MiniBank

The Java files are grouped into folders by practical number. Each folder holds the code first added for that practical. The later practicals build on the earlier account and service classes.

| Folder | Main work |
|---|---|
| `1st` | Bank information and menu |
| `2nd` | Customer and account classes |
| `3rd` | Account printing, comparison, address and clone demo |
| `4th` | Input validation, command parser and statement |
| `5th` | Savings, current and fixed-deposit accounts |
| `6th` | Interfaces and withdraw-rule examples |
| `7th` | Annotations and reflection validation |
| `8th` | Bank exceptions and try-with-resources |
| `9th` | Account worker and synchronized deposits |
| `10th` | Thread pool, transaction buffer and transfers |
| `11th` | Save/load, transaction log and report |
| `12th` | Account store and sorting |
| `13th` | Generic repository |
| `14th` | Final menu, service and batch operations; automated tests are in `tests/` |

Use JDK 17 or newer and Maven:

```text
mvn test
mvn package
java -jar target/mini-bank-1.0.jar
```

The app saves account data and logs in `data/`.
