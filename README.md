-
# Pagination
* To optimize performance, it is important to limit the amount of data returned, especially in the case of a mobile client.
* REST services have the ability to give clients access large datasets in manageable chunks, by splitting the data into discrete pages or _paging data_. 
* For this lab, we will approach this by implementing the _page number pagination pattern_.

-
### Get Data From Page 

* For example, a client wanting a blog post in page 3 of a hypothetical blog service can use a `GET` method resembling the following:
`http://blog.example.com/posts?page=3`

-
### Limit Data Retrieved From Page
* It is possible for the client to override the default page size by passing in a page-size parameter:
`http://blog.example.com/posts?page=3&size=20`

-
### Pagination Specific Information
* Pagination-specific information includes
	* total number of records
	* total number of pages
	* current page number
	* page size

-
### Pagination Data
* In the above scenario, one would expect a response body with pagination infromation closely resembling the `JSON` object below.

```JSON
{
"data": [
         ... Blog Data
    ],
    "totalPages": 9,
    "currentPageNumber": 2,
    "pageSize": 10,
    "totalRecords": 90
}
```
* Read more about REST pagination in Spring by clicking [here](https://dzone.com/articles/rest-pagination-spring).


-
# Taking Action!

0. Create a `src/main/resource/import.sql` file with [DML statements](http://lmgtfy.com/?q=DML+statement) for populating the database upon bootstrap. The `import.sql` should insert at least 10 polls, each with 3 or more options.
	* Below is an example of `SQL` statements for creating a single poll with only one option.
	
		* Poll Creation
		
			```sql
			insert into poll (poll_id, question) values (1, 'What is your favorite color?');
			```
		* Option Creation
	
			```sql
			insert into option (option_id, option_value, poll_id) values (1, 'Red', 1);
			``` 
	
0. Restart your application.
* Ensure database is populated by `import.sql`.
* Utilize Spring's built-in page number pagination support by researching the `PagingAndSortingRepository` class.
* Ensure the `Controller` methods handle `Pageable` arguments.
* Send a `GET` request to `http://localhost:8080/polls?page=0&size=2` via Postman.
	* Ensure the response is a `JSON` object with pagination-specific information.