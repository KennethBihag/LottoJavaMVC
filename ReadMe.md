# Info
## When running
- define persistenceUnitName=<any name>
- change persistence-unit name attribute in persistence.xml to be the same as the value set
- default is "com.kenneth.lotto"
- set context to "/lotto" and port to 8080 default
## Database
- create lotto_db schema in MySQL prior running
## Tomcat testing
- Client API
  - URL: http://localhost:8080/lotto/client?entries=<local path of csv>
    - example: http://localhost:8080/lotto/client?entries=C:/Users/Downloads/file.csv
  - if no entries set, use "body" as "raw text" and input csv lines
    - example: http://localhost:8080/lotto/client
    - body:
      - name1,1,2,3,4,5,6
      - name2,7,8,9,10,11,12
      - ...
  - If no entry is valid or if all entries are already uploaded, error will be received.
- Admin API
  - URL: http://localhost:8080/lotto/admin?prizePool=<integer val>
    - example: http://localhost:8080/lotto/admin?prizePool=100000
  - send a POST request and expect a result of a json array of winners
  - This can also be accessed via browser by going to home page: ...8080/lotto
- Admin/models API
  - URL: http://localhost:8080/lotto/admin/models?modelType=<type of object>
  - type of object can be: "entries","winningnumbers", or "winners"
  - example: http://localhost:8080/lotto/admin/models?modelType=winners