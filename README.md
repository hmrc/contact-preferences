
# Contact Preferences

This service provides APIs to update a Users Contact Preferences for a Tax Regime. This service has been built so that it can scale to support future Tax Regimes although currently the only supported regime is MTD VAT.

## Frontend Integration API Sequence Diagram

![Alt text](frontendApiSequence.png?raw=true "Frontend Integration API Sequence Diagram")

## APIs

- [Create Contact Preference Journey Context](#Create-Contact-Preference-Journey-Context)

- [Retrieve Journey Context](#Retrieve-Journey-Context)

- [Store Contact Preference for Frontend User Journey](#Store-Contact-Preference-for-Frontend-User-Journey)

- [Retrieve Contact Preference for Frontend User Journey](#Retrieve-Contact-Preference-for-Frontend-User-Journey)

- [Retrieve Stored Contact Preference from System of Record](#Retrieve-Stored-Contact-Preference-from-System-of-Record) **(NOT IMPLEMENTED)**

---

### Create Contact Preference Journey Context

`POST /contact-preferences/journey`

Provides an API for Frontend Microservices to call to generate a one-time journey.

A JourneyID will be generated and returned as a location header on the request with a redirect to the Contact Preferences Frontend microservice.

#### Request Body

- regime: JsonObject ***mandatory*** 
    - type: String ***mandatory** enum set: (`VAT`)*
    - identifier: JsonObject ***mandatory***
        - key: String ***mandatory** enum set: (`VRN`)*
        - value: String ***mandatory** - must be a valid identifier, e.g. valid VRN*
- continueUrl: String **mandatory** - must be a valid URL to redirect to
- email: String *optional* - if provided will be used as the email for digital communications
    
#### Responses

##### 201 (CREATED): 
    Response Body: N/A *empty*
    Response Headers: "Location" : "/contact-preferences/{journeyID}"
        
##### 400 (BAD_REQUEST): 
    Response Body: "Invalid JourneyModel payload: {json validation errors}"

##### 400 (BAD_REQUEST): 
    Response Body: "could not parse body due to {json validation errors}"
    
##### 401 (UNAUTHORISED): 
    Response Body: "The request was not authenticated"
    
##### 403 (FORBIDDEN): 
    Response Body: "The request was authenticated but the user does not have the necessary authority"
    
##### 500 (INTERNAL_SERVER_ERROR): 
    Response Body: "An error was returned from the MongoDB repository"

##### 500 (INTERNAL_SERVER_ERROR): 
    Response Body: "{error message}"

##### 503 (SERVICE_UNAVAILABLE): 
    Response Body: "An unexpected error occurred when communicating with the MongoDB repository"        
    

#### Example Request/Response

    POST /contact-preferences/journey    
    
    {
        "regime" : {
            "type" : "VAT",
            "identifier" : {
                "key" : "VRN",
                "value" : "999999999"
            }
        },
        "continueUrl" : "continueUrl",
        "email" : "email"
    }     
    
    Response Status: 201 (CREATED)
    Response Body: Empty
    Response Headers:
        location → /contact-preferences/42009459-90e8-416a-8947-37a60299680a

---        
        
### Retrieve Journey Context 

`GET /contact-preferences/journey/{journeyId}`

Provides an API for Frontend Microservices to call to get the journey context stored by the POST endpoint above.

#### Request Params
    
- `{journeyId}` ***mandatory*** *is the UUID returned in the location header response of [Create Contact Preference Journey Context](#Create-Contact-Preference-Journey-Context)*

    
#### Responses

##### 200 (OK): 
    Response Body:     
        {
             "regime" : {
                 "type" : "VAT",
                 "identifier" : {
                     "key" : "VRN",
                     "value" : "999999999"
                 }
             },
             "continueUrl" : "continueUrl",
             "email" : "email"
        }
    
##### 401 (UNAUTHORISED): 
    Response Body: "The request was not authenticated"
    
##### 403 (FORBIDDEN): 
    Response Body: "The request was authenticated but the user does not have the necessary authority"
    
##### 404 (NOT_FOUND): 
    Response Body: "Could not find JourneyContext matching JourneyID: {journeyId}"
    
##### 500 (INTERNAL_SERVER_ERROR): 
    Response Body: "An error was returned from the MongoDB repository"

##### 500 (INTERNAL_SERVER_ERROR): 
    Response Body: "{error message}"

##### 503 (SERVICE_UNAVAILABLE): 
    Response Body: "An unexpected error occurred when communicating with the MongoDB repository"        
    
    
#### Example Request/Response

    GET /contact-preferences/journey/42009459-90e8-416a-8947-37a60299680a    
    Response Status: 200 (OK)
    Response Body:
        {
            "regime" : {
                "type" : "VAT",
                "identifier" : {
                    "key" : "VRN",
                    "value" : "999999999"
                }
            },
            "continueUrl" : "continueUrl",
            "email" : "email"
        }
        
---

### Store Contact Preference for Frontend User Journey

`PUT /contact-preferences/{journeyId}`

Provides an API for the Frontend Microservice to call to store the preference picked by the User.

This endpoint is idempotent, if there is no existing preference record for the journey in MongoDBN it will be created, if there is an existing preference it will be updated.

#### Request Params
    
- `{journeyId}` ***mandatory*** *is the UUID returned in the location header response of [Create Contact Preference Journey Context](#Create-Contact-Preference-Journey-Context)*

#### Request Body

- preference: String ***mandatory*** enum set: (`DIGITAL`, `PAPER`)
    
#### Responses

##### 204 (NO_CONTENT): 
    Response Body: N/A *empty*
        
##### 400 (BAD_REQUEST): 
    Response Body: "Invalid ContactPreferenceModel payload: {json validation errors}"

##### 400 (BAD_REQUEST): 
    Response Body: "could not parse body due to {json validation errors}"
    
##### 401 (UNAUTHORISED): 
    Response Body: "The request was not authenticated"
    
##### 403 (FORBIDDEN): 
    Response Body: "The request was authenticated but the user does not have the necessary authority"
    
##### 500 (INTERNAL_SERVER_ERROR): 
    Response Body: "An error was returned from the MongoDB repository"

##### 500 (INTERNAL_SERVER_ERROR): 
    Response Body: "{error message}"

##### 503 (SERVICE_UNAVAILABLE): 
    Response Body: "An unexpected error occurred when communicating with the MongoDB repository"        
    

#### Example Request/Response

    PUT /contact-preferences/42009459-90e8-416a-8947-37a60299680a  
    
    {
        "preference" : "DIGITAL"
    }     
    
    Response Status: 204 (NO_CONTENT)
    Response Body: Empty

---

### Retrieve Contact Preference for Frontend User Journey

`GET /contact-preferences/{journeyId}`

Provides an API for Frontend Microservices to call to get the preference stored by the PUT endpoint above.

#### Request Params
    
- `{journeyId}` ***mandatory*** *is the UUID returned in the location header response of [Create Contact Preference Journey Context](#Create-Contact-Preference-Journey-Context)*
    
#### Responses

##### 200 (OK): 
    Response Body:     
        {
            "preference" : "DIGITAL"
        }
    
##### 401 (UNAUTHORISED): 
    Response Body: "The request was not authenticated"
    
##### 403 (FORBIDDEN): 
    Response Body: "The request was authenticated but the user does not have the necessary authority"
    
##### 404 (NOT_FOUND): 
    Response Body: "Could not find JourneyContext matching JourneyID: {journeyId}"
    
##### 500 (INTERNAL_SERVER_ERROR): 
    Response Body: "An error was returned from the MongoDB repository"

##### 500 (INTERNAL_SERVER_ERROR): 
    Response Body: "{error message}"

##### 503 (SERVICE_UNAVAILABLE): 
    Response Body: "An unexpected error occurred when communicating with the MongoDB repository"        
    
    
#### Example Request/Response

    GET /contact-preferences/42009459-90e8-416a-8947-37a60299680a    
    Response Status: 200 (OK)
    Response Body:
        {
            "preference" : "DIGITAL"
        }


---

### Retrieve Stored Contact Preference from System of Record

`GET /contact-preferences/{regimeType}/{regimeId}/{regimeIdValue`

Provides an API to call to get the preference stored in the backend System of Record.

#### Request Params
    
- `{regimeType}` ***mandatory*** enum set: (`vat`)
- `{regimeId}` ***mandatory*** enum set: (`vrn`)
- `{regimeIdValue}` ***mandatory***
    
#### Responses

##### 200 (OK): 
    Response Body:     
        {
            "preference" : "DIGITAL"
        }
    
##### 400 (BAD_REQUEST): 
    Response Body: "Invalid RegimeType supplied. Must be one of {valid regimeType enum set}"
    
##### 400 (BAD_REQUEST): 
    Response Body: "Invalid RegimeId supplied. Must be one of {valid regimeId enum set}"
    
##### 400 (BAD_REQUEST): 
    Response Body: "Invalid regimeIdValue supplied. Must be one of {valid regimeIdValue enum set}"
    
##### 401 (UNAUTHORISED): 
    Response Body: "The request was not authenticated"
    
##### 403 (FORBIDDEN): 
    Response Body: "The request was authenticated but the user does not have the necessary authority"
    
##### 412 (PRECONDITION_FAILED): 
    Response Body: "Downstream system of record has indicated that the record is in migration, try again later"

##### 500 (INTERNAL_SERVER_ERROR): 
    Response Body: "{error message}"

##### 503 (SERVICE_UNAVAILABLE): 
    Response Body: "Downstream system of record is unavailable, try again later"        
    
    
#### Example Request/Response

    GET /contact-preferences/vat/vrn/999999999    
    Response Status: 200 (OK)
    Response Body:
        {
            "preference" : "DIGITAL"
        }


---

### Update Stored Contact Preference from System of Record

`PUT /contact-preferences/{regimeType}/{regimeId}/{regimeIdValue`

Provides an API to call to update the preference stored in the backend System of Record.

#### Request Params

- `{regimeType}` ***mandatory*** enum set: (`vat`)
- `{regimeId}` ***mandatory*** enum set: (`vrn`)
- `{regimeIdValue}` ***mandatory***

#### Responses

##### 204 (OK):
    Response Body: N/A *empty*

##### 400 (BAD_REQUEST):
    Response Body: "Invalid RegimeType supplied. Must be one of {valid regimeType enum set}"

##### 400 (BAD_REQUEST):
    Response Body: "Invalid RegimeId supplied. Must be one of {valid regimeId enum set}"

##### 400 (BAD_REQUEST):
    Response Body: "Invalid regimeIdValue supplied. Must be one of {valid regimeIdValue enum set}"

##### 401 (UNAUTHORISED):
    Response Body: "The request was not authenticated"

##### 403 (FORBIDDEN):
    Response Body: "The request was authenticated but the user does not have the necessary authority"

##### 412 (PRECONDITION_FAILED):
    Response Body: "Downstream system of record has indicated that the record is in migration, try again later"

##### 500 (INTERNAL_SERVER_ERROR):
    Response Body: "{error message}"

##### 503 (SERVICE_UNAVAILABLE):
    Response Body: "Downstream system of record is unavailable, try again later"


#### Example Request/Response

    PUT /contact-preferences/vat/vrn/999999999
    Response Status: 204 (NO_CONTENT)

---

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
