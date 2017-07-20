

# Part 1 - Domain Implementation<br>
* _Domain objects_ are the backbone for an application and contain the [business logic](https://en.wikipedia.org/wiki/Business_logic).
* Create a sub package of `io.zipcoder.tc_spring_poll_application` named `domain`.


-
# Part 1.1 - Create class `Option`
* Create an `Option` class in the `domain` sub-package.
* `Option` class signature is annotated with `@Entity`
* `Option` has an `id` instance variable of type `Long`
	* `id` should be `annotated` with
		* `@Id`
			* denotes primary key of this entity
		* `@GeneratedValue`
			* configures the way of increment of the specified `column(field)`
		* `@Column(name = "OPTION_ID")`
			* specifies mapped column for a persistent property or field

* `Option` has a `value` instance variable of type `String`
	* `value` should be `annotated` with
		* `@Column(name = "OPTION_VALUE")`

* Create a `getter` and `setter` for each of the respective instance variables.


-
# Part 1.2 - Create class `Poll`
* Create a `Poll` class in the `domain` sub-package.
* `Poll` class signature is annotated with `@Entity`
* `Poll` has an `id` instance variable of type `Long`
	* `id` should be `annotated` with
		* `@Id`
		* `@GeneratedValue`
		* `Column(name = "POLL_ID")`

* `Poll` has a `question` instance variable of type `String`
	* `question` should be `annotated` with
		* `@Column(name = "QUESTION")`

* `Poll` has an `options` instance variable of type `Set` of `Option`
	* `options` should be `annotated` with
		* `@OneToMany(cascade = CascadeType.ALL)`
		* `@JoinColumn(name = "POLL_ID")`
		* `@OrderBy`

* Create a `getter` and `setter` for each of the respective instance variables.



-
# Part 1.3 - Create class `Vote`
* Create a `Vote` class in the `domain` sub-package.
* `Vote` class signature is annotated with `@Entity`
* `Vote` has an `id` instance variable of type `Long`
	* `id` should be `annotated` with
		* `@Id`
		* `@GeneratedValue`
		* `Column(name = "VOTE_ID")`

* `Vote` has a `option` instance variable of type `Option`
	* `option` should be `annotated` with
		* `@ManyToOne`
		* `@JoinColumn(name = "OPTION_ID")`

* Create a `getter` and `setter` for each of the respective instance variables.




-
-
# Part 2 - Repository Implementation
* _Repositories_ or [Data Access Objects (DAO)](https://en.wikipedia.org/wiki/Data_access_object), provide an abstraction for interacting with _datastores_.
* Typically DAOs include an interface that provides a set of finder methods such as `findById`, `findAll`, for retrieving data, and methods to persist and delete data.
* It is customary to have one `Repository` per `domain` object.
* Create a sub-package of `io.zipcoder.tc_spring_poll_application` named `repositories`.


-
# Part 2.1 - Create interface `OptionRepository`
* Create an `OptionRepository` interface in the `repositories` subpackage.
* `OptionRepository` extends `CrudRepository<Option, Long>`

-
# Part 2.2 - Create interface `PollRepository`
* Create a `PollRepository` interface in the `repositories` subpackage.
* `PollRepository` extends `CrudRepository<Poll, Long>`

-
# Part 2.3 - Create interface `VoteRepository`
* Create a `VoteRepository` interface in the `repositories` subpackage.
* `VoteRepository` extends `CrudRepository<Vote, Long>`






-
-
# Part 3 - Controller Implementation
* _Controllers_ provides all of the necessary [endpoints](https://en.wikipedia.org/wiki/Web_API#Endpoints) to access and manipulate respective domain objects.
	*  REST resources are identified using URI endpoints.
* Create a sub package of `io.zipcoder.tc_spring_poll_application` named `controller`.


-
# Part 3.1 - Create class `PollController`
* Create a `PollController` class in the `controller` sub package.
	* `PollController` signature should be `annotated` with `@RestController`

* `PollController` has a `pollRepository` instance variable of type `PollRepository`
	* `pollRepository` should be `annotated` with `@Inject`

-
# Part 3.1.1 - Create `GET` request method
* The method definition below supplies a `GET` request on the `/polls` endpoint which provides a collection of all of the polls available in the QuickPolls application. Copy and paste this into your `PollController` class.

```java
@RequestMapping(value="/polls", method= RequestMethod.GET)
public ResponseEntity<Iterable<Poll>> getAllPolls() {
    Iterable<Poll> allPolls = pollRepository.findAll();
    return new ResponseEntity<>(allPolls, HttpStatus.OK);
}
```

* The method above begins with reading all of the polls using the `PollRepository`.
* We then create an instance of `ResponseEntity` and pass in `Poll` data and the `HttpStatus.OK` status value.
* The `Poll` data becomes part of the response body and `OK` (code 200) becomes the response status code.




-
# Part 3.1.2 - Testing via Postman
* Ensure that the `start-class` tag in your `pom.xml` encapsulates `io.zipcoder.springdemo.QuickPollApplication`
* Open a command line and navigate to the project's root directory and run this command:
	* `mvn spring-boot:run`
* Launch the [Postman](https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=en) app in your Chrome browser and enter the URL `http://localhost:8080/polls` and hit Send.
* Because we don’t have any polls created yet, this command should result in an empty collection.




-
# Part 3.1.3 - Create `POST` request method
* We accomplish the capability to add new polls to the `PollController` by implementing the `POST` verb functionality in a `createPoll` method:

```java
@RequestMapping(value="/polls", method=RequestMethod.POST)
public ResponseEntity<?> createPoll(@RequestBody Poll poll) {
        poll = pollRepository.save(poll);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
}
```

* Take note that the method
	* has a parameter of type `@RequestBody Poll poll`
		* `@RequestBody` tells Spring that the entire request body needs to be converted to an instance of Poll
	* delegates the `Poll` persistence to `PollRepository`’s save method
		* `poll = pollRepository.save(poll);`




-
# Part 3.1.4 - Modify `createPoll`
* Best practice is to convey the URI to the newly created resource using the Location HTTP header via Spring's `ServletUriComponentsBuilder` utility class. This will ensure that the client has some way of knowing the URI of the newly created Poll.

```java
URI newPollUri = ServletUriComponentsBuilder
	.fromCurrentRequest()
	.path("/{id}")
	.buildAndExpand(poll.getId())
	.toUri();
```

* Modify the `createPoll` method so that it returns a `ResponseEntity` which takes an argument of a `new HttpHeaders()` whose _location_ has been _set_ to the above `newPollUri` via the `setLocation` method.




-
# Part 3.1.5 - Create `GET` request method
* The code snippet below enables us to access an individual poll.
* The _value attribute_ in the `@RequestMapping` takes a URI template `/polls/{pollId}`.
* The placeholder `{pollId}` along with `@PathVarible` annotation allows Spring to examine the request URI path and extract the `pollId` parameter value.
* Inside the method, we use the `PollRepository`’s `findOne` finder method to read the poll and pass it as part of a `ResponseEntity`.

```java
@RequestMapping(value="/polls/{pollId}", method=RequestMethod.GET)
public ResponseEntity<?> getPoll(@PathVariable Long pollId) {
	Poll p = pollRepository.findOne(pollId);
	return new ResponseEntity<> (p, HttpStatus.OK);
}
```




-
# Part 3.1.6 - Create `UPDATE` request method
* The code snippet below enables us to update a poll.

```java
RequestMapping(value="/polls/{pollId}", method=RequestMethod.PUT)
public ResponseEntity<?> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId) {
        // Save the entity
        Poll p = pollRepository.save(poll);
        return new ResponseEntity<>(HttpStatus.OK);
}
```



-
# Part 3.1.7 - Create `DELETE` request method.

* The code snippet below enables us to delete a poll.

```java
@RequestMapping(value="/polls/{pollId}", method=RequestMethod.DELETE)
public ResponseEntity<?> deletePoll(@PathVariable Long pollId) {
        pollRepository.delete(pollId);
        return new ResponseEntity<>(HttpStatus.OK);
}
```




-
# Part 3.1.8 - Test
* Restart the QuickPoll application.
* Use Postman to execute a `PUT` to `http://localhost:8080/polls/1` whose request body is the `JSON` object below.
* You can modify the request body in Postman by navigating to the `Body` tab, selecting the `raw` radio button, and selecting the `JSON` option from the text format dropdown.

```JSON
{
    "id": 1,
        "question": "What's the best netflix original?",
        "options": [
	    { "id": 1, "value": "Black Mirror" },
	    { "id": 2, "value": "Stranger Things" },
	    { "id": 3, "value": "Orange is the New Black"},
	    { "id": 4, "value": "The Get Down" }
	]
}
```


-
# Part 3.2 - Create class `VoteController`
* Following the principles used to create `PollController`, we implement the `VoteController` class.
* Below is the code for the `VoteController` class along with the functionality to create a vote.
* The `VoteController` uses an injected instance of `VoteRepository` to perform `CRUD` operations on Vote instances.

```java
@RestController
public class VoteController {
    @Inject
    private VoteRepository voteRepository;

    @RequestMapping(value = "/polls/{pollId}/votes", method = RequestMethod.POST)
    public ResponseEntity<?> createVote(@PathVariable Long pollId, @RequestBody Vote
            vote) {
        vote = voteRepository.save(vote);
        // Set the headers for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(ServletUriComponentsBuilder.
                fromCurrentRequest().path("/{id}").buildAndExpand(vote.getId()).toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
}
```

# Part 3.2.1 - Testing `VoteController`
* To test the voting capabilities, `POST` a new Vote to the `/polls/1/votes` endpoint with the option object expressed in `JSON` below.
* On successful request execution, you will see a Location response header with value http://localhost:8080/polls/1/votes/1.

```JSON
{
    "option": { "id": 1, "value": "Black Mirror" }
}
```




-
# Part 3.2.2 - Modify `VoteRepository`
* The method `findAll` in the `VoteRepository` retrieves all votes in a Database rather than a given poll.
* To ensure we can get votes for a given poll, we must add the code below to our `VoteRepository`.

```java
public interface VoteRepository extends CrudRepository<Vote, Long> {
    @Query(value = "SELECT v.* " +
            "FROM Option o, Vote v " +
            "WHERE o.POLL_ID = ?1 " +
            "AND v.OPTION_ID = o.OPTION_ID", nativeQuery = true)
    public Iterable<Vote> findVotesByPoll(Long pollId);
}
```

* The custom finder method `findVotesByPoll` takes the `ID` of the `Poll` as its parameter.
* The `@Query` annotation on this method takes a native SQL query along with the `nativeQuery` flag set to `true`.
* At runtime, Spring Data JPA replaces the `?1` placeholder with the passed-in `pollId` parameter value.






-
# Part 3.2.3 - Modify `VoteController`
* Create a `getAllVotes` method in the `VoteController`


```java
@RequestMapping(value="/polls/{pollId}/votes", method=RequestMethod.GET)
public Iterable<Vote> getAllVotes(@PathVariable Long pollId) {
        return voteRepository. findByPoll(pollId);
}
```





-
-
# Part 4 - Data Transfer Object (DTO) Implementation
* The final piece remaining for us is the implementation of the ComputeResult resource.
* Because we don’t have any domain objects that can directly help generate this resource representation, we implement two Data Transfer Objects or DTOs—OptionCount and VoteResult
* Create a sub package of `java` named `dtos`

-
# Part 4.1 - Create class `OptionCount`
* The `OptionCount` DTO contains the `ID` of the option and a count of votes casted for that option.

```java
public class OptionCount {
    private Long optionId;
    private int count;

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
```

# Part 4.2 - Create class `VoteResult`
* The `VoteResult` DTO contains the total votes cast and a collection of `OptionCount` instances.

```java
import java.util.Collection;
public class VoteResult {
    private int totalVotes;
    private Collection<OptionCount> results;

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Collection<OptionCount> getResults() {
        return results;
    }

    public void setResults(Collection<OptionCount> results) {
        this.results = results;
    }
}
```


# Part 4.3 - Create class `ComputeResultController`
* Following the principles used in creating the `PollController` and `VoteController`, we create a new `ComputeResultController` class

```java
@RestController
public class ComputeResultController {
    @Inject
    private VoteRepository voteRepository;

    @RequestMapping(value = "/computeresult", method = RequestMethod.GET)
    public ResponseEntity<?> computeResult(@RequestParam Long pollId) {
        VoteResult voteResult = new VoteResult();
        Iterable<Vote> allVotes = voteRepository.findVotesByPoll(pollId);

        // Algorithm to count votes
        return new ResponseEntity<VoteResult>(voteResult, HttpStatus.OK);
    }
```


* We inject an instance of `VoteRepository` into the controller, which is used to retrieve votes for a given poll.
* The `computeResult` method takes `pollId` as its parameter.
* The `@RequestParam` annotation instructs Spring to retrieve the `pollId` value from a HTTP query parameter.
* The computed results are sent to the client using a newly created instance of `ResponseEntity`.


# Part 4.4 - Test via Postman
* Start/restart the `QuickPoll` application.
* Using the earlier Postman requests, create a poll and cast votes on its options.
* Ensure a JSON file with a `status` of `200` is returned by executing a `GET` request of `http://localhost:8080/computeresults?pollId=1` via Postman
