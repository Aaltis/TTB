# TRACKTHATBARBELL

## Project main goal:
How to create api secured with Spring Security ACL.
This project is to test spring security acl, so it full of not-so-optimal decisions.

## TODO
- Add check to GetMovementsToWorkout error returns if too many or none found
- Add logging filter.
- Spamming protection
- Implement swagger
- Randomize workout per chosen muscles.
- Tests for sub exercise.
- failing tests
- implement peppering to security.
- dockerization.
- gitlab staging version.
- Where to run this cheaply?
- Add filtering to all apis.
	- register
		- password twice
		- email regex check
		- send email to user?
	
## Endpoints:
movement/all
movement?name=''&type='' either or.

POST api/authentication/register
json body:
{
    "username":"jumala",
    "password":"jumala"
}

Creates user, logins and returns jwt token.


POST api/authentication
 json body:
{
    "username":"jumala",
    "password":"jumala"
}

Logins and returns jwt token.

GET api/user/workouts
send X-Auth-Token as header, jwt token for user
get user workouts

POST api/user/workouts

{
    "workoutname": "test",
    "exercises": [{
        "ordernumber": "1",
        "movementname": "bench",
        "SetRepsWeight": [{
            "ordernumber": "1",
            "set": "5",
            "reps": "5",
            "weight": "100",
            "weightunit": "kg"
            }]
        }]
}



docker pull postgres
docker run --name ttb-postgres -e POSTGRES_PASSWORD=mysecretpassword -d postgres

 docker build --build-arg JAR_FILE=build/libs/*.jar -t trackthatbarbell .
 docker run -p 8080:8080 -t trackthatbarbell 
 
 

