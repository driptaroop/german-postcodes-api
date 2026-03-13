# functional requirement
- the german postcodes should be stored in a SQL database. That database should be the sole source of truth for all postcode lookup. 

# Technical requirement
- use the embedded h2 database as the sql database to store the postcodes.
- use Spring Boot JPA for database interfacing. for repository always JPARepository.
- define the ddl. define the indexes based on the search patterns.
- seed the database with the 3 postcodes from the code ("12107", "52062", "15837")

# development flow
- strictly follow the red-green TDD flow.