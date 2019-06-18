# Revolut Coding Challenge

## Build
This project requires `Java 1.8` or higher to execute and uses *Gradle* as build tool. Follows a quick description of the most important tasks:

- `run`: Runs this project as a JVM application.
- `integrationTest`: Runs the integration tests.
- `test`: Runs the unit tests.
- `clean`: Deletes the build directory.

## Service Contracts
#### POST /accounts
Create a new account based on an email identifier with the default value of 100.0. The consumer must provide the email in the request body and the email must be unique.

Example:
```
{
	"email": "john.doe@email.com"
}
```
- Returns 201 (CREATED) and the account details in the response body when the account was successfully created.
- Returns 409 (CONFLICT) when the email is already used by another account.
- Returns 400 (BAD REQUEST) when email field is null or empty.

#### GET /accounts/{email}
Retrieve the account details for a given email.

- Returns 200 (CREATED) and the most recent details of the source account in the response body when an account exists for the submitted email.
- Returns 404 (NOT FOUND) when there is no account for the submitted email.

#### POST /transfers
Transfer a certain amount from a source account to a destination account. The consumer must provide the source email, the destination email, and the amount to be transferred in the request body.

Example:
```
{
	"source": "john.doe@email.com",
	"destination": "jane.doe@email.com",
	"amount": 10.00
} 
```

- Returns 200 (CREATED) and the most recent details of the source account in the response body when the transfer was successful.
- Returns 422 (UNPROCESSABLE ENTITY) when the transfer wasn't successful because of any wrong info sent by the consumer

## Notes for Improvements
- Create an `Email` class to hold all business logics related to this concept such as validations and uniqueness rules
- Add the concept of `Currency` to make `MoneyAmount` more realistic. This would open possibilities to business rules related to money transfer between different currencies
- Register every transaction to allow auditing
- Make transfer between accounts an atomic operation
