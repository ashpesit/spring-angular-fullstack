## Basic Curd application using Spring boot for backend and angular for frontend

### What does this application do? ###
1. We have Employees and Departments. We can CRUD both.
2. Any Employee can belong to 0-N Departments
3. All Employees must belong to a read-only Department called "Organisation"
4. Hardcode nothing about these rules and relationships.
5. Use in-mem H2 and liquibase yaml for all things schema.

### HLD ###
* Straight forward rest based application
* All the apis are synchronous.
* Has two primary entity and a mapping entity for storing the n to n relationship
* Has one in-mem database with no long term persistence enabled.


### LLD ###

#### Packages information ####

Type of Class               | Package                                         | Usage 
--------------------------  |-------------------------------------------------| -------------  
Controllers                 | com.wisetechglobal.exercise.controller          | Api controller classes
Configurations              | com.wisetechglobal.exercise.config              | Swagger and other configs
Services                    | com.wisetechglobal.exercise.service             | Crud logic
Request,Database entity     | com.wisetechglobal.exercise.persistence.model      | request/entity objects
Responses                   | com.wisetechglobal.exercise.controller.response | resposne objects
Repositories                | com.wisetechglobal.exercise.persistence.respository|database repositories
Shared Utilities            | com.wisetechglobal.exercise.utilities           | Shared Uitility classes

* 