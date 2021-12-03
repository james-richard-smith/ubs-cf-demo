# UBS Cloud Foundry Demo

## About
- A Spring Boot application that reads and writes Employee data (id, name, email) to a MySQL/PostreSQL database.
- Also, comes with a rate-limiting app that we will use to create a route service.

## Instructions

### Build the project using maven:
```
mvn package
```

### Concepts covered:
- Deploying apps using a manifest (cf push -f)
- Creating and binding services (cf cs, cf bs)
- Creating user-provided-services (cf cups)
- Setting environment variables (cf set-env)

## Instructions

### Deploy the Spring Boot app ("demo")
```
cd ubs-cf-demo/demo
cf push -f manifest.yml --random-route --no-start
```

This will show as `down` since we used the flag `--no-start`

### Create the database service
In order to access to Postgres DB we need to create a Postgres service from PCF Marketplace.
Run the following code in the command line to see if Postgres service is available

```
cf marketplace

```
If Postgres service is not listed in PCF Marketplace due to UBS policy then use H2 database

If Postgres is listed under Marketplace services
Run the following code in the command line to see what plans are available for PostgreSQL:
```
cf marketplace -e postgres

```

Run the following code in the command line to create the Postgres service
```
cf create-service postgres PLAN SERVICE_NAME

```
where PLAN is the plan you want, and SERVICE_NAME is a unique descriptive name for this service instance. For example:
```
cf create-service postgres small-12 demo-db

```
It will take between 5 and 10 minutes to set up the service instance. To check its progress, run:
```
cf service SERVICE_NAME

```

### Bind the database service to "demo"
```
cf bind-service demo demo-db
cf restage demo
```

### Read and write to "demo"
```
curl https://<DEMO_URL>/employee
curl --request POST 'https://<DEMO_URL>/employee?name=bob&email=bob@bobsyouruncle.com'
curl https://<DEMO_URL>/employee
```

### Deploy rate limiter app ("ratelimiter")
```
cd ratelimit-service
cf push ratelimiter
```

### Turn "ratelimiter" into a route service for "demo"
The following will create a route service instance using a user-provided service
```
cf cups ratelimiter-service -r https://<RATE-LIMITER-URL>
```

### Bind "ratelimiter-service" service to "demo" route
When the route for "demo" is called, Cloud Foundry will call the ratelimiter-service.
```
cf bind-route-service <DOMAIN> ratelimiter-service --hostname <HOST_NAME>
```

### Set the rate limiting environment variable for "ratelimiter"
Setting this env var to 1, means that if we call the app more than once per second, we will see "Too many requests".
```
cf set-env ratelimiter RATE_LIMIT 1
cf restage ratelimiter
```

### Execute a script to call "demo" more than once per second
```
cd ..
bash ratelimiter-script.sh <DEMO_URL>
```
