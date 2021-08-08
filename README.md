= Gateway

Service initially retrieving all requests from users and forwarding them to the respective services.

== Structure
A user is required to be logged in to send any requests except for /login, which is forwarded to the user-services login-route. This is ensured via a GatewayFilter.

== Tech-Stack
The gateway uses the Spring Cloud Gateway framework to forward requests to their respective services.

== Further Reading
Please refer to the https://git.thm.de/microservicesss21/orga/-/blob/master/README.md[README] of the Orga-Repository for more information.
This service uses the https://git.thm.de/microservicesss21/service-lib/-/blob/master/README.md[service-lib] as a dependency.
