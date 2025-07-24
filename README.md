# Eagle Bank

Eagle Bank is a modern web API designed for personal banking management.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Installation](#installation)
- [Usage](#usage)

## Features

- **User Authentication**: Secure login and registration for users.
- **Account Management**: Users can create, view and update their account details.
- **Transaction Management**: Ability to add transactions.
- **Transaction History**: View a history of all transactions.

## Getting Started

To get a local copy of Eagle Bank up and running, follow these simple steps.

### Prerequisites

- Java 21 or later
- Latest Maven

### Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/jacques-wentworth-work/eagle-bank.git
    ```
2. Navigate to the project directory:
    ```bash
    cd eagle-bank
    ```

### Usage

To run the application, you need to start both the backend servers.

1. Start the backend:
    ```bash
    mvn spring-boot:run
    ```

2. These files contain examples of REST API calls:
- [users-api.http](http/users-api.http)
- [auth-api.http](http/auth-api.http)
- [accounts-api.http](http/accounts-api.http)
- [transactions-api.http](http/transactions-api.http)

Project Link: [https://github.com/jacques-wentworth-work/eagle-bank](https://github.com/jacques-wentworth-work/eagle-bank)
